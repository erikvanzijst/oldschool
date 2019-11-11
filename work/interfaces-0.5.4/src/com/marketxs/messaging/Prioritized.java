package com.marketxs.messaging;

/**
 * <P>
 * </P>
 *
 * @see	com.marketxs.messaging.MessagePriority
 * @see	com.marketxs.messaging.PrioritizedObjectFIFO
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 07.jan.2004
 */
public interface Prioritized
{
	/**
	 * <P>
	 * Returns an immutable comparable.
	 * </P>
	 * 
	 * @return
	 */
	public Comparable getPriority();
	
	/**
	 * <P>
	 * Some implementations may not allow the priority level to be changed. In
	 * that case, the implementation may throws an
	 * <code>UnsupportedOperationException</code>.
	 * </P>
	 * 
	 * @param priority
	 * @throws UnsupportedOperationException
	 */
	public void setPriority(Comparable priority) throws UnsupportedOperationException;
}
