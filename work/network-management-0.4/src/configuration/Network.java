package configuration;

import java.util.*;
import java.io.*;

import logging.OMMLogger;

/**
 *
 * @author  Laurence Crutcher
 */

public class Network {   
        private Vector routerSet;
        private Vector connectionSet;
        
        public Network() {
            routerSet = new Vector();
            connectionSet = new Vector();
        }
        
        public Router getRouter(int routerId) {
            try {
                return (Router)(routerSet.elementAt(ConfigurationUtilities.findRouterIndex(routerSet, routerId)));
            }
            catch (ConfigurationException ce) {
                return null;
            }
        }
        
        public void addRouter(Router r) {
            routerSet.addElement(r);
        }
        
    
        public void addConnection(Connection connection) {
            connectionSet.addElement(connection);
        }        
        
//        public void addInterface(int nodeId, RInterface i) {
//            try {
//                ((Router)routers.elementAt(ConfigurationUtilities.findRouterIndex(routers, nodeId))).addLink(i);
//            }
//            catch (ConfigurationException ce) {
//            }
//        }
        
//        public void updateDestinationNode(int nodeId, int srcLink, int dstNode) {
//            ((Router)routers.elementAt(findRouterIndex(nodeId))).updateDestinationNode(srcLink, dstNode);
//        }
  
//        public void updateLinkMetric(int nodeId, int srcLink, int metric) {
//            ((Router)routers.elementAt(findRouterIndex(nodeId))).updateLinkMetric(srcLink, metric);
//        }
//        
//        public void setListenerPort(int nodeId, int port) {
//            try {
//                ((Router)routers.elementAt(ConfigurationUtilities.findRouterIndex(routers, nodeId))).setListenerPort(port);
//            }
//            catch (ConfigurationException ce) {
//            }
//        }
//      
//        public void setHTTPPassword(int nodeId, String s) {
//            try {
//                ((Router)routers.elementAt(ConfigurationUtilities.findRouterIndex(routers, nodeId))).setHTTPPassword(s);
//            }
//            catch (ConfigurationException ce) {
//            }
//        }
//        
//        public void setHTTPPort(int nodeId, int port) {
//            try {
//                ((Router)routers.elementAt(ConfigurationUtilities.findRouterIndex(routers, nodeId))).setHTTPPort(port);
//            }
//            catch (ConfigurationException ce) {
//            }            
//        }
//        
//        public void setHTTPUser(int nodeId, String s) {
//            try {
//                ((Router)routers.elementAt(ConfigurationUtilities.findRouterIndex(routers, nodeId))).setHTTPUser(s);
//            }
//            catch (ConfigurationException ce) {
//            }
//        }
//        
//        public void setHost(int nodeId, String s) {
//            try {
//                ((Router)routers.elementAt(ConfigurationUtilities.findRouterIndex(routers, nodeId))).setHost(s);
//            }
//            catch (ConfigurationException ce) {
//            }
//        }
//        
//        public void setManagementPort(int nodeId, int port) {
//            try {
//                ((Router)routers.elementAt(ConfigurationUtilities.findRouterIndex(routers, nodeId))).setManagementPort(port);
//            }
//            catch (ConfigurationException ce) {
//            }
//        }
//          
//        public void setManagementDomain(int nodeId, String s) {
//            try {
//                ((Router)routers.elementAt(ConfigurationUtilities.findRouterIndex(routers, nodeId))).setManagementDomain(s);
//            }
//            catch (ConfigurationException ce) {
//            }
//        }
//        
//        public void setManagementAgent(int nodeId, String s) {
//            try {
//            ((Router)routers.elementAt(ConfigurationUtilities.findRouterIndex(routers, nodeId))).setManagementAgent(s); 
//            }
//            catch (ConfigurationException ce) {
//            }
//        }
//        
//        public void setManagementKey(int nodeId) {
//            try {
//                ((Router)routers.elementAt(ConfigurationUtilities.findRouterIndex(routers, nodeId))).setManagementKey(); 
//            }
//            catch (ConfigurationException ce) {
//            }
//        }        
        
        public void print() {
            Iterator i = routerSet.iterator();
            while (i.hasNext())
                OMMLogger.logger.info(((Router)(i.next())).print());
            Iterator j = connectionSet.iterator();
            while (j.hasNext())
                OMMLogger.logger.info(((Connection)(j.next())).toString());
        }
        
        public void toFile(PrintStream out) {
            Iterator i = routerSet.iterator();
            while (i.hasNext())
                out.println(((Router)(i.next())).print());
        }           
        
        public int getNumberRouters() {
            return routerSet.size();
        }   
        
        public int getNumberConnections() {
            return connectionSet.size();
        }
        
        public Vector getRouters() {
            return routerSet;
        }        
        
        public Vector getConnectionSet() {
            return connectionSet;
        }
}
