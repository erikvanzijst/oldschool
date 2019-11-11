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
public class LinkDisconnectCommand extends ManagementCommand implements Serializable {
    InterfaceAddress source;
    public final static String NAME = "LinkDisconnect";
    
    /** Creates a new instance of LinkConnect */
    public LinkDisconnectCommand() {
    }
    
    public LinkDisconnectCommand(int rId, RInterface s) {
        super(rId);
        source = s.getSrcAddress();
    }
    
    public InterfaceAddress getSource() {
        return source;
    }  
    
    public String toString() {
        String s = super.toString() + " " + source.toString();
        return s;
    }
}
