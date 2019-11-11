/*
 * ResetRoutingTable.java
 *
 * Created on 13 November 2003, 22:04
 */

package ManagementCommands;

import java.io.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class ResetRoutingTableCommand extends ManagementCommand implements Serializable {
    
    public final static String NAME = "ResetRoutingTable";       
    
    /** Creates a new instance of ResetRoutingTable */
    public ResetRoutingTableCommand() {
    }
    
    public ResetRoutingTableCommand(int i) {
        super(i);
    }    
    
    public String toString() {
        return super.toString();
    }
    
}
