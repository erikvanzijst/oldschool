package com.marketxs.messaging;

import java.util.Arrays;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;

/**
 * <P>
 * This application traces the path from this node to any specified
 * destination address. The result of the trace is returned as a string in the
 * following format:
 * </P>
 * <P>
 * 4 hops: s1.c (0ms) -> s1.a (2ms) -> s3.a (19ms) -> s2.c (43ms) -> s2.b (46ms)
 * </P>
 * 
 * <P>
 * When traceroute fails because a router returns "No route to host.", the
 * result looks like:
 * </P>
 * <P>
 * s1.c (0ms) -> s1.a (2ms) -> s3.a: "No route to host." (19ms)
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 05.jan.2004
 */
public class TracerouteServiceImpl implements Handler
{
	private Log logger = Log.instance(TracerouteServiceImpl.class);
	private boolean reached = false;
	private RoutablePing ping = null;
	private Routable pong = null;
	
	public synchronized String traceroute(final Address host, long timeout)
	{
		Transport t = Transport.getInstance();
		StringBuffer result = new StringBuffer();
		int identifier = (int)System.currentTimeMillis();	// bad..
		int ttl = 0;
		long timeLeft = timeout;

		t.subscribe(this, t.getLocalAddress());
		
		try
		{
			while(!reached)
			{
				pong = null;
				ping = new RoutablePing();
				ping.setDestination(host);
				ping.setIdentifier(identifier);
				ping.setTtl(ttl);
				logger.log("Sending ping with ttl = " + ping.getTtl(), Log.SEVERITY_DEBUG);
				long start = System.currentTimeMillis();
				t.send(ping);
				if(timeLeft > 0)
				{
					wait(timeLeft);
				}
				long rtt = System.currentTimeMillis() - start;
				
				String tmp = "";
				if(pong != null)
				{
					tmp = (ttl == 0 ? "Traceroute: " : " -> ") + pong.getSource() + " (" + rtt + "ms)";
					if(pong instanceof RoutablePing)
					{
						// we've reached the destination
						reached = true;
					}
					else if(pong instanceof NotifyData)
					{
						// error, if this is "ttl expired", increase ttl and retry.
						NotifyData nd = (NotifyData)pong;
						if(nd.getCode() == NotifyData.TTL_EXPIRED)
						{
							ttl++;
							timeLeft -= rtt;
						}
						else
						{
							// no route beyond this point in the network
							tmp = ": " + nd.getDescription() + " - Trace aborted.";
							reached = true;
						}
					}
					result.append(tmp);
				}
				else
				{
					result.append(" Timeout. Trace did not complete withing " + timeout + "ms.");
					reached = true;
				}
			}
		}
		catch(InterruptedException ie)
		{
			result.append(" Operation interrupted.");
		}
		catch(RuntimeException re)
		{
			logger.log("Exception during traceroute: " + LogUtil.getStackTrace(re), Log.SEVERITY_ERROR);
			throw re;
		}
		finally
		{
			t.unsubscribe(this, host);
		}
		
		return result.toString();
	}

	public synchronized void callback(Routable message)
	{
		if(message instanceof RoutablePing)
		{
			RoutablePing rp = (RoutablePing)message;
			// is this our reply?
			if(rp.getSource().equals(ping.getDestination()) &&
				rp.getIdentifier() == ping.getIdentifier() &&
				Arrays.equals(rp.getData(), ping.getData()))
			{
				logger.log("Ping reply received.", Log.SEVERITY_DEBUG);
				pong = rp;
				notify();
			}
		}
		else if(message instanceof NotifyData)
		{
			Message m = ((NotifyData)message).getCause();
			// is this for us?
			if(m instanceof RoutablePing)
			{
				RoutablePing cause = (RoutablePing)m;
				if(cause.getDestination().equals(ping.getDestination()) &&
					cause.getSource().equals(ping.getSource()) &&
					cause.getIdentifier() == ping.getIdentifier())
				{
					logger.log("Our ping failed. (" + ((NotifyData)message).getSource() + ": " + ((NotifyData)message).getDescription() + ")", Log.SEVERITY_DEBUG);
					pong = (NotifyData)message;
					notify();
				}
			}
		}
	}

}
