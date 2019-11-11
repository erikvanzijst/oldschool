package nl.vu.ip;

import java.util.List;
import java.util.Hashtable;
import java.util.Collection;
import java.util.Properties;
import java.util.LinkedList;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This is the ad server. <BR>
 * The server can receive three kinds of requests: <BR>
 *
 * <LI> Price request: an advertiser asks the price for purchasing ad space.
 * 	 The server returns prices for placing one ad, for placing 5 ads and
 * 	for placing 10 ads. Note that it is possible to return prices for
 * 	different amounts of ads by editing the prices.cfg configuration file.
 * </LI>
 * <P>
 *
 * <LI> Ad spce purchase: an advertiser sends a list of ads to the server,
 * 	together with a bank certificate corresponding to the requested price.
 * 	The ad server first checks if the certificate is correct, then saves
 * 	the the ad in memory.<BR>
 * 	An ad is an image file, which can be either in GIF or JPG format.
 * </LI>
 * <P>
 *
 * <LI> Ad retrieval: the server delivers an ad to the client. The client
 * 	cannot specify which ad should be retrieved. Ads are delivered in
 * 	round-robin fashion (if the server contains 3 ads, then subsequent
 * 	requests will deliver ads 1, 2, 3, 1, 2, 3, etc).
 * </LI>
 * <P>
 * The ad server extends Java's UnicastRemoteObject and is a remote object. It
 * is registered in the rmi registry and accepts rmi invocations from remote
 * clients. Because of the nature of the UnicastRemoteObject, the server can
 * accept and service multiple concurrent client requests.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.apr.2003
 */
public class AdServer extends UnicastRemoteObject implements RemoteAdServer
{
	private PriceList prices = null;
	private String bankAddress = "localhost";
	private int bankPort = 3211;
	private int account = 123;
	private List ads = null;
	private int nextAd = 0;

	/**
	 * Constructor for the AdServer. <BR>
	 *
	 * @throws	RemoteException if the UnicastRemoteObject could not
	 * 	initialize successfully.
	 */
	public AdServer() throws RemoteException
	{
		super();	// initialize the UnicastRemoteObject
		ads = new LinkedList();
	}

	/**
	 * This method returns the prices for the purchasing ad space. The more
	 * ads purchased at once, the lower their price (usually). The prices
	 * are returned as a PriceList object that can either be used to
	 * retrieve the prices manually, or it can calculate the maximum number
	 * of ads that can be purchased, given a budget.
	 * <P>
	 *
	 * @return	the server's pricing information.
	 */
	public PriceList getPriceList()
	{
		System.out.println("Price list requested.");
		return prices;
	}

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
	public synchronized void purchase(Collection _ads, int certificate, int srcaccount, float amount) throws NotPayedException
	{	
		// verify payment first
		try
		{
			// is the amount sufficient?
			if(prices.getPrice(_ads.size()) > amount)
				throw new NotPayedException("The specified amount of money is insufficient when purchasing space for " + _ads.size() + " ads.");

			// has the transfer actually been made?
			if(BankClient.bank_check(bankAddress, bankPort, srcaccount, account, amount, certificate) != 0)
				throw new NotPayedException("The specified payment could not be verified by the bank, discarding ads.");
		}
		catch(IOException e)
		{
			System.out.println("Unable to verify the specified payment due to communication problems: " + e.getMessage() + " (Is the bank up and running?)");
			throw new NotPayedException("Unable to verify the specified payment due to communication problems with the bank: " + e.getMessage());
		}

		// payment cleared, accept the new ads
		this.ads.addAll(_ads);
		System.out.println(_ads.size() + " new ads received.");
	}

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
	 * @return	an ad, note that null is returned if the server is
	 * 	empty.
	 */
	public synchronized Ad getAd()
	{
		if(ads.size() == 0)
			return null;

		// return the next ad in a round-robin fashion
		System.out.println("Serving an advertisement...");
		nextAd = nextAd % ads.size();
		return (Ad)ads.get(nextAd++);
	}

	/**
	 * Starts the application. <BR>
	 * <P>
	 * Usage: java nl.vu.ip.AdServer --help
	 * <P>
	 *
	 * @param	argv the raw list of arguments.
	 */
	public static void main(String[] argv)
	{
		try
		{
			AdServer server = new AdServer();
			server._main(argv);
		}
		catch(RemoteException e)
		{
			System.err.println("Unable to initialize the AdServer's UnicastRemoteObject: " + e.getMessage());
			System.exit(1);
		}

	}

	/**
	 * This method is invoked by the main() function directly after an
	 * instance has been created. Working directly from the main() method
	 * is inconvenient, as it is static.
	 *
	 * @param	argv the raw list of arguments.
	 * @throws	RemoteException when the rmi registry could not be
	 * 	created, nor be shared with an existing application.
	 */
	public void _main(String[] argv) throws RemoteException
	{
		parseArgs(argv);

		try
		{
			// first try to create a new registry on this host
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		}
		catch(RemoteException e)
		{
			// if it fails, try to share one
			LocateRegistry.getRegistry();
		}

		try
		{
			Naming.rebind("//localhost/nl.vu.ip.AdServer", this);
			System.out.println("Java AdServer started successfully, our account number is " + account + ".");
		}
		catch(RemoteException e)
		{
			System.out.println("The rmi registry could not be contacted: " + e.getMessage());
			System.exit(1);
		}
		catch(MalformedURLException e)
		{
			// should never happen, because the url is hardcoded
		}
	}
	
	/**
	 * Invoked at program startup and parses the command line arguments.
	 * <BR> A complete list of all available arguments can be obtained by
	 * specifying the argument "--help" at program start. The program will
	 * then print all known configuration switches and terminate
	 * immediately after.
	 * <P>
	 *
	 * @param	argv the raw list of arguments.
	 */
	private void parseArgs(String[] argv)
	{
		String usage = "usage: java nl.vu.ip.AdServer [OPTION]...\n\n" +
			"Options:\n\n" +
			"	--help (print this usage text and exit)\n" +
			"	-prices prices.cfg\n" +
			"	-bankaddress localhost\n" +
			"	-bankport 3211\n" +
			"	-account 123\n\n" +
			"Where the above values are the defaults when omitted.";

		try
		{
			for(int nX = 0; nX < argv.length; nX++)
			{
				if(argv[nX].equals("--help"))
				{
					System.out.println(usage);
					System.exit(0);
				}
				if(argv[nX].equals("-bankaddress"))
					bankAddress = argv[++nX];
				if(argv[nX].equals("-bankport"))
					bankPort = Integer.parseInt(argv[++nX]);
				if(argv[nX].equals("-account"))
					account = Integer.parseInt(argv[++nX]);
				if(argv[nX].equals("-prices"))
					prices = new PriceList(new FileReader(argv[++nX]));
			}
			
			// if no price file was specified, try to load the default
			if(prices == null)
			{
				try
				{
					prices = new PriceList(new FileReader("prices.cfg"));
				}
				catch(IOException e)
				{
					System.err.println("Unable to read the default price file: " + e.getMessage() + " (Please specify one explicitly using the -prices switch.)");
					System.exit(1);
				}
			}

		}
		catch(Exception e)
		{
			System.err.println("Error parsing command line arguments: " + e.getMessage());
			System.err.println(usage);
			System.exit(1);
		}
	}
}
