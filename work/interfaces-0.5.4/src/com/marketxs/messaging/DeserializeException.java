package com.marketxs.messaging;

/**
 * 
 * 
 * @location	api
 * @author erik
 */
public class DeserializeException extends RuntimeException
{
	public DeserializeException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DeserializeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public DeserializeException(Throwable cause) {
		super(cause);
	}

	public DeserializeException(String message)
	{
		super(message);
	}
}
