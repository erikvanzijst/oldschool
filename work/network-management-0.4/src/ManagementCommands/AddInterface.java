/*
 * addInterface.java
 *
 * Created on 3 december 2003, 16:40
 */

package ManagementCommands;

import java.io.*;

import configuration.*;

/**
 *
 * @author  laurence.crutcher
 */
public class AddInterface implements Serializable {
    public final static String NAME = "addInterface";  
    private String address;
    private String peerAddress;
    private String hostName;
    private Integer port;
    private String[] signature = {"java.lang.String", "java.lang.String", "java.lang.String", "int"};
    
    /** Creates a new instance of addInterface */
    public AddInterface(Router src, Router dest, RInterface from, RInterface to) {
        address=from.getName();
        peerAddress=to.getName();
        hostName = dest.getHost();
        port = new Integer(dest.getListenerPort());
    }
    
    public String[] getSignature() {
        return signature;
    }
    
    public Object[] getArgs() {
        return (new Object[] {address, peerAddress, hostName, port});
    }
}
