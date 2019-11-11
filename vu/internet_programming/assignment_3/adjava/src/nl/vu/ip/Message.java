package nl.vu.ip;

import java.util.StringTokenizer;

/**
 * This class represents a protocol message and implements the protocol's wire
 * format. It is able to serialize and deserialize messages. The Message class
 * is used by the static methods of the <code>BankClient</code> class.
 * <P>
 * Note that the protocol's wire format is documented in a separate file. <BR>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.apr.2003
 */
public class Message
{
	public static final int COMMAND_BANK_PAY = 1;
	public static final int COMMAND_BANK_CHECK = 2;
	public static final char ETX = 3;	// End-Of-Text, marks the end of serialized message
	private static final int BUFFER_SIZE = 256;

	public int command = 0;
	public int src = 0;
	public int dest = 0;
	public float amount = 0;
	public int certificate = 0;
	public int retval = 0;

	/**
	 * Takes the message properties and serializes them to a character
	 * string that can be send over a TCP stream. <BR>
	 * The implementation is similar to that of the C programs. <BR>
	 *
	 * @throws	MarshalException thrown when the message properties
	 * 	could not be serialized.
	 * @return	the serialized message string.
	 */
	public String serialize() throws MarshalException
	{
		String s = new String(command + "|" + src + "|" + dest + "|" + amount + "|" + certificate + "|" + retval + (char)ETX);
		
		if(s.length() > BUFFER_SIZE)
			throw new MarshalException();
		
		return s;
	}

	/**
	 * Takes a serialized message string and deserializes it. When this
	 * method returns, the public message properties will reflect the
	 * values from the serialized string. <BR>
	 * The implementation is similar to that of the C programs. <BR>
	 *
	 * @param	buf the serialized message string.
	 * @throws	MarshalException thrown when the serialized message
	 * 	could not be deserialized.
	 */
	public void deserialize(String buf) throws MarshalException
	{
		StringTokenizer strtok = new StringTokenizer(buf, "|" + (char)ETX + "\n");
		int field = 0;

		while(strtok.hasMoreTokens())
		{
			String tok = strtok.nextToken();
			
			switch(field)
			{
				case 0:
					command = Integer.parseInt(tok);
					break;
				case 1:
					src = Integer.parseInt(tok);
					break;
				case 2:
					dest = Integer.parseInt(tok);
					break;
				case 3:
					amount = Float.parseFloat(tok);
					break;
				case 4:
					certificate = Integer.parseInt(tok);
					break;
				case 5:
					retval = Integer.parseInt(tok);
					break;
				default:
					break;
			}
			field++;
		}
	}
}
