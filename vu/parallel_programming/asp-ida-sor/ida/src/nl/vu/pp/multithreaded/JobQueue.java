package nl.vu.pp.multithreaded;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Collection;

/**
 * This class implements the job queue for the multithreaded 15-puzzle
 * implementation. It is used to distribute work among worker threads and to
 * detect when all work has finished.
 * <P>
 * Unlimited jobs can be put in the queue that worker threads can pop
 * from the queue and work on in parallel. When the last job has been put on
 * the queue, the manager thread can invoke the <code>join()</code> method.
 * This method will block until both the queue is empty and all threads have
 * returned for more work. When the <code>join()</code> methods returns it
 * means the algorithm has finished.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 04.jan.2003
 */
public class JobQueue extends LinkedList
{
	private int peakSize = 0, waiters = 0;
	private Object alterMutex = new Object();
	private Object waitMutex = new Object();
	private Object joinMutex = new Object();
	private Object popMutex = new Object();

	public JobQueue()
	{
		super();
	}

	/**
	 * Adds a new job at the end of the list. <BR>
	 *
	 * @param	job	the job that is to be pushed on the queue.
	 */
	public void push(Job job)
	{
		synchronized(alterMutex)
		{
			addLast(job);
			peakSize = (size() > peakSize ? size() : peakSize);

			synchronized(waitMutex)
			{
				waitMutex.notifyAll();
			}
		}
	}

	/**
	 * Returns the first job (oldest) in the queue. If the queue
	 * is empty, this method will block until another thread adds a
	 * job.
	 * <P>
	 *
	 * @exception	InterruptedException
	 * @return	the oldest job in the queue
	 */
	public Job pop() throws InterruptedException
	{
		incrementWaiters();

		synchronized(popMutex)
		{
			synchronized(waitMutex)
			{
				if(size() == 0)
					waitMutex.wait();
			}

			decrementWaiters();
			synchronized(alterMutex)
			{
				Job job = (Job)removeFirst();
				synchronized(joinMutex)
				{
					joinMutex.notify();
				}
				return job;
			}
		}
	}

	private synchronized void incrementWaiters()
	{
		waiters++;
		synchronized(joinMutex)
		{
			joinMutex.notify();
		}
	}

	private synchronized void decrementWaiters()
	{
		waiters--;
	}

	/**
	 * This method blocks until both the queue is empty and all worker
	 * threads have entered the <code>pop()</code> method asking for more
	 * work. This is situation means all jobs have been executed and all
	 * worker threads are idle. <BR>
	 * Invoke this method from the program's main thread to wait for the
	 * whole parallel algorithm to finish. In case of the 15-puzzle, the
	 * main thread may decide to increment the bound, restart the algorithm
	 * in case the first iteration did not yield any solutions.
	 * <P>
	 *
	 * @param	waiters the number of active workers you want to wait
	 * 	for.
	 */
	public void join(int waiters) throws InterruptedException
	{
		synchronized(joinMutex)
		{
			while(this.waiters != waiters || size() != 0)
				joinMutex.wait();
			return;
		}
	}
}
