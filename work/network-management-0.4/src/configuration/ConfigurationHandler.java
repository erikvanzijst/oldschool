/*
 * File:           ConfigurationHandler.java
 * Date:           2 december 2003  20:15
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           2 december 2003  20:04
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           2 december 2003  19:51
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           2 december 2003  19:44
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           2 december 2003  19:30
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           2 december 2003  15:07
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           2 december 2003  14:49
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           26 November 2003  13:36
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           05 November 2003  22:44
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           29 October 2003  10:54
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           27 October 2003  18:37
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           27 October 2003  18:17
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           23 oktober 2003  11:49
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           14 October 2003  19:54
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           03 October 2003  20:19
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           03 October 2003  20:13
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           03 October 2003  19:59
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           03 October 2003  19:38
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           03 October 2003  19:31
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           03 October 2003  19:23
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationHandler.java
 * Date:           03 October 2003  10:54
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */

package configuration;

import org.xml.sax.*;

public interface ConfigurationHandler {
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_distance(final int data, final Attributes meta) throws SAXException;
    
    /**
     * A container element start event handling method.
     * @param meta attributes
     *
     */
    public void start_destination(final Attributes meta) throws SAXException;
    
    /**
     * A container element end event handling method.
     *
     */
    public void end_destination() throws SAXException;
    
    /**
     * A container element start event handling method.
     * @param meta attributes
     *
     */
    public void start_router(final Attributes meta) throws SAXException;
    
    /**
     * A container element end event handling method.
     *
     */
    public void end_router() throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_listenerPort(final int data, final Attributes meta) throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_addressElement(final java.lang.String data, final Attributes meta) throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_managementPort(final int data, final Attributes meta) throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_httpPassword(final java.lang.String data, final Attributes meta) throws SAXException;
    
    /**
     * A container element start event handling method.
     * @param meta attributes
     *
     */
    public void start_managementKey(final Attributes meta) throws SAXException;
    
    /**
     * A container element end event handling method.
     *
     */
    public void end_managementKey() throws SAXException;
    
    /**
     * A container element start event handling method.
     * @param meta attributes
     *
     */
    public void start_connection(final Attributes meta) throws SAXException;
    
    /**
     * A container element end event handling method.
     *
     */
    public void end_connection() throws SAXException;
    
    /**
     * A container element start event handling method.
     * @param meta attributes
     *
     */
    public void start_address(final Attributes meta) throws SAXException;
    
    /**
     * A container element end event handling method.
     *
     */
    public void end_address() throws SAXException;
    
    /**
     * A container element start event handling method.
     * @param meta attributes
     *
     */
    public void start_administration(final Attributes meta) throws SAXException;
    
    /**
     * A container element end event handling method.
     *
     */
    public void end_administration() throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_httpPort(final int data, final Attributes meta) throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_metric(final int data, final Attributes meta) throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_routerId(final int data, final Attributes meta) throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_managementDomain(final java.lang.String data, final Attributes meta) throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_host(final java.lang.String data, final Attributes meta) throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_httpUser(final java.lang.String data, final Attributes meta) throws SAXException;
    
    /**
     * A data element event handling method.
     * @param data value or null
     * @param meta attributes
     *
     */
    public void handle_managementAgent(final java.lang.String data, final Attributes meta) throws SAXException;
    
    /**
     * A container element start event handling method.
     * @param meta attributes
     *
     */
    public void start_interface(final Attributes meta) throws SAXException;
    
    /**
     * A container element end event handling method.
     *
     */
    public void end_interface() throws SAXException;
    
    /**
     * A container element start event handling method.
     * @param meta attributes
     *
     */
    public void start_source(final Attributes meta) throws SAXException;
    
    /**
     * A container element end event handling method.
     *
     */
    public void end_source() throws SAXException;
    
    /**
     * A container element start event handling method.
     * @param meta attributes
     *
     */
    public void start_network(final Attributes meta) throws SAXException;
    
    /**
     * A container element end event handling method.
     *
     */
    public void end_network() throws SAXException;
    
}
