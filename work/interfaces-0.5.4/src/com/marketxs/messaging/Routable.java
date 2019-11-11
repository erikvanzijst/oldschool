package com.marketxs.messaging;

/**
 * <P>
 * A message that implements this interface is a message that can travel more
 * than one single link. The UserData message is an example, whereas a
 * PingData message is not. The latter is sent between neighbors only and is
 * never forwarded. RoutingData is also not routable.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 02.jan.2004
 */
public interface Routable
{
	public Address getSource();
	public void setSource(Address source);
	
	public Address getDestination();
	public void setDestination(Address destination);
	
	public int getTtl();
	public void setTtl(int ttl);
	
	public boolean isMulticast();
}
