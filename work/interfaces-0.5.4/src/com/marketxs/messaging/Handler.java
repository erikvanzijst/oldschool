package com.marketxs.messaging;

/**
 * 
 * 
 * @location	api
 * @author Erik van Zijst
 */
public interface Handler
{
	public void callback(Routable packet);
}
