package nl.vu.ip;

/**
 * This RuntimeException is thrown by the ad server's <code>purchase()</code>
 * method when the client did not pay enough money.
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.apr.2003
 */
public class NotPayedException extends RuntimeException
{
	public NotPayedException()
	{
		super();
	}
	
	public NotPayedException(String m)
	{
		super(m);
	}
}
