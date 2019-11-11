/*
 * File:           ErikconfigurationHandlerImpl.java
 * Date:           4 december 2003  12:16
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
package configuration;

import org.xml.sax.*;

public class ErikconfigurationHandlerImpl implements ErikconfigurationHandler {
    
    public static final boolean DEBUG = false;
    
    public void handle_distance(final int data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_distance: " + data);
    }
    
    public void handle_messagingDomain(final java.lang.String data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_messagingDomain: " + data);
    }
    
    public void start_destination(final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("start_destination: " + meta);
    }
    
    public void end_destination() throws SAXException {
        if (DEBUG) System.err.println("end_destination()");
    }
    
    public void start_router(final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("start_router: " + meta);
    }
    
    public void end_router() throws SAXException {
        if (DEBUG) System.err.println("end_router()");
    }
    
    public void handle_listenerPort(final int data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_listenerPort: " + data);
    }
    
    public void handle_addressElement(final java.lang.String data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_addressElement: " + data);
    }
    
    public void handle_managementPort(final int data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_managementPort: " + data);
    }
    
    public void handle_httpPassword(final java.lang.String data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_httpPassword: " + data);
    }
    
    public void start_managementKey(final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("start_managementKey: " + meta);
    }
    
    public void end_managementKey() throws SAXException {
        if (DEBUG) System.err.println("end_managementKey()");
    }
    
    public void start_connection(final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("start_connection: " + meta);
    }
    
    public void end_connection() throws SAXException {
        if (DEBUG) System.err.println("end_connection()");
    }
    
    public void start_address(final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("start_address: " + meta);
    }
    
    public void end_address() throws SAXException {
        if (DEBUG) System.err.println("end_address()");
    }
    
    public void start_administration(final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("start_administration: " + meta);
    }
    
    public void end_administration() throws SAXException {
        if (DEBUG) System.err.println("end_administration()");
    }
    
    public void handle_httpPort(final int data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_httpPort: " + data);
    }
    
    public void handle_metric(final int data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_metric: " + data);
    }
    
    public void handle_messagingAgent(final java.lang.String data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_messagingAgent: " + data);
    }
    
    public void handle_routerId(final int data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_routerId: " + data);
    }
    
    public void handle_managementDomain(final java.lang.String data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_managementDomain: " + data);
    }
    
    public void handle_host(final java.lang.String data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_host: " + data);
    }
    
    public void handle_httpUser(final java.lang.String data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_httpUser: " + data);
    }
    
    public void start_interface(final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("start_interface: " + meta);
    }
    
    public void end_interface() throws SAXException {
        if (DEBUG) System.err.println("end_interface()");
    }
    
    public void handle_managementAgent(final java.lang.String data, final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("handle_managementAgent: " + data);
    }
    
    public void start_source(final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("start_source: " + meta);
    }
    
    public void end_source() throws SAXException {
        if (DEBUG) System.err.println("end_source()");
    }
    
    public void start_network(final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("start_network: " + meta);
    }
    
    public void end_network() throws SAXException {
        if (DEBUG) System.err.println("end_network()");
    }
    
}

