package com.marketxs.messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import com.marketxs.messaging.Address;
import com.marketxs.messaging.DistanceVector;
import com.marketxs.messaging.HelloData;
import com.marketxs.messaging.InvalidAddressException;
import com.marketxs.messaging.Message;
import com.marketxs.messaging.MessagePriority;
import com.marketxs.messaging.NotifyData;
import com.marketxs.messaging.PingData;
import com.marketxs.messaging.PrioritizedObjectFIFO;
import com.marketxs.messaging.RoutablePing;
import com.marketxs.messaging.RoutingData;
import com.marketxs.messaging.UserData;

/**
 * Some test used to manually test individual things with - please ignore.
 * 
 * @author Erik van Zijst
 */
public class TestClass
{
	public static void main(String[] argv) throws Exception
	{
		testFIFO(argv);
	}
	
	public static void testFIFO(String[] argv) throws Exception
	{
		PrioritizedObjectFIFO fifo = new PrioritizedObjectFIFO();
		Message ud = new UserData();

		Message nd = new NotifyData();
		nd.setPriority(MessagePriority.HIGH);

		Message hd = new HelloData();
		hd.setPriority(MessagePriority.NORMAL);
		
		fifo.add(new UserData());
		fifo.add(hd);
		fifo.add(ud);
		fifo.add(nd);
		
		Object[] m = fifo.toArray();
		for(int i = 0; i < m.length; i++)
		{
			System.out.println(((Message)m[i]).getClass().getName() + ": priority = " + ((MessagePriority)((Message)m[i]).getPriority()).getDescription());
		}

		System.out.println("-----");
		while(!fifo.isEmpty())
		{
			Message _m = (Message)fifo.remove();
			System.out.println(_m.getClass().getName() + ": priority = " + ((MessagePriority)_m.getPriority()).getDescription());
		}
	}
	
	public static void testPing(String[] argv)
	{
		if(argv.length < 1)
		{
			System.out.println("  Usage: java TestClass <from>");
			System.exit(1);
		}
		
		Address src = new Address(argv[0]);
		Date date = new Date();
		
		PingData hd = new PingData();
		hd.setSource(src);
		hd.setTimestamp(date);
		hd.serialize();
		
		byte[] raw = hd.getRawData();
		System.out.println("Serialized packet:\n" + hexView(raw));
		
		PingData hd2 = (PingData)Message.deserialize(raw);
		System.out.println("\nParsed packet:\n" + hd2.toString()); 	
	}
	
	public static void testSummarization(String[] argv)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			Address ref = null;
			
			while(true)
			{
				System.out.print("Enter the address to summarize to: ");
				try
				{
					ref = new Address(reader.readLine());
					break;
				}
				catch(InvalidAddressException iae)
				{
					System.out.println("That's not a valid address.");
				}
			}
			
			String addr;
			System.out.print("Enter an address to summarize: ");
			while( (addr = reader.readLine()).length() > 0)
			{
				try
				{
					System.out.println(addr + " is summarized into: " + (new Address(addr)).summarize(ref));
				}
				catch(InvalidAddressException iae)
				{
					System.out.println("That's not a valid address.");
				}

				System.out.print("Enter an address to summarize: ");
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}

	public static void testRoutingData(String[] argv)
	{
		RoutingData rd = new RoutingData();
		DistanceVector[] dvs = new DistanceVector[2];
		dvs[0] = new DistanceVector(new Address("dest.nr.1"), new Address("head.to.dest1"), 54124);
		dvs[1] = new DistanceVector(new Address("dest.nr.2"), new Address("head.to.dest2"), 31);
		rd.setDistanceVectors(dvs);
		rd.setSource(new Address("source.address"));
		rd.serialize();
		
		byte[] raw = rd.getRawData(); 
		System.out.println("Serialized packet:\n" + hexView(raw));

		RoutingData rd2 = (RoutingData)Message.deserialize(raw);
		System.out.println("\nParsed packet:\n" + rd2.toString()); 	
	}
	
	public static void testHelloData(String[] argv)
	{
		if(argv.length < 1)
		{
			System.out.println("  Usage: java TestClass <from>");
			System.exit(1);
		}
		
		Address src = new Address(argv[0]);
		
		HelloData hd = new HelloData();
		hd.setSource(src);
		hd.serialize();
		
		byte[] raw = hd.getRawData();
		System.out.println("Serialized packet:\n" + hexView(raw));
		
		HelloData hd2 = (HelloData)Message.deserialize(raw);
		System.out.println("\nParsed packet:\n" + hd2.toString()); 	
	}

	public static void testUserData(String[] argv)
	{
		if(argv.length < 4)
		{
			System.out.println("  Usage: java TestClass <to> <from> <neighbor> <payload>");
			System.exit(1);
		}
		
		Address to = new Address(argv[0]);
		Address from = new Address(argv[1]);
		Address neighbor = new Address(argv[2]);
		
		UserData packet = new UserData();
		packet.setDestination(to);
		packet.setSource(from);
		packet.setNeighbor(neighbor);
		packet.setPayload(argv[3].getBytes());
		packet.serialize();
		
		byte[] raw = packet.getRawData();
		System.out.println("Serialized packet:\n" + hexView(raw));
		
		UserData parsed = (UserData)Message.deserialize(raw);
		System.out.println("\nParsed packet:\n" + parsed.toString() + ""); 
	}
	
	public static void testNotifyData(String[] argv)
	{
		if(argv.length < 3)
		{
			System.out.println("  Usage: java TestClass <from> <to> <code>");
			System.exit(1);
		}
		
		Address to = new Address(argv[1]);
		Address from = new Address(argv[0]);
		byte code = Byte.parseByte(argv[2]);
		
		NotifyData nd = new NotifyData(from, to, code);
		nd.serialize();
		
		System.out.println("Serialized packet:\n" + hexView(nd.getRawData()));
		
		NotifyData parsed = (NotifyData)Message.deserialize(nd.getRawData());
		System.out.println("\nParsed packet:\n" + parsed.toString() + "");
	}
	
	public static void testRoutablePing(String[] argv)
	{
		if(argv.length < 6)
		{
			System.out.println("  Usage: java TestClass <from> <to> <id> <seq> <ack> <data> <ttl>");
			System.exit(1);
		}
		
		Address src = new Address(argv[0]);
		Address dest = new Address(argv[1]);
		int id = Integer.parseInt(argv[2]);
		int seq = Integer.parseInt(argv[3]);
		boolean ack = Boolean.valueOf(argv[4]).booleanValue();
		int ttl = Integer.parseInt(argv[6]);
		
		RoutablePing ping = new RoutablePing();
		ping.setSource(src);
		ping.setDestination(dest);
		ping.setIdentifier(id);
		ping.setSequencenr(seq);
		ping.setAcknowledged(ack);
		ping.setData(argv[5].getBytes());
		ping.setTtl(ttl);
		ping.serialize();
		
		System.out.println("Serialized packet:\n" + hexView(ping.getRawData()));
		
		RoutablePing parsed = (RoutablePing)Message.deserialize(ping.getRawData());
		System.out.println("\nParsed packet:\n" + parsed.toString() + "");
		System.out.println("Serialized packet:\n" + hexView(parsed.getRawData()) + "");
	}
	
	/**
	 * Simple binary viewer.
	 * 
	 * @param raw
	 * @return
	 */
	public static String hexView(byte[] raw)
	{
		StringBuffer sb = new StringBuffer(
			"	 0                   1                   2                   3\n" +
			"	 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1\n" +
			"	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");

		for(int nX = 0; nX < raw.length; nX = nX + 4)
		{
			sb.append("	|");
			for(int mX = 0; mX < 4 && (mX + nX < raw.length); mX++)
			{
				byte b = raw[nX + mX];
				for(int iX = 7; iX >= 0; iX--)
				{
					int set = (b >> iX) & 1;
					sb.append(set == 1 ? "1" : "0");

					if(iX == 0)
						sb.append("|");
					else
						sb.append(" ");
				}
			}
			sb.append("\n" +
				"	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
		}
		
		return sb.toString();
	}
}
