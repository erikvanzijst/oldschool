package com.marketxs.messaging;
/**
 * @author Erik van Zijst - 10.oct.2003 - erik@marketxs.com
 */
public interface TCPConnectionInitializerMBean
{
	public String getRemoteHost();
	public int getRemotePort();
	public void stopInitializer() throws InterruptedException;
}
