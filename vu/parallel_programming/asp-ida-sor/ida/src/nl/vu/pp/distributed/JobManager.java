package nl.vu.pp.distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.Collection;

/**
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.jan.2003
 */
public interface JobManager extends Remote
{
	public void registerWorker(Worker worker) throws RemoteException;
	public void deregisterWorker(Worker worker) throws RemoteException;
	public void setNumberOfWorkers(int numberOfWorkers) throws RemoteException;
	public void waitForWorkers() throws InterruptedException, RemoteException;
	public Set getWorkers() throws RemoteException;

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
	public void put(Job job) throws InterruptedException, RemoteException;

	/**
	 * Returns the first job (oldest) in the queue. If the queue is empty,
	 * this method will block until another thread adds a job.
	 * <P>
	 *
	 * @exception	RemoteException when there was a communication problem.
	 * @exception	OutOfWorkException when the queue is empty and no more
	 * 	new jobs will be created.
	 * @return	the oldest job in the queue.
	 */
	public Job get() throws RemoteException, InterruptedException, OutOfWorkException;

	/**
	 * Stores a solution to the puzzle. Remote workers invoke this method
	 * when an expanded job solved the game.
	 * <P>
	 *
	 * @exception	RemoteException when there was a communication problem.
	 * @param	move the Move that solved the game.
	 */
	public void addSolution(Move move) throws RemoteException;
	
	public Collection getSolutions() throws RemoteException;

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
	 * @exception	RemoteException when there was a communication problem.
	 */
	public void idle() throws InterruptedException, RemoteException;

	public void close() throws RemoteException;

	public void join() throws InterruptedException, RemoteException;

	/**
	 * Invoked by the master process' main thread before exiting the
	 * application after the puzzle has been solved. <BR>
	 * This method tells all registered worker processes to kill
	 * themselves.
	 * <P>
	 */
	public void terminate() throws RemoteException;
}
