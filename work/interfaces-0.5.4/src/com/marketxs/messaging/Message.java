package com.marketxs.messaging;

/**
 * <P>
 * The Message class is the base class of all messages. It defines the first
 * packet header (that includes the protocol version).
 * </P>
 * 
 * @author Erik van Zijst
 * @version	0.1
 */
public abstract class Message implements Prioritized
{
	public static final int TYPE_USER = 0;
	public static final int TYPE_ROUTING = 1;
	public static final int TYPE_CONTROL = 2;
	public static final int TYPE_ROUTABLEPING = 3;
	public static final int MTU = 4096;	// max transfer unit in bytes

	protected byte[] rawdata;
	protected int version;
	protected int type;
	protected int length;
	protected MessagePriority priority = MessagePriority.NORMAL;	// default priority

	/**
	 * <P>
	 * Looks at the packet header, instantiates the right data class
	 * and lets it deserialize itself.
	 * The header of all messages looks like this:
	 * </P>
	 *
	 * <PRE>
	 *  0                   1                   2                   3
	 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * |version| type  |    unused     |           length              |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * </PRE>
	 */
	public static Message deserialize(byte[] rawdata) throws DeserializeException
	{
		Message message = null;

		// version nibble must be 4 (0100) for ExBF with summarization and msg delimiters
		if((rawdata[0] >> 4) != 4)
			throw new DeserializeException("Unsupported protocol version: " + (int)(rawdata[0] >> 4));

		switch ((int)(rawdata[0] & 15))
		{
			case TYPE_USER:
				// user data
				message = (Message)UserData.deserialize(rawdata);
				break;
			case TYPE_ROUTING:
				// routing data
				message = (Message)RoutingData.deserialize(rawdata);
				break;
			case TYPE_CONTROL:
				// some control data subclass
				message = (Message)ControlData.deserialize(rawdata);
				break;
			case TYPE_ROUTABLEPING:
				message = (Message)RoutablePing.deserialize(rawdata);
				break;
			default:
				throw new DeserializeException("Unsupported message type: " + (int)(rawdata[0] & 15));
		}

		return message;
	}

	public void deserialize() throws DeserializeException
	{
		// version nibble must be 4 (0100)
		if((rawdata[0] >> 4) != 4)
			throw new DeserializeException("Unsupported protocol version: " + (int)(rawdata[0] >> 4));
		else
			version = 4;

		type = (int)(rawdata[0] & 15);
		length = (((int)rawdata[2]) & 0xFF) * 256 + (((int)rawdata[3]) & 0xFF);
	}

	public void serialize() throws SerializeException
	{
		// add the 32 bit header, calculate the length
		byte[] buf = new byte[rawdata.length + 4];
		try
		{
			buf[0] = (byte)(type | 64);	// set the first nibble to 3: 01000001 (if type = 1)
			buf[1] = (byte)0;	// unused
			buf[2] = (byte)((rawdata.length + 4) >> 8);
			buf[3] = (byte)(rawdata.length + 4);

			System.arraycopy(rawdata, 0, buf, 4, rawdata.length);
		}
		catch(Exception e)
		{
			throw new SerializeException(e.getClass().getName() + ": " + e.getMessage());
		}

		rawdata = buf;
		length = rawdata.length;
		if(length > MTU)
			throw new SerializeException("Packet too big, maximum size (including headers) is " + MTU + " bytes. This packet is " + length + " bytes.");
	}

	public byte[] getRawData()
	{
		return rawdata;
	}

	public String toString()
	{
		return "Protocol Version:	" + version + "\n" +
			"Message type:	" + type + "\n" +
			"Message length:	" + length + " bytes";
	}
	/**
	 * <P>
	 * Returns the priority level for this message. Message priorities are used
	 * when placing incoming and outgoing messages in buffers. Messages with a
	 * higher priority by-pass messages with a lower priority and placed at
	 * the front of the buffer queue. The default priority level is
	 * <code>MessagePriority.NORMAL</code>.
	 * </P>
	 * <P>
	 * Note that priority levels are not serialized and not enforced beyond
	 * the scope of the local router.
	 * </P>
	 * 
	 * @return	the priority level for this message (an instance of class
	 * 	MessagePriority).
	 * @version	0.5.1
	 */
	public Comparable getPriority()
	{
		return priority;
	}

	/**
	 * <P>
	 * Sets the priority level for this message. Message priorities are used
	 * when placing incoming and outgoing messages in buffers. Messages with a
	 * higher priority by-pass messages with a lower priority and placed at
	 * the front of the buffer queue. The default priority level is
	 * <code>MessagePriority.NORMAL</code>.
	 * </P>
	 * <P>
	 * Note that priority levels are not serialized and not enforced beyond
	 * the scope of the local router.
	 * </P>
	 * 
	 * @param priority	an instance of class MessagePriority.
	 * @version	0.5.1
	 */
	public void setPriority(Comparable priority)
	{
		this.priority = (MessagePriority)priority;
	}

}
