package com.marketxs.messaging;

import java.io.ByteArrayOutputStream;

/**
 * <P>
 * The NotifyData class is used by routers to announce delivery problems. It
 * is somewhat comparable to the ICMP protocol in IP (RFC 792). When a message cannot be
 * delivered by an intermediate router, the router sends back a NotifyData
 * message that indicates the problem. As an aid to the source, the original
 * message is appended to the NotifyData message. This is somewhat comparable
 * to ICMP where the first 64 bits of the original IP packet are attached to
 * ICMP packets. Currently there are three known network problems:
 * </P>
 * 
 * <P>
 * <B>NO_ROUTE</B><BR>
 * This notification type indicates that the router that sent this
 * notification has no route that matches the ultimate destination address and
 * therefor cannot route the message any further. This happens when the the
 * original message was sent to an non-existing address.
 * </P>
 * 
 * <P>
 * <B>DEST_UNREACHABLE</B><BR>
 * This notification type indicates that the ultmate destination address is in
 * fact a known host address, but there currently is no (finite) path towards
 * it. This happens when a message is sent to a host that recently died.
 * </P>
 * 
 * <P>
 * <B>TTL_EXPIRED</B><BR>
 * This notification type is used when a router has to drop a routable message
 * that is still in transit, but has an expired TTL (Time-to-Live) field. The
 * notification is sent back to the orginal source of the routable message.
 * Note that although the NotifyData message is routable itself and hence has
 * a TTL field, there will be no notification if its TTL expires. This is to
 * avoid notification storms.
 * </P>
 * <P>
 * A primary use of TTL_EXPIRED messages is tracing a path through the
 * network. This is done very similar to the way IP offers traceroute
 * functionality. Since there is no explicit way of obtaining routing
 * information from a remote router, tracing the path to a certain destination
 * works by sending a message (UserData or RoutablePing) to that destination, while setting
 * the TTL of the message to 1. This way the message will be killed by the
 * first router and a TTL_EXPIRED notification is returned, revealing the
 * router's address. Then a message with TTL = 2 is sent, causing a
 * notification to be returned by the next hop, and so on. Each time the
 * address of the next hop is revealed.
 * </P>
 * 
 * <P>
 * Message wire format:
 * </P>
 * <P>
 * <PRE>
 *  0                   1                   2                   3
 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |version| type  |    unused     |           length              |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * | control type  |                   unused                      |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                source address (max 128 bytes)                 |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |              destination address (max 128 bytes)              |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * | time-to-live  |     code      |      original message ...
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
 * </PRE>
 * </P>
 * 
 * <P>
 * Note that problems with NotifyData messages never cause new NotifyData
 * messages to be generated and sent.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 02.jan.2004
 */
public class NotifyData extends ControlData implements Routable
{
	/**
	 * <P>
	 * This notification type indicates that the router that sent this
	 * notification has no route that matches the ultimate destination address and
	 * therefor cannot route the message any further. This happens when the the
	 * original message was sent to an non-existing address.
	 * </P>
	 */
	public static final byte NO_ROUTE = 0;
	public static final String NO_ROUTE_MSG = "No route to host.";

	/**
	 * <P>
	 * This notification type is used when a router has to drop a routable message
	 * that is still in transit, but has an expired TTL (Time-to-Live) field. The
	 * notification is sent back to the orginal source of the routable message.
	 * Note that although the NotifyData message is routable itself and hence has
	 * a TTL field, there will be no notification if its TTL expires. This is to
	 * avoid notification storms.
	 * </P>
	 */
	public static final byte TTL_EXPIRED = 1;
	public static final String TTL_EXPIRED_MSG = "Time to live exceeded.";

	/**
	 * <P>
	 * This notification type indicates that the ultmate destination address is in
	 * fact a known host address, but there currently is no (finite) path towards
	 * it. This happens when a message is sent to a host that recently died.
	 * </P>
	 */
	public static final byte DEST_UNREACHABLE = 2;
	public static final String DEST_UNREACHABLE_MSG = "Destination unreachable.";

	private Address source = Address.RESERVED;
	private Address dest = Address.RESERVED;
	private boolean multicast = false;
	private int ttl = 30;	// healthy default
	private byte code;
	
	private Message cause = null;

	public NotifyData()
	{
		rawdata = null;
		type = TYPE_CONTROL;
		controltype = TYPE_NOTIFY;
	}
	
	public NotifyData(Address src, Address dest, byte code)
	{
		this();
		this.source = src;
		this.dest = dest;
		this.code = code;
	}

	public NotifyData(byte[] raw)
	{
		rawdata = raw;
		type = TYPE_CONTROL;
		controltype = TYPE_NOTIFY;
		deserialize();
	}

	public static Message deserialize(byte[] rawdata) throws DeserializeException
	{
		return new NotifyData(rawdata);
	}

	public void deserialize() throws DeserializeException
	{
		super.deserialize();

		try
		{
			source = new Address();
			dest = new Address();
			int pos = 1 + source.parseAddress(rawdata, 8);
			pos = 1 + dest.parseAddress(rawdata, pos);
			ttl = rawdata[pos++];
			code = rawdata[pos++];
			if(code != NO_ROUTE && code != TTL_EXPIRED && code != DEST_UNREACHABLE)
			{
				throw new DeserializeException("Unknown notification code: " + code);
			}
			
			if(rawdata.length == pos)
			{
				// there is no encapsulated message
				cause = null;
			}
			else
			{
				byte[] rawCause = new byte[rawdata.length - pos];
				System.arraycopy(rawdata, pos, rawCause, 0, rawCause.length);
				cause = Message.deserialize(rawCause);
			}
		}
		catch(Exception e)
		{
			throw new DeserializeException(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public void serialize() throws SerializeException
	{
		// do our own serialization
		ByteArrayOutputStream bos = null;
		
		try
		{
			bos = new ByteArrayOutputStream();
			bos.write(source.serialize());
			bos.write(dest.serialize());
			bos.write(new byte[]{(byte)ttl, code});
			
			if(cause != null)
			{
				cause.serialize();
				bos.write(cause.getRawData());
			}
		}
		catch(Exception e)
		{
			throw new SerializeException(e.getClass().getName() + ": " + e.getMessage(), e);
		}

		rawdata = bos.toByteArray();
		super.serialize();	// add control header
	}

	public Address getSource()
	{
		return source;
	}

	public void setSource(Address source)
	{
		this.source = source;
	}

	public Address getDestination()
	{
		return dest;
	}

	public void setDestination(Address destination)
	{
		this.dest = destination;
	}

	public int getTtl()
	{
		return ttl;
	}

	public void setTtl(int ttl)
	{
		this.ttl = ttl;
	}
	
	/**
	 * <P>
	 * Returns one of this class' predefined codes. For example
	 * <code>NotifyData.TTL_EXPIRED</code>.
	 * </P>
	 * 
	 * @return
	 */
	public byte getCode()
	{
		return code;
	}
	
	public void setCode(byte code)
	{
		this.code = code;
	}

	public boolean isMulticast()
	{
		return false;
	}
	
	public String toString()
	{
		return "Notify(" + source.toString() + " -> " + dest.toString() + "): '" + getDescription() + "'";
	}
	/**
	 * @return
	 */
	public Message getCause() {
		return cause;
	}

	/**
	 * @param message
	 */
	public void setCause(Message message) {
		this.cause = message;
	}

	public String getDescription()
	{
		switch(code)
		{
			case NO_ROUTE:
				return NO_ROUTE_MSG;
			case DEST_UNREACHABLE:
				return DEST_UNREACHABLE_MSG;
			case TTL_EXPIRED:
				return TTL_EXPIRED_MSG;
			default:
				return "Notification code not set.";
		}
	}
}
