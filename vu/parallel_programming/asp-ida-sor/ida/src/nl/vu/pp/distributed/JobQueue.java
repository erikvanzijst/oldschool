package nl.vu.pp.distributed;

import java.util.LinkedList;
import java.util.NoSuchElementException;

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
 * @author	Erik van Zijst - erik@prutser.cx - 07.jan.2003
 */
public class JobQueue extends LinkedList
{
	private int capacity = 0, jobs = 0;
	private Object alterMutex = new Object();
	private Object waitMutex = new Object();
	private boolean outOfWork = false;

	public JobQueue(int capacity)
	{
		super();
		setCapacity(capacity);
	}

	/**
	 * Adds a new job at the end of the list. <BR>
	 * This method blocks if the queue is full until at least one job is
	 * removed by another thread.
	 * <P>
	 *
	 * @param	job	the job that is to be pushed on the queue.
	 */
	public void put(Job job) throws InterruptedException
	{
		synchronized(alterMutex)
		{
			while(capacity != 0 && size() >= capacity)
				alterMutex.wait();

			addLast(job);
			jobs++;
		}
		synchronized(waitMutex)
		{
			waitMutex.notify();
		}
	}
	
	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}

	/**
	 * Returns the first job (oldest) in the queue. If the queue
	 * is empty, this method will block until another thread adds a
	 * job.
	 * <P>
	 *
	 * @exception	OutOfWorkException
	 * @return	the oldest job in the queue
	 */
	public Job get() throws InterruptedException, OutOfWorkException
	{
		Job job;
		synchronized(waitMutex)
		{
			while((job = (Job)removeFirst()) == null && !outOfWork)
				waitMutex.wait();
		}

		if(job == null && outOfWork)
			throw new OutOfWorkException();
		else
			return job;
	}

	public Object removeFirst()
	{
		synchronized(alterMutex)
		{
			try
			{
				Object o = super.removeFirst();
				alterMutex.notify();
				return o;
			}
			catch(NoSuchElementException e)
			{
				return null;
			}
		}
	}
	
	/**
	 * Call this method when there will be no more new jobs. <BR>
	 * After this, the <code>get()</code> method will always throw a
	 * <code>OutOfWorkException</code> without blocking.
	 * <P>
	 */
	public void close()
	{
		outOfWork = true;
//		System.out.println("Job queue closed after " + jobs + " jobs.");
		synchronized(waitMutex)
		{
			waitMutex.notifyAll();
		}
	}

	/**
	 * Call this method after the queue has been closed to re-open it for
	 * input. <BR>
	 * The <code>get()</code> method will no longer throw the
	 * <code>OutOfWorkException</code> and block util a job is available.
	 * <P>
	 */
	public void open()
	{
		outOfWork = false;
		jobs = 0;
	}
}
