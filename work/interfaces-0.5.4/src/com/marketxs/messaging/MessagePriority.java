package com.marketxs.messaging;

/**
 * <P>
 * This class defines priority levels for Messages. There are three
 * pre-defined levels, but new custom levels can be created when needed.
 * </P>
 * <P>
 * An important property of priority objects is that they must be immutable.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 07.jan.2004
 */
public class MessagePriority implements Comparable
{
	private final Integer priority;
	private final String description;
	public static final MessagePriority NORMAL = new MessagePriority(0, "normal");
	public static final MessagePriority HIGH = new MessagePriority(10, "high");
	public static final MessagePriority LOW = new MessagePriority(-10, "low");

	/**
	 * <P>
	 * Use this constructor to create a custom priority level for messages.
	 * </P>
	 * 
	 * @param priority
	 */
	public MessagePriority(int priority, String desc)
	{
		this.priority = new Integer(priority);
		this.description = desc;
	}

	public int compareTo(Object o)
	{
		return priority.compareTo(((MessagePriority)o).priority);
	}

	public String getDescription()
	{
		return description;
	}
}
