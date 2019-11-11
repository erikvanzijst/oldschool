package com.marketxs.messaging;

/**
 * <P>Implementation according to chapter 18 of Paul Hyde's 
 * 'Java Thread Programming'.</P>
 * <P>
 * N.B. Some additions including peaksize tracking.
 * </P>
 *
 * @author		Leon van Zantvoort
 * @author		Erik van Zijst
 * @version	0.1, 28.nov.2003
 */
public class ObjectFIFO {
	private	Object[]	queue;
	private int			capacity;
	private int			size;
	private int			head;
	private int			tail;
	private int			peakSize;
	
    public ObjectFIFO(int cap) {
		capacity = (cap > 0) ? cap : 1; // at least 1
		queue = new Object[capacity];
		head = 0;
		tail = 0;
		size = 0;
		peakSize = 0;
    }
	
	public int getCapacity() {
		return capacity;
	}
	
	public synchronized int getSize() {
		return size;
	}
	
	public synchronized int getPeakSize()
	{
		return peakSize;
	}
	
	public synchronized void resetPeakSize()
	{
		peakSize = 0;
	}
	
	public synchronized boolean isEmpty() {
		return  (size == 0);
	}
	
	public synchronized boolean isFull() {
		return (size == capacity);
	}
	
	public synchronized void add(Object obj) throws InterruptedException {
		waitWhileFull();
		
		queue[head] = obj;
		head = (head + 1) % capacity;
		size++;
		peakSize = (getSize() > peakSize ? getSize() : peakSize);
		
		notifyAll(); // let any waiting threads know about change
	}
	
	public synchronized void addEach(Object[] list) throws InterruptedException {
		// You might want to code a more efficient implementation here
		
		for (int i = 0; i < list.length; i++) {
			add(list[i]);
		}
	}
	
	public synchronized Object remove() throws InterruptedException {
		waitWhileEmpty();
		
		Object obj = queue[tail];
		
		// don't block GC by keeping unnecessary reference
		queue[tail] = null;
		
		tail = (tail + 1) % capacity;
		size--;
		
		notifyAll(); // let any waiting threads know about change

		return obj;
	}
	
	public synchronized Object[] removeAll() throws InterruptedException {
		// You might want to code a more efficent implementation here
		
		Object[] list = new Object[size]; // use the current size
		
		for (int i = 0; i < list.length; i++) {
			list[i] = remove();
		}
		
		// if FIFO was empty, a zero-length array is returned
		return list;
	}
	
	public synchronized Object[] removeAtLeastOne() throws InterruptedException {
		waitWhileEmpty(); // wait for at least one to be in the FIFO;
		
		return removeAll();
	}
	
	/**
	 * <P>
	 * Returns the values that are currently in the fifo. Note
	 * that this can be smaller than the capacity when the fifo is not yet full.
	 * The returned array is not backed by the fifo. It can be strucurally
	 * modified.
	 * </P>
	 * <P>
	 * The array returned is sorted chronologically. The first object is the
	 * oldest.
	 * </P> 
	 * 
	 * @return	a copy of the internal data array.
	 */
	public synchronized Object[] getValues()
	{
		Object[] values = new Object[size];
		for(int pos = tail, nX = 0; nX < size; pos = (pos + 1) % capacity, nX++)
		{
			values[nX] = queue[pos];
		}

		return values;
	}
		
	public synchronized boolean waitUntilEmpty(long msTimeout) throws InterruptedException {
		if (msTimeout == 0L) {
			waitUntilEmpty(); // use other method
			return true;
		}
		
		// wait only for the specified amount of time
		long endTime = System.currentTimeMillis() + msTimeout;
		long msRemaining = msTimeout;
		
		while(!isEmpty() && (msRemaining > 0L)) {
			wait(msRemaining);
			msRemaining = endTime - System.currentTimeMillis();
		}
		
		// May have timed out, or may have met condition,
		// calc return value.
		return isEmpty();
	}
	
	public synchronized void waitUntilEmpty() throws InterruptedException {
		while(!isEmpty()) {
			wait();
		}
	}
	
	public synchronized void waitWhileEmpty() throws InterruptedException {
		while(isEmpty()) {
			wait();
		}
	}
	
	public synchronized void waitUntilFull() throws InterruptedException {
		while(!isFull()) {
			wait();
		}
	}
	
	public synchronized void waitWhileFull() throws InterruptedException {
		while(isFull()) {
			wait();
		}
	}
}
