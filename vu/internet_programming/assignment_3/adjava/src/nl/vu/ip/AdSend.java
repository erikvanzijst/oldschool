package nl.vu.ip;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.ConnectException;
import java.net.MalformedURLException;

/**
 * This application is used to purchase ad space. <BR>
 * Given a certain budget, it first requests the AdServer's price list and
 * determines how many ads it can afford to buy. It then sends a payment
 * request to the bank and finally sends the ads to the AdServer process,
 * together with the bank certificate of the payment.
 * <P>
 * usage: java nl.vu.ip.AdSend --help
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.apr.2003
 */
public class AdSend
{
	// below are the default addresses that can be overridden at the
	// console
	private String adServerAddress = "localhost";
	private int adServerPort = 1099;
	private String bankAddress = "localhost";
	private int bankPort = 3211;
	private RemoteAdServer adServer = null;
	private float budget = 0;
	private int srcaccount = 123;
	private int destaccount = 555;
	private List ads = null;

	public AdSend()
	{
		ads = new ArrayList();
	}

	/**
	 * This method starts the application. <BR>
	 *
	 * @param	argv the raw list of arguments.
	 */
	public static void main(String[] argv)
	{
		AdSend adSend = new AdSend();
		adSend._main(argv);
	}

	/**
	 * This method is invoked by the main() function directly after an
	 * instance has been created. Working directly from the main() method
	 * is inconvenient, as it is static.
	 *
	 * @param	argv the raw list of arguments.
	 */
	public void _main(String[] argv)
	{
		parseArgs(argv);

		try
		{
			// get the pricing info using rmi
			System.out.print("Requesting pricing information... ");
			adServer = (RemoteAdServer)Naming.lookup("//" + adServerAddress + ":" + adServerPort + "/nl.vu.ip.AdServer");
			PriceList priceList = adServer.getPriceList();
			System.out.println("succeeded.");

			// determine the maximum number of ads we can purchase
			int numberOfAds = priceList.getNumberOfAds(budget);
			float cost = priceList.getPrice(numberOfAds);

			if(numberOfAds > 0)
				System.out.println("With our budget of " + budget + " we can purchase place " +
					"for " + numberOfAds + " ads, with a total cost of " + cost + ".");
			else
			{
				System.out.println("With our budget of " + budget + " we can not even buy space for a single ad!");
				System.exit(0);
			}

			// make the payment at the bank using sockets
			System.out.print("Making the payment at the bank; transferring " + cost + " from our account (" + srcaccount + ") to the server's account (" + destaccount + ")... ");
			int certificate = BankClient.bank_pay(bankAddress, bankPort, srcaccount, destaccount, cost);
			if(certificate == -1)
			{
				System.out.println("failed! (Is there enough credit on the source account? Are the account numbers valid?)");
				System.exit(1);
			}
			System.out.println("succeeded.");

			// prepare the collection of ads
			Collection col = new ArrayList();
			for(int nX = 0; nX < numberOfAds; nX++)
				col.add(ads.get(nX % ads.size()));

			// publish our ads
			System.out.print("Uploading the ads to the AdServer, along with the certificate of payment... ");
			try
			{
				adServer.purchase(col, certificate, srcaccount, cost);
			}
			catch(NotPayedException e)
			{
				System.out.println("failed! The server says our payment is not in order: " + e.getMessage());
				System.exit(1);
			}
			System.out.println("succeeded.");
		}
		catch(ConnectException e)
		{
			System.out.println("Connection problem, are both the bank and the ad server up and running?");
		}
		catch(IOException e)
		{
			System.out.println("Communications error: " + e.getMessage());
			e.printStackTrace();
		}
		catch( NotBoundException e )
		{
			System.out.println("The associated name has no binding in the registry: " + e.getMessage());
		}
		catch( ArithmeticException e )
		{
			System.out.println("An exceptional arithmetic condition has occurred: " + e.getMessage());
			e.printStackTrace();
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
		String usage = "usage: java nl.vu.ip.AdSend [OPTION]...\n\n" +
			"Options:\n\n" +
			"	--help (print this usage text and exit)\n" +
			"	-budget 0.0\n" +
			"	-adserveraddress localhost\n" +
			"	-adserverport 1099\n" +
			"	-bankaddress localhost\n" +
			"	-bankport 3211\n" +
			"	-srcaccount 123\n" +
			"	-destaccount 555\n" +
			"	-files file1.jpg file2.gif...\n\n" +
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
				if(argv[nX].equals("-budget"))
					budget = Float.parseFloat(argv[++nX]);
				if(argv[nX].equals("-adserveraddress"))
					adServerAddress = argv[++nX];
				if(argv[nX].equals("-adserverport"))
					adServerPort = Integer.parseInt(argv[++nX]);
				if(argv[nX].equals("-bankaddress"))
					bankAddress = argv[++nX];
				if(argv[nX].equals("-bankport"))
					bankPort = Integer.parseInt(argv[++nX]);
				if(argv[nX].equals("-srcaccount"))
					srcaccount = Integer.parseInt(argv[++nX]);
				if(argv[nX].equals("-destaccount"))
					destaccount = Integer.parseInt(argv[++nX]);
				if(argv[nX].equals("-files"))
				{
					while((nX+1) < argv.length && argv[nX+1].indexOf('-') != 0)
					{
						nX++;
						try
						{
							File file = new File(argv[nX]);
							Ad ad = new Ad(file.getName());
							ad.load(new FileInputStream(file));
							ads.add(ad);
						}
						catch(IOException e)
						{
							System.err.println("Error reading file: " + e.getMessage());
						}
					}
					System.out.println(ads.size() + " ads loaded from disk.");
				}
			}
		}
		catch(Exception e)
		{
			System.err.println("Error parsing command line arguments: " + e.getMessage());
			System.err.println(usage);
			System.exit(1);
		}

		if(ads.size() == 0)
		{
			System.err.println("No ads (image files) specified, please use the -files switch.");
			System.err.println(usage);
			System.exit(1);
		}
	}
}
