package nl.vu.pp.multithreaded;

import java.util.NoSuchElementException;

/**
 * This is the worker thread that takes parts of the game tree from the central
 * job queue and solves them in parallel with other workers. Workers do not
 * communicate with each other.
 * <P>
 * When a worker has been created, it starts automatically as a background
 * (daemon) thread and starts popping pending jobs from the given job queue.
 * <P>
 * N.B. <BR>
 * This implementation does not re-insert jobs back in the job queue after a
 * maximum search depth or time slice.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 28.dec.2002
 */
public class Worker extends Thread
{
	private JobQueue jobQueue = null;

	/**
	 * Creates and starts a new worker thread with the given name and
	 * associated job queue. The worker thread runs as a daemon thread.
	 * <P>
	 *
	 * @param	name this is the thread's name and is recommended to be
	 * 	unique.
	 * @param	jobQueue the job queue that the worker uses to obtain
	 * 	work.
	 */
	public Worker(String name, JobQueue jobQueue)
	{
		this.jobQueue = jobQueue;
		setName(name);
		setDaemon(true);
		start();
	}

	public void run()
	{
		try
		{
			for(;;)
			{
				Job job = (Job)jobQueue.pop();
				job.setMaxJobDepth(-1); // don't re-insert it into the queue anymore
				job.solve();
			}
		}
		catch(Throwable t)
		{
			System.out.println("Unexpected error during execution of worker " + getName() + ". Worker died.");
			t.printStackTrace();
		}
	}
}
