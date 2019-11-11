package nl.vu.ip;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

/**
 * Application that is used to retrieve a single ad from the AdServer. <BR>
 * When an ad is received, it is shown by starting the "xv" program under UNIX.
 * <P>
 * usage: java nl.vu.ip.AdGet -adserveraddress localhost -adserverport 1099 -viewer xv -d /tmp
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.apr.2003
 */
public class AdGet
{
	// below is the default address information that can be overridden at
	// the console
	private String adServerAddress = "localhost";
	private int adServerPort = 1099;
	private String viewer = "xv";
	private String path = "/tmp";

	/**
	 * This method starts the application. <BR>
	 *
	 * @param	argv the raw list of arguments.
	 */
	public static void main(String[] argv)
	{
		AdGet adGet = new AdGet();
		adGet._main(argv);
	}

	/**
	 * This method is invoked by the main() function directly after an
	 * instance has been created. Working directly from the main() method
	 * is inconvenient, as it is static.
	 *
	 * @param	argv the raw list of arguments.
	 */
	private void _main(String[] argv)
	{
		parseArgs(argv);
		
		try
		{
			// retrieve an ad from the AdServer using rmi
			RemoteAdServer adServer = (RemoteAdServer)Naming.lookup("//" + adServerAddress + ":" + adServerPort + "/nl.vu.ip.AdServer");
			Ad ad = adServer.getAd();
			if(ad == null)
			{
				System.out.println("The ad server currently has no advertisements.");
				System.exit(0);
			}
			System.out.println("Ad received.");

			try
			{
				// save the ad locally, using its original filename
				File file = new File(path + "/" + ad.getFilename());
				FileOutputStream fos = new FileOutputStream(file);
				ad.store(fos);
				System.out.println("Ad written to local file " + file.getAbsolutePath());

				try
				{
					// launch the external image viewer to display the ad
					Process p = Runtime.getRuntime().exec(new String[] {viewer, file.getAbsolutePath()});
					System.out.println("Launching the viewer...");
					p.waitFor();
					file.delete();
					System.out.println("Local ad file " + file.getAbsolutePath() + " removed.");
				}
				catch(Exception __e)
				{
					System.err.println("Unable to launch viewer command: " + __e.getMessage());
				}
			}
			catch(IOException _e)
			{
				System.err.println("Unable to write the ad to a local file: " + _e.getMessage());
			}
		}
		catch(RemoteException e)
		{
			System.err.println("Unable to get pricing information: " + e.getMessage());
		}
		catch(NotBoundException e)
		{
			System.out.println("The associated name has no binding in the registry: " + e.getMessage());
		}
		catch(MalformedURLException e)
		{
			System.out.println("Invalid binding name for the AdServer: " + e.getMessage());
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
		String usage = "usage: java nl.vu.ip.AdGet [OPTION]...\n\n" +
			"Options:\n\n" +
			"	--help (print this usage text and exit)\n" +
			"	-adserveraddress localhost\n" +
			"	-adserverport 1099\n" +
			"	-viewer xv\n" +
			"	-d /tmp\n\n" +
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
				if(argv[nX].equals("-adserveraddress"))
					adServerAddress = argv[++nX];
				if(argv[nX].equals("-adserverport"))
					adServerPort = Integer.parseInt(argv[++nX]);
				if(argv[nX].equals("-viewer"))
					viewer = argv[++nX];
				if(argv[nX].equals("-d"))
					path = argv[++nX];
			}
		}
		catch(Exception e)
		{
			System.err.println("Error parsing commandline arguments: " + e.getMessage());
			System.err.println(usage);
			System.exit(1);
		}
	}
}
