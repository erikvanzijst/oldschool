package nl.vu.pp.distributed;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface Worker extends Remote
{
	/**
	 * Invoked by the 
	 */
	public void kill() throws RemoteException;
	
	public long getNodeCount() throws RemoteException;

	public String getName() throws RemoteException;
}
