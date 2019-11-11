package com.marketxs.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;
import com.marketxs.management.ManagementHelper;
import com.marketxs.management.ManagementHelperException;
import com.marketxs.management.ManagementHelperInterface;

/**
 * <P>
 * The TCPInterface class is the actual implementation of an interface that
 * connects to a neighbor. It uses a single TCP connection for all
 * transmissions.
 * The class extends <code>java.util.Observable</code> and is capable of notifying the
 * central router class when messages are available. When the interface
 * received a new packet, it is first queued in an internal buffer. The
 * isReady() method now returns true. Then the observers (usually only one) is
 * notified. The normal thing to do would be to first test isReady() and only
 * call read() when isReady() returns true. Otherwise the read() method will
 * block untill new packets are received.
 * </P>
 * <P>
 * The TCPInterface can be configured using the following
 * properties:
 * </P>
 * 
 * <LI><code>com.marketxs.messaging.TCPInterface.AcceptSeconds = 10</code>
 * 
 * <P>
 * Note that the accept period is in seconds.
 * </P>
 * <P>
 * When the TCPInterface instance is activated, it'll register two MBeans:
 * </P>
 * 
 * <LI><code>messaging:type=interfaces,name="interface.address",peer="peer.address",module=base</code>
 * <LI><code>messaging:type=interfaces,name="interface.address",peer="peer.address",module=CostMetricUnit</code>
 * 
 * <P>
 * The first (module=base) is the real interface instance, while the second
 * (module=CostMetricUnit) is the interface's link cost module that monitors
 * the connection.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.1, 10.oct.2003
 */
public class TCPInterface extends Observable implements Runnable, PeerInterface
{
	protected static int ACCEPTSECS = 10;
	
	private static final int DISABLED = 0;
	private static final int ACCEPTING = 1;
	private static final int CONNECTED = 2;
	private static final int RUNNING = 3;
	
	private static final int DIE = 0;
	private static final int DISCONNECT = 1;
	private static final int NONE = 2;

	private int status = DISABLED;	// {DISABLED, ACCEPTING, CONNECTED, RUNNING}
	private InterfaceState state = InterfaceState.INACTIVE;
	private int cmd = NONE;	// {NONE, DIE, DISCONNECT}

	private final Object mutex = new Object();
	private final String id;
	private Address peerAddress = null;
	private InetAddress host = null;
	private int port = 0;
	private Thread connectThread = null;
	private Socket socket = null;
	private Socket tmpSocket = null;
	private Random random = new Random();
	private Log logger = Log.instance(TCPInterface.class);
	private SocketReader reader = new SocketReader(this);
	private SocketWriter writer = new SocketWriter(this);
	private CostMetricUnit pinger = new CostMetricUnit(this);
	private float costMultiplier = 1f;	// the pinger's metric is always multiplied by this factor
	private Date connectTime = null;	// connected since
	
	private Address newPeerAddress = peerAddress;
	private InetAddress newHost = host;
	private int newPort = port;
	
	static
	{
		try
		{
			ACCEPTSECS = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.TCPInterface.AcceptSeconds", String.valueOf(ACCEPTSECS)));
		}
		catch(Exception e)
		{
		}
	}
	
	public TCPInterface(String id)
	{
		this.id = id;
		writer.addInterceptor(pinger.getOutboundInterceptor());
		reader.addInterceptor(pinger.getInboundInterceptor());
	}
	
	public Object getMonitor()
	{
		return mutex;
	}
	
	/**
	 * Returns the id of this interface.
	 * 
	 * @return the id of this interface.
	 */
	public String getId()
	{
		return id;
	}
	
	public void setRemoteHost(InetAddress host)
	{
		newHost = host;

		synchronized(this)
		{
			if(status == DISABLED)
				this.host = host;
		}
	}
	
	public InetAddress getRemoteHost()
	{
		return host;
	}
	
	public void setPort(int port)
	{
		newPort = port;
		synchronized(mutex)
		{
			if(status == DISABLED)
				this.port = port;
		}
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void setPeerAddress(Address peer)
	{
		newPeerAddress = peer;
		
		synchronized(mutex)
		{
			if(status == DISABLED)
				this.peerAddress = peer;
		}
	}
	
	public Address getPeerAddress()
	{
		return peerAddress;
	}
	
	/**
	 * <P>
	 * Configures the interface's peer host. Note that the settings will only
	 * take effect after the interface has been (re)started. When this method
	 * is called on an interface that has already been enabled, it will
	 * continue to use the old settings until it has been disabled and enabled
	 * again explicitly using methods disable() and enable().
	 * </P>
	 * 
	 * @param peerAddress	the overlay address of the peer.
	 * @param host	the address of remote peer.
	 * @param port	the TCP port of the remote peer.
	 */
	public void setPeer(Address peerAddress, InetAddress host, int port)
	{
		synchronized(mutex)
		{
			setPeerAddress(peerAddress);
			setRemoteHost(host);
			setPort(port);
		}
	}
	
	/**
	 * <P>
	 * Enables this interface. When enabled, the interface instance uses a
	 * number of internal daemon threads. Note that this method does nothing
	 * when the peer address hasn't been configured yet. Always call setPeer()
	 * first.
	 * </P>
	 */
	public void enable()
	{
		synchronized(mutex)
		{
			if(status != DISABLED)
				return;
			else
			{
				peerAddress = newPeerAddress;
				host = newHost;
				port = newPort;
			
				cmd = NONE;
				connectThread = new Thread(this, toString());
				connectThread.setDaemon(true);
				connectThread.start();
			
				TCPListener.registerInterface(this, peerAddress);
				logger.log("Interface " + id + " (peer = " + peerAddress + "@" + host.getHostName() + ":" + port + ") enabled.", Log.SEVERITY_INFO);
				status = RUNNING;
				state = InterfaceState.ACTIVE;
			}
		}
	}
	
	/**
	 * <P>
	 * Interrupts all internal threads. After the method has returned it might
	 * take some time for the interface threads to exit.
	 * </P>
	 */
	public void disable()
	{
		synchronized(mutex)
		{
			if(status == DISABLED)
				return;
			else
			{
				cmd = DIE;
				logger.log("Sending interface " + id + " the command to deactivate.", Log.SEVERITY_INFO);
				TCPListener.deregisterInterface(this, peerAddress);
				mutex.notify();
			}
		}
	}
	
	public void run()
	{
		synchronized(mutex)
		{
			final String jmxName =			"messaging:type=interfaces,name=\"" + id + "\",peer=\"" + peerAddress.toString() + "\",module=base";
			final String pingerJmxName =	"messaging:type=interfaces,name=\"" + id + "\",peer=\"" + peerAddress.toString() + "\",module=CostMetricUnit";
			final String writerJmxName =	"messaging:type=interfaces,name=\"" + id + "\",peer=\"" + peerAddress.toString() + "\",module=SocketWriter";
			final String readerJmxName =	"messaging:type=interfaces,name=\"" + id + "\",peer=\"" + peerAddress.toString() + "\",module=SocketReader";

			try
			{
				try
				{
					// register the interface in JMX
					if(!ManagementHelper.isRegistered(jmxName))
						ManagementHelper.register(jmxName, this.new ManageableTCPInterface());
				}
				catch(ManagementHelperException mhe)
				{
					logger.log("Unable to register " + jmxName + " in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
				}
			
				try
				{
					// register the interface's cost metric unit in JMX
					if(!ManagementHelper.isRegistered(pingerJmxName))
						ManagementHelper.register(pingerJmxName, pinger.getMBean());
				}
				catch(ManagementHelperException mhe)
				{
					logger.log("Unable to register " + pingerJmxName + " in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
				}
			
				try
				{
					// register the interface's socket writer unit (output buffer) in JMX
					if(!ManagementHelper.isRegistered(writerJmxName))
						ManagementHelper.register(writerJmxName, writer.getMBean());
				}
				catch(ManagementHelperException mhe)
				{
					logger.log("Unable to register " + writerJmxName + " in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
				}
			
				try
				{
					// register the interface's socket reader unit (input buffer) in JMX
					if(!ManagementHelper.isRegistered(readerJmxName))
						ManagementHelper.register(readerJmxName, reader.getMBean());
				}
				catch(ManagementHelperException mhe)
				{
					logger.log("Unable to register " + readerJmxName + " in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
				}
			
				logger.log("Interface " + id + " (to " + peerAddress + ") activated.", Log.SEVERITY_INFO);

				while(cmd != DIE)
				{
					Thread.sleep(1000);	// avoid trashing during a reconnect frenzy
					if(cmd == DISCONNECT)
						cmd = NONE;
					
					socket = null;
					try
					{
						// first try being a client...
						socket = connect();
					}
					catch(IOException ioe)
					{
						logger.log("Connecting to " + peerAddress + " (" + host.getHostName() + ":" + port + ") failed: " + ioe.getMessage(), Log.SEVERITY_INFO);
						try
						{
							if(socket != null)
								socket.close();
						}
						catch(IOException __ioe)
						{
						}

						// ...if that failed, try being a server...
						try
						{
							socket = accept();
						}
						catch(IOException _ioe)
						{
							// ...if that also failed, start again
							continue;
						}
					}
				
					connected(socket);
					try
					{
						socket.close();	// good practice
					}
					catch(IOException e)
					{
					}
				}
			}
			catch(InterruptedException ie)
			{
				logger.log("Interface " + id + " (to " + peerAddress + ") was interrupted and will be deactivated: " + LogUtil.getStackTrace(ie), Log.SEVERITY_WARNING);
			}
			finally
			{
				// remove the interface itself from JMX
				try
				{
					if(ManagementHelper.isRegistered(jmxName))
						ManagementHelper.deregister(jmxName);
				}
				catch(ManagementHelperException mhe)
				{
					logger.log("Unable to deregister this initializer (" + jmxName + ") in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
				}

				// remove the pinger component from JMX
				try
				{
					if(ManagementHelper.isRegistered(pingerJmxName))
						ManagementHelper.deregister(pingerJmxName);
				}
				catch(ManagementHelperException mhe)
				{
					logger.log("Unable to deregister this initializer (" + pingerJmxName + ") in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
				}

				// remove the socket writer component from JMX
				try
				{
					if(ManagementHelper.isRegistered(writerJmxName))
						ManagementHelper.deregister(writerJmxName);
				}
				catch(ManagementHelperException mhe)
				{
					logger.log("Unable to deregister this initializer (" + writerJmxName + ") in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
				}

				// remove the socket reader component from JMX
				try
				{
					if(ManagementHelper.isRegistered(readerJmxName))
						ManagementHelper.deregister(readerJmxName);
				}
				catch(ManagementHelperException mhe)
				{
					logger.log("Unable to deregister this initializer (" + readerJmxName + ") in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
				}

				status = DISABLED;
				state = InterfaceState.INACTIVE;
				logger.log("Interface " + id + " (to " + peerAddress + ") deactivated.", Log.SEVERITY_INFO);
			}
		}
	}

	/**
	 * <P>
	 * Attempts to connect to the remote peer once. If it fails, an
	 * IOException is thrown. Note that the peer might not be listening. 
	 * </P>
	 * 
	 * @return	the socket connected to the peer.
	 * @throws IOException when a connection could not be established.
	 */
	private Socket connect() throws IOException
	{
		InputStream in = null;
		OutputStream out = null;
		Address peer = null;
		Socket s = null;
		boolean success = false;

		try
		{
			s = new Socket(host, port);
			s.setSoTimeout(10000);
			
			// first send our own address...
			out = s.getOutputStream();
			byte[] raw = Kernel.getInstance().getRouterAddress().serialize(); 
			out.write(raw);
			out.flush();
						
			try
			{
				// ...then read and verify the peer's address
				in = s.getInputStream();
				raw = new byte[128];	// must be enough to store an address (128 bytes)
				in.read(raw, 0, raw.length);	// throws java.io.InterruptedIOException when timed-out
				peer = new Address();
				peer.parseAddress(raw, 0);	// could throw com.marketxs.messaging.exbf.DeserializeException
			}
			catch(DeserializeException de)
			{
				out.write(TCPConnectionInitializer.NOADDRESS_MSG);
				out.flush();
				throw new IOException("Handshake failed; the peer we connected to sent a corrupted address.");
			}
		
			if(peer.equals(peerAddress))
			{
				success = true;	// prevent socket-close in the finally-clause
			}
			else
			{
				out.write(TCPConnectionInitializer.WRONGHOST_MSG);
				out.flush();
				throw new IOException("The peer we connected to wasn't " + peerAddress.toString() + ", but " + peer.toString());
			}
		}
		finally
		{
			if(!success)
			{
				try
				{
					s.close();
				}
				catch(Exception e)
				{
				}
			}
		}
		return s;
	}

	/**
	 * <P>
	 * Allows the remote peer to connect to us for a limited time. This period
	 * is ACCEPTSECS seconds + a random number of seconds between 0 and
	 * ACCEPTSECS (exclusive).
	 * </P>
	 * 
	 * @return	the socket connected to the peer.
	 * @throws IOException	if there was no incoming connection or the thread
	 * 	was instructed to exit.
	 * @throws InterruptedException	if this thread was interrupted while
	 * 	waiting for incoming connections.
	 */
	private Socket accept() throws IOException, InterruptedException
	{
		if(cmd == DIE)
			throw new IOException();
		else
		{
			status = ACCEPTING;
			state = InterfaceState.CONNECTING;
			connectThread.setName(toString());	// change thread name to reflect connected status
			if(tmpSocket == null)
			{
				long timeout = 1000l * (long)(ACCEPTSECS + random.nextInt(ACCEPTSECS));
				logger.log("Interface " + id + " (to " + peerAddress + ") waiting for peer to connect for " + timeout + "ms.", Log.SEVERITY_DEBUG);
				mutex.wait(timeout);
			}
		
			if(tmpSocket == null)
				throw new IOException();
			else
			{
				Socket s = tmpSocket;
				tmpSocket = null;
				logger.log("Peer " + peerAddress + " opened a connection to interface " + id + ".", Log.SEVERITY_DEBUG);
				return s;
			}
		}
	}
	
	public void handleIncoming(Socket inSocket)
	{
		synchronized(mutex)
		{
			try
			{
				if(status == ACCEPTING && (tmpSocket == null))
				{
					/**
					 * Before passing the connected socket to the interface thread, we
					 * have to complete the handshake. We have received and verified
					 * the peer's address, but we still have to send it ours.
					 */
					OutputStream out = inSocket.getOutputStream();
					out.write(Kernel.getInstance().getRouterAddress().serialize());
					out.flush();

					tmpSocket = inSocket;
					mutex.notify();
				}
				else	// already connected or interface disabled
				{
					logger.log("Incoming connection for interface " + id + " (to " + peerAddress + ") rejected. Interface " + (status == DISABLED ? "deactivated." : "already connected or busy connecting."), Log.SEVERITY_INFO);
					inSocket.close();	// we're not interested in incoming connections right now
				}
			}
			catch(IOException ioe)
			{
				try
				{
					inSocket.close();
				}
				catch(Exception e)
				{
				}
			}
		}
	}

	/**
	 * <P>
	 * This method is entered when the interface daemon thread was able to
	 * establish a connection to the peer. It starts an individual socket
	 * reader and writer thread to do the blocking IO with the socket while in
	 * the meantime it sleeps, waiting for one of the threads to detect an IO
	 * error.
	 * </P>
	 * <P>
	 * When an IO error occurs or when the interface's disable() method is
	 * invoked, the interface thread is woken up and kills both the reader and
	 * the writer. It then changes the status from CONNECTED to RUNNING and
	 * returns.
	 * </P>
	 * 
	 * @param socket
	 * @throws InterruptedException
	 */
	private void connected(Socket socket) throws InterruptedException
	{
		if(cmd == DIE || cmd == DISCONNECT)
			return;
		else
		{
			try
			{
				socket.setSoTimeout(0);	// no read timeout necessary anymore after initialization
				writer.setOutputStream(socket.getOutputStream());
				reader.setInputStream(socket.getInputStream());
		
				writer.start();
				reader.start();
				pinger.activate();

				status = CONNECTED;
				state = InterfaceState.CONNECTED;
				connectTime = new Date();
				connectThread.setName(toString());	// change thread name to reflect connected status
				logger.log("Interface " + id + " (to " + peerAddress + ") connected to peer " + peerAddress + " (" + host.getHostName() + ":" + port + ")", Log.SEVERITY_INFO);

				boolean readerFinished = false;
				boolean writerFinished = false;
				while(!readerFinished || !writerFinished)
				{
					/**
					 * Wait until we're notified either by one of the
					 * communication threads, or by some external thread that
					 * invoked disable() on this interface. When that happens,
					 * signal both the reader and the writer to terminate
					 * (this can be done several times as their stop() method
					 * is idempotent). Loop until both have stopped.
					 */
					mutex.wait();
					pinger.deactivate();
					reader.stop();	// send the stop signal
					writer.stop();	// send the stop signal
					
					if(reader.isFinished() && readerFinished == false)
					{
						readerFinished = true;
						reader.join();
					}
					if(writer.isFinished() && writerFinished == false)
					{
						writerFinished = true;
						writer.join();
					}
				}
			}
			catch(IOException e)
			{
			}
			finally
			{
				status = RUNNING;	// no longer connected
				state = InterfaceState.ACTIVE;
				connectThread.setName(toString());	// change thread name to reflect connected status
				logger.log("Interface " + id + " (to " + peerAddress + ") disconnected from peer.", Log.SEVERITY_INFO);
			}
		}
	}
	
	/**
	 * <P>
	 * Checks to see whether this interface is currently connected to its
	 * peer.
	 * </P>
	 * 
	 * @return	true if this interface has established a connection to its
	 * 	peer or false if not.
	 */
	public boolean isConnected()
	{
		return status == CONNECTED;
	}
	
	public InterfaceState getState()
	{
		return state;
	}

	/**
	 * <P>
	 * Used by JMX. Returns a string describing the current state of the
	 * interface such as "Interface enabled, but not connected.".
	 * </P>
	 * 
	 * @return	a string describing the current state of the interface.
	 */
	public String getStatusString()
	{
		synchronized(mutex)
		{
			switch(status)
			{
				case DISABLED:
					return "Interface disabled.";
				case ACCEPTING:
					return "Interface enabled and waiting for incoming connection from peer.";
				case CONNECTED:
					return "Interface enabled and connected to " + peerAddress + " (" + socket.getInetAddress().getHostName() + ":" + socket.getLocalPort() + ")";
				case RUNNING:
					return "Interface enabled, but not connected.";
				default:
					return "Interface in unknown state (" + status + ").";
			}
		}
	}
	
	/**
	 * <P>
	 * Returns the cost metric for this interface. A special value
	 * DistanceVector.UNREACHABLE is defined to identify an infinite cost.
	 * </P>
	 * 
	 * @return
	 */
	public int getCostMetric()
	{
		return pinger.getCostMetric();
	}

	/**
	 * <P>
	 * Returns the Observable instance that offers notifications on incoming
	 * messages and new link cost values.
	 * </P>
	 */
	public Observable getObservable()
	{
		return this;
	}

	/**
	 * <P>
	 * This method is invoked by the SocketReader class and the
	 * CostMetricUnit class when they received new messages or changed the
	 * link cost. It notifies all Observers (usually only the router
	 * central class).
	 * </P>
	 */
	public void statusChange()
	{
		setChanged();
		notifyObservers(peerAddress);
	}
	
	/**
	 * <P>
	 * Returns true is the send buffer of this interface is not full. This
	 * means the send() method can safely be invoked without risk of message
	 * loss.
	 * </P>
	 * 
	 * @return
	 */
	public boolean isReadyForWriting()
	{
		return !writer.isFull();
	}
	
	/**
	 * <P>
	 * Queues a message for transmission to this interface's peer.
	 * </P>
	 */
	public void send(Message message)
	{
		writer.send(message);
	}
	
	/**
	 * <P>
	 * Returns true when the interface has at least one pending message.
	 * </P>
	 * 
	 * @return	true when the interface has at least one pending message.
	 */
	public boolean isReadyForReading()
	{
		return reader.isReady();
	}
	
	public Message read() throws InterruptedException
	{
		return reader.read();
	}
	
	public long getTotalBytesIn()
	{
		return reader.getBytesTotal();
	}
	
	public long getTotalPacketsIn()
	{
		return reader.getPacketsTotal();
	}
	
	public long getTotalBytesOut()
	{
		return writer.getBytesTotal();
	}
	
	public long getTotalPacketsOut()
	{
		return writer.getPacketsTotal();
	}

	public String toString()
	{
		return "Interface thread of " + id + " (peer: " + peerAddress + " - " + getStatusString() + ")";
	}
	
	/**
	 * <P>
	 * MBean interface of the TCPInterface. It is implemented by the
	 * <code>ManageableTCPInterface</code> innerclass.
	 * </P>
	 * 
	 * @author Erik van Zijst - 10.oct.2003 - erik@marketxs.com
	 */
	public interface ManageableTCPInterfaceMBean
	{
		/**
		 * <P>
		 * Returns the id of this interface.
		 * </P>
		 * 
		 * @return the id of this interface.
		 */
		public String getId();

		public String getRemoteHost();

		/**
		 * <P>
		 * Returns the overlay address of the remote peer.
		 * </P>
		 * 
		 * @return the overlay address of the remote peer.
		 */
		public String getPeerAddress();
	
		public int getPort();
	
		/**
		 * <P>
		 * This method tells the interface to disconnect from its peer. This
		 * method does nothing when the interface isn't connected. It might take
		 * some time for the interface to really disconnect. After the interface
		 * disconnected, it automatically tries to reconnect.
		 * </P>
		 */
		public void reconnect();

		public InterfaceState getState();

		/**
		 * <P>
		 * Used by JMX. Returns a string describing the current state of the
		 * interface such as "Interface enabled, but not connected.".
		 * </P>
		 * 
		 * @return	a string describing the current state of the interface.
		 */
		public String getStatusString();
	
		/**
		 * <P>
		 * Returns the time and date at which the interface established a
		 * connection. Note that the return value is unspecified when the
		 * interface is not connected.
		 * </P>
		 * 
		 * @return
		 */
		public Date getConnectTime();

		/**
		 * <P>
		 * Returns the cost metric for this interface. A special value
		 * DistanceVector.UNREACHABLE is defined to identify an infinite cost.
		 * </P>
		 * 
		 * @return
		 */
		public int getCostMetric();

		public long getTotalBytesIn();
	
		public long getTotalBytesOut();
	
		public long getTotalPacketsIn();
	
		public long getTotalPacketsOut();
	}

	/**
	 * <P>
	 * MBean implementation for the TCPInterface.
	 * </P>
	 * 
	 * @author Erik van Zijst - erik@marketxs.com
	 * @version	0.5, 30.dec.2003
	 */
	public class ManageableTCPInterface implements ManageableTCPInterfaceMBean, ManagementHelperInterface
	{
		private String description = "The TCPInterface class is the actual implementation of an interface that " +
			"connects to a neighbor. It uses a single TCP connection for all transmissions.";
		private Map operationParameterNameMap = new HashMap();
		private Map attributeDescriptionMap = new HashMap();
		private Map operationDescriptionMap = new HashMap();
		private Map operationParameterDescriptionMap = new HashMap();

		public ManageableTCPInterface()
		{
			attributeDescriptionMap.put("ConnectTime", "The time and date at which the interface established a connection. Note that the return value is unspecified when the interface is not connected.");
			attributeDescriptionMap.put("CostMetric", "The cost metric for this interface. A special value DistanceVector.UNREACHABLE is defined to identify an infinite cost.");
			attributeDescriptionMap.put("Id", "The id of this interface.");
			attributeDescriptionMap.put("PeerAddress", "The overlay address of the remote peer (neighbor).");
			attributeDescriptionMap.put("Port", "The TCP listen port of the neighbor. Usually 5000.");
			attributeDescriptionMap.put("RemoteHost", "The hostname or IP address of the neighbor.");
			attributeDescriptionMap.put("StatusString", "Returns a string describing the current state of the interface such as: Interface enabled, but not connected.");
			attributeDescriptionMap.put("TotalBytesIn", "The total number of bytes that this interface has received since it was created.");
			attributeDescriptionMap.put("TotalBytesOut", "The total number of bytes that this interface has sent since it was created.");
			attributeDescriptionMap.put("TotalPacketsIn", "The total number of packets (or messages) that this interface has received since it was created.");
			attributeDescriptionMap.put("TotalPacketsOut", "The total number of packets (or messages) that this interface has sent since it was created.");
			attributeDescriptionMap.put("State", "Returns the current state the interface is in. Can be inactive, connecting, active, connected or unknown.");

			operationDescriptionMap.put("reconnect", "This method tells the interface to disconnect from its peer. This " +
		 		"method does nothing when the interface is not connected. It might take " +
		 		"some time for the interface to really disconnect. After the interface " +
		 		"disconnected, it automatically tries to reconnect.");
		}
		
		public String getId()
		{
			return id;
		}
		
		public String getPeerAddress()
		{
			return peerAddress.toString();
		}
		
		public String getRemoteHost()
		{
			return host.getHostName();
		}
		
		public int getPort()
		{
			return port;
		}
		
		public int getCostMetric()
		{
			return TCPInterface.this.getCostMetric();
		}

		public InterfaceState getState()
		{
			return state;
		}
		
		public String getStatusString()
		{
			return TCPInterface.this.getStatusString();
		}
	
		/**
		 * <P>
		 * Returns the time and date at which the interface established a
		 * connection. Note that the return value is unspecified when the
		 * interface is not connected.
		 * </P>
		 * 
		 * @return
		 */
		public Date getConnectTime()
		{
			return connectTime;
		}
	
		/**
		 * <P>
		 * This method tells the interface to disconnect from its peer. This
		 * method does nothing when the interface isn't connected. It might take
		 * some time for the interface to really disconnect. After the interface
		 * disconnected, it automatically tries to reconnect.
		 * This method is only accessible on JMX.
		 * </P>
		 */
		public void reconnect()
		{
			synchronized(TCPInterface.this.mutex)
			{
				if(status == CONNECTED)
				{
					cmd = DISCONNECT;
					logger.log("Sending interface " + id + " (to " + peerAddress + ") the command to reconnect.", Log.SEVERITY_INFO);
					TCPInterface.this.mutex.notify();
				}
			}
		}

		public long getTotalBytesIn()
		{
			return reader.getBytesTotal();
		}
	
		public long getTotalPacketsIn()
		{
			return reader.getPacketsTotal();
		}
	
		public long getTotalBytesOut()
		{
			return writer.getBytesTotal();
		}
	
		public long getTotalPacketsOut()
		{
			return writer.getPacketsTotal();
		}
		
		/**
		 * Returns the description string that is displayed in JMX.
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
}
