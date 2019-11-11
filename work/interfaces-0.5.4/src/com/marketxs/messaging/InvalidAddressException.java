package com.marketxs.messaging;

/**
 * Thrown by the Address class when fed with an address identifier that is not
 * syntactically correct.
 * 
 * @location	api
 * @author Erik van Zijst
 */
public class InvalidAddressException extends RuntimeException {

	/**
	 * 
	 */
	public InvalidAddressException() {
		super();
	}

	/**
	 * @param message
	 */
	public InvalidAddressException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidAddressException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public InvalidAddressException(Throwable cause) {
		super(cause);
	}

}
