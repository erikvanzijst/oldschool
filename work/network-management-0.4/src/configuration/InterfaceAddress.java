/*
 * LinkAddress.java
 *
 * Created on 27 October 2003, 19:11
 */

package configuration;

import java.util.*;
import java.io.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class InterfaceAddress implements Serializable {
    Vector addressElements = new Vector();
    private final static String DELIMITER = ".";
    
    /** Creates a new instance of LinkAddress */
    public InterfaceAddress() {
    }
    
    public void addElement(String s) {
        addressElements.addElement(s);
    }
    
    public String toString() {
        String s = "";
        Iterator i = addressElements.iterator();
        while (i.hasNext()) {
            s+=i.next();
            if (i.hasNext())
                s+=".";
        }
        return s;
    }
 
    // the parser in setAddress is not quite bullet proof 
    // picks out the strings before/after the delimiter and checks the delimiter, but doesn't look inside the tokens
    
    public void setAddress(String s) throws ConfigurationException {
        StringTokenizer addressTokenizer = new StringTokenizer(s, InterfaceAddress.DELIMITER, true);
        while (addressTokenizer.hasMoreTokens()) {
            addressElements.addElement(addressTokenizer.nextToken());
            if (addressTokenizer.hasMoreTokens()) {             
                if (! (addressTokenizer.nextToken()).equals(InterfaceAddress.DELIMITER))
                    throw new ConfigurationException("Illegal interface address");
            }
        }
    }
    
}
