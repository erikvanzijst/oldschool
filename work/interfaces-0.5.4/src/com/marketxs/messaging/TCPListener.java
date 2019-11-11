package com.marketxs.messaging;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.marketxs.management.ManagementHelper;
import com.marketxs.management.ManagementHelperException;
import com.marketxs.management.ManagementHelperInterface;
import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;

/**
 * <P>
 * Internal class that handles incoming connections for all interface
 * instances. This allows all interfaces to be reachable through the same
 * TCP port. This class is a singleton. By default the listener binds to port
 * 5000. The listener is not started automatically, but will be started when
 * the first interface registers.
 * </P>
 * <P>
 * The TCPListener can be configured using the following property:
 * </P>
 * 
 * <LI><code>com.marketxs.messaging.TCPListener.port = 5000</code>
 * 
 * @author Erik van Zijst - 10.oct.2003 - erik@marketxs.com
 */
public class TCPListener implements Runnable, ManagementHelperInterface, TCPListenerMBean
{
	private static TCPListener ref = null;
	private boolean listening = false;
	private boolean running = false;
	private final Object runMutex = new Object();
	private int port = 5000;	// default is 5000
	private Thread listenThread = null;
	private ServerSocket socket = null;
	private Log logger = Log.instance(TCPListener.class);
	private ListenerState state = ListenerState.STOPPED;
	
	private final Map ifaceTable = Collections.synchronizedMap(new HashMap());	// indexed on peerAddress
	private final String jmxName = "messaging:type=TCPListener";
	
	// JMX descriptions
	private String description = "The TCPListener component handles incoming connections for all interface " +
		"instances. This allows all interfaces to be reachable through the same " +
		"TCP port. This component is a singleton. By default the listener binds to port " +
		"5000. The listener is not started automatically, but will be started when " +
		"the first interface registers.";
	private Map operationParameterNameMap = new HashMap();
	private Map attributeDescriptionMap = new HashMap();
	private Map operationDescriptionMap = new HashMap();
	private Map operationParameterDescriptionMap = new HashMap();

	/**
	 * <P>
	 * Singleton factory method.
	 * </P>
	 * 
	 * @return
	 */
	protected static synchronized TCPListener getInstance()
	{
		if(ref == null)
			ref = new TCPListener();
		
		return ref;
	}
	
	/**
	 * <P>
	 * Protected constructor to go with the singleton pattern. It registers
	 * the listener in JMX.
	 * </P>
	 *
	 */
	private TCPListener()
	{
		attributeDescriptionMap.put("MaxConcurrentInitializers", "For each incoming connection an initializer is spawned to do the handshake. This defines the maximum number of concurrent handshakes.");
		attributeDescriptionMap.put("Port", "This is the TCP port the listener binds to.");
		attributeDescriptionMap.put("State", "This is the current state of the listener component. Can be stopped, listening or stopped.");
		attributeDescriptionMap.put("StatusString", "More human readable representation of the current status.");
		
		operationDescriptionMap.put("startListener", "Starts the listener on the configured port.");
		operationDescriptionMap.put("stopListener", "Stops the listener and releases the TCP port.");
		
		try
		{
			port = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.TCPListener.port", String.valueOf(port)));
		}
		catch(Exception e)
		{
		}
		
		try
		{
			if(!ManagementHelper.isRegistered(jmxName))
				ManagementHelper.register(jmxName, this);
		}
		catch(ManagementHelperException mhe)
		{
			logger.log("Unable to register " + jmxName + " in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
		}
	}
	
	public void run()
	{
		try
		{
			state = ListenerState.LISTENING;
			while(listening)
			{
				try
				{
					Socket inSocket = socket.accept();
				
					if(!listening)	// this is our wake-up call to die
					{
						try
						{
							inSocket.close();
						}
						catch(IOException ioe)
						{
						}
						break;
					}

					/**
					 * Spawn a new TCPConnectionInitializer thread to handle this
					 * incoming connection in the background while we wait for
					 * another one. 
					 */
					Thread initializer = new Thread(new TCPConnectionInitializer(inSocket, ifaceTable), "Connection initializer for " + inSocket.getInetAddress().getHostName() + ":" + inSocket.getPort());
					initializer.setDaemon(true);
					initializer.start();
				}
				catch(IOException e)
				{
				}
			}
		
			try
			{
				socket.close();	// make sure the socket is closed before returning
			}
			catch(IOException e)
			{
			}
		
			// stop all connection initializers that might be running
			try
			{
				TCPConnectionInitializer.stopAllInitializers();
			}
			catch(InterruptedException e)
			{
				logger.log("Unable to kill all initializer threads. There may be runnaway threads now.", Log.SEVERITY_ERROR);
			}
		
			state = ListenerState.STOPPED;
			logger.log("Listener-thread stopped.", Log.SEVERITY_DEBUG);
		}
		catch(Throwable t)
		{
			state = ListenerState.UNKNOWN;
			logger.log("Unexpected exception in listener thread. Listener died.", Log.SEVERITY_CRITICAL);
		}
	}
	
	
	public void setMaxConcurrentInitializers(int maxConcurrent)
	{
		TCPConnectionInitializer.setMaxConcurrentInitializers(maxConcurrent);
	}
	
	/**
	 * <P>
	 * Used to retrieve the number of initializer threads that can be active
	 * concurrently. This method is exposed in JMX.
	 * </P>
	 * 
	 * @return
	 */
	public int getMaxConcurrentInitializers()
	{
		return TCPConnectionInitializer.getMaxConcurrentInitializers();
	}
	
	/**
	 * <P>
	 * This method starts the listener. It first binds the configured TCP
	 * port and then spawns a daemon thread to accept incoming connections.
	 * This method is thread-safe and idempotent.
	 * </P> 
	 * 
	 * @throws IOException	if the TCP port could not be bound.
	 */
	public void startListener() throws IOException
	{
		synchronized(runMutex)
		{
			if(running)
				return;
			else
			{
				state = ListenerState.STARTING;
				try
				{
					socket = new ServerSocket(port);	// bind the port first
				
					listening = true;	// allow the thread to call accept()
					listenThread = new Thread(getInstance(), "Listener on port " + port);
					listenThread.setDaemon(true);
					listenThread.start();
					running = true;
					logger.log("Listener started on port " + port, Log.SEVERITY_DEBUG);
				}
				catch(IOException e)
				{
					state = ListenerState.STOPPED;
					throw e;
				}
			}
		}
	}
	
	/**
	 * <P>
	 * Stops the listening thread. This could take some time to complete, as
	 * it needs to wake up the thread and wait for it to terminate. Also, it
	 * stops all background threads that are currently busy initializing
	 * incoming connections. After this, there are no background threads
	 * running anymore and the listener instance could be garbage collected.
	 * </P>
	 * 
	 * @throws InterruptedException
	 */
	public void stopListener() throws InterruptedException
	{
		synchronized(runMutex)
		{
			if(!running)
				return;
			else
			{
				state = ListenerState.STOPPING;
				listening = false;	// make sure the thread doesn't call accept() again

				try
				{
					Socket socket = new Socket(InetAddress.getLocalHost(), port);
					socket.close();	// wake up the listening thread (it's not interruptable)
				}
				catch(IOException e)
				{
					/**
					 * Failed to wake up the listening thread by connecting to it.
					 */
				}

				listenThread.join();
				running = false;
				logger.log("Listener stopped successfully.", Log.SEVERITY_DEBUG);
			}
		}
	}
	
	/**
	 * <P>
	 * Configures the TCP port to bind to. Note that the listener has to be
	 * restarted to take effect.
	 * </P>
	 * 
	 * @param listenPort	the port bind (default is 5000).
	 */
	public void setPort(int listenPort)
	{
		port = listenPort;
	}
	
	/**
	 * <P>
	 * Returns the TCP port this listener is configured to connect to.
	 * </P>
	 */
	public int getPort()
	{
		return port;
	}
	
	/**
	 * <P>
	 * Adds an interface to the list of subscribed interfaces. The interface
	 * will be notified when a new connection comes in from the specified
	 * address. Incoming connections from addresses that have no registered
	 * interface in the internal registration table are closed immediately.
	 * If the listener was not yet running, it will be started automatically.
	 * </P>
	 * 
	 * @param iface	the interface instance.
	 * @param peerAddress	the overlay address of the peer to connect with.
	 */
	public static void registerInterface(PeerInterface iface, Address peerAddress)
	{
		TCPListener listener = getInstance();
		listener.ifaceTable.put(peerAddress, iface);
		
		try
		{
			listener.startListener();	// the startListener method is idempotent 
		}
		catch(IOException ioe)
		{
			listener.logger.log("Unable to start the listener on port " + listener.getPort() + " (" + ioe.getMessage() + "). Interface registered, but unreachable for incoming connections.", Log.SEVERITY_WARNING);
		}

	}
	
	public static void deregisterInterface(PeerInterface iface, Address peerAddress)
	{
		TCPListener listener = getInstance();
		Map registry = listener.ifaceTable;
		
		synchronized(listener.ifaceTable)
		{
			PeerInterface _iface = (PeerInterface)listener.ifaceTable.get(peerAddress);
			if(_iface != null && iface.equals(_iface))
			{
				listener.ifaceTable.remove(peerAddress);
				listener.logger.log("Deregistering interface " + iface.getId() + " with peer " + peerAddress.toString(), Log.SEVERITY_INFO);
			}
			else
				listener.logger.log("Unable to deregister interface " + iface.getId() + ". This interface is registered with a different peer address.", Log.SEVERITY_WARNING);
		} 
	}
	
	public ListenerState getState()
	{
		return state;
	}
	
	/**
	 * <P>
	 * Method exposed through JMX showing the current status of the listener.
	 * Returns either "Listening on port xxxx" or "Stopped".
	 * </P>
	 * 
	 * @return	"Listening on port xxxx" or "Stopped".
	 */
	public String getStatusString()
	{
		synchronized(runMutex)
		{
			return running ? "Listening on port " + socket.getLocalPort() : "Stopped";
		}
	}
	
	/**
	 * <P>
	 * Returns the description strings that are displayed in JMX.
	 * </P>
	 */
	public String getDescription()
	{
		return description;
	}
	
	public Map getOperationParameterNameMap()
	{
		return operationParameterNameMap;
	}
	
	public Map getAttributeDescriptionMap()
	{
		return attributeDescriptionMap;
	}
	
	public Map getOperationDescriptionMap()
	{
		return operationDescriptionMap;
	}
	
	public Map getOperationParameterDescriptionMap()
	{
		return operationParameterDescriptionMap;
	}
}
