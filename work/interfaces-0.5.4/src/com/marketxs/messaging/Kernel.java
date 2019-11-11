package com.marketxs.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;
import java.util.TreeMap;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;
import com.marketxs.management.ManagementHelper;
import com.marketxs.management.ManagementHelperException;
import com.marketxs.management.ManagementHelperInterface;

/**
 * <P>
 * This class implements the forwarding algorithm. It runs a daemon thread
 * that waits to be notified by the interfaces when new messages have arrived.
 * The kernel is implemented as a singleton class that starts up its thread
 * when it is instantiated (i.e. when getInstance() is first called). As long
 * as the kernel thread runs, it is registered in JMX under the name
 * <code>"messaging:type=Kernel"</code>. De kernel thread does not start by
 * default but is explicity started by the <code>MessagingConfigurator</code>
 * at library initialization.
 * </P>
 * <P>
 * The kernel can be configured using the following properties:
 * </P>
 * 
 * <LI><code>com.marketxs.messaging.Kernel.maxNodes = 1000</code>
 * <LI><code>com.marketxs.messaging.Kernel.sendBuffer = 1000</code>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.1, 13.oct.2003
 */
public class Kernel implements Observer, Runnable
{
	public static final int MAX_INTERFACES = 8;	// can be significantly higher in future versions
	public int MAX_NODES = 1000;
	private static Kernel _ref = null;
	private Thread thread = null;
	private final HashMap subscriptions = new HashMap();	// holds the address/handler mappings
	private TreeMap dt = new TreeMap();
	private final Object dtMutex = new Object();	// used to lock the dt during modifications
	private Log logger = Log.instance(Kernel.class);
	private final String jmxName = "messaging:type=Kernel";
	private final ManageableKernel kernelMBean = this.new ManageableKernel();
	private State state = State.STOPPED;
	private Address routerAddress = Address.RESERVED;
	private final Object runMutex = new Object();
	private boolean die = false;
	
	private int[] previousCost = new int[MAX_INTERFACES];	// used to detect cost changes
	private ObjectFIFO userDataBuffer = null;

	private PeerInterface[] ifaces = null;	// array that holds all interface objects
	private int nextIface = 0;	//points to the next interface that will be serviced, used by getActiveInterface()
	private final Set newIfaces = new HashSet();	// set that holds the new interfaces
	private final Object ifaceMutex = new Object();	// used to lock the ifaces array
	private boolean ifaceChange = false;	// true when an interface is added/removed
	private boolean advertise = false;	// true makes the kernel advertise its routes once
	private final Object advertiseMutex = new Object();	// used to lock the advertise boolean

	/**
	 * <P>
	 * Singleton factory method.
	 * </P>
	 */
	private Kernel()
	{
		try
		{
			MAX_NODES = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.Kernel.maxNodes", String.valueOf(MAX_NODES)));
		}
		catch(Exception e)
		{
		}
		
		int userDataBufferSize = 1000;
		try
		{
			userDataBufferSize = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.Kernel.sendBuffer", String.valueOf(userDataBufferSize)));
		}
		catch(Exception e)
		{
		}
		finally
		{
			userDataBuffer = new ObjectFIFO(userDataBufferSize);
		}
		
		ifaces = new PeerInterface[MAX_INTERFACES];
		for(int nX = 0; nX < MAX_INTERFACES; nX++)
			ifaces[nX] = null;
			
		try
		{
			if(!ManagementHelper.isRegistered(jmxName))
				ManagementHelper.register(jmxName, kernelMBean);
		}
		catch(ManagementHelperException mhe)
		{
			logger.log("Unable to register " + jmxName + " in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
		}
	}
	
	/**
	 * <P>
	 * Used to start the kernel thread. This method only works when the kernel is
	 * currently stopped. Calling this method when the kernel was already
	 * running results in an <code>InvalidStateException</code>.
	 * </P>
	 * 
	 * @throws InvalidStateException	if the kernel is not currently
	 * 	stopped.
	 */
	public void activate() throws InvalidStateException
	{
		synchronized(runMutex)
		{
			if(state.equals(State.STOPPED))
			{
				state = State.STARTING;
				if(routerAddress.isReserved())
				{
					// generate a random address for fallback
					StringBuffer buf = new StringBuffer("unconfigured.");
					Random rnd = new Random(System.currentTimeMillis());
					for(int nX = 0; nX < 10; nX++, buf.append(rnd.nextInt(10)));	// add 10 random digits
					Address rndAddress = new Address(buf.toString());
					
					try
					{
						routerAddress = new Address(Configurator.getProperty("com.marketxs.messaging.Kernel.routerAddress"));
					}
					catch(Exception e)
					{
					}
					
					if(routerAddress.isReserved())
					{
						routerAddress = rndAddress;
						logger.log("Configuration parameter \"com.marketxs.messaging.Kernel.routerAddress\" missing or invalid, using generated address \"" + routerAddress.toString() + "\" instead.", Log.SEVERITY_ERROR);
					}
				}
				
				die = false;
				thread = new Thread(this, "Kernel thread.");
				thread.setDaemon(true);
				thread.start();
				logger.log("Kernel thread started.", Log.SEVERITY_INFO);
			}
			else
			{
				throw new InvalidStateException("Kernel not stopped. Current state: " + state.getName());
			}
		}
	}
	
	/**
	 * <P>
	 * Shuts down the router core. This method only works when the kernel is
	 * currently running. Calling this method when the kernel was already
	 * stopped results in an <code>InvalidStateException</code>.
	 * </P>
	 * <P>
	 * This method waits for the kernel thread to terminate. This could take a
	 * while.
	 * </P>
	 *
	 * @throws InvalidStateException	if the kernel is not currently in the
	 * 	STARTED state.
	 * @throws InterruptedException	if the invocation was interrupted while
	 * 	waiting for the kernel thread to terminate.
	 */
	public void deactivate() throws InvalidStateException, InterruptedException
	{
		synchronized(runMutex)
		{
			if(state.equals(State.STARTED))
			{
				state = State.STOPPING;
				die = true;
				synchronized(this)
				{
					notify();
				}
				thread.join();
			}
			else
			{
				throw new InvalidStateException("Kernel not running. Current state: " + state.getName());
			}
		}
	}
	
	/**
	 * Singleton factory method.
	 * 
	 * @return
	 */
	public static synchronized Kernel getInstance()
	{
		if(_ref == null)
			_ref = new Kernel();
			
		return _ref;
	}
	
	/**
	 * <P>
	 * Use this method to change the router's address. It can only be used
	 * when the kernel is deactivated. Any attempt to use this method when the
	 * kernel is not in the <code>State.STOPPED</code> state results in an
	 * <code>InvalidStateException</code>.
	 * </P>
	 * 
	 * @param addr	the new address for the router kernel.
	 * @throws InvalidStateException	when the kernel is not currently
	 * 	stopped.
	 * @throws InvalidAddressException	when the specified address is not a
	 * 	valid unicast address.
	 */
	public void setRouterAddress(Address addr) throws InvalidStateException, InvalidAddressException
	{
		synchronized(runMutex)
		{
			if(state.equals(State.STOPPED))
			{
				if(addr == null || addr.isReserved() || addr.isMulticast())
				{
					throw new InvalidAddressException("Invalid address specified. Address must be unicast, not null and not reserved.");
				}
				else
				{
					routerAddress = addr;
				}
			}
			else
			{
				throw new InvalidStateException("Kernel thread must be stopped when configuring the address. Use deactivate() first.");
			}
		}
	}
	
	public Address getRouterAddress()
	{
		return routerAddress;
	}
	
	/**
	 * <P>
	 * Initializes the distance table. The first entries it stores are the 
	 * local address of the router (with cost 0 and no preferred
	 * hop) and the addresses of the router's neighbors (with a link cost
	 * equal to the interface's cost metric and our own address as the head).
	 * </P>
	 */
	private void initializeDT()
	{
		synchronized(dtMutex)
		{
			dt.clear();
			// add our own address first
			DistanceTable local = new DistanceTable();
			local.dest = routerAddress;
			local.setCost(0);
			dt.put(routerAddress, local);
			
			// our interfaces are the first entries
			for(int k = 0; k < MAX_INTERFACES; k++)
			{
				if(ifaces[k] != null)
				{
					// interface's peer address
					DistanceTable entry = new DistanceTable();
					entry.dest = ifaces[k].getPeerAddress().summarize(routerAddress);
					entry.setCost(ifaces[k].getCostMetric());
					entry.hop = k;
					entry.cost_via[k] = ifaces[k].getCostMetric();
					entry.head_via[k] = routerAddress;
					dt.put((Address)entry.dest, entry);
				}
			}
		}
		
		logger.log("Distance table: " + toString(), Log.SEVERITY_INFO);
	}
	
	/**
	 * <P>
	 * Main program loop of the kernel forwarding mechanism.
	 * </P>.
	 */
	public void run()
	{
		final VectorExchangeTask task = this.new VectorExchangeTask();
		
		try
		{
			initializeDT();
			Scheduler.getScheduler().schedule(task, task.INTERVAL, task.INTERVAL);
			
			state = State.STARTED;
	
			while(!die)
			{
				try
				{
					manageInterfaces();	// configure new interfaces that might have been added
					
					int iface_pos = -1;
					synchronized(this)
					{
						// wait for something to happen
						while((iface_pos = getActiveInterface()) == -1 && !ifaceChange && !advertise && userDataBuffer.isEmpty() && !die)
						{
							wait();
						}
					}
					
					if(iface_pos > -1 && ifaces[iface_pos].isReadyForReading())
					{
						// service the first interface that has pending messages
						if(processMessage(ifaces[iface_pos].read(), iface_pos))
						{
							synchronized(advertiseMutex)
							{
								advertise = true;
							}
						}
					}
					if(iface_pos > -1 && ifaces[iface_pos].getCostMetric() != previousCost[iface_pos])
					{
						// service the first interface that has a changed link cost
						if(updateMetric(iface_pos, previousCost[iface_pos], ifaces[iface_pos].getCostMetric()))
						{
							synchronized(advertiseMutex)
							{
								advertise = true;
							}
						}
					}
					
					if(!userDataBuffer.isEmpty())
					{
						// service the oldest pending UserData message from the user application
						processMessage((Message)userDataBuffer.remove(), -1);
					}

					if(advertise)
						advertiseRoutes();
				}
				catch(InterruptedException ie)
				{
					logger.log("Kernel main program loop interrupted while waiting for new messages; resuming operations. Cause: " + ie.getMessage(), Log.SEVERITY_ERROR);
				}
				catch(RuntimeException re)
				{
					logger.log("RuntimeException in the kernel thread: " + LogUtil.getStackTrace(re), Log.SEVERITY_ERROR);
				}
			}
		}
		catch(Throwable t)
		{
			logger.log("Unexpected kernel problem. Kernel thread terminated. Caused by: " + LogUtil.getStackTrace(t), Log.SEVERITY_CRITICAL);
		}
		finally
		{
			task.cancel();	// stop the periodic distance vector exchanges
			dt.clear();
			state = State.STOPPED;
			logger.log("Kernel thread stopped.", Log.SEVERITY_INFO);
		}
	}
	
	/**
	 * <P>
	 * Returns > -1 if there is at least one interface that has its state
	 * changend since the previous check. The return value is the index in the
	 * internal interfaces array that identifies the interface. When all
	 * interfaces are idle, the method returns -1.
	 * </P>
	 * <P>
	 * This method cycles through the interfaces with a persistent index.
	 * </P>
	 * 
	 * @return	the index of the first active interface that was found, -1 if
	 * 	nothing is active.
	 */
	private int getActiveInterface()
	{
		for(int cnt = 0; cnt < MAX_INTERFACES; cnt++, nextIface = ++nextIface % MAX_INTERFACES)
		{
			if(ifaces[nextIface] != null && (ifaces[nextIface].isReadyForReading() || ifaces[nextIface].getCostMetric() != previousCost[nextIface]))
			{
				return nextIface;
			}
		}
		
		return -1;
	}
	
	/**
	 * <P>
	 * This private method checks to see if the list of interfaces has
	 * changed. If a new interface instance was added using addInterface(),
	 * its reference was stored in the newIfaces array. This method swaps the
	 * current ifaces array with the newIfaces so that the next iteration of
	 * the router thread uses the new interfaces.
	 * </P>
	 * 
	 * @return	<code>true</code> if one or more interfaces were added or
	 * 	removed, <code>false</code> if the interface configuration was not
	 * 	changed (most of the time).
	 */
	private boolean manageInterfaces()
	{
		synchronized(ifaceMutex)
		{
			if(ifaceChange)
			{
				int nX = 0;
				// first deregister from all current interfaces
				for(; nX < MAX_INTERFACES; nX++)
				{
					if(ifaces[nX] != null)
					{
						ifaces[nX].getObservable().deleteObserver(this);
						ifaces[nX] = null;
					}
				}
				
				// now pull in the new interface configuration and register
				Iterator it = newIfaces.iterator();
				for(nX = 0; nX < MAX_INTERFACES && it.hasNext(); nX++)
				{
					ifaces[nX] = (PeerInterface)it.next();
					ifaces[nX].getObservable().addObserver(this);
				}
				
				initializeDT();	// empty the distance table
				logger.log("Interface configuration changed. " + nX + " interface" + (nX > 1 ? "s" : "") + " configured.", Log.SEVERITY_INFO);
				ifaceChange = false;

				return true;
			}
			
			return false;	// we didn't do anything
		}
	}
	
	/**
	 * <P>
	 * Called when the cost metric of a neighbor link has changed. It updates
	 * all relevant entries in the distance table to reflect the new link
	 * cost. If the new cost caused a sortest path to change, this method does
	 * not automatically advertise the new routes to its neighbors, but
	 * returns <code>true</code>.
	 * </P>
	 * 
	 * @param k_pos	the index in ifaces[] pointing to the interface.
	 * @param oldCost	the cost metric currently used.
	 * @param newCost	the new cost metric.
	 * @return	true if this update changed preferred routes, false if not.
	 */
	private boolean updateMetric(int k_pos, int oldCost, int newCost)
	{
		boolean updated = false;
		previousCost[k_pos] = newCost;
		
		synchronized(dtMutex)
		{
			Iterator it = dt.entrySet().iterator();
			while(it.hasNext())
			{
				/**
				 * Before summarization was added, this method had a special
				 * case for direct neighbors. When the address of a neighbor
				 * (interface peer address) was encountered, it's cost_via
				 * value was directly set to the new link cost value,
				 * regardless of its current value. This way, the cost to a
				 * neighbor that was previously unreachable (because the link
				 * cost was infinity) is actively set to the new cost.
				 * Unreachable nodes that are not direct neighbors are not
				 * touched. With summarization this is no longer possible
				 * because the neighbor addresses are summarized into
				 * wildcards that can contain other (non-neighbor) nodes. As a
				 * result we have to wait for the next periodic vector
				 * exchange to discover the new cost to our neighbor. In some
				 * cases this increases the converge time.
				 */
				DistanceTable entry = (DistanceTable)((Map.Entry)it.next()).getValue();
				
				if(entry.cost_via[k_pos] != DistanceVector.UNREACHABLE)
				{
					entry.cost_via[k_pos] = (entry.cost_via[k_pos] - oldCost) + newCost;
					if(entry.cost_via[k_pos] > DistanceVector.UNREACHABLE)
					{
						entry.cost_via[k_pos] = DistanceVector.UNREACHABLE;
					}
					
					// see if this changed this entry's preferred hop
					if(!updated && ( entry.hop != getPreferredHop(entry.dest) ||
						entry.getCost() != getShortestPath(entry.dest)))
						updated = true;
				}
			}
			
			if(updated)
				updateDt();
		}

		logger.log("Link cost metric of interface " + ifaces[k_pos].getId() + " changed from " + oldCost + " to " + newCost + ". Preferred hops and shortest paths were " + (updated ? "" : "not ") + "changed.", Log.SEVERITY_DEBUG);
		return updated;
	}
	
	/**
	 * <P>
	 * This method is invoked for every message that is received. It first
	 * examines the message to determine its type (UserData / RoutingData) and
	 * then processes it. When it was a RoutingData message which caused
	 * preferred hops or shortest distances to change, the method returns
	 * <code>true</code>.
	 * </P>
	 * 
	 * @param m	the message to be processed.
	 * @param k_pos	the index of the interface that received the message. This
	 * 	is important for some messages.
	 * @return	<code>true</code> if this message was a RoutingData message
	 * 	that caused the distance table to be updated, <code>false</code> if no
	 * 	preferred (shortest) routes were changed.
	 */
	private boolean processMessage(Message m, int k_pos)
	{
		if(m instanceof RoutingData)
		{
			return doRoutingData((RoutingData)m, k_pos);
		}
		else if(m instanceof NotifyData)
		{
			return doNotifyData((NotifyData)m, k_pos);
		}
		else if(m instanceof RoutablePing)
		{
			return doRoutablePing((RoutablePing)m, k_pos);
		}
		else if(m instanceof UserData)
		{
			return doUserData((UserData)m, k_pos);
		}
		else
		{
			logger.log("Received an unknown message type: " + m.getClass().getName(), Log.SEVERITY_ERROR);
			return false;
		}
	}
	
	private boolean doRoutingData(RoutingData rd, int k_pos)
	{
		boolean updated = false;
		logger.log("Processing distance vectors sent by " + rd.getSource(), Log.SEVERITY_DEBUG);

		DistanceVector[] dvs = rd.getDistanceVectors();
		synchronized(dtMutex)
		{
			for(int nX = 0; nX < dvs.length; nX++)
			{
				logger.log("Processing: " + dvs[nX].toString(), Log.SEVERITY_DEBUG);
				
				Address dest = dvs[nX].destination.summarize(routerAddress);
				Address head = dvs[nX].head.summarize(routerAddress);
					
				if(dest.matches(routerAddress) || dest.equals( new Address(routerAddress.toString() + ".*") ))
				{
					/**
					 * Of course we can ignore the vector with dest to ourselves,
					 * but also, if we are A.B.C, we can ignore vector A.B.C.*
					 * because we already keep track of individual nodes below
					 * A.B.C.
					 */
					continue;
				}
					
				DistanceTable entry = (DistanceTable)dt.get(dest);
				if(entry == null)
				{
					if(dvs[nX].distance == DistanceVector.UNREACHABLE)
					{
						// ignore new unreachable destinations
						continue;
					}
					else if((entry = addDestination(dest)) == null)
					{
						logger.log("Distance table full. Ignoring new destination " + dest.toString(), Log.SEVERITY_WARNING);
						continue;
					}
					updated = true;
				}
					
				if(dest.equals(ifaces[k_pos].getPeerAddress().summarize(routerAddress)))
				{
					/**
					 * This destination equals the summarized address of the neighbor
					 * that sent it. If we are A.B.C and the neighbor is A.X.Y, he
					 * ends up in our distance table under A.X.*. Now if this
					 * neighbor sends a route to A.X.W, we summarize that under A.X.*
					 * and merges both into the same route. We only keep the shortest
					 * path to A.X.*, which is equal to the link cost to that
					 * neighbor.
					 */
					entry.cost_via[k_pos] = previousCost[k_pos];
					entry.head_via[k_pos] = routerAddress;
				}
				else
				{
					entry.head_via[k_pos] = head;
					if(dvs[nX].distance == DistanceVector.UNREACHABLE)
					{
						entry.cost_via[k_pos] = DistanceVector.UNREACHABLE;
					}
					else
					{
						// the cost for this address is the neighbor's cost plus the cost
						// to reach that neighbor
						entry.cost_via[k_pos] = dvs[nX].distance + previousCost[k_pos];
						if(entry.cost_via[k_pos] > DistanceVector.UNREACHABLE)
						{
							entry.cost_via[k_pos] = DistanceVector.UNREACHABLE;
						}
					}
				}

				// see if this changed the routing table
				if(updated == false &&
					(entry.hop != getPreferredHop(dest) ||
					entry.getCost() != getShortestPath(dest)))
					updated = true;
			}
			
			if(updated)
			{
				updateDt();
			}
			updateChildlinks();
		}

		logger.log(dvs.length + " distance vectors received on interface " + ifaces[k_pos].getId() + "; distance table was " + (updated ? "" : "not ") + "changed.", Log.SEVERITY_DEBUG);
		return updated;
	}
	
	private boolean doNotifyData(NotifyData nd, int k_pos)
	{
		if(nd.getDestination().equals(routerAddress))
		{
			callback(nd);
		}
		else
		{
			if(nd.getTtl() <= 0)
			{
				logger.log("Notification dropped with expired ttl (source: " + nd.getSource() + ", destination: " + nd.getDestination() + ").", Log.SEVERITY_WARNING);
				// to avoid TTL_EXPIRED storms, we don't generate a new TTL_EXPIRED notification
			}
			else
			{
				DistanceTable record = (DistanceTable)dt.get(nd.getDestination().summarize(routerAddress));
				if(record != null && record.hop != -1)
				{
					ifaces[record.hop].send(nd);
				}
				else
				{
					logger.log("Notification (" + nd.toString() + ") dropped: no route to host or destination unreachable.", Log.SEVERITY_INFO);
				}
			}
		}
			
		return false;	// a notification has no influence on the dt
	}
	
	private boolean doRoutablePing(RoutablePing ping, int k_pos)
	{
		if(ping.getDestination().equals(routerAddress))
		{
			if(ping.isAcknowledged())
			{
				// our ping has returned
				logger.log("Ping reply received from node " + ping.getSource(), Log.SEVERITY_DEBUG);
				callback(ping);	// to whoever is listening
			}
			else
			{
				// we're being pinged - respond
				logger.log("Responding to ping from node " + ping.getSource().toString(), Log.SEVERITY_DEBUG);
				ping.setAcknowledged(true);
				ping.setDestination(ping.getSource());
				ping.setSource(routerAddress);
				ping.setTtl(30);	// reset ttl for the way back
				
				return processMessage(ping, -1);
			}
		}
		else
		{
			// this is someone else's ping, just route
			if(ping.getTtl() <= 0)
			{
				logger.log("RoutablePing dropped with expired ttl (source: " + ping.getSource() + ", destination: " + ping.getDestination() + ").", Log.SEVERITY_WARNING);

				// send TTL_EXPIRED back to source
				NotifyData err = new NotifyData(routerAddress, ping.getSource(), NotifyData.TTL_EXPIRED);
				err.setCause(ping);
				return processMessage(err, -1);
			}
			else
			{
				DistanceTable record = (DistanceTable)dt.get(ping.getDestination().summarize(routerAddress));
				if(record == null)
				{
					logger.log("Dropping RoutablePing message for " + ping.getDestination() + ": no route to host.", Log.SEVERITY_INFO);
						
					// send NO_ROUTE back to source
					NotifyData err = new NotifyData(routerAddress, ping.getSource(), NotifyData.NO_ROUTE);
					err.setCause(ping);
					return processMessage(err, -1);
				}
				else
				{
					if(record.hop == -1)
					{
						logger.log("Dropping RoutablePing message for " + ping.getDestination() + ": destination unreachable.", Log.SEVERITY_INFO);
							
						// send DEST_UNREACHABLE back to source
						NotifyData err = new NotifyData(routerAddress, ping.getSource(), NotifyData.DEST_UNREACHABLE);
						err.setCause(ping);
						return processMessage(err, -1);
					}
					else if(record.hop == k_pos)
					{
						logger.log("Message received from the preferred hop to destination " + ping.getDestination().toString() + ". This means neighbor " + ifaces[record.hop].getPeerAddress().toString() + " created a routing loop! Discarding message.", Log.SEVERITY_ERROR);
					}
					else
					{
						ifaces[record.hop].send(ping);
					}
				}
			}
		}
			
		return false;
	}
	
	private boolean doUserData(UserData ud, int k_pos)
	{
		if(ud.isMulticast())
		{
			DistanceTable source = (DistanceTable)dt.get(ud.getSource().summarize(routerAddress));
			
			if(source == null)
			{
				logger.log("The broadcast message came from a node (" + ud.getSource() + ") for which we have no route.", Log.SEVERITY_WARNING);
			}
			else if(source.hop == k_pos || ud.getSource().equals(routerAddress))
			{
				logger.log("Notifying application for broadcast message from " + ud.getSource() + " to " + ud.getDestination(), Log.SEVERITY_DEBUG);
				callback(ud);

				// send to all child links for this dest
				if(ud.getTtl() <= 0)
				{
					logger.log("Multicast message with expired ttl not forwarded any further (source: " + ud.getSource() + ", destination: " + ud.getDestination() + ").", Log.SEVERITY_WARNING);
					// TTL_EXPIRED, do not return NotifyData
				}
				else
				{
					for(int k = 0; k < MAX_INTERFACES; k++)
					{
						if((source.childlinks & ((long)Math.pow(2, k))) == (long)Math.pow(2, k))
						{
							logger.log("Forwarding broadcast message from " + ud.getSource() + " addressed to " + ud.getDestination() + " to interface " + ifaces[k].getId(), Log.SEVERITY_DEBUG);
							ifaces[k].send(ud);
						}
					}
				}
			}
			else
			{
				logger.log("The broadcast message was not received via the preferred neighbor.", Log.SEVERITY_WARNING);
			}
		}
		else	// unicast
		{
			if(ud.getDestination().equals(routerAddress))
			{
				// this is a unicast message addressed to us
				logger.log("A unicast message addressed to one of our interfaces has arrived; passing on to the application.", Log.SEVERITY_DEBUG);
				callback(ud);
			}
			else
			{
				if(ud.getTtl() <= 0)
				{
					logger.log("Message dropped with expired ttl (source: " + ud.getSource() + ", destination: " + ud.getDestination() + ").", Log.SEVERITY_WARNING);

					// send TTL_EXPIRED back to source
					NotifyData err = new NotifyData(routerAddress, ud.getSource(), NotifyData.TTL_EXPIRED);
					err.setCause(ud);
					return processMessage(err, -1);
				}
				else
				{
					// this is a message in transit; pass to the preferred hop
					DistanceTable record = (DistanceTable)dt.get(ud.getDestination().summarize(routerAddress));
					if(record == null)
					{
						logger.log("Dropping unicast message for " + ud.getDestination() + ": no route to host.", Log.SEVERITY_INFO);
						
						// send NO_ROUTE back to source
						NotifyData err = new NotifyData(routerAddress, ud.getSource(), NotifyData.NO_ROUTE);
						err.setCause(ud);
						return processMessage(err, -1);
					}
					else
					{
						if(record.hop == -1)
						{
							logger.log("Dropping unicast message for " + ud.getDestination() + ": destination unreachable.", Log.SEVERITY_INFO);
							
							// send DEST_UNREACHABLE back to source
							NotifyData err = new NotifyData(routerAddress, ud.getSource(), NotifyData.DEST_UNREACHABLE);
							err.setCause(ud);
							return processMessage(err, -1);
						}
						else if(record.hop == k_pos)
						{
							logger.log("Message received from the preferred hop to destination " + ud.getDestination().toString() + ". This means neighbor " + ifaces[record.hop].getPeerAddress().toString() + " created a routing loop! Discarding message.", Log.SEVERITY_ERROR);
						}
						else
						{
							ifaces[record.hop].send((Message)ud);
						}
					}
				}
			}
		}
			
		return false;	// a UserData message can not cause any dt[] changes
	}
	
	/**
	 * <P>
	 * This method sends a set of distance vectors to each neighbor. It uses
	 * ExBF to avoid loops.
	 * </P>
	 */
	private void advertiseRoutes()
	{
		logger.log("Advertising distance vectors.", Log.SEVERITY_INFO);
		
		for(int k_pos = 0; k_pos < MAX_INTERFACES; k_pos++)
		{
			if(ifaces[k_pos] != null)
			{
				RoutingData rd = new RoutingData();
				rd.setSource(Kernel.getInstance().getRouterAddress());
				rd.setDistanceVectors(getTriplets(ifaces[k_pos].getPeerAddress()));
				ifaces[k_pos].send(rd);
			}
		}
		
		synchronized(advertiseMutex)
		{
			advertise = false;
		}
	}
	
	/**
	 * <P>
	 * Returns the list of distance vectors (dest-head-cost triplets) that
	 * can be sent to a specified neighbor. This method uses isInPath() to make sure
	 * no triplets are sent that cause loops.
	 * </P>
	 * 
	 * @param k_pos	the index of the interface that connects to a certain
	 * 	neighbor.
	 * @return	the distance vectors that can be sent to the neighbor.
	 */
	private DistanceVector[] getTriplets(Address neighbor)
	{
		List vectors = new ArrayList();
		
		Iterator it = dt.entrySet().iterator();
		while(it.hasNext())
		{
			DistanceTable record = (DistanceTable)((Map.Entry)it.next()).getValue();
			if(!record.dest.isReserved())
			{
				DistanceVector dv = null;

				if(record.dest.equals(Kernel.getInstance().getRouterAddress()))
				{
					dv = new DistanceVector(record.dest, Address.RESERVED, 0);
				}
				else
				{
					if(record.hop == -1 || isInPath(neighbor.summarize(Kernel.getInstance().getRouterAddress()), record.dest))
					{
						dv = new DistanceVector(record.dest, Address.RESERVED, DistanceVector.UNREACHABLE);
					}
					else
					{
						dv = new DistanceVector(record.dest, record.head_via[record.hop], record.getCost());
					}
				}

				vectors.add(dv);
			}
		}

		return (DistanceVector[])vectors.toArray(new DistanceVector[vectors.size()]);
	}

	/**
	 * <P>
	 * Adds a row for destination j and returns its DistanceTable record or
	 * <code>null</code> if dt is full. If the destination already exists, its
	 * record will be overridden.
	 * </P>
	 * 
	 * @param	j the new destination address.
	 * @return	the new DistanceTable record that was added.
	 */
	private DistanceTable addDestination(Address j)
	{
		synchronized(dtMutex)
		{
			if(dt.size() < MAX_NODES)
			{
				DistanceTable record = new DistanceTable();
				record.dest = j;
				if(dt.put((Address)j, record) != null)
				{
					logger.log("Overwriting distance table entry for " + j.toString() + " with new record.", Log.SEVERITY_WARNING);
				}
				return record;
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * <P>
	 * Updates the dt[j].cost and dt[j].hop values to ensure they represent
	 * the lowest link-cost and preferred-neighbor correctly for every
	 * destination j. This method is invoked after a RoutingData message
	 * changed one or more preferred paths.
	 * </P>
	 * <P>
	 * This routine also updates all childlink bits and expunges expired
	 * unreachable entries.
	 * </P>
	 */
	private void updateDt()
	{
		synchronized(dtMutex)
		{
			Iterator it = dt.entrySet().iterator();
			while(it.hasNext())
			{
				DistanceTable record = (DistanceTable)((Map.Entry)it.next()).getValue();
				if(!record.dest.isReserved())
				{
					record.setCost(getShortestPath(record.dest));
					record.hop = getPreferredHop(record.dest);
					
					if(record.isStale())
					{
						// never expunge neighbor addresses
						boolean isNeighbor = false;
						for(int nX = 0; nX < MAX_INTERFACES; nX++)
						{
							if(ifaces[nX] != null && ifaces[nX].getPeerAddress().summarize(routerAddress).equals(record.dest))
							{
								// we should ignore B.* if a neighbor address (B.C.D) summarizes to B.*
								isNeighbor = true;
								break;
							}
						}
						
						if(!isNeighbor)
						{
							it.remove();
							logger.log("Expunged unreachable, stale entry " + record.dest + " from distance table.", Log.SEVERITY_INFO);
						}
					}
				}
			}

			logger.log("Distance table updated: " + toString(), Log.SEVERITY_INFO);
		}
	}
	
	/**
	 * <P>
	 * Recomputes all childlinks. This is done after every routing update.
	 * Childlinks can change even if no preferred hops change.
	 * </P>
	 */
	private void updateChildlinks()
	{
		synchronized(dtMutex)
		{
			Iterator it = dt.entrySet().iterator();
			while(it.hasNext())
			{
				DistanceTable record = (DistanceTable)((Map.Entry)it.next()).getValue();
				if(!record.dest.isReserved())
				{
					// Compute all the child links for broadcast traffic
					// coming from this destination.
					if(record.getCost() != DistanceVector.UNREACHABLE)
					{
						// This dest is reachable, so any neighbor that claims
						// differently becomes a childlink.
						for(int k_pos = 0; k_pos < MAX_INTERFACES; k_pos++)
						{
							if(ifaces[k_pos] != null)
							{
								if(record.cost_via[k_pos] == DistanceVector.UNREACHABLE)
								{
									// make this a childlink
									record.childlinks |= (long)Math.pow(2, k_pos);
								}
								else
								{
									// remove this childlink
									record.childlinks &= ~((long)Math.pow(2, k_pos));
								}
							}
						}
					}
					else
					{
						// this destination is totally unreachable; no childlinks
						record.childlinks = 0;
					}
				}
			}
		}
	}
	
	/**
	 * <P>
	 * Recursively determines if an address is in the path from this node to a
	 * given destination when the preferred (shortest) path is used.
	 * </P>
	 * 
	 * @param addr	the address that is looked for in the path to dest.
	 * @param dest	the destination address of a path.
	 * @return	<code>true</code> if addr is in the path to dest,
	 * 	<code>false</code> if not.
	 */
	private boolean isInPath(Address addr, Address dest)
	{
		int iface = ((DistanceTable)dt.get(dest)).hop;
		if(iface == -1)
		{
			return false;	// there's no path to dest
		}
		else
		{
			return isInPath(addr, dest, iface, 0);
		}
	}
	
	/**
	 * <P>
	 * Recursively determines if an address is in the path from this node to a
	 * given destination when the preferred (shortest) path is used. A counter
	 * is used to detect stack overflows due to incorrect termination checks.
	 * </P>
	 * 
	 * @param addr	the address that is looked for in the path to dest.
	 * @param dest	the destination address of a path.
	 * @param depth	counter used to detect incorrect termination checks.
	 * @return	<code>true</code> if addr is in the path to dest,
	 * 	<code>false</code> if not.
	 */
	private boolean isInPath(Address addr, Address dest, int iface, int depth)
	{
		// TEMP DEBUG STATEMENTS TO TRACK A STACKOVERFLOWERROR
		if(depth == 100)
		{
			logger.log("A stack overflow is likely to occur!", Log.SEVERITY_CRITICAL);
			return false;
		}

		if(dest.equals(Kernel.getInstance().getRouterAddress()) || dest.isReserved())
		{
			return false;
		}
		
		Address head = getHead(dest, iface);

		if((addr.equals(dest) && Kernel.getInstance().getRouterAddress().equals(head)) || head.equals(addr))
		{
			return true;
		}
		else
		{
			return isInPath(addr, head, iface, ++depth);
		}
	}
	
	/**
	 * <P>
	 * Returns the index in the interfaces array that points to the interface
	 * instance that has the given address as its peer. Returns -1 if there is
	 * no interface with the given address.
	 * </P>
	 * 
	 * @param iface	the address of the interface to be found.
	 * @return	the index in the interfaces array or -1 if no interface has
	 * 	the given address.
	 */
	private int indexAtInterfaces(Address iface)
	{
		for(int nX = 0; nX < MAX_INTERFACES; nX++)
			if(ifaces != null && ifaces[nX] != null && ifaces[nX].getPeerAddress().equals(iface))
				return nX;

		return -1;
	}
	
	/**
	 * <P>
	 * This methods returns the index of the interface with the shortest
	 * distance to the given destination. It can then be stored in the
	 * dt[j].hop field. In case of a draw, the interface with the lowest
	 * address is chosen. The given address must be an existing one.
	 * </P>
	 * 
	 * @param dest	the destination address for which to find the preferred
	 * 	interface.
	 * @return	the index of the interface with the shortest path to dest.
	 */
	private int getPreferredHop(Address dest)
	{
		int sp = DistanceVector.UNREACHABLE, ph = -1;
		DistanceTable record = (DistanceTable)dt.get(dest);
		
		if(record.dest != null && !dest.equals(routerAddress))
		{
			for(int k_pos = 0; k_pos < MAX_INTERFACES; k_pos++)
			{
				if(ifaces[k_pos] != null)
				{
					if(record.cost_via[k_pos] != DistanceVector.UNREACHABLE && (record.cost_via[k_pos] < sp ||
						(record.cost_via[k_pos] == sp && ifaces[k_pos].getPeerAddress().compareTo(ifaces[ph].getPeerAddress()) > 0) ))
					{
						sp = record.cost_via[k_pos];
						ph = k_pos;
					}
				}
			}
		}
		
		return ph;
	}
	
	/**
	 * <P>
	 * Returns the shortest path (lowest link-cost) to a given destination.
	 * To determine the shortest path, the links costs via all neighbors
	 * are compared and the lowest returned.
	 * This method does not simply return the value stored in <code>DistanceTable.cost</code>
	 * but instead returns the value that can be stored there.
	 * </P>
	 * 
	 * @param dest	the destination address for which to find the shortest
	 * 	path.
	 * @return	the cost of the shortest known path towards dest.
	 */
	private int getShortestPath(Address dest)
	{
		if(dest.equals(Kernel.getInstance().getRouterAddress()))
		{
			// our own address is a special case as the cost_via[] array cannot be used
			return 0;
		}
		
		int sp = DistanceVector.UNREACHABLE;
		DistanceTable record = (DistanceTable)dt.get(dest);
		
		for(int k_pos = 0; k_pos < MAX_INTERFACES && record != null; k_pos++)
		{
			if(ifaces[k_pos] != null && record.cost_via[k_pos] < sp)
				sp = record.cost_via[k_pos];
		}
		
		return sp;
	}
	
	/**
	 * <P>
	 * Returns the head-of-path address for the path to the given
	 * destination through the specified interface. Both dest and iface must
	 * be a valid and existing address/index.
	 * </P>
	 * 
	 * @param dest	the destination for which to get the head-of-path.
	 * @param iface	the index of the interface that starts the path to dest.
	 * @return	the head-of-path address of the path to dest via interface
	 * 	iface.
	 */
	private Address getHead(Address dest, int iface)
	{
		if(dest.isReserved())
		{
			return Address.RESERVED;
		}
		else
		{
			return ((DistanceTable)dt.get(dest)).head_via[iface];
		}
	}

	/**
	 * <P>
	 * Registers Handler h as an observer for the given address. When
	 * address is a broadcast address, h will be invoked for every packet
	 * that was addressed or published to that address, but only if the
	 * packet was received according to the spanning tree from the source.
	 * When the same handler is subscribed to the same address n-times, it
	 * will be invoked n-times for every packet that matches.
	 * </P>
	 */
	public void subscribe(Handler h, Address address)
	{
		synchronized(subscriptions)
		{
			List handlers = (List)subscriptions.get(address);
			if(handlers == null)
			{
				handlers = new ArrayList();
				subscriptions.put(address, handlers);
			}

			handlers.add(h);
		}
	}

	/**
	 * <P>
	 * Deregisters Handler h that was previously registered as an observer
	 * for the specified address. If the same handler was registered for
	 * the same address more than once, this call will remove one
	 * subscription only.
	 * </P>
	 */
	public void unsubscribe(Handler h, Address address)
	{
		synchronized(subscriptions)
		{
			List handlers = (List)subscriptions.get(address);
			if(handlers != null)
				handlers.remove(h);
		}
	}
	
	/**
	 * <P>
	 * Method exposed by the Transport class that allows user applications to
	 * transmit messages. The message is queued in an internal
	 * message buffer until the router thread has time to process the message.
	 * This is also the point at which Routable messages get their source
	 * address set to the local router address.
	 * </P>
	 * 
	 * @param message	the message to send.
	 * @exception InterruptedException	when the thread was interrupted while
	 * 	placing the message in the send buffer. If this happens, the message
	 * 	needs to be re-sent.
	 */
	public void send(Message message) throws InterruptedException
	{
		if(message instanceof Routable)
		{
			((Routable)message).setSource(routerAddress);
		}
		synchronized(this)
		{
			userDataBuffer.add(message);
			notify();
		}
	}

	/**
	 * <P>
	 * Invokes the callback() method on all Handler instances that subscribed
	 * to the destination address of the UserData message.
	 * </P>
	 * 
	 * @param ud	the message that was received.
	 */
	private void callback(Routable ud)
	{
		List copy = new ArrayList();
		
		synchronized(subscriptions)
		{
			List handlers = (List)subscriptions.get(ud.getDestination());
			if(handlers != null)
			{
				copy = new ArrayList(handlers);
			}
		}

		// do not invoke the callback methods inside the synchronized block,
		// as it could lead to a deadlock
		Iterator it = copy.iterator();
		while(it.hasNext())
		{
			Handler h = (Handler)it.next();
			h.callback(ud);
		}
	}
	
	/**
	 * <P>
	 * Returns (a copy of) the internal array of interface objects. This array
	 * can be used to obtain interface info (such as addresses) with a valid
	 * array index.
	 * </P>
	 * 
	 * @return	a copy of the internal array of interfaces.
	 */
	public PeerInterface[] getInterfaces()
	{
		PeerInterface[] copy = new PeerInterface[ifaces.length];
		System.arraycopy(ifaces, 0, copy, 0, ifaces.length);
		return copy;
	}
	
	/**
	 * <P>
	 * Adds a new interface. The changes will only take effect after the
	 * router thread has finished its current iteration.
	 * </P>
	 * 
	 * @param iface	new interface that is to be added.
	 * @exception MaxInterfaceException	when the router core cannot support
	 * 	another interface (currently 8 interfaces is the maximum).
	 */
	public void addInterface(PeerInterface iface) throws MaxInterfaceException
	{
		synchronized(ifaceMutex)
		{
			if(newIfaces.size() >= MAX_INTERFACES)
			{
				throw new MaxInterfaceException("This routing kernel only supports " + MAX_INTERFACES + " interfaces.");
			}
			else if(!newIfaces.contains(iface))
			{
				newIfaces.add(iface);
				ifaceChange = true;
			}
		}
		
		synchronized(this)
		{
			notify();
		}
	}
	
	/**
	 * <P>
	 * Removes the given interface. The router thread will first finish its
	 * current iteration.
	 * </P>
	 * 
	 * @param iface	existing interface to be removed.
	 */
	public void removeInterface(PeerInterface iface)
	{
		synchronized(ifaceMutex)
		{
			if(newIfaces.contains(iface))
			{
				newIfaces.remove(iface);
				ifaceChange = true;
			}
		}
		
		synchronized(this)
		{
			notify();
		}
	}

	/**
	 * <P>
	 * This method is invoked whenever the status of an interface changes.
	 * This means it has new incoming messages waiting, or its cost metric has
	 * changed.
	 * </P>
	 * 
	 * @param observable
	 * @param o	the (peer)address of the interface (instance of class
	 * 	Address).
	 */
	public void update(Observable observable, Object o)
	{
		synchronized(this)
		{
			notify();
		}
	}
	
	/**
	 * <P>
	 * Returns a copy of the kernel's entire distance table.
	 * </P>
	 * 
	 * @return a deep-copy of the kernel's distance table.
	 */
	public DistanceTable[] getDistanceTable()
	{
		List entries = new ArrayList();

		// ensure the kernel thread cannot modify the datastructure while we
		// do the cloning
		synchronized(dtMutex)
		{
			Iterator it = dt.entrySet().iterator();
			while(it.hasNext())
			{
				entries.add( ((DistanceTable)((Map.Entry)it.next()).getValue()).clone() );
			}
		}
		
		return (DistanceTable[])entries.toArray(new DistanceTable[entries.size()]);
	}
	
	/**
	 * <P>
	 * Returns the current state of the kernel. This can be
	 * <code>State.STOPPED</code>, <code>State.STARTING</code>, <code>State.STARTED</code>
	 * or <code>State.STOPPING</code>.
	 * </P>
	 * 
	 * @return	the current state of the kernel. 
	 */
	public State getState()
	{
		return state;
	}

	/**
	 * <P>
	 * Returns the distance table as an array of strings. Each string contains
	 * one entry:
	 * <code>(j = a.b.d, pi = iface_id, sp = 63, cv = [id0 = 71, id1 = 163, id2 = inf], hv = [id0 = a.b.c, id1 = x.y.z, id2 = ?], cl = [id0, id2])</code>
	 * where j is the destination address, pi is the id of the preferred interface for
	 * this destination, sp is the shortest path (cost), cv is the cost_via
	 * array with cost metric via other interfaces and hv is the head_via
	 * array showing the head nodes of the paths when going via the other
	 * interfaces. When a node is unreachable, the cost is represented by
	 * "inf" (infinity).
	 * </P>
	 * 
	 * @return	the distance table entries in string format.
	 */
	private String[] getDistanceTableStrings()
	{
		try
		{
			DistanceTable[] entries = getDistanceTable();
			List strings = new ArrayList();
			
			for(int j_pos = 0; j_pos < entries.length; j_pos++)
			{
				if(!entries[j_pos].dest.isReserved())
				{
					StringBuffer buf = new StringBuffer();
					buf.append("j = " + entries[j_pos].dest)
						.append(", pi = " + (entries[j_pos].hop == -1 ? "?" : ifaces[entries[j_pos].hop].getId()))
						.append(", sp = " + (entries[j_pos].getCost() == DistanceVector.UNREACHABLE ? "inf" : String.valueOf(entries[j_pos].getCost())))
						.append(", cv = [");
						
					for(int k_pos = 0; k_pos < MAX_INTERFACES; k_pos++)
					{
						if(ifaces[k_pos] != null)
						{
							if(k_pos > 0)
							{
								buf.append(", ");
							}
							buf.append(ifaces[k_pos].getId() + " = " + (entries[j_pos].cost_via[k_pos] == DistanceVector.UNREACHABLE ? "inf" : String.valueOf(entries[j_pos].cost_via[k_pos])));
						}
					}
					buf.append("], hv = [");

					for(int k_pos = 0; k_pos < MAX_INTERFACES; k_pos++)
					{
						if(ifaces[k_pos] != null)
						{
							if(k_pos > 0)
							{
								buf.append(", ");
							}
							buf.append(ifaces[k_pos].getId() + " = " + entries[j_pos].head_via[k_pos]);
						}
					}
					buf.append("], cl = [");
					
					boolean first = true;
					for(int k_pos = 0; k_pos < MAX_INTERFACES; k_pos++)
					{
						if(ifaces[k_pos] != null)
						{
							if((entries[j_pos].childlinks & ((long)Math.pow(2, k_pos))) == (long)Math.pow(2, k_pos))
							{
								if(!first)
								{
									buf.append(", ");
								}
								else
								{
									first = false;
								}
								buf.append(ifaces[k_pos].getId());
							}
						}
					}
					buf.append("]");
					
					strings.add(buf.toString());
				}
			}
		
			return (String[])strings.toArray(new String[strings.size()]);
		}
		catch(RuntimeException re)
		{
			logger.log("Unexpected exception during getDistanceTableStrings(): " + LogUtil.getStackTrace(re), Log.SEVERITY_ERROR);
			throw re;
		}
	}
	
	/**
	 * <P>
	 * Returns the kernel's distance table in the standard line-format:
	 * </P>
	 * <P>
	 * <code>DT = {(j = com.mxs.net2.*, pi = if0, sp = 65535, cv = [if0 = inf], hv = [if0 = com.mxs.net1.node1]), (j = com.mxs.net1.node1, pi = ?, sp = 0, cv = [if0 = inf], hv = [if0 = ?])}</code>
	 * </P>
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("DT = {");
		String[] entries = getDistanceTableStrings();
		
		for(int nX = 0; nX < entries.length; nX++)
		{
			sb.append("(" + entries[nX] + ")");
			if(nX < entries.length - 1)
				sb.append(", ");
		}
		sb.append("}");

		return sb.toString(); 
	}
	
	/**
	 * <P>
	 * This class implements the scheduled task of exchanging distance vectors
	 * periodically. This task is invoked using the Scheduler class. The task
	 * is scheduled at a fixed interval. This means it could run just after
	 * the kernel thread already advertised its routes caused by a change in
	 * the distance table. Doing an additional exchange in this situation
	 * wouldn't be very useful. This can be seen as a known limitation. Note
	 * that the task only sets the <code>advertise</code> flag of the outer
	 * class to <code>true</code>, while the router thread does the actual
	 * work.
	 * </P>
	 * 
	 * @author Erik van Zijst - erik@marketxs.com
	 * @version	0.1, 20.oct.2003
	 */
	private class VectorExchangeTask extends TimerTask
	{
		public final long INTERVAL = 30000;	// 30 sec interval
		
		public VectorExchangeTask()
		{
			super();
		}
		
		public void run()
		{
			synchronized(advertiseMutex)
			{
				advertise = true;
			}
			
			synchronized(Kernel.this)
			{
				Kernel.this.notify();
			}
		}
	}
	
	/**
	 * <P>
	 * This inner class implements the manageable wrapper around the kernel. 
	 * It is used to keep JMX specific logic and methods out of the real
	 * kernel class.
	 * </P>
	 * 
	 * @author Erik van Zijst - erik@marketxs.com
	 * @version	0.1, 11.nov.2003
	 */
	public class ManageableKernel implements Kernel.ManageableKernelMBean, ManagementHelperInterface
	{
		private String description = "Offers a view on the kernel of the MarketXS messaging library.";
		private Map operationParameterNameMap = new HashMap();
		private Map attributeDescriptionMap = new HashMap();
		private Map operationDescriptionMap = new HashMap();
		private Map operationParameterDescriptionMap = new HashMap();

		public ManageableKernel()
		{
			attributeDescriptionMap.put("State", "Returns the current state the kernel is in. Can be stopped, starting or started.");
			attributeDescriptionMap.put("NumberOfInterfaces", "Returns the number of interfaces that are configured. Interfaces may be inactive.");
			attributeDescriptionMap.put("DistanceTableStrings", "Returns the entries in the distance table as individual strings. Formatting is according to rules of the DistanceTable class.");
			attributeDescriptionMap.put("DistanceTable", "Returns a machine readable version of the distance table, opposed to the more human-readable version of DistanceTableStrings. The datastructure is returned as a (sorted) list consisting of distance table entries. For more info, refer to the javadoc of the Kernel.ManageableKernelMBean interface.");
			attributeDescriptionMap.put("Version", "This is the current version of the software.");
			
			operationDescriptionMap.put("sendMessage", "Sends a UserData message containing a string to a specific unicast or multicast address.");
			operationDescriptionMap.put("traceRoute", "Returns the reverse route messages take to a given destination. Note that this path will usually be fuzzy as a result of address summarization.");
		
			operationParameterNameMap.put("sendMessage", new String[]{"destinationAddress", "payload", "Time-to-Live", "multicast"});
			operationParameterNameMap.put("traceRoute", new String[]{"destinationAddress"});
			
			operationParameterDescriptionMap.put("sendMessage", new String[]{"Send the new message to this address.", "This will be the content of the message. May be empty.", "This is the ttl (Time-To-Live) value for the message. The default is 30.", "Make the destination address multicast or unicast (multicast not implemented yet)."});
			operationParameterDescriptionMap.put("traceRoute", new String[]{"The address to trace through the in-memory distance table. The path may be incomplete due to summarization."});
		}
		
		public String getVersion()
		{
			return Version.getVersion();
		}

		public String getRouterAddress()
		{
			return routerAddress.toString();
		}

		/**
		 * <P>
		 * Returns the distance table as an array of strings. Each string contains
		 * one entry:
		 * <code>(j = a.b.d, pi = iface_id, sp = 63, cv = [id0 = 71, id1 = 163, id2 = inf], hv = [id0 = a.b.c, id1 = x.y.z, id2 = ?])</code>
		 * where j is the destination address, pi is the id of the preferred interface for
		 * this destination, sp is the shortest path (cost), cv is the cost_via
		 * array with cost metric via other interfaces and hv is the head_via
		 * array showing the head nodes of the paths when going via the other
		 * interfaces. When a node is unreachable, the cost is represented by
		 * "inf" (infinity).
		 * </P>
		 * 
		 * @return	the distance table entries in string format.
		 */
		public String[] getDistanceTableStrings()
		{
			return Kernel.this.getDistanceTableStrings();
		}
		
		/**
		 * <P>
		 * Returns a machine readable version of the distance table, opposed
		 * to the more human-readable version of
		 * <code>getDistanceTableStrings()</code>.
		 * </P>
		 * <P>
		 * Note that addresses are returned as strings, not as instances of
		 * the Address class. This is to lower the dependencies between client
		 * and server. This way the addresses loose their multicast/unicast
		 * flag, but since the distance table only contains unicast addresses,
		 * this is not regarded as problem.
		 * </P>
		 * <P>
		 * The datastructure is returned as a (sorted) list consisting of
		 * distance table entries. Each entry contains information about one
		 * destination address (often a wildcard address). The entry is
		 * essentially a map containing key/value tuples. There are currently
		 * 4 keys: "<code>dest</code>" (yielding an address string),
		 * "<code>cost</code>" (yielding an <code>Integer</code> instance or
		 * <code>-1</code>), "<code>hop</code>" (yielding the address string
		 * of the preferred interface towards the destination) and
		 * "<code>head</code>" (yielding the address string of the
		 * head-of-path of the shortest path to the detination). Note that
		 * when an address is reserved, it is encoded as "?". Values are never
		 * <code>null</code>. When the cost is infinity (unreachable), the
		 * integer -1 is returned.
		 * </P>
		 * <P>
		 * The records in the returned list are sorted. The most specific
		 * destination is the first record, while the least specific
		 * destination (most probably a wildcard matching many subnets) is the
		 * last. Messages that have to be forwarded are sent to the hop of the
		 * first record that matches the message's destination.
		 * </P>
		 * 
		 * @return	datastructure containing the distance table information.
		 */
		public List getDistanceTable()
		{
			List list = new ArrayList();
			DistanceTable[] records = Kernel.this.getDistanceTable();	// make a safe copy of the dt
			
			for (int i = 0; i < records.length; i++)
			{
				Map record = new HashMap();
				
				String dest = records[i].dest == null ? "?" : records[i].dest.toString();
				String hop = records[i].hop == -1 ? "?" : ifaces[records[i].hop].getId();
				String head = "?";
				if(!hop.equals("?"))
				{
					if(records[i].hop != -1 && records[i].head_via[records[i].hop] != null)
					{
						head = records[i].head_via[records[i].hop].toString();
					}
				}

				record.put("dest", dest);
				record.put("hop", hop);
				record.put("head", head);
				record.put("cost", (records[i].getCost() == DistanceVector.UNREACHABLE ? new Integer(-1) : new Integer(records[i].getCost())) );

				list.add(record);
			}
			
			return list;
		}
	
		/**
		 * <P>
		 * Returns the number of configured interfaces. Note that these interfaces
		 * do not necessarily need to be active of connected.
		 * </P>
		 * 
		 * @return	the number of configured interfaces.
		 */
		public int getNumberOfInterfaces()
		{
			int count = 0;
		
			for(int nX = 0; nX < MAX_INTERFACES; nX++)
			{
				if(ifaces[nX] != null)
					count++;
			}
		
			return count;
		}

		public State getState()
		{
			return Kernel.this.getState();
		}
	
		/**
		 * <P>
		 * Sends a message with the given contents to the specified
		 * destination address. The destination address can be a unicast or
		 * multicast address.
		 * </P>
		 * 
		 * @param	ttl Time-to-Live field
		 * @exception InvalidStateException	when the router is not in the
		 * 	<code>State.STARTED</code> state.
		 * @exception InterruptedException
		 */
		public void sendMessage(String destination, String data, int ttl, boolean multicast) throws InvalidStateException, InterruptedException
		{
			synchronized(runMutex)
			{
				if(state.equals(State.STARTED))
				{
					try
					{
						UserData ud = new UserData();
						Address dest = new Address(destination);
						dest.setMulticast(multicast);
						ud.setDestination(dest);
						ud.setPayload(data.getBytes());
						ud.setTtl(ttl);
						send(ud);
					}
					catch(RuntimeException e)
					{
						logger.log("Exception while attempting to send a UserData message through JMX: " + LogUtil.getStackTrace(e), Log.SEVERITY_ERROR);
						throw e;
					}
				}
				else
				{
					throw new InvalidStateException("Message sending is only available when the router is started.");
				}
			}
		}
		
		/**
		 * <P>
		 * Returns the reverse route messages take to a given destination. Note
		 * that this is not a complete path. Since the path is derived from
		 * the local distance table without further communication with
		 * neighbors and the distance table only contains partial information
		 * of the network due to address summarization.
		 * </P>
		 * <P>
		 * The return value is in the format: <code>dest, head,..., this.node</code>
		 * or <code>No route to host.</code>.
		 * </P>
		 * 
		 * @param dest	a valid dotted address string.
		 * @return
		 */
		public String traceRoute(String dest) throws InvalidAddressException
		{
			try
			{
				Address j = (new Address(dest)).summarize(Kernel.getInstance().getRouterAddress());
				StringBuffer sb = new StringBuffer();
			
				synchronized(dtMutex)
				{
					DistanceTable record = (DistanceTable)dt.get(j);
				
					if(record == null || record.hop == -1)
					{
						sb.append("No route to host.");
					}
					else
					{
						sb.append(j.toString());
						Address head = null;
					
						while(!(head = getHead(j, record.hop)).isReserved())
						{
							sb.append(", " + head.toString());
							j = head;
						}
					}
				}
			
				return sb.toString();
			}
			catch(RuntimeException e)
			{
				logger.log("Unknown exception during JMX traceroute operation: " + LogUtil.getStackTrace(e), Log.SEVERITY_ERROR);
				throw e;
			}
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
	
	/**
	 * The MBean interface of the ManageableKernel inner class.
	 * 
	 * @author Erik van Zijst - 17.Oct.2003
	 */
	public interface ManageableKernelMBean
	{
		public String getVersion();
		
		public String getRouterAddress();
		
		/**
		 * Returns the distance table as an array of strings. Each string contains
		 * one entry: <code>destination, hop, head, cost</code>
		 * 
		 */
		public String[] getDistanceTableStrings();
		
		/**
		 * <P>
		 * Returns a machine readable version of the distance table, opposed
		 * to the more human-readable version of
		 * <code>getDistanceTableStrings()</code>.
		 * </P>
		 * <P>
		 * Note that addresses are returned as strings, not as instances of
		 * the Address class. This is to lower the dependencies between client
		 * and server. This way the addresses loose their multicast/unicast
		 * flag, but since the distance table only contains unicast addresses,
		 * this is not regarded as problem.
		 * </P>
		 * <P>
		 * The datastructure is returned as a (sorted) list consisting of
		 * distance table entries. Each entry contains information about one
		 * destination address (often a wildcard address). The entry is
		 * essentially a map containing key/value tuples. There are currently
		 * 4 keys: "<code>dest</code>" (yielding an address string),
		 * "<code>cost</code>" (yielding an <code>Integer</code> instance or
		 * <code>-1</code>), "<code>hop</code>" (yielding the address string
		 * of the preferred interface towards the destination) and
		 * "<code>head</code>" (yielding the address string of the
		 * head-of-path of the shortest path to the detination). Note that
		 * when an address is reserved, it is encoded as "?". Values are never
		 * <code>null</code>. When the cost is infinity (unreachable), the
		 * integer -1 is returned.
		 * </P>
		 * <P>
		 * The records in the returned list are sorted. The most specific
		 * destination is the first record, while the least specific
		 * destination (most probably a wildcard matching many subnets) is the
		 * last. Messages that have to be forwarded are sent to the hop of the
		 * first record that matches the message's destination.
		 * </P>
		 * 
		 * @return	datastructure containing the distance table information.
		 */
		public List getDistanceTable();
	
		/**
		 * Returns the number of configured interfaces. Note that these interfaces
		 * do not necessarily need to be active of connected.
		 * 
		 * @return	the number of configured interfaces.
		 */
		public int getNumberOfInterfaces();
		
		public State getState();
	
		public void sendMessage(String data, String destination, int ttl, boolean multicast) throws InterruptedException;

		/**
		 * <P>
		 * Returns the reverse route messages take to a given destination. Note
		 * that this is not a complete path. Since the path is derived from
		 * the local distance table without further communication with
		 * neighbors and the distance table only contains partial information
		 * of the network due to address summarization.
		 * </P>
		 * <P>
		 * The return value is in the format: <code>dest, head,..., this.node</code>
		 * or <code>no route to host</code>.
		 * </P>
		 * 
		 * @param dest	a valid dotted address string.
		 * @return
		 */
		public String traceRoute(String dest) throws InvalidAddressException;
	}
}
