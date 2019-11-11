package com.marketxs.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;
import com.marketxs.management.ManagementHelper;
import com.marketxs.management.ManagementHelperException;

/**
 * This class is used to initialize an incoming TCP connection before passing
 * it to the right interface instance. This class runs a daemon thread that
 * waits for the remote peer to send its overlay address. After it has been
 * received successfully, the socket is passed to the correct interface
 * instance and this daemon thread exits.
 * Only a limited number of these initializer threads can be running
 * concurrently. Initializers that exceed that concurrency number will
 * immediately close their socket and exit.
 * This class binds to JMX.
 * <P>
 * The TCPConnectionInitializer can be configured using the following
 * properties:
 * </P>
 * 
 * <LI><code>com.marketxs.messaging.TCPConnectionInitializer.timeout = 10000</code>
 * <LI><code>com.marketxs.messaging.TCPConnectionInitializer.maxConcurrent = 10</code>
 * 
 * <P>
 * Note that the timeout is in milliseconds.
 * </P>
 * 
 * @author Erik van Zijst - 10.oct.2003 - erik@marketxs.com
 */
public class TCPConnectionInitializer implements Runnable, TCPConnectionInitializerMBean
{
	private static int TIMEOUT = 10000;	// initialization has to complete within 10000ms
	private static int MAXCONCURRENT = 10;	// never spawn more than 10 concurrent initializer threads 
	private static final Set initializers = new HashSet();	// contains references to all active initializers
	private static Log logger = Log.instance(TCPConnectionInitializer.class);

	public static final byte[] WRONGHOST_MSG = (new String("We're not configured to accept connections from your address.\n")).getBytes();
	public static final byte[] TIMEOUT_MSG = (new String("I didn't receive your address within " + TIMEOUT + "ms. Handshake aborted.\n")).getBytes();
	public static final byte[] FULL_MSG = (new String("Sorry, but we're already busy establishing a connection with too many other hosts. Try again later.\n")).getBytes();
	public static final byte[] NOADDRESS_MSG = (new String("Invalid address.\n")).getBytes();

	private Thread thread = null;
	private Socket socket = null;
	private Map interfaces = null;
	private String jmxName = null;
	private boolean stop = false;
	
	static
	{
		try
		{
			TIMEOUT = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.TCPConnectionInitializer.timeout", String.valueOf(TIMEOUT)));
		}
		catch(Exception e)
		{
		}
		
		try
		{
			MAXCONCURRENT = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.TCPConnectionInitializer.maxConcurrent", String.valueOf(MAXCONCURRENT)));
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * Creates a new TCPConnectionInitializer instance that can be used to
	 * initialize a new incoming connection.
	 * 
	 * @param socket	the socket connected to the remote peer.
	 * @param interfaces	the map that contains all registered interfaces.
	 */
	public TCPConnectionInitializer(Socket socket, Map interfaces)
	{
		this.socket = socket;
		this.interfaces = interfaces;
		jmxName = new String("messaging:type=initialisers,name=\"" + socket.getInetAddress().getHostName() + ":" + socket.getPort() + "\"");

		// add to initializer-map
		synchronized(initializers)
		{
			initializers.add(this);
		}
		
		try
		{
			if(!ManagementHelper.isRegistered(jmxName))
				ManagementHelper.register(jmxName, this);
		}
		catch(ManagementHelperException mhe)
		{
			logger.log("Unable to register this initializer (" + jmxName + ") in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
		}
	}
	
	/**
	 * Method exposed through JMX
	 * 
	 * @return	the hostname of the remote host.
	 */
	public String getRemoteHost()
	{
		return socket.getInetAddress().getHostName();
	}
	
	/**
	 * Method exposed through JMX
	 * 
	 * @return	the remote port of the initialized socket.
	 */
	public int getRemotePort()
	{
		return socket.getPort();
	}
	
	public static void setMaxConcurrentInitializers(int maxConcurrent)
	{
		MAXCONCURRENT = maxConcurrent;
	}
	
	/**
	 * Used to retrieve the number of initializer threads that can be active
	 * concurrently. This method is exposed in JMX.
	 * 
	 * @return
	 */
	public static int getMaxConcurrentInitializers()
	{
		return MAXCONCURRENT;
	}
	
	/**
	 * This is the main method of the initializer thread. It reads the
	 * serialized peer address from the socket, deserializes it, finds the
	 * interface that registered that address and passes the socket to that
	 * interface. It uses a socket timeout to prevent the thread from
	 * deadlocking when the peer doesn't send anything.
	 */
	public void run()
	{
		thread = Thread.currentThread();

		try
		{
			if(stop)
			{
				try
				{
					socket.close();
				}
				catch(Exception e)
				{
				}
				return;
			}
			
			try
			{
				socket.setSoTimeout(TIMEOUT);

				int count;
				synchronized(initializers)
				{
					count = initializers.size();
				}
			
				if(count > MAXCONCURRENT)
				{
					logger.log("Rejecting incoming connection from " + socket.getInetAddress().getHostName() + " due to too many pending connections.", Log.SEVERITY_WARNING);
					try
					{
						OutputStream out = socket.getOutputStream();
						out.write(FULL_MSG);
						out.flush();
						out.close();
					}
					catch(IOException ioe)
					{
					}
					socket.close();
				}
			
				InputStream in = socket.getInputStream();
				byte[] buf = new byte[128];	// must be enough to store an address (128 bytes)
				in.read(buf, 0, buf.length);	// throws java.io.InterruptedIOException when timed-out
				Address peer = new Address();
				peer.parseAddress(buf, 0);	// could throw com.marketxs.messaging.DeserializeException
				
				TCPInterface iface = (TCPInterface)interfaces.get(peer);
				
				if(iface != null && !peer.isMulticast())
				{
					/**
					 * Pass the socket to the interface that wants to receive
					 * incoming connections from this host and return.
					 */
					iface.handleIncoming(socket);
				}
				else
				{
					/**
					 * there's no interface that wants to connect with this peer.
					 * Try to send a meaningfull error msg back and close the
					 * socket.
					 */
					logger.log("Rejecting incoming connection from " + peer.toString() + "@" + socket.getInetAddress().getHostName() + ". No interface configured for this host.", Log.SEVERITY_WARNING);
					try
					{
						OutputStream out = socket.getOutputStream();
						out.write(WRONGHOST_MSG);
						out.flush();
						out.close();
					}
					catch(IOException ioe)
					{
					}
					socket.close();
				}
			}
			catch(InterruptedIOException iioe)
			{
				/**
				 * We didn't receive the peer's address in time. Send an error
				 * string, close the connection and return.
				 */
				logger.log("Rejecting incoming connection from " + socket.getInetAddress().getHostName() + ". Peer didn't send its address within " + TIMEOUT + "ms.", Log.SEVERITY_WARNING);
				try
				{
					OutputStream out = socket.getOutputStream();
					out.write(TIMEOUT_MSG);
					out.flush();
					out.close();
				}
				catch(IOException ioe)
				{
				}
				finally
				{
					try
					{
						socket.close();
					}
					catch(IOException ioe)
					{
					}
				}
			}
			catch(DeserializeException de)
			{
				/**
				 * The peer sent an address that was unreadable. Send and
				 * error string and close the connection.
				 */
				logger.log("Rejecting incoming connection from " + socket.getInetAddress().getHostName() + ". Peer sent a corrupt address.", Log.SEVERITY_WARNING);
				try
				{
					OutputStream out = socket.getOutputStream();
					out.write(NOADDRESS_MSG);
					out.flush();
					out.close();
				}
				catch(IOException ioe)
				{
				}
				finally
				{
					try
					{
						socket.close();
					}
					catch(IOException ioe)
					{
					}
				}
			}
			catch(IOException e)
			{
				/**
				 * Error reading from the socket. Consider the socket dead, do
				 * not attempt to send any error, just close and return.
				 */
				logger.log("Rejecting incoming connection from " + socket.getInetAddress().getHostName() + ": socket error: " + e.getMessage(), Log.SEVERITY_WARNING);
				try
				{
					socket.close();
				}
				catch(IOException ioe)
				{
				}
			}
		}
		finally
		{
			/**
			 * Whatever happens, when this thread exits, make sure it is
			 * removed from the initializer set.
			 */
			synchronized(initializers)
			{
				initializers.remove(this);
			}

			try
			{
				if(ManagementHelper.isRegistered(jmxName))
					ManagementHelper.deregister(jmxName);
			}
			catch(ManagementHelperException mhe)
			{
				logger.log("Unable to deregister this initializer (" + jmxName + ") in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
			}
		}
	}
	
	/**
	 * <P>
	 * Interrupts this initializer thread. When this method returns, the
	 * thread is guaranteed to have exited or will exit shortly. An
	 * InterruptedException indicates that this method was interrupted while
	 * waiting for the initializer thread to exit. This leaves the system in
	 * an undefined state.
	 * </P>
	 */
	public void stopInitializer() throws InterruptedException
	{
		boolean stop = true;
		if(thread != null)
		{
			try
			{
				socket.close();
			}
			catch(Exception e)
			{
			}
			thread.join();
		}
	}
	
	/**
	 * <P>
	 * Interrupts all running connection initializer threads. This might take
	 * some time as the method waits for every thread to exit. When this
	 * method returns, all connection initializers are guaranteed to have
	 * exited.
	 * </P>
	 * 
	 * @throws InterruptedException
	 */
	public static void stopAllInitializers() throws InterruptedException
	{
		TCPConnectionInitializer[] threads = null;
		
		synchronized(initializers)
		{
			threads = (TCPConnectionInitializer[])initializers.toArray(new TCPConnectionInitializer[initializers.size()]);
		}
		logger.log("Stopping all " + threads.length + " pending connection initializers.", Log.SEVERITY_VERBOSE);
		
		for(int nX = 0; nX < threads.length; nX++)
		{
			threads[nX].stopInitializer();
		}
	}
}
