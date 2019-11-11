/*
 * ConfigurationUtilities.java
 *
 * Created on 25 November 2003, 18:16
 */

package configuration;

import java.util.Vector;
import java.util.Iterator;

import logging.OMMLogger;

/**
 *
 * @author  Laurence Crutcher
 */
public class ConfigurationUtilities {
    private static int commandCounter;    
    
    /** Creates a new instance of ConfigurationUtilities */
    public ConfigurationUtilities() {
    }
            // assume nodes are numbered 1 to n. This handles gaps, but doesn't handle duplicates very well
        public static int findRouterIndex(Vector routers, int nodeId) throws ConfigurationException {
            int index = -1;
            try {
                Iterator i = routers.iterator();
                while (i.hasNext() && (index == -1)) {
                    Router r = (Router)i.next();
                    if (r.name() == Integer.toString(nodeId))
                        index = routers.indexOf(r);
                }
            }
            catch (Exception e) {
                OMMLogger.logger.error("Exception in indexing router list" + e);
                throw new ConfigurationException("Indexing error:" + e);                
            }
            return index;
        }

        public static Router findRouter(Vector routers, int nodeId) throws ConfigurationException {
            int index = -1;
            Router r = null;
            try {
                Iterator i = routers.iterator();
                while (i.hasNext() && (index == -1)) {
                    r = (Router)i.next();
                    if (r.name() == Integer.toString(nodeId))
                        index = routers.indexOf(r);
                }
            }
            catch (Exception e) {
                throw new ConfigurationException("Indexing error:" + e);
            }
            if (index == -1) {
                throw new ConfigurationException("router not found in router list");
            }
            return r;
        }        
        
    // Using vectors everywhere is logically right, but a bit clunky. Change to something more efficient later.
    public static int findInterfaceIndex(Vector v, InterfaceAddress interfaceAddress) {
        int index = -1;
        try {
            Iterator i = v.iterator();
            while (i.hasNext() && (index == -1)) {            
               RInterface r = (RInterface)i.next();           
               if (r.equals(interfaceAddress)) {
                    index = v.indexOf(r);
               }
             }
        }
        catch (Exception e) {
            OMMLogger.logger.error("Exception in indexing Interface vector:" + e);
        }
        return index;
     }        
 
    public static RInterface findInterface(Vector v, InterfaceAddress interfaceAddress) throws ConfigurationException {
        int index = findInterfaceIndex(v, interfaceAddress);
        if (index == -1) {
            throw new ConfigurationException("interface not found in interface list");
        }        
        return (RInterface)v.elementAt(index);
    }
    
    public static int getCommandCount() {
        commandCounter++;
        if (commandCounter < 0)
            commandCounter=0;
        return commandCounter;
    }
}
