/*
 * Class.java
 *
 * Created on 13 November 2003, 21:55
 */

package ManagementCommands;

import configuration.*;

import java.io.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class ResetInterfaceCommand extends ManagementCommand implements Serializable {
    private boolean all;
    private InterfaceAddress interfaceAddress;
    
    public final static String NAME = "ResetInterface";       
    
    public ResetInterfaceCommand() {
    }
    
    // This constructor is for reset all interfaces on a router
    public ResetInterfaceCommand(int r) {
        super(r);
        all = true;
    }
    
    // This constructor is to reset a specified interface
    public ResetInterfaceCommand(int r, InterfaceAddress ia) {
        super(r);
        all = false;
        interfaceAddress = ia;
    }
    
    public String toString() {
        String s = super.toString() + " All:" + all;
        if (!all)
            s+=" Address:" + interfaceAddress.toString();
        return s;
    }    
    
}
