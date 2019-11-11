package com.marketxs.messaging;

/**
 * <P>
 * This state implementation defines the different states an interface can be
 * in. It is used for interface instances only. It defines 5 states which map
 * onto the states defined by the <code>State</code> class.
 * </P>
 * <P>
 * InterfaceState objects are immutable.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com - 09.dec.2003
 */
public class InterfaceState extends State
{
	private static final long serialVersionUID = -6670410953901791852L;	// allow different versions at client and server
	private static final int _CONNECTED = 0;	// interface activated and connected
	private static final int _CONNECTING = 1;	// interface activated and connecting
	private static final int _UNKNOWN = 2;	// interface in unknown state
	private static final int _ACTIVE = 3;	// interface enabled
	private static final int _INACTIVE = 4;	// interface disabled
	
	/**
	 * <P>
	 * When the interface is activated and successfully connected, it is in
	 * this state.
	 * </P>
	 */
	public static final InterfaceState CONNECTED = new InterfaceState(State.STARTED, _CONNECTED);
	
	/**
	 * <P>
	 * Use this state to indicate an interface is currently busy connecting to
	 * its peer.
	 * </P>
	 * <P>
	 * This implies <code>State.STARTED</code>, so
	 * <code>InterfaceState.CONNECTING.isInState(State.STARTED) == true</code>.
	 * </P>
	 */
	public static final InterfaceState CONNECTING = new InterfaceState(State.STARTED, _CONNECTING);
	
	/**
	 * <P>
	 * Use this state to indicate an interface is activated, but not currently
	 * connected to its peer.
	 * </P>
	 * <P>
	 * This implies <code>State.STARTED</code>, so
	 * <code>InterfaceState.ACTIVE.isInState(State.STARTED) == true</code>.
	 * </P>
	 */
	public static final InterfaceState ACTIVE = new InterfaceState(State.STARTED, _ACTIVE);

	/**
	 * <P>
	 * Use this state to indicate an interface is inactive (i.e. it's
	 * <code>disable()</code> method has been invoked).
	 * </P>
	 * <P>
	 * This implies <code>State.STOPPED</code>, so
	 * <code>InterfaceState.INACTIVE.isInState(State.STOPPED) == true</code>.
	 * </P>
	 */
	public static final InterfaceState INACTIVE = new InterfaceState(State.STOPPED, _INACTIVE);
	
	/**
	 * <P>
	 * Use this state to indicate an interface is in an unknown state.
	 * </P>
	 * <P>
	 * This implies <code>State.UNKNOWN</code>, so
	 * <code>InterfaceState.UNKNOWN.isInState(State.UNKNOWN) == true</code>.
	 * </P>
	 */
	public static final InterfaceState UNKNOWN = new InterfaceState(State.UNKNOWN, _UNKNOWN);
	
	private final State superState;
	private final int statusValue;
	private final String name;
	
	private InterfaceState(State superState, int status)
	{
		super(superState);
		this.superState = superState;
		statusValue = status;
		
		switch(statusValue)
		{
			case _CONNECTING:
				name = "connecting";
				break;
			case _CONNECTED:
				name = "connected";
				break;
			case _ACTIVE:
				name = "active";
				break;
			case _INACTIVE:
				name = "inactive";
				break;
			default:
				name = "unknown";
		}
	}
	
	/**
	 * This constructor should be used by subclasses of the InterfaceState
	 * object.
	 * 
	 * @param state
	 */
	protected InterfaceState(InterfaceState state)
	{
		this(state.superState, state.statusValue);
	}
	
	/**
	 * This method tests to see if this state object represents a state that
	 * implies, or is equal to, the given state. For example,
	 * <code>State.STARTING.isInState(State.STARTING) == true</code>. Also, 
	 * <code>InterfaceState.CONNECTING.isInState(State.STARTED) == true</code>
	 * because an interface can only connect to its peer after it has been
	 * started. Consequently, the opposite does not hold
	 * (<code>State.STARTED.isInState(InterfaceState.CONNECTING) != true</code>)
	 * because a started interface is not necessarily connecting.
	 */
	public boolean isInState(State state)
	{
		if(state instanceof InterfaceState)
		{
			return statusValue == ((InterfaceState)state).statusValue;
		}
		else
		{
			return super.isInState(state);
		}
	}
	
	/**
	 * <P>
	 * Returns the "name" of this status or basically a string representation of
	 * it. All possible return values are "connecting", "connected", "active",
	 * "inactive" and "unknown".
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
