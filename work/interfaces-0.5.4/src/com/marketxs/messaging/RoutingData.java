package com.marketxs.messaging;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * <P>
 * A RoutingData message contains distance vectors from a neighbor.
 * They look like this:
 * </P>
 *
 * <PRE>
 *  0                   1                   2                   3
 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |version| type  |    unused     |            length             |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                         source address                        |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                      destination address                      |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                     head-of-path address                      |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |            distance           |     destination address       |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                     head-of-path address                      |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |            distance           |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * </PRE>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.1, 10.oct.2003
 */
public class RoutingData extends Message
{
	protected Address src;
	protected DistanceVector[] vectors = null;

	public RoutingData()
	{
		rawdata = null;
		type = TYPE_ROUTING;
	}

	public RoutingData(byte[] raw)
	{
		rawdata = raw;
		deserialize();
	}

	public static Message deserialize(byte[] rawdata) throws DeserializeException
	{
		// we don't have no subclasses
		return new RoutingData(rawdata);
	}

	public void deserialize() throws DeserializeException
	{
		super.deserialize();

		// our own deserialization
		try
		{
			int pos = 4;
			
			src = new Address();
			pos = src.parseAddress(rawdata, pos);
			pos++;

			ArrayList vectorList = new ArrayList();
			while(pos < length)
			{
				DistanceVector vector = new DistanceVector();
				pos = vector.destination.parseAddress(rawdata, pos);
				pos = vector.head.parseAddress(rawdata, ++pos);
				vector.distance = (((int)rawdata[++pos]) & 0xFF) * 256 + (((int)rawdata[++pos]) & 0xFF);
				vectorList.add(vector);
				pos++;
			}
			
			vectors = (DistanceVector[])vectorList.toArray(new DistanceVector[vectorList.size()]);
		}
		catch(Exception e)
		{
			throw new DeserializeException(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public void serialize() throws SerializeException
	{
		// our own serialization
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf;

		try
		{
			// first is our own address
			buf = src.serialize();
			bos.write(buf, 0, buf.length);

			for(int nX = 0; nX < vectors.length; nX++)
			{
				// first is the destination address j...
				buf = vectors[nX].destination.serialize();
				bos.write(buf, 0, buf.length);

				// ...next is the head-of-path address to j...
				buf = vectors[nX].head.serialize();
				bos.write(buf, 0, buf.length);

				// ...last 16 bits are the distance from j to src
				bos.write(vectors[nX].distance >> 8);
				bos.write(vectors[nX].distance);
			}
		}
		catch(Exception e)
		{
			throw new SerializeException(e.getClass().getName() + ": " + e.getMessage());
		}

		rawdata = bos.toByteArray();
		super.serialize();	// add protocol header
	}

	public void setDistanceVectors(DistanceVector[] vectors)
	{
		this.vectors = vectors;
	}

	public DistanceVector[] getDistanceVectors()
	{
		return vectors;
	}

	public void setSource(Address src)
	{
		this.src = src;
	}

	/**
	 * <P>
	 * Returns the address of the neighbor that sent the packet.
	 * </P>
	 */
	public Address getSource()
	{
		return src;
	}

	/**
	 * <P>
	 * Displays the routing data in the following format:
	 * </P>
	 * <PRE>
	 * 		DV(source) = {(dest, head, distance), ...}
	 * </PRE>
	 * <P>
	 * For example: <code>DV(mxs.node1) = {(mxs.node2, mxs.node1, 30), (l3.*, mxs.gw, 83)}</code>
	 * describing a collection of two distance vectors coming from neighbor
	 * mxs.node1.
	 * Question mark (?) is used to identify a reserved address.
	 * </P>
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("DV(" + src.toString() + ") = {");

		for(int nX = 0; nX < vectors.length; nX++)
		{
			sb.append("(" + (vectors[nX].destination.isReserved() ? "?" : vectors[nX].destination.toString()) + ", ")
			.append((vectors[nX].head.isReserved() ? "?" : vectors[nX].head.toString()) + ", ")
			.append((vectors[nX].distance == DistanceVector.UNREACHABLE ? "inf" : String.valueOf(vectors[nX].distance)) + ")");
			
			if(nX != (vectors.length - 1))
				sb.append(", ");
		}

		return sb.append("}").toString();
	}
}
