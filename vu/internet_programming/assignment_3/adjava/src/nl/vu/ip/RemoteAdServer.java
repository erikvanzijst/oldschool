package nl.vu.ip;

import java.util.HashMap;
import java.util.Collection;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the remote interface of the AdServer. It is implemented by the
 * client-side stub as well as the AdServer itself. Its methods are exposed to
 * remote clients.
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.apr.2003
 */
public interface RemoteAdServer extends Remote
{
	/**
	 * This method returns the prices for the purchasing ad space. The more
	 * ads purchased at once, the lower their price (usually). The prices
	 * are returned as a PriceList object that can either be used to
	 * retrieve the prices manually, or it can calculate the maximum number
	 * of ads that can be purchased, given a budget.
	 * <P>
	 *
	 * @throws	RemoteException if a communication problem occurred.
	 * @return	the server's pricing information.
	 */
	public PriceList getPriceList() throws RemoteException;
	
	/**
	 * This method is used by remote clients to purchase ad space. <BR>
	 * Money must have been transferred to the server's account at the bank
	 * before this method is invoked. Along with the advertisements, a
	 * client needs to supply the payment's certificate, as well as the
	 * transferred amount etc, so the server can verify the transaction
	 * before accepting the ads.
	 * <P>
	 *
	 * @param	_ads the client's advertisements.
	 * @param	certificate the certificate returned by the bank when
	 * 	it made the money transfer to the server's account.
	 * @param	srcaccount the client's account number from where the
	 * 	money was withdrawn.
	 * @param	amount the amount of money that was transferred to the
	 * 	server's account.
	 * @throws	NotPayedException if the certificate was invalid to the
	 * 	bank, or when the amount was insufficient for the number of ads.
	 */
	public void purchase(Collection ads, int certificate, int srcaccount, float amount) throws NotPayedException, RemoteException;
	
	/**
	 * This method is used by remote clients to obtain an advertisement.
	 * <BR>
	 * The server returns advertisements in a round-robin fashion. The
	 * getAd() method is synchronized with the purchase() method, because
	 * the latter changes the data structure that holds the ads. A second
	 * client can not simultaneously retrieve an ad (and thus access the
	 * data structure), while a new ad is being added. <BR>
	 * Synchronization is required because Java's UnicastRemoteObject uses
	 * a thread pool to serve concurrent client requests. <BR>
	 * Note that because the internal price list data structure is never
	 * altered, the getPriceList() method is not synchronized.
	 * <P>
	 *
	 * @throws	RemoteException if a communication problem occurred.
	 * @return	an ad, note that null is returned if the server is
	 * 	empty.
	 */
	public Ad getAd() throws RemoteException;
}
