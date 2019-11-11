package com.marketxs.messaging;

import java.io.Serializable;

/**
 * <P>
 * The State object represents the state of an object or module. It defines 5
 * basic states (stopped, starting, started, stopping and unknown). Components
 * that can be in more distinct states have their own State subclass. An
 * example is the InterfaceState that also defines connected and disconnected.
 * </P>
 * <P>
 * State objects are immutable.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com - 09.dec.2003
 */
public class State implements Serializable
{
	private static final long serialVersionUID = 6823189667841788515L;	// allow different versions at client and server
	private static final int _STOPPED = 1;
	private static final int _STARTING = 2;
	private static final int _STARTED = 3;
	private static final int _STOPPING = 4;
	private static final int _UNKNOWN = 0;
	
	/**
	 * Use this state to indicate a component is stopped.
	 */
	public static final State STOPPED = new State(_STOPPED);
	
	/**
	 * Use this state to indicate a component is starting up. Not every
	 * component that can be in the STARTED state defines this state as well.
	 */
	public static final State STARTING = new State(_STARTING);
	
	/**
	 * Use this state to indicate a component is started successfully.
	 */
	public static final State STARTED = new State(_STARTED);
	
	/**
	 * Use this state to indicate a component is stopping.
	 */
	public static final State STOPPING = new State(_STOPPING);
	
	/**
	 * Use this state to indicate a component is in an unknown state.
	 */
	public static final State UNKNOWN = new State(_UNKNOWN);
	
	private final int statusValue;
	private final String name;
	
	private State(int status)
	{
		statusValue = status;

		switch(statusValue)
		{
			case _STARTED:
				name = "started";
				break;
			case _STARTING:
				name = "starting";
				break;
			case _STOPPED:
				name = "stopped";
				break;
			case _STOPPING:
				name = "stopping";
				break;
			default:
				name = "unknown";
		}
	}
	
	protected State(State status)
	{
		this(status.statusValue);
	}
	
	/**
	 * This method checks to see if this state object represents a state that
	 * implies, or is equal to, the given state. This means that an instance
	 * of this state class can
	 * 
	 * @param state
	 * @return
	 */
	public boolean isInState(State state)
	{
		if(state.getClass().isAssignableFrom(this.getClass()))
		{
			return state.statusValue == statusValue;
		}
		else
		{
			/**
			 * This (state) is in fact a subclass of State that defines its own
			 * states. These states may imply our state (subclass.CONNECTED
			 * probably implies this.STARTED), however it doesn't work both
			 * ways. a STARTED component is not necessarily CONNECTED.
			 * Especially since CONNECTED may not even be applicable to the
			 * component that returned this instance.
			 */
			return false;
		}
	}
	
	/**
	 * <P>
	 * Returns the "name" of this status or basically string representation of
	 * it. All possible return values are "started", "starting", "stopped",
	 * "stopping" and "unknown".
	 * </P>
	 * <P>
	 * Note that this string should never be parsed or evaluated. If states
	 * are to be compared, always use <code>state1.equals(state2)</code> or
	 * (better yet) <code>state1.isInState(state2)</code>. The latter also
	 * catches any relationships between state classes.
	 * </P>
	 * 
	 * @return	the string representation or description of the state.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * <P>
	 * Equal to <code>getName()</code>.
	 * </P>
	 * 
	 * @see com.marketxs.messaging.State.getName()
	 * @return	the string representation or description of the state.
	 */
	public String toString()
	{
		return getName();
	}
}
