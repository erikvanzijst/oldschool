package com.marketxs.messaging;

/**
 * RuntimeException for convenience (professional exception ignoring ;)
 * Hey, it's a proof of concept!
 */
public class SerializeException extends RuntimeException
{
	public SerializeException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SerializeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public SerializeException(Throwable cause) {
		super(cause);
	}

	public SerializeException(String message)
	{
		super(message);
	}
}
