package com.marketxs.messaging;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;
import com.marketxs.management.ManagementHelper;
import com.marketxs.management.ManagementHelperException;
import com.marketxs.management.ManagementHelperInterface;

/**
 * <P>
 * This is the class that is exposed in JMX. It offers functionality to add
 * and remove interfaces and to view an interface's status. Underneath, this
 * class delegates all configuration actions to the MessagingConfigurator
 * class that deals with the actual reconfiguration. That class is also
 * thread-safe so the application can be reconfigured simultaneously by
 * different clients without race conditions.
 * </P>
 * 
 * <P>
 * The configurator is registers itself in JMX under the name
 * "messaging:type=Configurator". Note that it there is no way to deregister
 * the instance once it has been instantiated.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com - 22.oct.2003
 */
public class ManagableConfigurator implements ManagementHelperInterface, ManagableConfiguratorMBean
{
	private MessagingConfigurator config = null;
	private Log logger = Log.instance(ManagableConfigurator.class);
	private final String jmxName = "messaging:type=Configurator";
	
	private String description = "Configures the interface setup of the MarketXS messaging library.";
	private Map operationParameterNameMap = new HashMap();
	private Map attributeDescriptionMap = new HashMap();
	private Map operationDescriptionMap = new HashMap();
	private Map operationParameterDescriptionMap = new HashMap();
	
	public ManagableConfigurator(MessagingConfigurator _config)
	{
		config = _config;
		
		attributeDescriptionMap.put("Interfaces", "Returns an array of strings that each contain information about a single interface. The format of the strings is as follows: 0 (tr25.iface4 @ tr25.marketxs.com:5000): Interface enabled, but not connected.");
		attributeDescriptionMap.put("State", "The current state of the messaging library. When started, the router kernel will be running and interfaces can be created.");
		attributeDescriptionMap.put("RouterAddress", "This is the overlay address of the messaging library as a whole. It must be unique throughout the entire network. If changed, it will only become active after the library has been restarted using the start and stop operations.");
		
		operationDescriptionMap.put("start", "Activates the messaging library. After activation, interfaces can be added. When stopped, the library only runs a minimal of threads and does not participate in the network.");
		operationDescriptionMap.put("stop", "Deactivates the messaging library. After deactivation, interfaces will be gone and the router kernel stopped. It does no longer participate in the network as a node.");
		operationDescriptionMap.put("addInterface", "Adds a new interface to the router kernel. The interface id must be locally unique at the node level. Note that only " + Kernel.MAX_INTERFACES + " interfaces can be added.");
		operationDescriptionMap.put("removeInterface", "Removes the interface with the specified id. This method throws an InterfaceIDException when there is no interface configured with the given id. Always deactivate the interface first!");
		operationDescriptionMap.put("activateInterface", "Activates the interface with the specified id. When an interface is activated, it will actively try to establish a connection with its peer. When activated, the interface will use one or more internal daemon threads.");
		operationDescriptionMap.put("deactivateInterface", "Deactivates the interface with the specified id. When an interface is deactivated, it will disconnect from its peer and not try to reconnect anymore. When an interface is deactivated it does not use any internal daemon threads or other active resources. When there is no interface configured with the given id, this method throws an InterfaceIDException.");
		operationDescriptionMap.put("forceRouterAddress", "While the RouterAddress attribute simply tells the router to use a new address at next startup, this operation will actually force the router to be restarted. Also, it restores the interface configuration. In short: when this method completes successfully, the router will be in the same state as it was before invocation.");
		operationDescriptionMap.put("removeInterfaces", "Removes all interfaces, active or inactive. It is essentially an atomic version of iterating over the interfaces, removing them individually.");

		operationParameterDescriptionMap.put("addInterface", new String[]{"The id of the new interface. Must be unique among the interfaces of this node.", "The logical address of the peer that the new interface will be connected to.", "The hostname of the machine running the neighbor node.", "The listening port of the neighbor. Together with the hostname this is the IP network address of the neighbor node."});
		operationParameterDescriptionMap.put("removeInterface", new String[]{"The id of an existing local interface."});
		operationParameterDescriptionMap.put("activateInterface", new String[]{"The id of an existing local interface that is currently deactivated and is to be activated."});
		operationParameterDescriptionMap.put("deactivateInterface", new String[]{"The id of an existing local interface that is currently activated and needs to be deactivated."});
		operationParameterDescriptionMap.put("forceRouterAddress", new String[]{"This is the overlay address of the messaging library as a whole. It must be unique throughout the entire network."});
		operationParameterDescriptionMap.put("removeInterfaces", new String[]{});
		
		operationParameterNameMap.put("addInterface", new String[]{"id", "peerAddress", "hostname", "port"});
		operationParameterNameMap.put("removeInterface", new String[]{"id"});
		operationParameterNameMap.put("activateInterface", new String[]{"id"});
		operationParameterNameMap.put("deactivateInterface", new String[]{"id"});
		operationParameterNameMap.put("forceRouterAddress", new String[]{"routerAddress"});
		operationParameterNameMap.put("removeInterfaces", new String[]{});
		
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
	
	/**
	 * <P>
	 * Returns the current state of the messaging library.
	 * </P>
	 * 
	 * @return	the current library state.
	 */
	public State getState()
	{
		return config.getState();
	}
	
	/**
	 * <P>
	 * Starts up the messaging library. This spawns the kernel thread and
	 * allows you to start adding interfaces.
	 * </P>
	 * 
	 * @throws InvalidStateException
	 */
	public void start() throws InvalidStateException
	{
		config.start();
	}
	
	/**
	 * <P>
	 * Stops the messaging library. When this method returns successfully, the
	 * kernel (router) thread and the TCPListener will be stopped and the
	 * interfaces disabled and removed.
	 * </P>
	 * 
	 * @throws InvalidStateException	when the library is not currently
	 * 	running.
	 */
	public void stop() throws InvalidStateException, InterruptedException
	{
		config.stop();
	}
	
	/**
	 * <P>
	 * Returns the router address that will be used after the next invocation
	 * of the activate() method.
	 * </P>
	 * 
	 * @return	the new router address.
	 */
	public String getRouterAddress()
	{
		return config.getRouterAddress().toString();
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
	public void setRouterAddress(String addr) throws InvalidAddressException
	{
		config.setRouterAddress(new Address(addr));
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
	public void forceRouterAddress(String addr) throws InvalidAddressException, InterruptedException
	{
		config.forceRouterAddress(new Address(addr));
	}
	
	/**
	 * <P>
	 * Returns an array of strings that each contain information about a
	 * single interface. The format of the strings is as follows:
	 * </P>
	 * <code>0 (tr25.iface4 @ tr25.marketxs.com:5000): Interface enabled, but not connected.</code>
	 * 
	 * @return	an array of strings that each contain one interface.
	 */
	public String[] getInterfaces()
	{
		PeerInterface[] ifaces = config.getInterfaces();
		String[] lines = new String[ifaces.length];
		
		try
		{
			for(int nX = 0; nX < lines.length; nX++)
			{
				lines[nX] = ifaces[nX].getId() + " (" + ifaces[nX].getPeerAddress() + " @ " + ifaces[nX].getRemoteHost().getHostAddress() + ":" + ifaces[nX].getPort() + "): " + ifaces[nX].getStatusString();
			}
		}
		catch(RuntimeException re)
		{
			logger.log("Error displaying interface list: " + LogUtil.getStackTrace(re), Log.SEVERITY_ERROR);
			throw re;
		}
		
		return lines;
	}
	
	/**
	 * <P>
	 * Adds a new interface to the router kernel. The interface's id must
	 * be unique for this node.
	 * </P>
	 * 
	 * @param id	the id of the new interface.
	 * @param peerAddress	the address of the peer interface this interface
	 * 	will connect to.
	 * @param hostname	the hostname or IP address of the neighbor node.
	 * @param port	the IP port number of the neighbor node.
	 * @throws InvalidAddressException	when the peerAddress is
	 * 	syntactically incorrect.
	 * @throws InterfaceIDException	when the specified id is not locally
	 * 	unique.
	 */
	public void addInterface(String id, String peerAddress, String hostname, int port) throws InterfaceIDException, UnknownHostException, InvalidAddressException
	{
		try
		{
			Address peerAddr = new Address(peerAddress);
			InetAddress host = InetAddress.getByName(hostname);
		
			PeerInterface iface = new TCPInterface(id);
			iface.setPeerAddress(peerAddr);
			iface.setRemoteHost(host);
			iface.setPort(port);
			
			config.addInterface(iface);
		}
		catch(UnknownHostException e)
		{
			logger.log("The specified hostname or IP address was invalid: " + e.getMessage(), Log.SEVERITY_ERROR);
			throw e;
		}
		catch(InvalidAddressException e)
		{
			logger.log("Invalid address specified: " + e.getMessage(), Log.SEVERITY_ERROR);
			throw e;
		}
	}
	
	/**
	 * <P>
	 * Removes the interface with the specified id. This method throws an
	 * InterfaceIDException when there is no interface configured with the
	 * given id.
	 * </P>
	 * 
	 * @param id	the id of the interface that is to be removed.
	 * @throws InterfaceIDException	when the specified id is not locally
	 * 	unique.
	 */
	public void removeInterface(String id) throws InterfaceIDException
	{
		try
		{
			config.removeInterface(id);
		}
		catch(InterfaceIDException e)
		{
			logger.log("Interface " + id + " does not exist and hence could not be removed.", Log.SEVERITY_ERROR);
			throw e;
		}
	}
	
	/**
	 * <P>
	 * Activates the interface with the specified id. When an interface is
	 * activated, it will actively try to establish a connection with its
	 * peer. When activated, the interface will use one or more internal 
	 * daemon threads.
	 * </P>
	 * 
	 * @param id	the id of the interface that is to be activated.
	 * @throws InterfaceIDException	when the specified id is not locally
	 * 	unique.
	 */
	public void activateInterface(String id) throws InterfaceIDException
	{
		try
		{
			config.activateInterface(id);
		}
		catch(InterfaceIDException e)
		{
			logger.log("Interface " + id + " does not exist and hence could not be activated.", Log.SEVERITY_ERROR);
			throw e;
		}
	}
	
	/**
	 * <P>
	 * Deactivates the interface with the specified id. When an interface
	 * is deactivated, it will disconnect from its peer and not try to
	 * reconnect anymore. When an interface is deactivated it does not use any
	 * internal daemon threads or other active resources. When there is no
	 * interface configured with the given address, this method throws an
	 * <code>InterfaceIDException</code>.
	 * </P>
	 * 
	 * @param id	the id of the interface that is to be removed.
	 * @throws InterfaceIDException	when the specified id is not locally
	 * 	unique.
	 */
	public void deactivateInterface(String id) throws InterfaceIDException
	{
		try
		{
			config.deactivateInterface(id);
		}
		catch(InterfaceIDException e)
		{
			logger.log("Interface " + id + " does not exist and hence could not be deactivated.", Log.SEVERITY_ERROR);
			throw e;
		}
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
	public void removeInterfaces()
	{
		try
		{
			config.removeInterfaces();
		}
		catch(RuntimeException e)
		{
			logger.log("RemoveInterfaces() failed: " + LogUtil.getStackTrace(e), Log.SEVERITY_ERROR);
			throw e;
		}
	}
	
	/**
	 * <P>
	 * Returns the description string that is displayed in JMX.
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
