/*
 * ManagerEnv.java
 *
 * Created on 21 November 2003, 11:04
 */

package ClientGUI;

import java.net.URL;
import java.util.*;


/**
 *
 * @author  Laurence Crutcher
 */
public class OMMEnv {
    public static String iconURL;
    public static String loggingConfigurationURL;
    public static String log4jWindowLayoutConversionPattern;
    public static String log4jWindowThreshold;
    private ResourceBundle ommResourceBundle;
    private String ommRoot;
    private String ommIconsLocation;
    private String ommLoggingLocation;
    private String ommLoggingConfigurationFileName;
    
    /** Creates a new instance of OMMEnv */
    public OMMEnv() {
        ommResourceBundle = ResourceBundle.getBundle("ClientGUI.OMMproperties");
        
        try {
            ommRoot = ommResourceBundle.getString("ommRoot");
            ommIconsLocation = ommResourceBundle.getString("ommIconsLocation");
            ommLoggingLocation = ommResourceBundle.getString("ommLoggingLocation");
            ommLoggingConfigurationFileName = ommResourceBundle.getString("ommLoggingConfigurationFileName");
            log4jWindowLayoutConversionPattern = ommResourceBundle.getString("log4jWindowLayoutConversionPattern");
            log4jWindowThreshold = ommResourceBundle.getString("log4jWindowThreshold");
        }
        catch (NullPointerException npe) {
            System.out.println("Error in configuration from properties file " + npe.getMessage());
            System.exit(0);
        }
        catch (MissingResourceException mre) {
            System.out.println("Error in configuration from properties file " + mre.getMessage());
            System.exit(0);
        }
        catch (ClassCastException cce) {
            System.out.println("Error in configuration from properties file " + cce.getMessage());
            System.exit(0);
        }
        
        iconURL = ommRoot + ommIconsLocation;
        loggingConfigurationURL = ommRoot + ommLoggingLocation + ommLoggingConfigurationFileName;        
    }
    
}
