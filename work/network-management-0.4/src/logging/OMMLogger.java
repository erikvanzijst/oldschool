/*
 * logger.java
 *
 * Created on 18 November 2003, 13:27
 */

package logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.FileAppender;
import org.apache.log4j.xml.DOMConfigurator;

import javax.swing.JTextArea;
import java.net.*;

import ClientGUI.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class OMMLogger {
    private String className = this.getClass().getName();  
    private URL loggingConfigurationURL;
    public static Logger logger;
    
    public OMMLogger() {
//        java.net.URL url = OMMLogger.class.getResource("loggingconfiguration.xml");
        
        try {
            loggingConfigurationURL = new URL(OMMEnv.loggingConfigurationURL);
        }
        catch (MalformedURLException mue) {
            System.out.println("Exception in logging configuration URL: " + mue.getMessage());
        }        
        logger = Logger.getLogger("OMMLogger");
        DOMConfigurator.configure(loggingConfigurationURL);        
        logger.info("Start of Logging");
        logger.info("Logging configuration loaded from " + loggingConfigurationURL);        
    }
    
    public void addWindowLogger(JTextArea logArea) {
        WindowAppender windowAppender = new WindowAppender(logArea, OMMEnv.log4jWindowLayoutConversionPattern, OMMEnv.log4jWindowThreshold);
        logger.addAppender(windowAppender);
    }
}

