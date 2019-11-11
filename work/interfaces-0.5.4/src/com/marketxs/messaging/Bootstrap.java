package com.marketxs.messaging;

import java.lang.reflect.Method;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <P>
 * Bootstrap class used to start an application, while appending a URL to the
 * classpath. Basic authentication for HTTP can be used. Note that only a
 * single URL is currently supported.
 * </P>
 * <P>
 * Usage: java com.marketxs.messaging.Bootstrap -url http://user:passwd@remote.host:80/classes class-with-main [program parameters]
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.5.4, 28.jan.2004
 */
public class Bootstrap
{
	private URL url = null;
	private String username = null;
	private String password = null;
	private String classname = null;
	private String[] arguments = null;
	
	public static void main(String[] args) throws Exception
	{
		(new Bootstrap())._main(args);
	}

	private void _main(String[] argv) throws Exception
	{
		parseArgs(argv);
		
		if(url.getUserInfo() != null)
		{
			String credentials = url.getUserInfo();
			username = credentials.substring(0, credentials.indexOf(':'));
			password = credentials.substring(credentials.indexOf(':') + 1, credentials.length());
			Authenticator.setDefault(new Authenticator()
				{
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(username, password.toCharArray());
					}
				}
			);
		}
		
		URLClassLoader cl = new URLClassLoader(new URL[]{url});
		Class clazz = cl.loadClass(classname);
		Method method = clazz.getDeclaredMethod("main", new Class[]{String[].class});
		method.invoke(clazz.newInstance(), new Object[]{arguments});
	}
	
	private void parseArgs(String[] argv)
	{
		String usage = "usage:\n\n" +
			"	java com.marketxs.messaging.Bootstrap [OPTION] classname [program parameters]...\n\n" +
			"Options:\n\n" +
			"	--help (print this usage text and exit)\n" +
			"	-url the url that contains the class that is to be run";

		try
		{
			for(int nX = 0; nX < argv.length; nX++)
			{
				if(argv[nX].equals("--help"))
				{
					System.out.println(usage);
					System.exit(0);
				}
				if(argv[nX].equals("-url"))
				{
					url = new URL(argv[++nX]);
				}
				else
				{
					// we must have reached the end of the cmdline switches
					classname = argv[nX++];
					arguments = new String[argv.length - nX];
					System.arraycopy(argv, nX, arguments, 0, arguments.length);
					break;
				}
			}
			
			if(classname == null)
			{
				System.err.println("Missing classname.");
				System.err.println(usage);
				System.exit(1);
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
