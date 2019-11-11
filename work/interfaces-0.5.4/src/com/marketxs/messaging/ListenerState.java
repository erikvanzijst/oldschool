package com.marketxs.messaging;

/**
 * <P>
 * Represents the state of the TCPListener class.
 * </P>
 * 
 * @see	com.marketxs.messaging.TCPListener
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.3, 10.dec.2003
 */
public class ListenerState extends State
{
	private static final long serialVersionUID = -4153167749613711662L;	// stay in control of client/server compatibility
	private static final int _UNKNOWN = 0;	// interface in unknown state
	private static final int _STOPPED = 1;
	private static final int _STARTING = 2;
	private static final int _LISTENING = 3;
	private static final int _STOPPING = 4;

	/**
	 * <P>
	 * Use this state to indicate a listener is in an unknown state.
	 * </P>
	 */
	public static final ListenerState UNKNOWN = new ListenerState(State.UNKNOWN, _UNKNOWN);
	
	/**
	 * <P>
	 * Use this state to indicate a listener is currently stopped and not
	 * listening.
	 * </P>
	 */
	public static final ListenerState STOPPED = new ListenerState(State.STOPPED, _STOPPED);
	
	/**
	 * <P>
	 * Use this state to indicate a listener is currently busy starting.
	 * </P>
	 */
	public static final ListenerState STARTING = new ListenerState(State.STARTING, _STARTING);
	
	/**
	 * <P>
	 * Use this state to indicate a listener is currently started and
	 * listening. This is implies <code>State.STARTED</code>.
	 * </P>
	 */
	public static final ListenerState LISTENING = new ListenerState(State.STARTED, _LISTENING);
	
	/**
	 * <P>
	 * Use this state to indicate a listener is currently stopped and not
	 * listening.
	 * </P>
	 */
	public static final ListenerState STOPPING = new ListenerState(State.STOPPING, _STOPPING);
	
	private final State superState;
	private final int statusValue;
	private final String name;

	/**
	 * 
	 * @param superState
	 * @param status
	 */
	private ListenerState(State superState, int status)
	{
		super(superState);
		this.superState = superState;
		statusValue = status;
		
		switch(statusValue)
		{
			case _STOPPED:
				name = "stopped";
				break;
			case _STARTING:
				name = "starting";
				break;
			case _LISTENING:
				name = "listening";
				break;
			case _STOPPING:
				name = "stopping";
				break;
			default:
				name = "unknown";
		}
	}

	/**
	 * Protected constructor that is used when creating a subclass.
	 * 
	 * @param state
	 */
	protected ListenerState(ListenerState state)
	{
		this(state.superState, state.statusValue);
	}

	/**
	 * This method tests to see if this state object represents a state that
	 * implies, or is equal to, the given state. For example,
	 * <code>State.STARTING.isInState(State.STARTING) == true</code>. Also, 
	 * <code>ListenerState.LISTENING.isInState(State.STARTED) == true</code>
	 * because an interface can only listen for connections after it has been
	 * started. Consequently, the opposite does not hold
	 * (<code>State.STARTED.isInState(ListenerState.LISTENING) != true</code>).
	 */
	public boolean isInState(State state)
	{
		if(state instanceof ListenerState)
		{
			return statusValue == ((ListenerState)state).statusValue;
		}
		else
		{
			return super.isInState(state);
		}
	}
	
	/**
	 * <P>
	 * Returns the "name" of this status or basically a string representation of
	 * it. All possible return values are "stopped", "starting", "listening",
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
	 * @see getName
	 * @return	the string representation or description of the state.
	 */
	public String toString()
	{
		return getName();
	}
}
