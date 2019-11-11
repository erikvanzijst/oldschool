package com.marketxs.messaging;

import java.util.HashMap;
import java.util.Map;

import com.marketxs.management.ManagementHelperInterface;

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
public class TracerouteService
{
	private final ManageableTracerouteService mbean = this.new ManageableTracerouteService();
	private long timeout = 10000;	// default timeout
	
	public String traceroute(Address host)
	{
		return (new TracerouteServiceImpl()).traceroute(host, timeout);
	}
	
	public ManageableTracerouteService getMBean()
	{
		return mbean;
	}

	public interface ManageableTracerouteServiceMBean
	{
		public String traceroute(String address) throws InvalidAddressException;

		/**
		 * @return
		 */
		public long getTimeout();

		/**
		 * @param l
		 */
		public void setTimeout(long l);
	}
	
	public class ManageableTracerouteService implements ManageableTracerouteServiceMBean, ManagementHelperInterface
	{
		private String description = "This utility application is used to trace paths through the network. The attributes below are used as properties of the traceroute operations. When they are changed, they will be used for all traceroutes.";
		private Map operationParameterNameMap = new HashMap();
		private Map attributeDescriptionMap = new HashMap();
		private Map operationDescriptionMap = new HashMap();
		private Map operationParameterDescriptionMap = new HashMap();

		public ManageableTracerouteService()
		{
			attributeDescriptionMap.put("Timeout", "This is the ping timeout. When a reply is not received within this time, the ping is aborted. The default is 10000ms.");
			operationDescriptionMap.put("traceroute", "Traces the path from this node to any given node in the network.");
			operationParameterNameMap.put("traceroute", new String[]{"destinationAddress"});
			operationParameterDescriptionMap.put("traceroute", new String[]{"The address of the node to which the path starting at this node is to be traced."});
		}
		
		public String traceroute(String address) throws InvalidAddressException
		{
			return TracerouteService.this.traceroute(new Address(address));
		}
		
		/**
		 * @return
		 */
		public long getTimeout() {
			return timeout;
		}

		/**
		 * @param l
		 */
		public void setTimeout(long l) {
			timeout = l;
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
