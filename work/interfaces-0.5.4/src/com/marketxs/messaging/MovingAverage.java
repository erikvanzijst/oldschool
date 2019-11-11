package com.marketxs.messaging;

/**
 * <P>
 * Simple class that implements a moving average with a fixed period. A moving
 * average has a list of float values. New values are added at the front of
 * the list. When the list is full, the oldest value is removed before adding
 * the new value. The method getAverage() computes the average of the list.
 * This class is used by the CostMetricUnit class to dampen the function of
 * ping delay values.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.1, 10.oct.2003
 */
public class MovingAverage
{
	private double[] queue;
	private int capacity = 0;
	private int head = 0;
	private int tail = 0;
	private int size = 0;
	private double sum = 0;
	
	public MovingAverage(int period)
	{
		queue = new double[period];
		capacity = period;
	}
	
	/**
	 * <P>
	 * Returns the values that are currently in the moving average list. Note
	 * that this can be smaller than the capacity when the list not yet full.
	 * The returned array is a copy of the internal values and can be
	 * modified.
	 * </P>
	 * 
	 * @return	a copy of the internal data array.
	 */
	public synchronized double[] getValues()
	{
		double[] values = new double[size];
		for(int pos = tail, nX = 0; nX < size; pos = (pos + 1) % capacity, nX++)
			values[nX] = queue[pos];

		return values;
	}
	
	/**
	 * <P>
	 * Adds a new value to the moving average list and recomputes the new
	 * average value.
	 * </P>
	 * 
	 * @param value
	 */
	public synchronized void add(double value)
	{
		if(isFull())
		{
			remove();
		}
		queue[head] = value;
		head = (head + 1) % capacity;
		sum += value;
		size++;
	}
	
	public synchronized int getSize()
	{
		return size;
	}
	
	public synchronized boolean isFull()
	{
		return size == capacity;
	}
	
	/**
	 * <P>
	 * Removes the oldest value and recomputes the moving average value.
	 * </P>
	 */
	public synchronized void remove()
	{
		if(size > 0)
		{
			double value = queue[tail];
		
			tail = (tail + 1) % capacity;
			size--;
		
			sum -= value;
		}
	}
	
	public synchronized void clear()
	{
		tail = head = size = 0;
		sum = 0;
	}
	
	/**
	 * <P>
	 * This method throws an ArithmeticException when invoked on an empty
	 * list. Use this method only when this object contains at least one
	 * value.
	 * </P>
	 * 
	 * @return	the calculated average of the list.
	 */
	public synchronized double getAverage()
	{
		return sum / size;
	}
}
