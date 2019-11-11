package com.marketxs.messaging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.marketxs.log.Log;

/**
 * <P>
 * Very simple utility class that provides configuration facilities. When a
 * configuration parameter is requested, it first checks the system
 * variables to see if it contains the configuration key and if not, it looks
 * for a file named "messaging.properties" in the current directory and checks
 * whether the file contains the property.
 * </P>
 * <P>
 * A specific properties file can be specified using the JVM switch
 * <code>-Dmessaging.properties = /path/to/properties</code>
 * </P>
 * 
 * @see	java.util.Properties
 * @author Erik van Zijst - erik@marketxs.com - 21.Oct.2003
 */
public class Configurator
{
	private static Properties fileProps = new Properties();
	private static Properties envProps = System.getProperties();
	private static final String filename = "messaging.properties";
	private static Log logger = Log.instance(Configurator.class);
	
	static
	{
		String file = null;
		try
		{
			file = System.getProperty("messaging.properties", filename);
			fileProps.load(new FileInputStream(file));
			logger.log("Configuration file " + file + " parsed. " + fileProps.size() + " properties read.", Log.SEVERITY_INFO);
		}
		catch(FileNotFoundException e)
		{
			logger.log("No configuration file found (" + file + "), using system variables only for configuration.", Log.SEVERITY_WARNING);
		}
		catch(IOException e)
		{
			logger.log("Error reading configuration file " + file + " (" + e.getMessage() + "), using system variables only for configuration.", Log.SEVERITY_WARNING);
		}
	}
	
	/**
	 * <P>
	 * Searches the configuration for the specified key. If the key was not
	 * found in the system variables and neither in the configuration
	 * file, <code>null</code> is returned.
	 * </P>
	 * 
	 * @param key	the property name.
	 * @return	the value of the specified property name or null if it was not
	 * 	found.
	 */
	public static String getProperty(String key)
	{
		String value = envProps.getProperty(key);
		return (value == null ? fileProps.getProperty(key) : value);
	}
	
	/**
	 * <P>
	 * Searches the configuration for the specified key. If the key was not
	 * found in the system variables and neither in the configuration
	 * file, the default value is returned instead.
	 * </P>
	 * 
	 * @param key	the property name.
	 * @param defaultValue	the value that should be returned if the specified
	 * 	property was not found.
	 * @return	the value of the specified property name or the given default
	 * 	value if it was not found.
	 */
	public static String getProperty(String key, String defaultValue)
	{
		String value = getProperty(key);
		return (value == null ? defaultValue : value);
	}
}
