package com.marketxs.messaging;

import java.net.UnknownHostException;

/**
 * 
 * @author Erik van Zijst - erik@marketxs.com - 22.oct.2003
 */
public interface ManagableConfiguratorMBean
{
	/**
	 * <P>
	 * Returns the current state of the messaging library.
	 * </P>
	 * 
	 * @return	the current library state.
	 */
	public State getState();
	
	/**
	 * <P>
	 * Starts up the messaging library. This spawns the kernel thread and
	 * allows you to start adding interfaces.
	 * </P>
	 * 
	 * @throws InvalidStateException
	 */
	public void start() throws InvalidStateException;
	
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
	public void stop() throws InvalidStateException, InterruptedException;
	
	/**
	 * <P>
	 * Returns the router address that will be used after the next invocation
	 * of the activate() method.
	 * </P>
	 * 
	 * @return	the new router address.
	 */
	public String getRouterAddress();
	
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
	public void setRouterAddress(String addr) throws InvalidAddressException;

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
	public void forceRouterAddress(String addr) throws InvalidAddressException, InterruptedException;

	/**
	 * <P>
	 * Returns an array of strings that each contain information about a
	 * single interface. The format of the strings is as follows:
	 * </P>
	 * <code>0 (tr25.iface4 @ tr25.marketxs.com:5000): Interface enabled, but not connected.</code>
	 * 
	 * @return	an array of strings that each contain one interface.
	 */
	public String[] getInterfaces();
	
	/**
	 * Adds a new interface to the router kernel. The interface's id must
	 * be unique among the node's interfaces.
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
	public void addInterface(String id, String peerAddress, String hostname, int port) throws UnknownHostException, InterfaceIDException, InvalidAddressException;
	
	/**
	 * Removes the interface with the specified id. This method throws an
	 * InterfaceIDException when there is no interface configured with the
	 * given id.
	 * 
	 * @param id	the id of the interface that is to be removed.
	 * @throws InterfaceIDException	when the specified id does not match that
	 * 	of any existing interface.
	 */
	public void removeInterface(String id) throws InterfaceIDException;
	
	/**
	 * Activates the interface with the specified id. When an interface is
	 * activated, it will actively try to establish a connection with its
	 * peer. When activated, the interface will use one or more internal 
	 * daemon threads.
	 * 
	 * @param id	the id of the interface that is to be activated.
	 * @throws InterfaceIDException	when the specified id does not match that
	 * 	of any existing interface.
	 */
	public void activateInterface(String id) throws InterfaceIDException;
	
	/**
	 * Deactivates the interface with the specified id. When an interface
	 * is deactivated, it will disconnect from its peer and not try to
	 * reconnect anymore. When an interface is deactivated it does not use any
	 * internal daemon threads or other active resources. When there is no
	 * interface configured with the given id, this method throws an
	 * <code>InterfaceIDException</code>.
	 * 
	 * @param id	the id of the interface that is to be removed.
	 * @throws InterfaceIDException	when the specified id does not match that
	 * 	of any existing interface.
	 */
	public void deactivateInterface(String id) throws InterfaceIDException;
	
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
	public void removeInterfaces();
}
