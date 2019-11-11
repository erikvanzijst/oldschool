package com.marketxs.messaging;

/**
 * <P>
 * Thrown by the MessagingConfigurator class when the user is trying to add or
 * remove interfaces with non-existing or non-unique ID's.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com - 23.dec.2003
 */
public class InterfaceIDException extends RuntimeException
{
	public InterfaceIDException()
	{
		super();
	}
	
	public InterfaceIDException(String msg)
	{
		super(msg);
	}
}
