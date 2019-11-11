/*
 * ConfigurationException.java
 *
 * Created on 30 October 2003, 21:38
 */

package configuration;

/**
 *
 * @author  Laurence Crutcher
 */
public class ConfigurationException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>ConfigurationException</code> without detail message.
     */
    public ConfigurationException() {
    }
    
    
    /**
     * Constructs an instance of <code>ConfigurationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ConfigurationException(String msg) {
        super(msg);
    }
}
