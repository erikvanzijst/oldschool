package com.marketxs.messaging;

/**
 * <P>
 * This is a very simple application that starts the messaging library and
 * then just sits and waits. Effectively this is all that is needed to deploy
 * a single overlay router. The router can be configured using either the
 * default properties file, environment variables or an external JMX client
 * (HTML or RMI). The SimpleRouter application is included in the .jar file
 * and can be run with the following command:
 * </P>
 * 
 * <code>java -jar mxs-messaging-0.5.jar com.marketxs.messaging.SimpleRouter</code>
 * 
 * @location	api
 * @author Erik van Zijst - 22.oct.2003 - erik@marketxs.com
 */
public class SimpleRouter
{
	public static void main(String[] args) throws Exception
	{
		(new SimpleRouter())._main(args);
	}
	
	private void _main(String[] argv) throws Exception
	{
		Transport transport = Transport.getInstance();
		
		synchronized(this)
		{
			wait();
		}
	}
}
