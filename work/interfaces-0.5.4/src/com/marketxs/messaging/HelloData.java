package com.marketxs.messaging;

/**
 * This control message is send by new routers to their neighbors to announce
 * their presence. Upon receipt, the neighbors reply with their routing table.
 * The source address is the address of the new neighbor.
 *
 * The message looks like this:
 *
 * <PRE>
 *  0                   1                   2                   3
 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |version| type  |    unused     |           length              |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * | control type  |                   unused                      |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                        source address                         |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * </PRE>
 *
 * @author	Erik van Zijst - 26.aug.2002 - erik@marketxs.com
 */
public class HelloData extends ControlData
{
	protected Address address;

	public HelloData()
	{
		rawdata = null;
		type = TYPE_CONTROL;
		controltype = TYPE_HELLO;
	}

	public HelloData(byte[] raw)
	{
		rawdata = raw;
		deserialize();
	}

	public static Message deserialize(byte[] rawdata) throws DeserializeException
	{
		return new HelloData(rawdata);
	}

	public void deserialize() throws DeserializeException
	{
		super.deserialize();

		try
		{
			address = new Address();
			address.parseAddress(rawdata, 8);
		}
		catch(Exception e)
		{
			throw new DeserializeException(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public void serialize() throws SerializeException
	{
		// do our own serialization; just the local address
		rawdata = address.serialize();

		super.serialize();
	}

	public void setSource(Address address)
	{
		this.address = address;
	}

	public Address getSource()
	{
		return address;
	}

	public String toString()
	{
		return super.toString() + "\nHello from:	" + address;
	}
}
