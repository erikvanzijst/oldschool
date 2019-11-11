package nl.vu.pp.distributed;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;
import java.io.IOException;

/**
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.jan.2002
 */
public class WorkerImpl extends UnicastRemoteObject implements Worker, Runnable
{
	private String name = null;
	private Thread worker = null;
	private boolean die = false;
	private DasInfo env = null;
	private Worker stub = null;
	private JobManager jobManager = null;
	private LocalQueue localQueue = null;
	private int prefetch = 1;

	/**
	 * At construction, a worker instance obtains a reference to the local
	 * or remote JobManager, registers itself in the RMI registry running
	 * at the master process and passes an RMI stub to itself to the
	 * (possibly remote) JobManager. <BR>
	 * It is important that the central job manager can actively instruct
	 * the workers to die if the puzzle is solved.
	 * <P>
	 */
	public WorkerImpl(int nr, int prefetch) throws RemoteException
	{
		super();

		env = new DasInfo();
		name = new String("//" + env.hostName() + ":" + FifteenPuzzle.registryPort + "/nl.vu.pp.distributed.Worker-" + env.hostNumber() + "-" + nr);

		this.prefetch = prefetch;
		worker = new Thread(this);
		worker.setName(name);
		worker.setPriority(Thread.MIN_PRIORITY);
		worker.setDaemon(true);
		worker.start();
	}

	private void init() throws InterruptedException
	{
		boolean bound = false, registered = false;

		// register ourselves with the RMI registry
		for(;;)
		{
			try
			{
				if(!bound)
				{
					Naming.rebind(name, this);
					bound = true;
				}
				if(jobManager == null) jobManager = JobManagerImpl.instance();

				if(!registered)
				{
					jobManager.registerWorker( (stub = (Worker)Naming.lookup(name)) );
					registered = true;
				}
				break;
			}
			catch(RemoteException e)
			{
				// retry until the master is up and running
				System.out.println("Worker " + name + " unable to contact the job manager: " + e.getClass().getName() + ": " + e.getMessage());
//				e.printStackTrace();
			}
			catch(NotBoundException e)
			{
				// retry until the master is up and running
				System.out.println("Worker " + name + " unable to contact the job manager: " + e.getClass().getName() + ": " + e.getMessage());
//				e.printStackTrace();
			}
			catch(MalformedURLException e)
			{
				System.out.println("Worker " + name + " unable to bind to the registry due to invalid name: " + e.getMessage());
			}

			worker.sleep(2000);
		}

		localQueue = new LocalQueue(jobManager, prefetch);
		System.out.println("Worker " + name + " started successfully.");
	}

	public String getName()
	{
		return name;
	}

	/**
	 * When a worker runs, it repeatetly gets a pending job from the
	 * central job manager's queue and computes it. When the queue is empty
	 * and closed, the worker synchronizes with the other threads and
	 * processes through the <code>idle()</code> barrier in the job manager
	 * and enters a new iteration of the algorithm. <BR>
	 * The worker can be stopped and terminated by calling the
	 * <code>die()</code> method.
	 * <P>
	 * N.B. <BR>
	 * This method is invoked automatically from the worker's constructor.
	 * <P>
	 */
	public void run()
	{
		try
		{
			init();

			while(!die)
			{
				try
				{
					Job job = localQueue.get();
					job.setMaxJobDepth(-1); // don't re-insert it into the queue anymore
					job.solve();
				}
				catch(OutOfWorkException e)
				{
					// wait for the next iteration of the algorithm
					jobManager.idle();
					localQueue.open();
				}
				catch(IOException e)
				{
					if(die)
						break;
					else
						System.out.println("Worker " + name + ": " + e.getClass().getName() + ": " + e.getMessage());
				}
			}
		}
		catch(InterruptedException ie)
		{
//			System.out.println("Worker " + name + " got interrupted (remote request to die); terminating worker thread.");
		}
		catch(Throwable t)
		{
			System.out.println("Unexpected error during execution of worker " + name + ". Worker died.");
			t.printStackTrace();
		}

		try
		{
			Naming.unbind(name);
		}
		catch(Exception e)
		{
		}

		System.out.println("Worker " + name + " computed " + Job.getNodeCount() + " job and exited cleanly.");
	}
	
	public long getNodeCount()
	{
		return Job.getNodeCount();
	}

	/**
	 * Tells the worker thread to exit. <BR>
	 * The worker may not terminate immediately as it finishes its current
	 * job first.
	 * <P>
	 */
	public void kill()
	{
		DasInfo env = new DasInfo();
		die = true;
		if(env.hostNumber() != 0)
		{
			synchronized(FifteenPuzzle.waiterThread)
			{
				FifteenPuzzle.waiterThread.notify();
			}
		}
	}
}
