package nl.vu.pp.distributed;

/**
 * This is a very ugly hack to get all processes to terminate. <BR>
 * A normal Java virtual machine exits when the last non-daemon thread has
 * returned. With RMI (&gt;= 1.2) however, one must first unexport any remote
 * objects (such as UnicastRemoteObjects), since they have a non-daemon RMI
 * reaper thread that will make sure the JVM keeps running and the object
 * available even after the main thread has exited. <BR>
 * With Java 1.1 however (Manta's implementation regarding RMI), there it is
 * not necessary to unexport remote objects as they have no non-daemon threads.
 * If fact, it is not even possible. As a result, our program cannot use
 * the explicit unexport method, meaning we cannot terminate the JVM the normal
 * way (return from last non-daemon thread). Instead, we'll have to use
 * <code>System.exit()</code>, which is available on (&gt;= 1.0), but once
 * again there appears to be an issue regarding Manta. <BR>
 * Our first implementation using <code>System.exit()</code> from an exported
 * method, called by the master process remotely, caused the worker JVM to exit
 * during the invocation. Normally this causes a RemoteException to be thrown
 * in the RMI runtime at the caller (master process) because the socket was
 * closed. With Manta however, it seems this RemoteException is never thrown,
 * causing the master process to deadlock inside the remote invocation, waiting
 * for it to return.
 * <P>
 * To be able to run automated, scheduled test runs with our program, it was
 * absolutely imperative that all processes (master and slaves) "gracefully"
 * terminated at the end of a computation. Eventually, the only successfull way
 * to accomplish this, turned out to be this construction. Every JVM (master
 * and slaves) has a single instance of this sleeping non-daemon thread. When
 * it is notified, it waits for 5 seconds and then calls
 * <code>System.exit()</code>. These 5 seconds allow all RMI invocations to
 * finish. It's a terrible hack and we're not proud of it.
 * <P>
 * In addition to being ugly, this construction also causes this message
 * everytime a process exits:
 * <P>
 * <PRE>
 *   EEEK internal exited ?!
 * </PRE>
 * <P>
 * Apparently, one is not supposed to use <code>System.exit()</code> in Manta
 * compiled Java programs.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 28.dec.2002
 */
public class WaiterThread extends Thread
{
	public WaiterThread()
	{
	}
	
	public void run()
	{
		try
		{
			synchronized(this)
			{
				wait();
			}
			sleep(5000);
			Runtime.getRuntime().exit(0);
		}
		catch(Exception e)
		{
			System.out.println("WaiterThread died.");
		}
	}
}
