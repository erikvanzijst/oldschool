package com.marketxs.messaging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;
import com.marketxs.management.ManagementHelperInterface;

/**
 * <P>
 * This class determines the cost of the link between an interface and its
 * peer. It does so by exchanging so-called ping messages. At periodic
 * intervals a ping message is sent to the peer, carrying the sender's
 * timestamp. The peer bounces the message back without altering the message.
 * When the interface receives the message back, it calculates the difference
 * between the current time and the message's timestamp to derive the
 * round-trip delay.
 * </P>
 * <P>
 * The CostMetricUnit sends a ping message every 5000 milliseconds by default
 * and uses an outbound message interceptor to insert the messages directly
 * into the stream of outgoing messages. It uses the central (singleton)
 * scheduler instance instead of a daemon thread to send the periodic
 * messages. The unit determines the round-trip delay of each ping message and
 * uses a moving average function with a period of 20 to flatten spikes in the
 * ping results. The moving average value is stored as the interface's link
 * cost metric. When a ping message doesn't return (possibly because the peer
 * is in deadlock, or because of a serious congestion problem, or because it
 * was dropped in the send buffer), the messages times out and the link cost
 * is increased.
 * </P>
 * <P>
 * The unit can be configured using the following config properties:
 * </P>
 * 
 * <LI><code>com.marketxs.messaging.CostMetricUnit.pingInterval = 5000</code>
 * <LI><code>com.marketxs.messaging.CostMetricUnit.movingAverage = 20</code>
 *
 * <P>
 * Note that the interval is in milliseconds.
 * </P>
 * TODO: dampen the link cost values much more.
 * 
 * @author Erik van Zijst - 10.oct.2003 - erik@marketxs.com
 */
public class CostMetricUnit
{
	public int PING_INTERVAL = 5000;	// do a ping experiment every 5 seconds
	private int costMetric = DistanceVector.UNREACHABLE;
	private float multiplier = 1f;	// multiplier of 1 by default
	private boolean multiplierConfigured = false;
	private TCPInterface iface = null;	// the interface we work for.
	private boolean activated = false;
	private OutboundInterceptor outbound = new OutboundInterceptor();
	private InboundInterceptor inbound = new InboundInterceptor();
	private PingTask pingTask = null;
	private boolean pingOutstanding = false;
	private float lastValue = DistanceVector.UNREACHABLE;	// used in case of ping timeouts
	private MovingAverage ma = new MovingAverage(20);	// 1 minute moving average
	private Log logger = Log.instance(CostMetricUnit.class);
	private ManageableCostMetricUnit mbean = this.new ManageableCostMetricUnit();
	
	public CostMetricUnit(TCPInterface iface)
	{
		this.iface = iface;
		
		try
		{
			PING_INTERVAL = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.CostMetricUnit.pingInterval", String.valueOf(PING_INTERVAL)));
		}
		catch(Exception e)
		{
		}
		
		try
		{
			ma = new MovingAverage(Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.CostMetricUnit.movingAverage", "20")));
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * <P>
	 * Used to set the value of the cost metric multiplier. The real link
	 * cost is always multiplied by this factor. This could be useful to
	 * simulate sluggish links.
	 * </P>
	 * <P>
	 * This method calculates the new cost metric by first apply the current
	 * multiplier in reverse on the complete data set of ping results and then
	 * applying the new multiplier. This means the new multiplier does not
	 * slowly have to propagate through the whole moving average before
	 * reaching its true effect.
	 * </P>
	 * 
	 * @param multiplier
	 */
	public void setCostMultiplier(float _multiplier)
	{
		synchronized(ma)
		{
			double[] values = ma.getValues();
			ma.clear();
			
			for(int nX = 0; nX < values.length; nX++)
			{
				ma.add( (values[nX] / this.multiplier) * _multiplier );
			}

			this.multiplier = _multiplier;
		}
	}
	
	/**
	 * <P>
	 * Used to retrieve the value of the cost metric multiplier. The real link
	 * cost is always multiplied by this factor. This could be useful to
	 * simulate sluggish links.
	 * </P>
	 * 
	 * @return
	 */
	public float getCostMultiplier()
	{
		synchronized(ma)
		{
			return this.multiplier;
		}
	}
	
	private class PingTask extends TimerTask
	{
		public void run()
		{
			try
			{
				synchronized(ma)
				{
					if(pingOutstanding)
					{
						lastValue += (PING_INTERVAL * multiplier);
						lastValue = (lastValue > DistanceVector.UNREACHABLE ? DistanceVector.UNREACHABLE : lastValue);

						ma.add(lastValue);
					}

					if(ma.getSize() > 0)
					{
						int _cost = (int)ma.getAverage() > DistanceVector.UNREACHABLE ? DistanceVector.UNREACHABLE : (int)ma.getAverage();
						if(_cost != costMetric)
						{
							logger.log("Cost metric on interface " + iface.getId() + " changed from " + costMetric + " to " + (int)ma.getAverage(), Log.SEVERITY_INFO);
							costMetric = _cost;
							iface.statusChange();	// notify the interface observers of the new link cost
						}
					}

					pingOutstanding = true;
				}
				CostMetricUnit.this.outbound.sendPing();
			}
			catch(RuntimeException e)
			{
				logger.log("RuntimeException caught while detecting a ping timeout on interface " + iface.getId() + ": " + LogUtil.getStackTrace(e), Log.SEVERITY_ERROR);
			}
		}
	}
	
	/**
	 * <P>
	 * This class is registered as an interceptor in the outbound interceptor
	 * pipeline. It periodically inserts a ping message in the send buffer.
	 * This message will be bounced back by the peer and received by the
	 * InboundInterceptor class on its return.
	 * </P>
	 * 
	 * @author Erik van Zijst
	 */
	private class OutboundInterceptor extends MessageInterceptor 
	{
		public void sendPing()
		{
			if(activated)
			{
				logger.log("CostMetricUnit of interface " + iface.getId() + " sending a ping message.", Log.SEVERITY_DEBUG);
				PingData ping = new PingData();
				ping.setSource(Kernel.getInstance().getRouterAddress());
				ping.setTimestamp(new Date());
		
				delegate(ping);	// insert message into the outbound stream
			}
		}
	}
	
	/**
	 * <P>
	 * This is the message interceptor that filters out the incoming ping
	 * messages. Ping messages that do not bare our address are bounced back
	 * immediately.
	 * </P>
	 */
	private class InboundInterceptor extends MessageInterceptor
	{
		public void intercept(Message message)
		{
			if(message instanceof PingData)
			{
				PingData ping = (PingData)message; 
				if(ping.getSource().equals(Kernel.getInstance().getRouterAddress()))
				{
					// our message has returned
					synchronized(ma)
					{
						/**
						 * We always increment the measured delay with 1.
						 * Otherwise we could get a 0-delay which makes
						 * routing impossible (every path becomes an optimal
						 * path, even non-simple paths).
						 */
						long delay = (System.currentTimeMillis() - ping.getTimestamp().getTime()) + 1;
						if(delay <= 0)
						{
							logger.log("Clock skew detected; ping message on interface " + iface.getId().toString() + " has a timestamp in the future (message was sent at: " + ping.getTimestamp() + ", current time: " + (new Date()) + "), ignoring ping.", Log.SEVERITY_INFO);
						}
						else
						{
							lastValue = delay * multiplier;
							ma.add(lastValue);
							logger.log("Ping reply after " + delay + "ms.", Log.SEVERITY_DEBUG);
							pingOutstanding = false;
						}
					}
				}
				else
				{
					// this is a message of our peer; bounce it back
					CostMetricUnit.this.outbound.intercept(ping);
				}
			}
			else
			{
				// this is not a ping message, let it pass
				super.intercept(message);
			}
		}
	}
	
	public MessageInterceptor getOutboundInterceptor()
	{
		return outbound;
	}
	
	public MessageInterceptor getInboundInterceptor()
	{
		return inbound;
	}

	public synchronized void activate()
	{
		if(!activated)
		{
			try
			{
				if(!multiplierConfigured)
				{
					// read the multiplier value as configured in the config file for this interface
					// it is read only once, so a modified multiplier is not reset to the config file
					// value when the interface is reconnected. 
					multiplier = Float.parseFloat(Configurator.getProperty("com.marketxs.messaging.interface." + iface.getId() + ".costMultiplier", String.valueOf(multiplier)));
					multiplierConfigured = true;
				}
			}
			catch(Exception e)
			{
			}

			activated = true;
			ma.clear();
			costMetric = DistanceVector.UNREACHABLE;
			
			/**
			 * There's something funny with the scheduled ping messages. When
			 * both peers exchange messages exactly at the same time, the
			 * messages of the host that goes second are delayed and their
			 * round-trip times go tenfold. 2 healthy milliseconds on a LAN
			 * versus 20 to 30ms for the second message. I'm unsure what the
			 * cause is, but it doesn't seem to be related to the application.
			 * Experiments with different JVM's didn't improve things either.
			 * The workaround is to make sure both peers ping at different
			 * times and never simultaneously. That's why we delay each peer
			 * for a random period of time and make sure the pings are
			 * scheduled at an exact fixed frequency.
			 * 
			 * N.B.
			 * This appears to be related to the marketxs logging library.
			 * When log messages are suppressed, round-trips are normal.
			 */
			pingTask = new  PingTask();
			Scheduler.getScheduler().scheduleAtFixedRate(pingTask, (new Random()).nextInt(PING_INTERVAL), PING_INTERVAL);	// schedule for repeated execution
			logger.log("CostMetricUnit for interface " + iface.getId() + " activated.", Log.SEVERITY_INFO);
		}
	}
	
	public synchronized void deactivate()
	{
		if(activated)
		{
			pingTask.cancel();
			activated = false;
			costMetric = DistanceVector.UNREACHABLE;
		}
	}
	
	public int getCostMetric()
	{
		return costMetric;
	}
	
	/**
	 * <P>
	 * Returns the MBean implementation for this instance of CostMetricUnit.
	 * It still has to be registered to JMX.
	 * </P>
	 * 
	 * @return
	 */
	public ManageableCostMetricUnit getMBean()
	{
		return mbean;
	}
	
	public interface ManageableCostMetricUnitMBean
	{
		public float getCostMultiplier();
		
		public void setCostMultiplier(float _multiplier);
		
		/**
		 * <P>
		 * Returns the raw values of the previous ping experiments. The cost
		 * metric value is the moving average over a window of previous ping
		 * values. This method returns the raw values of this moving average
		 * and gives an idea of the jitter in the link cost.
		 * </P>
		 * 
		 * @return
		 */
		public double[] getRawMetricValues();
		
		public int getCostMetric();
	}
	
	/**
	 * <P>
	 * This is the JMX MBean for the CostMetricUnit class.
	 * </P>
	 * 
	 * @author Erik van Zijst - erik@marketxs.com - 12.nov.2003
	 */
	public class ManageableCostMetricUnit implements ManageableCostMetricUnitMBean, ManagementHelperInterface
	{
		private String description = "This class determines the cost of the link between an interface and its " +
			"peer. It does so by exchanging so-called ping messages. At periodic " +
			"intervals a ping message is sent to the peer, carrying the sender's " +
			"timestamp. The peer bounces the message back without altering the message. " +
			"When the interface receives the message back, it calculates the difference " +
			"between the current time and the message's timestamp to derive the " +
			"round-trip delay.";
		private Map operationParameterNameMap = new HashMap();
		private Map attributeDescriptionMap = new HashMap();
		private Map operationDescriptionMap = new HashMap();
		private Map operationParameterDescriptionMap = new HashMap();

		public ManageableCostMetricUnit()
		{
			attributeDescriptionMap.put("CostMetric", "This is the current link cost value. It is based on the moving average of the ping results multiplied by the multiplier value.");
			attributeDescriptionMap.put("CostMultiplier", "The result of every ping is first multiplied by this value before being added to the moving average.");
			attributeDescriptionMap.put("RawMetricValues", "These are the contents of the moving average with ping results. They are used to compute the link cost.");
		}

		public float getCostMultiplier()
		{
			return CostMetricUnit.this.getCostMultiplier();
		}
		
		public void setCostMultiplier(float _multiplier)
		{
			CostMetricUnit.this.setCostMultiplier(_multiplier);
		}

		/**
		 * <P>
		 * Returns a copy of the raw values of the moving average array that 
		 * is used to calculate the link cost metric.
		 * </P>
		 */
		public double[] getRawMetricValues()
		{
			synchronized(ma)
			{
				return ma.getValues();
			}
		}

		/**
		 * <P>
		 * Returns the current (calculated) cost metric as exposed to the
		 * kernel.
		 * </P>
		 */
		public int getCostMetric()
		{
			return CostMetricUnit.this.getCostMetric();
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
}
