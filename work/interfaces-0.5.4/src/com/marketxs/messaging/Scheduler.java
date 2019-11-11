package com.marketxs.messaging;

import java.util.Timer;

/**
 * A singleton version of java.util.Timer
 * 
 * @author Erik van Zijst
 */
public class Scheduler extends Timer
{
	private static Scheduler _ref = null;
	
	private Scheduler()
	{
	}
	
	public static synchronized Scheduler getScheduler()
	{
		if(_ref == null)
			_ref = new Scheduler();
			
		return _ref;
	}
}
