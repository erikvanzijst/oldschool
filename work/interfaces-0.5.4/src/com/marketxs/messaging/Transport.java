package com.marketxs.messaging;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;
import com.marketxs.log.handler.StdOutLogHandler;
import com.marketxs.management.ManagementHelper;
import com.marketxs.management.ManagementHelperException;

/**
 * <P>
 * The Transport class is the heart of the messaging library. It is the strut
 * that is used by user applications for sending and receiving messages. Using
 * this class is simple, as it initializes itself before it is used.
 * </P>
 * 
 * <P>
 * The transport class also starts up the JMX access adaptors. It runs both
 * the HTML adaptor and the RMI adaptor. These can be configured using the
 * properties:
 * </P>
 * 
 * <P>
 * <LI><code>com.marketxs.messaging.jmxUser = administrator</code>
 * <LI><code>com.marketxs.messaging.jmxPass = secret</code>
 * <LI><code>com.marketxs.messaging.jmxHtmlPort = 8082</code>
 * <LI><code>com.marketxs.messaging.jmxRmiPort = 1099</code>
 * </P>
 * 
 * @location	api
 * @author Erik van Zijst - erik@marketxs.com - 22.oct.2003
 */
public class Transport
{
	static
	{
		Class handler = StdOutLogHandler.class;	// initialize Leon's logging api if it wasn't initialized already
	}
	
	private static Transport _ref = null;
	private final Log logger = Log.instance(Transport.class);
	private final Kernel kernel = Kernel.getInstance();
	private final MessagingConfigurator transportConfig = new MessagingConfigurator(kernel);
	
	private String jmxUser = "administrator";
	private String jmxPass = "secret";
	private int jmxHtmlPort = 8082;
	private int jmxRmiPort = 1099;

	/**
	 * This is the first method that is invoked when the messaging library is
	 * first accessed. It is used to do one-time initialization such as
	 * setting up the JMX adaptors.
	 * 
	 */
	private Transport()
	{
		jmxUser = Configurator.getProperty("com.marketxs.messaging.jmxUser", jmxUser);
		jmxPass = Configurator.getProperty("com.marketxs.messaging.jmxPass", jmxPass);
		
		try
		{
			jmxHtmlPort = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.jmxHtmlPort", String.valueOf(jmxHtmlPort)));
		}
		catch(Exception e)
		{
		}
		
		try
		{
			jmxRmiPort = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.jmxRmiPort", String.valueOf(jmxRmiPort)));
		}
		catch(Exception e)
		{
		}

		try
		{
			ManagementHelper.runHtmlAdaptor(jmxUser, jmxPass, jmxHtmlPort);
			logger.log("JMX HTML adaptor started on port " + jmxHtmlPort, Log.SEVERITY_INFO);
		}
		catch(ManagementHelperException e)
		{
			logger.log("Unable to start the JMX HTML adaptor: " + LogUtil.getStackTrace(e), Log.SEVERITY_ERROR);
		}
		
		try
		{
			ManagementHelper.runRmiConnector(jmxRmiPort);
			logger.log("JMX RMI adaptor started on port " + jmxRmiPort, Log.SEVERITY_INFO);
		}
		catch(ManagementHelperException e)
		{
			logger.log("Unable to start the JMX RMI adaptor: " + LogUtil.getStackTrace(e), Log.SEVERITY_ERROR);
		}
		
		logger.log("Messaging library instantiated.", Log.SEVERITY_INFO);
	}

	public static synchronized Transport getInstance()
	{
		if(_ref == null)
			_ref = new Transport();
		
		return _ref;
	}
	
	public Address getLocalAddress()
	{
		return kernel.getRouterAddress();
	}
	
	/**
	 * Registers Handler h as an observer for the given address. When
	 * address is a broadcast address, h will be invoked for every packet
	 * that was addressed or published to that address, but only if the
	 * packet was received according to the spanning tree from the source.
	 * When the same handler is subscribed to the same address n-times, it
	 * will be invoked n-times for every packet that matches.
	 */
	public void subscribe(Handler h, Address address)
	{
		kernel.subscribe(h, address);
	}

	/**
	 * Deregisters Handler h that was previously registered as an observer
	 * for the specified address. If the same handler was registered for
	 * the same address more than once, this call will remove one
	 * subscription only.
	 */
	public void unsubscribe(Handler h, Address address)
	{
		kernel.unsubscribe(h, address);
	}

	/**
	 * Submits a UserData message to the kernel for transmission. This method
	 * simply puts the message a message buffer of the kernel and then
	 * notifies the kernel. The message will be processed when the kernel
	 * thread has time.
	 * 
	 * @param packet	a UserData message that needs to be transmitted.
	 */
	public void send(Message packet) throws InterruptedException
	{
		kernel.send(packet);
	}
	
	/**
	 * Returns the MessagingConfigurator that allows the user application to
	 * change the library's interface configuration. Normally, the library
	 * will be configured using the configuration file, environment variables
	 * or an external JMX client, but it is not recommended to let the
	 * application tamper with the core's configuration autonomously.
	 * 
	 * @return
	 */
	public MessagingConfigurator getConfigurator()
	{
		return transportConfig;
	}
}
