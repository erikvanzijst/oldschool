package com.marketxs.messaging;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * <P>
 * </P>
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
 * |                source address (max 128 bytes)                 |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |              destination address (max 128 bytes)              |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |           identifier          |        sequence number        |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * | time-to-live  |      ack      |            data...
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++-+-+-+-+-+-+-+-+-
 * </PRE>
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 02.jan.2004
 */
public class RoutablePing extends Message implements Routable
{
	private Address source = Address.RESERVED;
	private Address destination = Address.RESERVED;
	private int ttl = 30;	// healthy default
	private final boolean multicast = false;	// ping's are always unicast
	private int identifier = 0;	// as in icmp, used for matching echos and replies
	private int sequencenr = 0;	// as in icmp, used for matching echos and replies
	private boolean acknowledged = false;	// this true on the way back (comparable to icmp type 8 & 0)
	private byte[] data = new byte[0];	// automatically truncated to MTU

	public RoutablePing()
	{
		rawdata = null;
		type = TYPE_ROUTABLEPING;
	}
	
	public RoutablePing(Address dest)
	{
		this();
		this.destination = dest;
	}
	
	public RoutablePing(Address src, Address dest)
	{
		this(dest);
		this.source = src;
	}

	public RoutablePing(byte[] raw)
	{
		rawdata = raw;
		type = TYPE_ROUTABLEPING;
		deserialize();
	}

	public static Message deserialize(byte[] rawdata) throws DeserializeException
	{
		return new RoutablePing(rawdata);
	}

	public void deserialize() throws DeserializeException
	{
		super.deserialize();

		// our own deserialization
		source = new Address();
		destination = new Address();
		try
		{
			int pos = 1 + source.parseAddress(rawdata, 4);
			pos = 1 + destination.parseAddress(rawdata, pos);
			
			identifier = (((int)rawdata[pos++]) & 0xFF) * 256 + (((int)rawdata[pos++]) & 0xFF);
			sequencenr = (((int)rawdata[pos++]) & 0xFF) * 256 + (((int)rawdata[pos++]) & 0xFF);
			
			ttl = (((int)rawdata[pos]) & 0xFF);
			acknowledged = (rawdata[++pos] & (byte)128) == (byte)128;	// only the MSB is used
			
			data = new byte[length - ++pos];
			System.arraycopy(rawdata, pos, data, 0, data.length);
		}
		catch(Exception e)
		{
			throw new DeserializeException(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public void serialize() throws SerializeException
	{
		// do our own serialization; address header plus payload
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf;

		try
		{
			bos.write(source.serialize());
			bos.write(destination.serialize());
			
			bos.write(new byte[]{(byte)(identifier >> 8), (byte)(identifier)});	// only use the right 2 bytes
			bos.write(new byte[]{(byte)(sequencenr >> 8), (byte)(sequencenr)});	// only use the right 2 bytes

			bos.write((byte)ttl);
			bos.write(acknowledged ? (byte)128 : (byte)0);
			
			// truncate payload data if necessary
			if(data.length > 0)
			{
				bos.write(data, 0, data.length);
			}
			
			rawdata = bos.toByteArray();
		}
		catch(Exception e)
		{
			throw new SerializeException(e.getClass().getName() + ": " + e.getMessage());
		}

		super.serialize();	// add the protocol header
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
		return destination;
	}

	public void setDestination(Address destination)
	{
		this.destination = destination;
	}

	public int getTtl()
	{
		return ttl;
	}

	public void setTtl(int ttl)
	{
		this.ttl = ttl;
	}

	public boolean isMulticast()
	{
		return false;
	}
	/**
	 * @return
	 */
	public boolean isAcknowledged() {
		return acknowledged;
	}

	/**
	 * @param b
	 */
	public void setAcknowledged(boolean b) {
		acknowledged = b;
	}

	/**
	 * @return
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * @return
	 */
	public int getSequencenr() {
		return sequencenr;
	}

	/**
	 * <P>
	 * Note that only the 16 low order bits are used.
	 * </P>
	 * 
	 * @param i
	 */
	public void setIdentifier(int i) {
		identifier = (i & 65535);
	}

	/**
	 * <P>
	 * Note that only the 16 low order bits are used.
	 * </P>
	 * 
	 * @param i
	 */
	public void setSequencenr(int i) {
		sequencenr = (i & 65535);
	}

	public boolean equals(Object o)
	{
		RoutablePing ping = (RoutablePing)o;
		
		return ping.acknowledged == acknowledged &&
			ping.multicast == multicast &&
			ping.destination.equals(destination) &&
			ping.source.equals(source) &&
			ping.identifier == identifier &&
			ping.sequencenr == sequencenr &&
			ping.ttl == ping.ttl &&
			Arrays.equals(ping.data, data);
	}
	
	/**
	 * <P>
	 * Returns a string representation of the ping message. Format:
	 * RPing(src -> dest): {identifier = id, seq = seq#, ack = [false|true], size = 0}
	 * </P> 
	 */
	public String toString()
	{
		return "RPing(" + source.toString() + " -> " + destination.toString() + "): {identifier = " + identifier +
			", seq = " + sequencenr + ", ack = " + acknowledged + ", ttl = " + ttl + ", size = " + data.length + "}";
	}
	
	/**
	 * @return
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * <P>
	 * Appends additional data to the ping message. This can be used to
	 * experiment with bandwidth measurements. Since the RoutablePing message
	 * can occupy a maximum of 274 bytes (128 bytes source address, 128 bytes
	 * destination address, 4 bytes general message header, 2 bytes
	 * identifier, 2 bytes sequencenr, 1 byte ttl and 1 byte acknowledgement),
	 * the additional data is automatically truncated to MTU - 274 bytes.
	 * </P>
	 * 
	 * @param bs
	 */
	public void setData(byte[] bs)
	{
		data = new byte[bs.length];
		System.arraycopy(bs, 0, data, 0, data.length);
	}

	/**
	 * <P>
	 * The priority level of normal UserData messages may not be changed. This
	 * method always throws an <code>UnsupportedOperationException</code>.
	 * </P>
	 * 
	 * @version	0.5.1
	 */
	public final void setPriority(int priority) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Message priority is not available for this message type.");
	}
}
