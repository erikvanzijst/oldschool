package com.marketxs.messaging;

/**
 * <P>
 * Thrown by the activate() and deactivate() method of various classes when
 * these methods are invoked while the class is in an invalid state. For
 * example invoking deactivate() on an instance that is already stopped.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5, 23.dec.2003
 */
public class InvalidStateException extends RuntimeException
{

	/**
	 * 
	 */
	public InvalidStateException()
	{
		super();
	}

	/**
	 * @param message
	 */
	public InvalidStateException(String message)
	{
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidStateException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public InvalidStateException(Throwable cause)
	{
		super(cause);
	}
}
