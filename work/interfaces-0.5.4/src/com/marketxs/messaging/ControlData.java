package com.marketxs.messaging;

/**
 * <P>
 * ControlData messages carry topology and management information.
 * The message looks like this:
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
 * </PRE>
 *
 * <P>
 * ControlData is an abstract superclass. It has a number of subclasses
 * such as LinkData (for neighbor configuration), CommandData (such as die and
 * print routing table) and HelloData (used as a handshake between neighbors.
 * Causes the receiving node to reply with its routing table).
 * </P>
 * 
 * @author	Erik van Zijst- erik@marketxs.com
 * @version	0.1, 16.aug.2002
 */

public abstract class ControlData extends Message
{
//	public static final int TYPE_COMMAND = 0;
	public static final int TYPE_HELLO = 1;
	public static final int TYPE_NOTIFY = 2;
//	public static final int TYPE_LINK_CHANGE = 3;
	public static final int TYPE_PING = 4;

	protected int controltype;

	/**
	 * <P>
	 * Looks at the first byte of the second header (fifth byte).
	 * This byte determines which ControlData subclass this message
	 * represents.
	 * 
	 * <UL>
	 * <LI> HelloData
	 * <LI> NotifyData
	 * <LI> PingData
	 * </UL>
	 * </P>
	 */
	public static Message deserialize(byte[] rawdata) throws DeserializeException
	{
		Message message = null;

		switch ((int)(rawdata[4]))
		{
			case TYPE_HELLO:
				message = (Message)HelloData.deserialize(rawdata);
				break;
			case TYPE_PING:
				message = (Message)PingData.deserialize(rawdata);
				break;
			case TYPE_NOTIFY:
				message = (Message)NotifyData.deserialize(rawdata);
				break;
			default:
				throw new DeserializeException("Unsupported control data type: " + (int)(rawdata[4]));
		}

		return message;
	}

	public void serialize() throws SerializeException
	{
		// add the 32 bit control header
		byte[] buf = new byte[rawdata.length + 4];

		try
		{
			buf[0] = (byte)controltype;
			buf[1] = (byte)0;
			buf[2] = (byte)0;
			buf[3] = (byte)0;

			System.arraycopy(rawdata, 0, buf, 4, rawdata.length);
		}
		catch(Exception e)
		{
			throw new DeserializeException(e.getClass().getName() + ": " + e.getMessage());
		}

		rawdata = buf;

		super.serialize();	// add the protocol header
	}

	public void deserialize() throws DeserializeException
	{
		super.deserialize();

		controltype = rawdata[4];
	}

	public String toString()
	{
		return super.toString() + "\nControlData type:	" + controltype;
	}
}
