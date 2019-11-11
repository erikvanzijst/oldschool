/*
 * LinkConnect.java
 *
 * Created on 05 November 2003, 21:08
 */

package ManagementCommands;

import java.io.*;

import configuration.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class LinkConnectCommand extends ManagementCommand implements Serializable {
    InterfaceAddress source;
    InterfaceAddress destination;
    public final static String NAME = "LinkConnect";
    
    /** Creates a new instance of LinkConnect */
    public LinkConnectCommand() {
    } 
    
    public LinkConnectCommand(int rId, RInterface s, RInterface d) {
        super(rId);
        source = s.getSrcAddress();
        destination = d.getSrcAddress();
    }
    
    public InterfaceAddress getSource() {
        return source;
    }
    
    public InterfaceAddress getDestination() {
        return destination;
    }
    
    public String toString() {
        String s = super.toString() + " " + source.toString() + " to " + destination.toString();
        return s;
    }
}
