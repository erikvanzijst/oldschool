/*
 * ManagementCommand.java
 *
 * Created on 25 November 2003, 23:09
 */

package ManagementCommands;

import java.io.*;

import configuration.ConfigurationUtilities;

/**
 *
 * @author  Laurence Crutcher
 */
public class ManagementCommand implements Serializable {
    private int routerId;
    private int commandId;
    
    /** Creates a new instance of ManagementCommand */
    public ManagementCommand() {
    }
    
    public ManagementCommand(int rId) {
        routerId = rId;
        commandId = ConfigurationUtilities.getCommandCount();       
    }
        
    public int getRouterId() {
        return routerId;
    }
    
    public void setRouterId(int id) {
        routerId = id;
    }
    
    public int getCommandId() {
        return commandId;
    }
    
    public void setCommandId(int id) {
        commandId = id;
    }
    
    public String toString() {
        String s = "CommandID:" + (new Integer(commandId)).toString() + " RouterID:" + (new Integer(routerId)).toString();
        return s;
    }
}
