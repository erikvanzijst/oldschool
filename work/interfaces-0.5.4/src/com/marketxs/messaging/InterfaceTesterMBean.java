package com.marketxs.messaging;

import java.net.UnknownHostException;

/**
 * @author Erik van Zijst
 */
public interface InterfaceTesterMBean
{
	public String[] getInterfaces();
	
	public void createInterface();

	public void removeInterface();

	public void activateInterface();

	public void deactivateInterface();

 	public String getIfaceId();

	/**
	 * @return
	 */
	public String getIfaceHostName();

	/**
	 * @return
	 */
	public String getIfacePeerAddress();

	/**
	 * @return
	 */
	public int getIfacePort();

	/**
	 * @param string
	 */
	public void setIfaceId(String string);

	/**
	 * @param string
	 */
	public void setIfaceHostName(String string) throws UnknownHostException;

	/**
	 * @param string
	 */
	public void setIfacePeerAddress(String string);

	/**
	 * @param i
	 */
	public void setIfacePort(int i);
}
