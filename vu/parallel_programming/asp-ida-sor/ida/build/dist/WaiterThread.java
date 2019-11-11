package nl.vu.pp.distributed;

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
