
package configuration;

/**
 *
 * @author  Laurence Crutcher
 */


import java.util.*;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import configuration.*;
import logging.OMMLogger;
        
// public class NetworkConfiguration extends Network implements ConfigurationHandler { // ConfigurationParslet {
public class NetworkConfiguration extends Network implements ErikconfigurationHandler {
    int parsingRouterId = -1;
    Router parsingRouter;
    RInterface parsingInterface;
    InterfaceAddress parsingAddress;
    Connection parsingConnection;
    
    public NetworkConfiguration() {
    }
    
    public void loadFromFile(String configFile) {
//        ConfigurationParsletImpl parslet = new ConfigurationParsletImpl();
//        ConfigurationParser parser = new ConfigurationParser(this, null, parslet);
        ErikconfigurationParsletImpl parslet = new ErikconfigurationParsletImpl();
        ErikconfigurationParser parser = new ErikconfigurationParser(this, null, parslet);        
        OMMLogger.logger.info("about to parse " + configFile);
        try {
            parser.parse(new InputSource("file:" + configFile));
        }
        catch (SAXException se) {
            OMMLogger.logger.error("SAX Exception from parser" + se);
        }
        catch (IOException ioe) {
            OMMLogger.logger.error("IO Exception from parser" + ioe);
        }
        catch (ParserConfigurationException pce) {
            OMMLogger.logger.error("Parser Configuration Exception" + pce);
        }
        OMMLogger.logger.info("parsing complete");
    }
            
      public void saveConfiguration(String file) {
          File outputFile;
          FileOutputStream outputStream;
          PrintStream out;
          try {
            outputFile = new File(file);
            outputStream = new FileOutputStream(outputFile);
            out = new PrintStream(outputStream);
            toFile(out);
            out.close();
          }
          catch (IOException ioe) {
              OMMLogger.logger.error("IO Exception on output file open " + file + ioe.getMessage());
          }
          
      }
      
    public void end_administration() throws SAXException {
        OMMLogger.logger.info("End of Administration Parameters");
    }
            
    public void end_interface() throws SAXException {
        try {
            parsingInterface.setAddress(parsingAddress);
            parsingRouter.addInterface(parsingInterface);
        } catch (IllegalArgumentException ex) {
             throw new SAXException("end_interface", ex);
         }
      }
            
    public void end_network() throws SAXException {
        OMMLogger.logger.info("End of network. There are " + getNumberRouters() + " routers and " + getNumberConnections() + " connections.");
        Vector v = getConnectionSet();
        print();
    }
                     
      public void handle_host(java.lang.String data, Attributes meta) throws SAXException {
         try {
            parsingRouter.setHost(data.trim());
            OMMLogger.logger.info("host:" + data.trim());
         } catch (IllegalArgumentException ex) {
             throw new SAXException("Host(" + data.trim() + ")", ex);
         }
      }

      
      public void start_administration(Attributes meta) throws SAXException {
            try {
                OMMLogger.logger.info("Administration Parameters");
            } catch (IllegalArgumentException ex) {
                throw new SAXException(ex);
            }
      }
            
      public void start_interface(Attributes meta) throws SAXException {
             try {
                 OMMLogger.logger.info("New Interface for router:" + parsingRouterId);
                 parsingInterface = new RInterface(parsingRouterId);
             } catch (IllegalArgumentException ex) {
                  throw new SAXException(ex);
              } 
      }
            
      public void start_network(Attributes meta) throws SAXException {
           try {
                OMMLogger.logger.info("New Network");
           } catch (IllegalArgumentException ex) {
                throw new SAXException(ex);
           }  
      }   
      
    public void end_address() throws SAXException {
           try {
               OMMLogger.logger.info("End of Address");
           } catch (IllegalArgumentException ex) {
               throw new SAXException(ex);
           }        
    }
    
    public void end_connection() throws SAXException {
        try {
            OMMLogger.logger.info("End of Connection");
            addConnection(parsingConnection);
        } catch (IllegalArgumentException ex) {
            throw new SAXException(ex);
        }         
    }
    
    public void end_destination() throws SAXException {
        try {
            OMMLogger.logger.info("End of Destination");
            parsingConnection.setDestRouter(parsingRouterId);
            parsingConnection.setDestAddress(parsingAddress);
        } catch (IllegalArgumentException ex) {
            throw new SAXException(ex);
        }        
    }
    
    public void end_managementKey() throws SAXException {
        try {
            OMMLogger.logger.info("End of management key");   
        } catch (IllegalArgumentException ex) {
            throw new SAXException(ex);
        }
    }
    
    public void end_router() throws SAXException {
        try {
            OMMLogger.logger.info("End of Router:" + parsingRouterId);     
            parsingRouter.setId(parsingRouterId);
            addRouter(parsingRouter);
        } catch (IllegalArgumentException ex) {
            throw new SAXException(ex);
        }        
    }
    
    public void end_source() throws SAXException {
        try {
            OMMLogger.logger.info("End of Source");
            parsingConnection.setSrcRouter(parsingRouterId);
            parsingConnection.setSrcAddress(parsingAddress);
        } catch (IllegalArgumentException ex) {
            throw new SAXException(ex);
        }          
    }
    
    public void handle_addressElement(java.lang.String data, Attributes meta) throws SAXException {
          try {
              OMMLogger.logger.info("New address element:" + data.trim());
              parsingAddress.addElement(data.trim());
          }
          catch (IllegalArgumentException ex) {
              throw new SAXException(ex);
          }        
    }
    
    public void handle_distance(int data, Attributes meta) throws SAXException {
          try {
              parsingConnection.setDistance(data);
              OMMLogger.logger.info("distance:" + data);
          } catch (IllegalArgumentException ex) {
              throw new SAXException("distance(" + data + ")", ex);
          }        
    }
    
    public void handle_httpPassword(java.lang.String data, Attributes meta) throws SAXException {
        try {
            parsingRouter.setHTTPPassword(data.trim());
            OMMLogger.logger.info("HTTP Password: " + data.trim());
         } catch (IllegalArgumentException ex) {
             throw new SAXException("httpPassword(" + data.trim() + ")", ex);
         }        
    }
    
    public void handle_httpPort(int data, Attributes meta) throws SAXException {
         try {
            parsingRouter.setHTTPPort(data);
            OMMLogger.logger.info("HTTP Port: " + data);
         } catch (IllegalArgumentException ex) {
            throw new SAXException("httpPort(" + data + ")", ex);
         }        
    }
    
    public void handle_httpUser(java.lang.String data, Attributes meta) throws SAXException {
            try {
                parsingRouter.setHTTPUser(data.trim());
                OMMLogger.logger.info("HTTP User: " + data.trim());
            } catch (IllegalArgumentException ex) {
                 throw new SAXException("httpUser(" + data.trim() + ")", ex);
            }        
    }
    
    public void handle_listenerPort(int data, Attributes meta) throws SAXException {
          try {
            parsingRouter.setListenerPort(data);
            OMMLogger.logger.info("listener port: " + data);           
         } catch (IllegalArgumentException ex) {
            throw new SAXException("Listener Port(" + data + ")", ex);
         }         
    }
    
    public void handle_managementAgent(java.lang.String data, Attributes meta) throws SAXException {
          try {
            parsingRouter.setManagementAgent(data.trim());
            OMMLogger.logger.info("management agent:" + data);           
         } catch (IllegalArgumentException ex) {
             throw new SAXException("Management Agent(" + data + ")", ex);
         }        
    }
    
    public void handle_managementDomain(java.lang.String data, Attributes meta) throws SAXException {
          try {
            parsingRouter.setManagementDomain(data.trim());
            OMMLogger.logger.info("management domain:" + data);           
         } catch (IllegalArgumentException ex) {
             throw new SAXException("Management Domain(" + data + ")", ex);
         }         
    }
    
    public void handle_managementPort(int data, Attributes meta) throws SAXException {
         try {
            parsingRouter.setManagementPort(data);
            OMMLogger.logger.info("management port:" + data);
         } catch (IllegalArgumentException ex) {
             throw new SAXException("Management Port(" + data + ")", ex);
         }        
    }
    
    public void handle_metric(int data, Attributes meta) throws SAXException {
        try {
            OMMLogger.logger.info("metric:" + data);
            parsingInterface.setMetric(data);
        } catch (IllegalArgumentException ex) {
            throw new SAXException(ex);
        }        
    }
    
    public void handle_routerId(int data, Attributes meta) throws SAXException {
           try {
               OMMLogger.logger.info("router ID:" + data);
               parsingRouterId = data;
           } catch (IllegalArgumentException ex) {
               throw new SAXException("routerId(" + data + ")", ex);
           }        
    }
    
    public void start_address(Attributes meta) throws SAXException {
           try {
                 OMMLogger.logger.info("Start of Address");
                 parsingAddress = new InterfaceAddress();
           } catch (IllegalArgumentException ex) {
                  throw new SAXException(ex);
           }        
    }
    
    public void start_connection(Attributes meta) throws SAXException {
        try {
            OMMLogger.logger.info("Start of Connection");
            parsingConnection = new Connection();
        } catch (IllegalArgumentException ex) {
            throw new SAXException(ex);
        }         
    }
    
    public void start_destination(Attributes meta) throws SAXException {
           try {
                 OMMLogger.logger.info("Start of Destination");
           } catch (IllegalArgumentException ex) {
                  throw new SAXException(ex);
           }           
    }
    
    public void start_managementKey(Attributes meta) throws SAXException {
           try {
               OMMLogger.logger.info("Start of management key");
           } catch (IllegalArgumentException ex) {
               throw new SAXException(ex);
           }          
    }
    
    public void start_router(Attributes meta) throws SAXException {
           try {
               OMMLogger.logger.info("New Router");
               parsingRouter = new Router();
           } catch (IllegalArgumentException ex) {
                throw new SAXException(ex);
           }        
    }
    
    public void start_source(Attributes meta) throws SAXException {
           try {
                 OMMLogger.logger.info("Start of Source");
           } catch (IllegalArgumentException ex) {
                  throw new SAXException(ex);
           }           
    }

    public void handle_messagingAgent(java.lang.String data, Attributes meta) throws SAXException {
         try {
            parsingRouter.setMessagingAgent(data.trim());
            OMMLogger.logger.info("messaging agent:" + data);
         } catch (IllegalArgumentException ex) {
             throw new SAXException("Messaging agent(" + data + ")", ex);
         }          
    }
    
    public void handle_messagingDomain(java.lang.String data, Attributes meta) throws SAXException {
         try {
            parsingRouter.setMessagingDomain(data.trim());
            OMMLogger.logger.info("messaging domain:" + data);
         } catch (IllegalArgumentException ex) {
             throw new SAXException("Messaging domain(" + data + ")", ex);
         }         
    }
    
}

        
