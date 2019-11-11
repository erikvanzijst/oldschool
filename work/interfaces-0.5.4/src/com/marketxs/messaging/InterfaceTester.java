package com.marketxs.messaging;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.marketxs.log.Log;
import com.marketxs.log.LogUtil;
import com.marketxs.log.handler.FileLogHandler;
import com.marketxs.log.handler.StdOutLogHandler;
import com.marketxs.management.ManagementHelper;
import com.marketxs.management.ManagementHelperException;

/**
 * @author Erik van Zijst - 10.oct.2003 - erik@marketxs.com
 */
public class InterfaceTester implements InterfaceTesterMBean
{
	private final String jmxName = "test:type=InterfaceTester";
	private Log logger = Log.instance(InterfaceTester.class);
	private Map ifaces = new HashMap();
	
	// default JMX configuration
	private int jmxPort = 8082;
	private String jmxUser = "administrator";
	private String jmxPass = "secret";

	private String id = "blah";
//	private Address ifaceAddress = new Address("node1");
	private Address ifacePeerAddress = new Address("node2");
	private InetAddress ifaceHostName = null;
	private int ifacePort = 0;

	/**
	 * 
	 */
	public InterfaceTester()
	{
		super();

		try
		{
			if(!ManagementHelper.isRegistered(jmxName))
				ManagementHelper.register(jmxName, this);
		}
		catch(ManagementHelperException mhe)
		{
			logger.log("Unable to register " + jmxName + " in JMX: " + LogUtil.getStackTrace(mhe), Log.SEVERITY_WARNING);
		}
	}

	public static void main(String[] args) throws Exception
	{
		Class c = StdOutLogHandler.class;	// initialize stdout log handler
		c = FileLogHandler.class;	// initialize file log handler
		(new InterfaceTester())._main(args);
	}
	
	public void _main(String[] argv) throws Exception
	{
		parseArgs(argv);
		ifaceHostName = InetAddress.getLocalHost();
		ManagementHelper.runHtmlAdaptor(jmxUser, jmxPass, jmxPort);
		logger.log("InterfaceTester started. JMX console at port " + jmxPort, Log.SEVERITY_INFO);

		synchronized(this)
		{
			wait();
		}
	}
	
	public void createInterface()
	{
		if(ifaces.containsKey(id))
		{
			logger.log("Interface " + id + " already exists.", Log.SEVERITY_ERROR);
			return;
		}

		PeerInterface iface = new TCPInterface(id);
		iface.setPeerAddress(ifacePeerAddress);
		iface.setRemoteHost(ifaceHostName);
		iface.setPort(ifacePort);
		
		ifaces.put(id, iface);
		iface.enable();
	}
	
	public void removeInterface()
	{
		PeerInterface iface = (PeerInterface)ifaces.remove(id);
		if(iface != null)
			iface.disable();
	}
	
	public void activateInterface()
	{
		PeerInterface iface = (PeerInterface)ifaces.get(id);
		
		if(iface != null)
		{
			iface.enable();
		}
		else
		{
			logger.log("No such interface (" + id + ")", Log.SEVERITY_WARNING);
		}
	}
	
	public void deactivateInterface()
	{
		PeerInterface iface = (PeerInterface)ifaces.get(id);
		
		if(iface != null)
		{
			iface.disable();
		}
		else
		{
			logger.log("No such interface (" + id + ")", Log.SEVERITY_WARNING);
		}
	}
	
	public String[] getInterfaces()
	{
		String[] lines = null;
		
		try
		{
			lines = new String[ifaces.size()];
			Iterator it = ifaces.keySet().iterator();
		
			for(int nX = 0; nX < lines.length && it.hasNext(); nX++)
			{
				Address addr = (Address)it.next();
				TCPInterface iface = (TCPInterface)ifaces.get(addr);
				lines[nX] = iface.getId() + " (" + iface.getPeerAddress() + " @ " + iface.getRemoteHost().getHostAddress() + ":" + iface.getPort() + "): " + iface.getStatusString();
			}
		}
		catch(RuntimeException re)
		{
			logger.log("Error displaying interface list: " + LogUtil.getStackTrace(re), Log.SEVERITY_ERROR);
			throw re;
		}
		
		return lines;
	}
	
	/**
	 * @return
	 */
	public String getIfaceId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getIfaceHostName() {
		return ifaceHostName.getHostAddress();
	}

	/**
	 * @return
	 */
	public String getIfacePeerAddress() {
		return ifacePeerAddress.toString();
	}

	/**
	 * @return
	 */
	public int getIfacePort() {
		return ifacePort;
	}

	/**
	 * @param string
	 */
	public void setIfaceId(String string) {
		id = string;
	}

	/**
	 * @param string
	 */
	public void setIfaceHostName(String string) throws UnknownHostException
	{
		ifaceHostName = InetAddress.getByName(string);
	}

	/**
	 * @param string
	 */
	public void setIfacePeerAddress(String string) {
		ifacePeerAddress = new Address(string);
	}

	/**
	 * @param i
	 */
	public void setIfacePort(int i) {
		ifacePort = i;
	}

	private void parseArgs(String[] argv)
	{
		final String usage = "usage:\n\n" +
			"	java " + getClass().getName() + " [OPTION]...\n\n" +
			"Options:\n\n" +
			"	--help (print this usage text and exit)\n" +
			"	-http_port 8082 (JMX HTTP management port)\n" +
			"	-http_user administrator (JMX HTTP management username)\n" +
			"	-http_passwd secret (JMX HTTP management password)\n";

		try
		{
			for(int nX = 0; nX < argv.length; nX++)
			{
				if(argv[nX].equals("--help"))
				{
					System.out.println(usage);
					System.exit(0);
				}
				if(argv[nX].equals("-http_port"))
					jmxPort = Integer.parseInt(argv[++nX]);
				if(argv[nX].equals("-http_user"))
					jmxUser = argv[++nX];
				if(argv[nX].equals("-http_passwd"))
					jmxPass = argv[++nX];
			}
		}
		catch(Exception e)
		{
			logger.log("Error parsing commandline arguments: " + e.getMessage(), Log.SEVERITY_EMERGENCY);
			System.err.println(usage);
			System.exit(1);
		}
	}
}
