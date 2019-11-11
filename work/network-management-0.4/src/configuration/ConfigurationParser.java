/*
 * File:           ConfigurationParser.java
 * Date:           2 december 2003  20:15
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           2 december 2003  20:04
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           2 december 2003  19:51
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           2 december 2003  19:44
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           2 december 2003  19:30
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           2 december 2003  15:07
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           2 december 2003  14:49
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           26 November 2003  13:36
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           05 November 2003  22:44
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           29 October 2003  10:54
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           27 October 2003  18:37
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           27 October 2003  18:17
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           23 oktober 2003  11:49
 *
 * @author  laurence.crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           14 October 2003  19:54
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           03 October 2003  20:19
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           03 October 2003  20:13
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           03 October 2003  19:59
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           03 October 2003  19:38
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           03 October 2003  19:31
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           03 October 2003  19:23
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
/*
 * File:           ConfigurationParser.java
 * Date:           03 October 2003  10:54
 *
 * @author  Laurence Crutcher
 * @version generated by NetBeans XML module
 */
package configuration;

import org.xml.sax.*;

/**
 * The class reads XML documents according to specified DTD and
 * translates all related events into ConfigurationHandler events.
 * <p>Usage sample:
 * <pre>
 *    ConfigurationParser parser = new ConfigurationParser(...);
 *    parser.parse(new InputSource("..."));
 * </pre>
 * <p><b>Warning:</b> the class is machine generated. DO NOT MODIFY</p>
 *
 */
public class ConfigurationParser implements ContentHandler {
    
    private java.lang.StringBuffer buffer;
    
    private ConfigurationParslet parslet;
    
    private ConfigurationHandler handler;
    
    private java.util.Stack context;
    
    private EntityResolver resolver;
    
    /**
     * Creates a parser instance.
     * @param handler handler interface implementation (never <code>null</code>
     * @param resolver SAX entity resolver implementation or <code>null</code>.
     * It is recommended that it could be able to resolve at least the DTD.@param parslet convertors implementation (never <code>null</code>
     *
     */
    public ConfigurationParser(final ConfigurationHandler handler, final EntityResolver resolver, final ConfigurationParslet parslet) {
        this.parslet = parslet;
        this.handler = handler;
        this.resolver = resolver;
        buffer = new StringBuffer(111);
        context = new java.util.Stack();
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void setDocumentLocator(Locator locator) {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void startDocument() throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void endDocument() throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void startElement(java.lang.String ns, java.lang.String name, java.lang.String qname, Attributes attrs) throws SAXException {
        dispatch(true);
        context.push(new Object[] {qname, new org.xml.sax.helpers.AttributesImpl(attrs)});
        if ("destination".equals(qname)) {
            handler.start_destination(attrs);
        } else if ("router".equals(qname)) {
            handler.start_router(attrs);
        } else if ("managementKey".equals(qname)) {
            handler.start_managementKey(attrs);
        } else if ("connection".equals(qname)) {
            handler.start_connection(attrs);
        } else if ("address".equals(qname)) {
            handler.start_address(attrs);
        } else if ("administration".equals(qname)) {
            handler.start_administration(attrs);
        } else if ("interface".equals(qname)) {
            handler.start_interface(attrs);
        } else if ("source".equals(qname)) {
            handler.start_source(attrs);
        } else if ("network".equals(qname)) {
            handler.start_network(attrs);
        }
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void endElement(java.lang.String ns, java.lang.String name, java.lang.String qname) throws SAXException {
        dispatch(false);
        context.pop();
        if ("destination".equals(qname)) {
            handler.end_destination();
        } else if ("router".equals(qname)) {
            handler.end_router();
        } else if ("managementKey".equals(qname)) {
            handler.end_managementKey();
        } else if ("connection".equals(qname)) {
            handler.end_connection();
        } else if ("address".equals(qname)) {
            handler.end_address();
        } else if ("administration".equals(qname)) {
            handler.end_administration();
        } else if ("interface".equals(qname)) {
            handler.end_interface();
        } else if ("source".equals(qname)) {
            handler.end_source();
        } else if ("network".equals(qname)) {
            handler.end_network();
        }
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void characters(char[] chars, int start, int len) throws SAXException {
        buffer.append(chars, start, len);
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void ignorableWhitespace(char[] chars, int start, int len) throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void processingInstruction(java.lang.String target, java.lang.String data) throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void startPrefixMapping(final java.lang.String prefix, final java.lang.String uri) throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void endPrefixMapping(final java.lang.String prefix) throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void skippedEntity(java.lang.String name) throws SAXException {
    }
    
    private void dispatch(final boolean fireOnlyIfMixed) throws SAXException {
        if (fireOnlyIfMixed && buffer.length() == 0) return; //skip it
        
        Object[] ctx = (Object[]) context.peek();
        String here = (String) ctx[0];
        Attributes attrs = (Attributes) ctx[1];
        if ("distance".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_distance(parslet.distanceConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("listenerPort".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_listenerPort(parslet.listenerPortConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("addressElement".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_addressElement(parslet.addressElementConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("managementPort".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_managementPort(parslet.managementPortConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("httpPassword".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_httpPassword(parslet.httpPasswordConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("httpPort".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_httpPort(parslet.httpPortConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("metric".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_metric(parslet.metricConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("routerId".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_routerId(parslet.routerIdConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("managementDomain".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_managementDomain(parslet.managementDomainConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("host".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_host(parslet.hostConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("httpUser".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_httpUser(parslet.httpUserConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else if ("managementAgent".equals(here)) {
            if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            handler.handle_managementAgent(parslet.managementAgentConvertor(buffer.length() == 0 ? null : buffer.toString()), attrs);
        } else {
            //do not care
        }
        buffer.delete(0, buffer.length());
    }
    
    /**
     * The recognizer entry method taking an InputSource.
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     *
     */
    public void parse(final InputSource input) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, this);
    }
    
    /**
     * The recognizer entry method taking a URL.
     * @param url URL source to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     *
     */
    public void parse(final java.net.URL url) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), this);
    }
    
    /**
     * The recognizer entry method taking an Inputsource.
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     *
     */
    public static void parse(final InputSource input, final ConfigurationHandler handler, final ConfigurationParslet parslet) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, new ConfigurationParser(handler, null, parslet));
    }
    
    /**
     * The recognizer entry method taking a URL.
     * @param url URL source to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     *
     */
    public static void parse(final java.net.URL url, final ConfigurationHandler handler, final ConfigurationParslet parslet) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), handler, parslet);
    }
    
    private static void parse(final InputSource input, final ConfigurationParser recognizer) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
        factory.setValidating(true);  //the code was generated according DTD
        factory.setNamespaceAware(false);  //the code was generated according DTD
        XMLReader parser = factory.newSAXParser().getXMLReader();
        parser.setContentHandler(recognizer);
        parser.setErrorHandler(recognizer.getDefaultErrorHandler());
        if (recognizer.resolver != null) parser.setEntityResolver(recognizer.resolver);
        parser.parse(input);
    }
    
    /**
     * Creates default error handler used by this parser.
     * @return org.xml.sax.ErrorHandler implementation
     *
     */
    protected ErrorHandler getDefaultErrorHandler() {
        return new ErrorHandler() {
            public void error(SAXParseException ex) throws SAXException  {
                if (context.isEmpty()) System.err.println("Missing DOCTYPE.");
                throw ex;
            }
            
            public void fatalError(SAXParseException ex) throws SAXException {
                throw ex;
            }
            
            public void warning(SAXParseException ex) throws SAXException {
                // ignore
            }
        };
        
    }
    
}

