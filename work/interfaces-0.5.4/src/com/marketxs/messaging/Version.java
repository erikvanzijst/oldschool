package com.marketxs.messaging;

/**
 * <P>
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version
 */
public class Version
{
	private static final String version = "0.5.4";

	public static String getVersion()
	{
		return version;
	}

	public String toString()
	{
		return getVersion();
	}
}
