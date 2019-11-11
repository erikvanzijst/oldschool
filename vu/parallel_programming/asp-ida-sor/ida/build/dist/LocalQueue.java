package nl.vu.pp.distributed;

import java.rmi.RemoteException;

/**
 * This class is used by the workers. It acts as a very small private job queue
 * That pre-fetches one or more jobs to keep the cpu busy and minimize IO wait.
 * <BR>
 * This class was later added after the IO wait proved to prevent efficient
 * scaling especially with increased workers and jobs.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 10.jan.2003
 */
public class LocalQueue extends JobQueue implements Runnable
{
	private JobManager jobManager = null;
	private Thread localQueue = null;
	private int capacity = 1;
	private boolean outOfWork = false;
	private Object suspendMutex = new Object();

	/**
	 * Creates a new local job queue that actively pre-fetches jobs from
	 * the central job queue at the job manager with a daemon thread. <BR>
	 * <P>
	 * The local queue's daemon thread is automatically started.
	 *
	 * @param	jobManager local or remote reference (stub) to the
	 * 	central job manager.
	 * @param	capacity the local queue's capacity (number of
	 * 	pre-fetched jobs).
	 */
	public LocalQueue(JobManager jobManager, int capacity)
	{
		super(capacity);
		this.jobManager = jobManager;
		localQueue = new Thread(this);
		localQueue.setDaemon(true);
		localQueue.setPriority(Thread.NORM_PRIORITY);
		localQueue.start();
	}
	
	public void open()
	{
		super.open();
		synchronized(suspendMutex)
		{
			suspendMutex.notify();
		}
	}

	public void run()
	{
		try
		{
			for(;;)
			{
				try
				{
					put(jobManager.get());
				}
				catch(OutOfWorkException e)
				{
					synchronized(suspendMutex)
					{
						close();
						suspendMutex.wait();
					}
				}
			}
		}
		catch(RemoteException e)
		{
			System.out.println("Communication problems pre-fetching jobs.");
		}
		catch(InterruptedException e)
		{
			System.out.println("LocalQueue interrupted; exiting.");
		}
	}
}
