package nl.vu.ip;

/**
 * This RuntimeException is thrown by the Message class when it is unable to
 * serialize or deserialize a message. This can happen when the bank server
 * returns an invalid response for example.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.apr.2003
 */
public class MarshalException extends RuntimeException
{
	public MarshalException()
	{
		super();
	}

	public MarshalException(String m)
	{
		super(m);
	}
}

