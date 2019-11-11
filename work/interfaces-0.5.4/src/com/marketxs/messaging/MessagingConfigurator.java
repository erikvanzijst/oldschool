package com.marketxs.messaging;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;
import com.marketxs.management.ManagementHelper;
import com.marketxs.management.ManagementHelperException;

/**
 * <P>
 * This class is used to configure the messaging library. It is instantiated
 * by the Transport class when the messaging library is first accessed. It
 * first tries to find any interface configuration information using the
 * Configurator class. The information it looks for describes interface
 * settings:
 * </P>
 * 
 * <P>
 * <LI><code>com.marketxs.messaging.interface.ids = 0,1</code>
 * <LI><code>tr19.iface1.peerHost = tr21.marketxs.com</code>
 * <LI><code>tr19.iface1.peerPort = 5000</code>
 * <LI><code>tr19.iface1.id = 0</code>
 * <LI><code>tr19.iface1.costMultiplier = 2.5</code>
 * <LI><code>tr19.iface2.peerHost = tr25.marketxs.com</code>
 * <LI><code>tr19.iface2.peerPort = 5000</code>
 * <LI><code>tr19.iface2.id = 1</code>
 * <LI><code>tr19.iface2.costMultiplier = 1.0</code>
 * </P>
 * 
 * <P>
 * The above configuration defines two interfaces: 0 and
 * 1. The first interface is configured to connect to host
 * tr21.marketxs.com on port 5000 and the peer interface should have the
 * address tr21.iface1. The second interface connects to host
 * tr25.marketxs.com on port 5000 and the peer interface should have the
 * address tr25.iface4. The costMultiplier can be configured per interface and
 * multiplies the real ping values by this value to derive the metric exposed
 * to the kernel. The default multiplier value is 1. The property is read by
 * the CostMetricUnit class.
 * </P>
 * 
 * <P>
 * It is also possible to manually (re)configure the MessagingConfigurator
 * using the class' method for adding and removing interfaces. This is not
 * recommended however. The configurator also starts a JMX module that
 * exposes the configurator's functionality on JMX. This way, the messaging
 * library can be reconfigured at runtime using external clients.
 * </P>
 * 
 * @location	api
 * @author Erik van Zijst - erik@marketxs.com - 22.oct.2003
 */
public class MessagingConfigurator
{
	private Kernel kernel;
	private Log logger = Log.instance(MessagingConfigurator.class);
	private Set ifaces = new HashSet();
	private State state = State.STOPPED;
	private Address newAddress = Address.RESERVED;
	
	private ManagableConfigurator mbean = new ManagableConfigurator(this);
	
	public MessagingConfigurator(Kernel kernel)
	{
		synchronized(this)
		{
			this.kernel = kernel;
			start();	// start the kernel thread.
			init();	// load interface configuration from the config file
			state = State.STARTED;
		}
	}
	
	/**
	 * <P>
	 * Loads the configuration from the configuration file and starts the
	 * kernel. This method is invoked automatically when the library is
	 * initialized.
	 * </P>
	 */
	private void init()
	{
		StringTokenizer tok = new StringTokenizer(Configurator.getProperty("com.marketxs.messaging.interface.ids", ""), ", ");
		int count = 0;
		
		while(tok.hasMoreTokens())
		{
			String id = tok.nextToken();
			try
			{
				InetAddress peerHost = InetAddress.getByName(Configurator.getProperty("com.marketxs.messaging.interface." + id + ".peerHost"));
				int peerPort = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.interface." + id + ".peerPort"));
				Address peerAddress = new Address(Configurator.getProperty("com.marketxs.messaging.interface." + id + ".peerAddress"));
				
				PeerInterface peer = new TCPInterface(id);	// the use of TCPInterface is hardcoded at this point
				peer.setPeerAddress(peerAddress);
				peer.setRemoteHost(peerHost);
				peer.setPort(peerPort);
				
				addInterface(peer);
				count++;
			}
			catch(NullPointerException e)
			{
				logger.log("Missing interface configuration property for interface " + id + ". Make sure properties \"com.marketxs.messaging.interface." + id + ".peerAddress\", \"com.marketxs.messaging.interface." + id + ".peerPort\" and \"com.marketxs.messaging.interface." + id + ".peerAddress\" are specified and valid.", Log.SEVERITY_ERROR);
			}
			catch(NumberFormatException e)
			{
				logger.log("Invalid port number for interface " + id + " specified.", Log.SEVERITY_ERROR);
			}
			catch(UnknownHostException e)
			{
				logger.log("Invalid hostname or IP address specified for interface " + id + ": " + e.getMessage(), Log.SEVERITY_ERROR);
			}
		}
		
		logger.log(count + " default interfaces configured.", Log.SEVERITY_INFO);
		
		// register the utility applications
		try
		{
			// the ping utility
			if(!ManagementHelper.isRegistered("utils:type=PingService"))
			{
				ManagementHelper.register("utils:type=PingService", (new PingService()).getMBean());
			}
		}
		catch(ManagementHelperException mhe)
		{
			logger.log("Unable to register \"utils:type=PingService\" in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
		}

		try
		{
			// the traceroute utility
			if(!ManagementHelper.isRegistered("utils:type=Traceroute"))
			{
				ManagementHelper.register("utils:type=Traceroute", (new TracerouteService()).getMBean());
			}
		}
		catch(ManagementHelperException mhe)
		{
			logger.log("Unable to register \"utils:type=Traceroute\" in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
		}
	}
	
	/**
	 * <P>
	 * Returns the router address that will be used after the next invocation
	 * of the activate() method.
	 * </P>
	 * 
	 * @return	the new router address.
	 */
	public synchronized Address getRouterAddress()
	{
		return newAddress;
	}
	
	/**
	 * <P>
	 * Use this method to specifiy the router's address. Note that this
	 * address will become active only after the router has been deactivated
	 * first. The Kernel MBean shows the real address used by the router.
	 * </P>
	 * 
	 * @param addr	the address to be used after the next activate()
	 * 	invocation.
	 * @throws InvalidAddressException	when the the specified address is
	 * 	invalid.
	 */
	public synchronized void setRouterAddress(Address addr) throws InvalidAddressException
	{
		if(addr.isReserved())
		{
			throw new InvalidAddressException("Address may not be an empty string.");
		}
		
		newAddress = addr;
	}
	
	/**
	 * <P>
	 * While the setRouterAddress() method simply tells the router to use a
	 * new address at next startup, this method will actually force the router
	 * to be restarted. Also, it restores the interface configuration. In
	 * short: when this method completes successfully, the router will be in
	 * the same state as it was before invocation.
	 * </P>
	 * 
	 * @param addr
	 * @throws InvalidAddressException
	 * @throws InterruptedException
	 */
	public synchronized void forceRouterAddress(Address addr) throws InvalidAddressException, InterruptedException
	{
		try
		{
			long start = System.currentTimeMillis();
			logger.log("Forcing a new router address.", Log.SEVERITY_INFO);
			setRouterAddress(addr);
		
			if(state.equals(State.STARTED))
			{
				PeerInterface[] oldIfaces = getInterfaces();
				stop();	// automatically destroys all interfaces
				start();
			
				// restore interface configuration
				for(int nX = 0; nX < oldIfaces.length; nX++)
				{
					TCPInterface newIface = new TCPInterface(oldIfaces[nX].getId());
					newIface.setPeerAddress(oldIfaces[nX].getPeerAddress());
					newIface.setRemoteHost(oldIfaces[nX].getRemoteHost());
					newIface.setPort(oldIfaces[nX].getPort());
				
					addInterface(newIface);
				}
			}
		
			logger.log("Router address successfully set to " + addr.toString() + " and router state restored in " + (System.currentTimeMillis() - start) + " ms.", Log.SEVERITY_INFO);
		}
		catch(RuntimeException re)
		{
			logger.log("Unexpected exception during forceRouterAddress(). Router may be left in an unusable state: " + LogUtil.getStackTrace(re), Log.SEVERITY_CRITICAL);
			throw re;
		}
	}

	/**
	 * <P>
	 * Starts up the messaging library. This spawns the kernel thread and
	 * allows you to start adding interfaces.
	 * </P>
	 * 
	 * @throws InvalidStateException	if the library is not currently in the
	 * 	stopped state.
	 */
	public synchronized void start() throws InvalidStateException
	{
		if(state.equals(State.STOPPED))
		{
			if(kernel.getState().equals(State.STOPPED))
			{
				if(!newAddress.isReserved())
				{
					kernel.setRouterAddress(newAddress);
				}
				kernel.activate();
			}
			// we don't need to start the TCPListener, it'll start itself
			
			state = State.STARTED;
		}
		else
		{
			throw new InvalidStateException("Library must be stopped first.");
		}
	}
	
	/**
	 * <P>
	 * Stops the messaging library. When this method returns successfully, the
	 * kernel (router) thread and the TCPListener will be stopped and the
	 * interfaces disabled and removed. If the router address needs to be
	 * changed, now is a good time.
	 * </P>
	 * 
	 * @throws InvalidStateException	when the library is not currently
	 * 	running.
	 */
	public synchronized void stop() throws InvalidStateException, InterruptedException
	{
		if(state.equals(State.STARTED))
		{
			try
			{
				// first kill all interfaces
				logger.log("Removing all interfaces...", Log.SEVERITY_DEBUG);
				Iterator it = (new ArrayList(ifaces)).iterator();	// iterate over a shallow copy
				while(it.hasNext())
				{
					PeerInterface _iface = (PeerInterface)it.next();
					logger.log("Removing interface " + _iface.getId(), Log.SEVERITY_DEBUG);
					removeInterface(_iface.getId());
				}
			
				try
				{
					// stop the listener, together with all initializers
					logger.log("Shutting down the TCPListener.", Log.SEVERITY_DEBUG);
					TCPListener.getInstance().stopListener();
				}
				catch(InterruptedException e)
				{
					logger.log("MessagingConfigurator.stop() was interrupted while waiting for the TCPListener to shutdown: " + LogUtil.getStackTrace(e), Log.SEVERITY_ERROR);
					throw e;
				}

				try
				{
					// stop the kernel thread
					if(kernel.getState().equals(State.STARTED))
					{
						logger.log("Shutting down the kernel thread.", Log.SEVERITY_DEBUG);
						kernel.deactivate();
					}
				}
				catch(InterruptedException e)
				{
					logger.log("MessagingConfigurator.stop() was interrupted while waiting for the kernel to shutdown: " + LogUtil.getStackTrace(e), Log.SEVERITY_ERROR);
					throw e;
				}
				state = State.STOPPED;
			}
			catch(RuntimeException re)
			{
				logger.log("RuntimeException while stopping the messaging library: " + LogUtil.getStackTrace(re), Log.SEVERITY_ERROR);
				throw re;
			}
		}
		else
		{
			throw new InvalidStateException("Library must be running for this method to work.");
		}
	}

	/**
	 * <P>
	 * Returns the current state of the messaging library.
	 * </P>
	 * 
	 * @return	the current library state.
	 */
	public synchronized State getState()
	{
		return state;
	}
	
	/**
	 * <P>
	 * Adds a new interface to the library's configuration. The interface must
	 * have a locally unique id. If there already is an interface with the same
	 * address as the given one, the method returns false and the
	 * configuration is not changed.
	 * </P>
	 * 
	 * @param iface	a new interface instance that is to be added.
	 * @exception	InterfaceIDException when the new interface's id is not
	 * 	unique.
	 * @exception	InvalidAddressException	when the interface's peer address
	 * 	is already used by an existing interface.
	 * @exception	InvalidStateException	when the messaging library is not
	 * 	currently in the started state.
	 */
	public synchronized void addInterface(PeerInterface iface) throws InterfaceIDException, InvalidStateException, InvalidAddressException
	{
		if(state.equals(State.STARTED))
		{
			Iterator it = ifaces.iterator();
			while(it.hasNext())
			{
				PeerInterface _iface = (PeerInterface)it.next();
				if(_iface.getId().equals(iface.getId()))
				{
					throw new InterfaceIDException("Interface ID not unique.");	// there already is an interface with this address
				}
				if(_iface.getPeerAddress().equals(iface.getPeerAddress()))
				{
					throw new InvalidAddressException("The interface's peer address is already in use by interface " + _iface.getId() + ". This software cannot support multiple identical links.");
				}
			}

			ifaces.add(iface);
			kernel.addInterface(iface);
			iface.enable();
		}
		else
		{
			throw new InvalidStateException("Router must be started before configuring interfaces.");
		}
	}
	
	/**
	 * <P>
	 * Removes the interface with the specified id. This method does
	 * nothing when there is no interface configured with the given id.
	 * </P>
	 * 
	 * @param ifaceAddress	the address of the interface that is to be removed.
	 * @exception	InterfaceIDException	when the specified id does not
	 * 	match that of any interface.
	 */
	public synchronized void removeInterface(String id) throws InterfaceIDException
	{
		Iterator it = ifaces.iterator();
		while(it.hasNext())
		{
			PeerInterface iface = (PeerInterface)it.next();
			if(iface.getId().equals(id))
			{
				iface.disable();
				kernel.removeInterface(iface);
				ifaces.remove(iface);
				return;
			}
		}
		
		throw new InterfaceIDException("Unknown id. Specify the unique id of an existing interface instance.");
	}
	
	/**
	 * <P>
	 * Activates an existing interface instace. When activated, the interface
	 * will spawn internal threads and try to establish a connection with its
	 * peer. The messaging library must be in the started state.
	 * </P>
	 * 
	 * @param ifaceAddress	the address of the interface that is to be
	 * 	activated.
	 * @throws	InterfaceIDException	when the specified id does not
	 * 	match that of any interface.
	 * @exception	InvalidStateException	when the messaging library is not
	 * 	currently in the started state.
	 */
	public synchronized void activateInterface(String id) throws InterfaceIDException, InvalidStateException
	{
		if(state.equals(State.STARTED))
		{
			Iterator it = ifaces.iterator();
			while(it.hasNext())
			{
				PeerInterface iface = (PeerInterface)it.next();
				if(iface.getId().equals(id))
				{
					iface.enable();
					return;
				}
			}
		
			throw new InterfaceIDException("Unknown id. Specify the unique id of an existing interface instance.");
		}
		else
		{
			throw new InvalidStateException("Router must be started before configuring interfaces.");
		}
	}
	
	/**
	 * <P>
	 * Deactivates an interface instance.
	 * </P>
	 * 
	 * @param ifaceAddress	the address of the interface that is to be
	 * 	deactivated.
	 * @throws	InterfaceIDException	when the specified id does not
	 * 	match that of any interface
	 */
	public synchronized void deactivateInterface(String id) throws InterfaceIDException
	{
		Iterator it = ifaces.iterator();
		while(it.hasNext())
		{
			PeerInterface iface = (PeerInterface)it.next();
			if(iface.getId().equals(id))
			{
				iface.disable();
				return;
			}
		}
		
		throw new InterfaceIDException("Unknown id. Specify the unique id of an existing interface instance.");
	}
	
	public synchronized PeerInterface[] getInterfaces()
	{
		return (PeerInterface[])ifaces.toArray(new PeerInterface[ifaces.size()]);
	}
	
	/**
	 * <P>
	 * Removes all interfaces, active or inactive. It is essentially an atomic
	 * version of:
	 * </P>
	 * <P>
	 * <PRE>
	 * ifaces = getInterfaces();
	 * foreach iface in ifaces
	 * do
	 * 	removeInterface(iface);
	 * done
	 * </PRE>
	 * </P>
	 *
	 */
	public synchronized void removeInterfaces()
	{
		PeerInterface[] _ifaces = getInterfaces();
		for (int i = 0; i < _ifaces.length; i++)
		{
			removeInterface(_ifaces[i].getId());
		}
	}
}
