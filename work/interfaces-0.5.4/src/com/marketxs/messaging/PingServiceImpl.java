package com.marketxs.messaging;

import java.util.Arrays;

import com.marketxs.log.Log;

/**
 * <P>
 * Implementation of a simple ping application. It is used by the
 * <code>PingService</code> MBean.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 05.jan.2004
 */
public class PingServiceImpl implements Handler
{
	private RoutablePing ping = null;
	private Routable pong = null;
	private Log logger = Log.instance(PingServiceImpl.class);
	
	/**
	 * <P>
	 * The ping method returns one of the following possible strings:
	 * 
	 * <UL>
	 * <LI>Ping reply from some.address after 14ms.
	 * <LI>Error from intermediate.router.address: No route to host.
	 * <LI>Ping timeout. No response within 10000ms.
	 * <LI>Operation interrupted.
	 * </UL>
	 * </P>
	 * <P>
	 * The default timeout value is 10000 milliseconds.
	 * </P>
	 * 
	 * @param host
	 * @return
	 */
	public synchronized String ping(Address host, int ttl, int size, long timeout)
	{
		Transport t = Transport.getInstance();
		String result = null;
		ping = new RoutablePing();
		ping.setDestination(host);
		ping.setIdentifier((int)System.currentTimeMillis());	// bad..
		ping.setSequencenr(0);
		ping.setTtl(ttl);
		ping.setData(new byte[size]);
		
		t.subscribe(this, t.getLocalAddress());

		try
		{
			long start = System.currentTimeMillis();
			t.send(ping);
			wait(timeout);
			
			if(pong != null)
			{
				if(pong instanceof RoutablePing)
				{
					// success
					result = "Ping reply from " + host + " after " + (System.currentTimeMillis() - start) + "ms.";
				}
				else if(pong instanceof NotifyData)
				{
					result = "Error from " + pong.getSource() + ": " + ((NotifyData)pong).getDescription();
				}
			}
			else
			{
				result = "Ping timeout. No response within " + timeout + "ms.";
			}
		}
		catch(InterruptedException e)
		{
			result = "Operation interrupted.";
		}
		finally
		{
			t.unsubscribe(this, t.getLocalAddress());
		}

		return result;
	}

	public synchronized void callback(Routable message)
	{
		if(message instanceof RoutablePing)
		{
			RoutablePing rp = (RoutablePing)message;
			// is this our reply?
			if(rp.getSource().equals(ping.getDestination()) &&
				rp.getIdentifier() == ping.getIdentifier() &&
				rp.getSequencenr() == ping.getSequencenr() &&
				Arrays.equals(rp.getData(), ping.getData()))
			{
				logger.log("Ping reply received.", Log.SEVERITY_DEBUG);
				pong = rp;
				notify();
			}
		}
		else if(message instanceof NotifyData)
		{
			NotifyData nd = (NotifyData)message;
			// is this for us?
			Message m = nd.getCause();
			if(m instanceof RoutablePing)
			{
				RoutablePing cause = (RoutablePing)m;
				if(cause.getDestination().equals(ping.getDestination()) &&
					cause.getSource().equals(ping.getSource()) &&
					cause.getIdentifier() == ping.getIdentifier() &&
					cause.getSequencenr() == ping.getSequencenr())
				{
					// yes it's for us; our ping didn't make it
					logger.log("Our ping failed.", Log.SEVERITY_DEBUG);
					pong = nd;
					notify();
				}
			}
		}
	}
}
