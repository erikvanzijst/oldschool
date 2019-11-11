package com.marketxs.messaging;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * <P>
 * The ping message looks like this:
 * </P>
 *
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
 * |                          timestamp in                         |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                        millis (64 bits)                       |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * </PRE>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.1, 10.oct.2003
 */
public class PingData extends ControlData
{
	protected Address address;
	protected Date timestamp;

	public PingData()
	{
		rawdata = null;
		type = TYPE_CONTROL;
		controltype = TYPE_PING;
		priority = MessagePriority.HIGH;
	}

	public PingData(byte[] raw)
	{
		this();
		rawdata = raw;
		deserialize();
	}

	public static Message deserialize(byte[] rawdata) throws DeserializeException
	{
		return new PingData(rawdata);
	}

	public void deserialize() throws DeserializeException
	{
		super.deserialize();

		try
		{
			address = new Address();
			int pos = 1 + address.parseAddress(rawdata, 8);
			
			long l = 0;
			for(int nX = 56; nX >= 0; pos++, nX -= 8)
				l |= (((long)rawdata[pos] & 0xFF) << nX);

			timestamp = new Date(l);
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
			bos.write(address.serialize());
			
			long l = timestamp.getTime();
			for(int nX = 7; nX >= 0; nX--)
				bos.write((byte)(l >> (nX * 8)));
		}
		catch(Exception e)
		{
			throw new SerializeException(e.getClass().getName() + ": " + e.getMessage());
		}
	
		rawdata = bos.toByteArray();
		super.serialize();	// add protocol header
	}

	public void setSource(Address address)
	{
		this.address = address;
	}

	public Address getSource()
	{
		return address;
	}
	/**
	 * @return
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param date
	 */
	public void setTimestamp(Date date) {
		timestamp = date;
	}
	
	/**
	 * <P>
	 * Returns the string representation of this ping message. Format:
	 * <code>ping(src.address): timestamp</code>
	 * </P>
	 */
	public String toString()
	{
		return "ping(" + address.toString() + "): " + timestamp;
	}
}
