package com.marketxs.messaging;

import java.util.HashMap;
import java.util.Map;

import com.marketxs.management.ManagementHelperInterface;

/**
 * <P>
 * Simple application that can ping any host in the network. It is registered
 * in JMX under the name "utils:PingService" and is registered by the
 * <code>MessagingConfigurator</code> when the library is initialized.
 * </P>
 * <P>
 * When the service is used, it spawns a PingServiceImpl class instance that
 * handles the request. This way, multiple concurrent pings can take place.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 03.jan.2004
 */
public class PingService
{
	private final ManageablePingService mbean = this.new ManageablePingService();
	private int ttl = 30;	// default ttl
	private int size = 0;	// 0 extra bytes by default
	private long timeout = 10000L;	// default timeout
	
	/**
	 * <P>
	 * The ping method returns one of the following possible strings:
	 * 
	 * <UL>
	 * <LI>Ping reply from some.address after 14ms
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
	public String ping(Address dest)
	{
		return (new PingServiceImpl()).ping(dest, ttl, size, timeout);
	}
	
	public ManageablePingService getMBean()
	{
		return mbean;
	}
	
	public interface ManageablePingServiceMBean
	{
		public String ping(String host) throws InvalidAddressException;
		
		/**
		 * <P>
		 * This is the number of additional bytes that is appended to the ping
		 * message. Appending data can be useful to measure bandwidth instead
		 * of just delay.
		 * </P>
		 * 
		 * @return
		 */
		public int getSize();

		/**
		 * @return
		 */
		public long getTimeout();

		/**
		 * @return
		 */
		public int getTtl();

		/**
		 * @param i
		 */
		public void setSize(int i);

		/**
		 * @param l
		 */
		public void setTimeout(long l);

		/**
		 * @param i
		 */
		public void setTtl(int i);
	}

	public class ManageablePingService implements PingService.ManageablePingServiceMBean, ManagementHelperInterface
	{
		private String description = "This utility application is used to ping nodes in the network. The attributes below are used as properties of the ping messages. When they are changed, they will be used for all pings.";
		private Map operationParameterNameMap = new HashMap();
		private Map attributeDescriptionMap = new HashMap();
		private Map operationDescriptionMap = new HashMap();
		private Map operationParameterDescriptionMap = new HashMap();

		public ManageablePingService()
		{
			attributeDescriptionMap.put("Timeout", "This is the ping timeout. When a reply is not received within this time, the ping is aborted. The default is 10000ms.");
			attributeDescriptionMap.put("Size", "This is the number of additional bytes that is appended to the ping message. Appending data can be useful to measure bandwidth instead of just delay. The default is 0 additional bytes.");
			attributeDescriptionMap.put("Ttl", "This is the Time-To-Live value used for ping messages. The default is 30.");
			
			operationDescriptionMap.put("ping", "Pings the specified node.");
		
			operationParameterNameMap.put("ping", new String[]{"destinationAddress"});
			
			operationParameterDescriptionMap.put("ping", new String[]{"The address of the node that is to be pinged."});
		}

		public String ping(String address) throws InvalidAddressException
		{
			return PingService.this.ping(new Address(address));
		}
		
		/**
		 * <P>
		 * This is the number of additional bytes that is appended to the ping
		 * message. Appending data can be useful to measure bandwidth instead
		 * of just delay.
		 * </P>
		 * 
		 * @return
		 */
		public int getSize()
		{
			return size;
		}

		/**
		 * @return
		 */
		public long getTimeout() {
			return timeout;
		}

		/**
		 * @return
		 */
		public int getTtl() {
			return ttl;
		}

		/**
		 * @param i
		 */
		public void setSize(int i) {
			size = i;
		}

		/**
		 * @param l
		 */
		public void setTimeout(long l) {
			timeout = l;
		}

		/**
		 * @param i
		 */
		public void setTtl(int i) {
			ttl = i;
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
