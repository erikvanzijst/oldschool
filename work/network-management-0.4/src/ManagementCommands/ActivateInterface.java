/*
 * RemoveInterface.java
 *
 * Created on 4 december 2003, 9:19
 */

package ManagementCommands;

import configuration.*;

/**
 *
 * @author  laurence.crutcher
 */
public class ActivateInterface {
    public final static String NAME = "activateInterface";  
    private String address;    
    private String[] signature = {"java.lang.String"};
    
    /** Creates a new instance of RemoveInterface */
    public ActivateInterface(RInterface source) {
        address = source.getName();
    }
    
    public String[] getSignature() {
        return signature;
    }
    
    public Object[] getArgs() {
        return (new Object[] {address});
    }    
    
}
