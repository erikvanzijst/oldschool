package com.marketxs.messaging;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <P>
 * This class implements a datastructure comparable to that of the ObjectFIFO,
 * but with priorities. When all elements have the same priority level, the
 * two classes behave similar. Elements must implement the
 * <code>Prioritized</code> interface.
 * </P>
 * <P>
 * Depicted below is how the class sorts the elements. New elements are added
 * at the end (tail) of their sublist. For example, when a new element with
 * normal priority is added, it will be inserted at position "C", while the
 * size of the entire list increases by one.
 * </P>
 * <P>
 * <PRE>
 * tail                                                           head
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  |    low    |           normal            |        high         |
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   A         B C                           D E                   F
 * </PRE>
 * </P>
 * <P>
 * When an element is removed using the <code>removeMostSignificant()</code>
 * method, the element at position "F" will be dequeued and returned.
 * </P>
 * 
 * @see	com.marketxs.messaging.Prioritized
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.1, 07.jan.2004
 */
public class PrioritizedObjectFIFO
{
	private TreeMap map = null;
	private int size = 0;
	
	public PrioritizedObjectFIFO()
	{
		map = new TreeMap();
	}
	
	/**
	 * <P>
	 * Elements that are added to the <code>PrioritizedObjectFIFO</code> must
	 * implement the <code>Prioritized</code> interface.
	 * </P>
	 * 
	 * @param o
	 */
	public synchronized void add(Object o)
	{
		Comparable key = ((Prioritized)o).getPriority();
		LinkedList fifo = (LinkedList)map.get(key);
		if(fifo == null)
		{
			fifo = new LinkedList();
			map.put(key, fifo);
		}
		fifo.addLast(o);
		size++;
		notifyAll();
	}
	
	/**
	 * <P>
	 * Equivalent to <code>removeMostSignificant()</code>.
	 * </P>
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized Object remove() throws InterruptedException
	{
		return removeMostSignificant();
	}

	/**
	 * <P>
	 * Selects all elements with the highest priority level and removes the
	 * oldest element of that sublist. This corresponds with position "F" in
	 * the illustration above.
	 * </P>
	 * <P>
	 * This method blocks until there is at least one element in the list.
	 * </P>
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized Object removeMostSignificant() throws InterruptedException
	{
		waitWhileEmpty();
		return remove((Comparable)map.lastKey(), true);
	}
	
	/**
	 * <P>
	 * Selects all elements with the lowest priority level and removes the
	 * oldest element of that sublist. This corresponds with position "B" in
	 * the illustration above.
	 * </P>
	 * <P>
	 * This method blocks until there is at least one element in the list.
	 * </P>
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized Object removeLeastSignificant() throws InterruptedException
	{
		waitWhileEmpty();
		return remove((Comparable)map.firstKey(), true);
	}

	/**
	 * <P>
	 * Selects all messages with giving priority and removes either the newest
	 * or the oldest element from that sublist.
	 * </P>
	 * <P>
	 * This method blocks until there is at least one element in the list.
	 * </P>
	 * 
	 * @param priority
	 * @param oldest
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized Object remove(Comparable priority, boolean oldest) throws InterruptedException
	{
		Object element = null;
		waitWhileEmpty();
		
		LinkedList fifo = (LinkedList)map.get(priority);
		if(fifo == null)
		{
			return null;
		}
		else
		{
			element = oldest ? fifo.removeFirst() : fifo.removeLast();
			size--;
			
			if(fifo.size() == 0)
			{
				map.remove(priority);
			}
			
			notifyAll();
			return element;
		}
	}
	
	public synchronized void removeAll()
	{
		map.clear();
		size = 0;
	}
	
	public synchronized Object[] toArray()
	{
		List list = new ArrayList();
		
		ArrayList keybuf = new ArrayList(map.entrySet());
		for(int nX = keybuf.size() - 1; nX >= 0; nX--)
		{
			LinkedList fifo = (LinkedList) ((Map.Entry)keybuf.get(nX)).getValue();
			list.addAll( fifo );
		}
		return list.toArray();
	}
	
	/**
	 * <P>
	 * Returns the number of all queued objects, regardless of their priority.
	 * </P>
	 * 
	 * @return
	 */
	public synchronized int size()
	{
		return size;
	}
	
	public synchronized boolean isEmpty()
	{
		return size == 0;
	}
	
	private synchronized void waitWhileEmpty() throws InterruptedException
	{
		while(size == 0)
		{
			wait();
		}
	}
}
