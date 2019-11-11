package com.marketxs.messaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <P>
 * The SimpleSink application is a utility class that can be used to listen
 * for messages sent to a certain address. It can be configured to listen to a
 * number of addresses (both unicast and multicast) and will print the content
 * of all received messages.
 * </P>
 * <P>
 * Note that the messaging library still needs to be configured using the
 * messaging.properties (or system parameters) that defines one or more
 * interfaces to routers.
 * </P>
 * <P>
 * Usage: $ java com.marketxs.messaging.SimpleSink --help
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.1, 11.nov.2003
 */
public class SimpleSink implements Handler
{
	private Transport transport = null;
	private List addresses = new ArrayList();
	private boolean mcast = false;
	private boolean verbose = false;
	
	public static void main(String[] args) throws Exception 
	{
		(new SimpleSink())._main(args);
	}
	
	public void _main(String[] argv) throws Exception
	{
		parseArgs(argv);
		transport = Transport.getInstance();
		
		Iterator it = addresses.iterator();
		while(it.hasNext())
		{
			transport.subscribe(this, (Address)it.next());
		}
		
		System.out.println((new Date()).toString() + " - SimpleSink started and subscribed to " + addresses.size() + " "+ (mcast ? "multicast" : "unicast") + " address(es).");
	}

	public void callback(Routable ud)
	{
		if(verbose)
		{
			System.out.println("\t" + (new Date()).toString() + "\n" + ud.toString() + "\n\n");
		}
		else
		{
			if(ud instanceof UserData)
			{
				System.out.println("Message from " + ud.getSource() + " received on " + ud.getDestination() + ": \"" + new String(((UserData)ud).getPayload()) + "\"");
			}
			else if(ud instanceof NotifyData)
			{
				System.out.println("Notification from " + ud.getSource() + ": " + ((NotifyData)ud).toString());

			}
		}
	}
	
	private void parseArgs(String[] argv)
	{
		String usage = "usage:\n\n" +
			"	java com.marketxs.messaging.SimpleSink [OPTION]...\n\n" +
			"Options:\n\n" +
			"	--help (print this usage text and exit)\n" +
			"	-addresses topics (comma-delimited addresses to subscribe to)\n" +
			"	-v (verbose, print message details)\n" +
			"	-mcast (used to make all addresses multicast. Unicast when omited)";

		try
		{
			for(int nX = 0; nX < argv.length; nX++)
			{
				if(argv[nX].equals("--help"))
				{
					System.out.println(usage);
					System.exit(0);
				}
				if(argv[nX].equals("-addresses"))
				{
					for(StringTokenizer tok = new StringTokenizer(argv[++nX], ", "); tok.hasMoreTokens(); addresses.add(new Address(tok.nextToken())))
						;
				}
				if(argv[nX].equals("-mcast"))
					mcast = true;
				if(argv[nX].equals("-v"))
					verbose = true;
			}

			if(addresses.size() == 0)
			{
				System.err.println(usage);
				System.exit(1);
			}
			
			if(mcast)
			{
				// if the -mcast parameter was set, then all addresses are
				// turned into multicast addresses
				Iterator it = addresses.iterator();
				while(it.hasNext())
				{
					((Address)it.next()).setMulticast(true);
				}
			}
		}
		catch(Exception e)
		{
			System.err.println("Error parsing commandline arguments: " + e.getMessage());
			System.err.println(usage);
			System.exit(1);
		}
	}
}
