package nl.vu.pp.distributed;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.util.Collections;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

/**
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.jan.2003
 */
public class JobManagerImpl extends UnicastRemoteObject implements JobManager
{
	private static JobManager _reference = null;
	private static Registry registry = null;
	private static String name = null;
	private JobQueue jobQueue = null;
	private Collection solutions = null;
	private Set workers = null;
	private int waiters = 0, numberOfWorkers = 0;
	private long count = 0;
	private Object barrier = new Object(), waitMutex = new Object();

	private JobManagerImpl(int maxQueueSize) throws RemoteException
	{
		super();
		jobQueue = new JobQueue(maxQueueSize);
		solutions = Collections.synchronizedList(new ArrayList());
		workers = Collections.synchronizedSet(new HashSet());
	}
	
	private synchronized void incrementCount()
	{
		count++;
	}
	
	public long getJobCount()
	{
		return count;
	}
	
	/**
	 * The main thread in the master process invokes this method to tell
	 * the job manager how many workers participate in the distributed
	 * algorithm. <BR>
	 * The number is based on the number of participating nodes (machines),
	 * multiplied by the number of worker threads per node (the -worker
	 * command-line argument).
	 * <P>
	 */
	public void setNumberOfWorkers(int numberOfWorkers)
	{
		this.numberOfWorkers = numberOfWorkers;
	}
	
	/**
	 * This method is called by the main thread of the master process. <BR>
	 * After it has set up its RMI registry, job manager and spawned its
	 * local worker(s), it calls <code>setNumberOfWorkers()</code> and then
	 * blocks in this method to wait for all workers (both local and
	 * remote) to register themselves at this job manager. <BR>
	 * Only when all workers are ready, the distributed IDA* algorithm is
	 * ready to start its first iteration.
	 * <P>
	 */
	public void waitForWorkers() throws InterruptedException
	{
		synchronized(waitMutex)
		{
			while(workers.size() < numberOfWorkers)
			{
				waitMutex.wait();
			}
		}
	}

	/**
	 * Use this factory method to obtain a reference to the central job
	 * manager. <BR>
	 * Since there is only one central job manager in the entire
	 * distributed system, this method will return a remote reference when
	 * invoked in a remote process that is not the master.
	 * <P>
	 * When this method is invoked for the first time from the master
	 * process, it will create a local RMI registry and register the local
	 * job manager instance. <BR>
	 * When invoked from a remote worker process for the first time, it
	 * will do a lookup in the RMI registry on the master process and
	 * return a reference to the remote object.
	 * <P>
	 *
	 * @exception	Exception
	 * @return	a reference to the (either local or remote) job
	 * 	manager.
	 */
	public synchronized static JobManager instance() throws RemoteException
	{
		if(_reference == null)
		{
			DasInfo env = new DasInfo();

			if(env.hostNumber() == 0)
			{
				try
				{
					JobManager ref = new JobManagerImpl(10000);
					name = "//" + env.hostName() + ":" + FifteenPuzzle.registryPort + "/nl.vu.pp.distributed.JobManager";
					Naming.rebind(name, ref);
					_reference = ref;
					System.out.println("Local JobManager started and registered.");
				}
				catch(Exception e)
				{
					throw new RemoteException("Unable to start the local JobManager: " + e.getClass().getName() + ": " + e.getMessage());
				}
			}
			else
			{
				try
				{
					_reference = (JobManager)Naming.lookup("//" + env.getHost(0) + ":" + FifteenPuzzle.registryPort + "/nl.vu.pp.distributed.JobManager");
//					System.out.println("Remote JobManager contacted at " + env.getHost(0));
				}
				catch(Exception e)
				{
					throw new RemoteException("Unable to contact the remote JobManager: " + e.getClass().getName() + ": " + e.getMessage());
				}
			}
		}

		return _reference;
	}

	public void registerWorker(Worker worker)
	{
		workers.add(worker);
		synchronized(waitMutex)
		{
			waitMutex.notify();
		}
	}

	public void deregisterWorker(Worker worker)
	{
		workers.remove(worker);
		synchronized(waitMutex)
		{
			waitMutex.notify();
		}
	}
	
	public Set getWorkers()
	{
		return workers;
	}

	/**
	 * Method to put a new job in the job manager's queue. <BR>
	 * This job will at some point be returned by <code>get()</code>.
	 * <BR>
	 * In this implementation this method is only used by the manager
	 * thread that is in charge of creating new jobs and is never called
	 * by a remote process, but the implementation could be changed so that
	 * remote worker processes put partially expanded jobs back in the job
	 * manager's queue.
	 * <P>
	 * This method blocks if the queue is full until at least one job is
	 * removed by another thread.
	 * <P>
	 *
	 * @param	job the job that is to be added to the manager's queue.
	 */
	public void put(Job job) throws InterruptedException
	{
		jobQueue.put(job);
		incrementCount();
	}

	public Job get() throws InterruptedException, OutOfWorkException
	{
		return jobQueue.get();
	}

	public void addSolution(Move move)
	{
		System.out.println("Solution found after " + move.getDepth() + " moves: " + move.toString());
		solutions.add(move);
	}

	/**
	 * Not used remotely.
	 */
	public Collection getSolutions()
	{
		return solutions;
	}

	/**
	 * When a worker process is out of jobs and the manager's
	 * <code>get()</code> method throws the <code>OutOfWorkException</code>
	 * to indicate no new jobs will come available, the worker invokes this
	 * method that acts as a barrier to synchronize all threads and detect
	 * termination of the algorithm.
	 * <P>
	 * When this method returns, the worker should again start calling the
	 * <code>get()</code> method as the algorithms starts a new iteration.
	 * <P>
	 *
	 */
	public void idle() throws InterruptedException
	{
		synchronized(barrier)
		{
			waiters++;
			if(waiters >= workers.size() + 1)
			{
//				System.out.println("All " + (waiters-1) + " workers synchronized with main thread.");
				jobQueue.open();
				waiters = 0;
				barrier.notifyAll();
			}
			else
				barrier.wait();
		}
	}

	public void close()
	{
		jobQueue.close();
	}

	public void join() throws InterruptedException
	{
		System.out.println("Waiting for all " + workers.size() + " workers to finish their work...");
		close();
		idle();
	}
	
	/**
	 * Invoked by the master process' main thread before exiting the
	 * application after the puzzle has been solved. <BR>
	 * This method tells all registered worker processes to kill
	 * themselves.
	 * <P>
	 */
	public void terminate()
	{
/*
		synchronized(workers)
		{
			Iterator it = workers.iterator();

			while(it.hasNext())
			{
				Worker w = (Worker)it.next();
				try
				{
					w.kill();
				}
				catch(Exception e)
				{
					System.out.println("Error killing worker: " + e.getClass().getName() + ": " + e.getMessage());
				}
			}
		}
*/		
		try
		{
			registry.unbind(name);
		}
		catch(Exception e)
		{
			// always unexport or the JVM won't exit
		}
	}
}
