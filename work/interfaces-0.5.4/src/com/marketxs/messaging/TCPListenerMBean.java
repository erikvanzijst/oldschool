package com.marketxs.messaging;
import java.io.IOException;

/**
 * @author Erik van Zijst - 10.oct.2003 - erik@marketxs.com
 */
public interface TCPListenerMBean
{
	public int getPort();
	public void setPort(int port);
	public ListenerState getState();
	public String getStatusString();
	public void startListener() throws IOException;
	public void stopListener() throws InterruptedException;
	public void setMaxConcurrentInitializers(int maxConcurrent);
	
	/**
	 * Used to retrieve the number of initializer threads that can be active
	 * concurrently. This method is exposed in JMX.
	 * 
	 * @return
	 */
	public int getMaxConcurrentInitializers();
}
