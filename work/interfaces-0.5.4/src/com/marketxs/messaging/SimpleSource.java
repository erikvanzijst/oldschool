package com.marketxs.messaging;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <P>
 * Simple source application that reads text on standard input and sends every
 * line of text entered on the console as a separate UserData message to the
 * destination addresses specified at the command line. The destination can
 * either be broadcast or multicast.
 * </P>
 * 
 * @author Erik van Zijst - 27.nov.2003 - erik@marketxs.com
 */
public class SimpleSource
{
	private Transport transport = null;
	private List addresses = new ArrayList();
	private int ttl = 30;

	public static void main(String[] args) throws Exception
	{
		(new SimpleSource())._main(args);
	}

	public void _main(String[] argv) throws Exception
	{
		parseArgs(argv);
		transport = Transport.getInstance();
		
		String buf;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("SimpleSource started. Type some input and press enter to send a message to the specified address(es). Ctrl-d terminates the application.");
		while( (buf = reader.readLine()) != null)
		{
			Iterator it = addresses.iterator();
			while(it.hasNext())
			{
				UserData ud = new UserData();
				ud.setPayload(buf.getBytes());
				ud.setDestination((Address)it.next());
				ud.setTtl(ttl);
			
				ud.serialize();
				transport.send(ud);
			}
		}

		System.exit(0);	// normal termination
	}

	private void parseArgs(String[] argv)
	{
		String usage = "usage:\n\n" +
			"	java com.marketxs.messaging.SimpleSource [OPTION]...\n\n" +
			"Options:\n\n" +
			"	--help (print this usage text and exit)\n" +
			"	-addresses destinations (comma-delimited addresses to publish to)\n" +
			"	-ttl 30 (time-to-live)\n" +
			"	-mcast (used to make all addresses multicast. Unicast when omited)";
		boolean mcast = false;

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
				if(argv[nX].equals("-ttl"))
				{
					ttl = Integer.parseInt(argv[++nX]);
				}
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
