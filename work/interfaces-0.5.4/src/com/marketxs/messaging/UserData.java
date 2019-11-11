package com.marketxs.messaging;

import java.io.ByteArrayOutputStream;

/**
 * The UserData message looks like this: <BR>
 *
 * <PRE>
 *  0                   1                   2                   3
 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |version| type  |    unused     |           length              |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                         source address                        |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                      destination address                      |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * | time-to-live  |               neighbor address                |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                          payload data                         |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * </PRE>
 *
 * While the source and destination addresses never change during a packet's
 * journey through the network, the neighbor address is set by every router
 * before forwarding the packet to the next hop. It is an ugly aid that allows the
 * receiving router to determine through which neighbor the packet was
 * received. This information is necessary for making decisions on forwarding
 * incoming broadcast traffic. <BR>
 * A real implementation won't define this header as it uses proper socket
 * pairs to communicate with individual neighbors instead of having one single
 * UDP port to which every neighbors sends traffic to.
 * <P>
 * The 8 bit time-to-live (ttl) field contains the number of hops the packet can
 * pass until it should be killed. It is decremented by every router. When a
 * router receives a packet with a ttl of 0, it is dropped. Default ttl value
 * is 30. When a packet is first inserted into the network, its ttl field is 30
 * (not 29).
 *
 * @location	api
 * @author	Erik van Zijst
 */
public class UserData extends Message implements Routable
{
	protected Address src = new Address();
	protected Address dest = new Address();
	protected int ttl = 30;
	protected Address neighbor;
	protected byte[] payload;

	public UserData()
	{
		rawdata = null;
		type = TYPE_USER;
		
		Address n = new Address();
		n.setReserved();
		setNeighbor(n);
	}

	/**
	 * <P>
	 * This method is given the raw network packet to deserialize.
	 * </P>
	 */
	public UserData(byte[] raw)
	{
		rawdata = raw;
		type = TYPE_USER;
		deserialize();
	}

	public static Message deserialize(byte[] rawdata) throws DeserializeException
	{
		// we don't have no subclasses
		return new UserData(rawdata);
	}

	public void deserialize() throws DeserializeException
	{
		super.deserialize();
		int pos = 0;
		src = new Address();
		dest = new Address();
		neighbor = new Address();

		// our own deserialization
		try
		{
			pos = src.parseAddress(rawdata, 4);
			pos = dest.parseAddress(rawdata, pos + 1);
			ttl = (((int)rawdata[++pos]) & 0xFF);
			pos = neighbor.parseAddress(rawdata, pos + 1);
			
			payload = new byte[length - (pos + 1)];	// everything but the header
			System.arraycopy(rawdata, pos + 1, payload, 0, payload.length);
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
			// first is the src address...
			buf = src.serialize();
			bos.write(buf, 0, buf.length);
			
			// ...second the dest address...
			buf = dest.serialize();
			bos.write(buf, 0, buf.length);
			
			// ...third is the ttl...
			bos.write(ttl);

			// ...and last the neighbor address
			buf = neighbor.serialize();
			bos.write(buf, 0, buf.length);

			// add the payload data to the packet
			bos.write(payload, 0, payload.length);
			
			rawdata = bos.toByteArray();
		}
		catch(Exception e)
		{
			throw new SerializeException(e.getClass().getName() + ": " + e.getMessage());
		}

		super.serialize();	// add the protocol header
	}

	/**
	 * To send a message, create an instance using the empty constructor,
	 * call setPayload(), call setSrc(), call setDest(), call serialize()
	 * and finally call getRawData()
	 */
	public void setPayload(byte[] payload)
	{
		this.payload = payload;
	}

	public byte[] getPayload()
	{
		return payload;
	}

	public void setSource(Address src)
	{
		this.src = src;
	}

	public Address getSource()
	{
		return src;
	}

	public void setDestination(Address dest)
	{
		this.dest = dest;
	}

	public Address getDestination()
	{
		return dest;
	}
	
	public void setTtl(int ttl)
	{
		this.ttl = ttl;
	}
	
	public int getTtl()
	{
		return ttl;
	}

	/**
	 * <P>
	 * Called by the router thread before a packet is send out.
	 * </P>
	 */
	public void setNeighbor(Address neighbor)
	{
		this.neighbor = neighbor;
	}

	/**
	 * <P>
	 * Returns the address of the neighbor that sent us this packet. Only
	 * applicable for incoming packets.
	 * </P>
	 */
	public Address getNeighbor()
	{
		return neighbor;
	}

	/**
	 * <P>
	 * Checks to see if this message is addressed to a multicast or a
	 * unicast address. Returns true is multicast.
	 * </P>
	 */
	public boolean isMulticast()
	{
		return dest.isMulticast();
	}

	/**
	 * <P>
	 * The priority level of normal UserData messages may not be changed. This
	 * method always throws an <code>UnsupportedOperationException</code>.
	 * </P>
	 * 
	 * @version	0.5.1
	 */
	public final void setPriority(Comparable priority) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Message priority is not available for this message type.");
	}

	public String toString()
	{
		return super.toString() + "\nSource address:	" + src + "\n" +
			"Destination address:	" + dest + " (" + (isMulticast() ? "multicast" : "unicast") + ")\n" +
			"TTL:	" + ttl + "\n" +
			"Neighbor:	" + neighbor + "\n" +
			"Payload:	\"" + new String(payload) + "\"";
	}
}
