/*
 * RouterStatusParameter.java
 *
 * Created on 30 October 2003, 11:46
 */

package ManagementCommands;

import java.io.*;
 
import configuration.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class RouterStateCommand extends ManagementCommand implements Serializable {
    State state;
    public final static String NAME = "RouterState";
    
    /** Creates a new instance of RouterStatus */
    public RouterStateCommand() {
    }
    
    public RouterStateCommand(int rId, State s) {
        super(rId);
        state = s;
    }      
    
    public void setState(State s) {
        state = s;
    }
    
    public State getState() {
        return state;
    }
    
    public String toString() {
        return super.toString() + " " + state.toString();
    }
    
}
