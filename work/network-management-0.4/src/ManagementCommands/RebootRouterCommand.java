/*
 * RebootRouter.java
 *
 * Created on 13 November 2003, 19:36
 */

package ManagementCommands;

import java.io.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class RebootRouterCommand extends ManagementCommand implements Serializable {
    public final static String NAME = "RebootRouter";    
    
    /** Creates a new instance of RebootRouter */
    public RebootRouterCommand() {
    }
    
    public RebootRouterCommand(int i) {
        super(i);
    }
    
    public String toString() {
        return super.toString();
    }
        
}
