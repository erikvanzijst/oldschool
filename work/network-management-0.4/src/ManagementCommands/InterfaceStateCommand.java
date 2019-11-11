/*
 * LinkStatusParameter.java
 *
 * Created on 29 October 2003, 18:29
 */

package ManagementCommands;

import java.io.*;

import configuration.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class InterfaceStateCommand extends ManagementCommand implements Serializable {
    public State state;
    public InterfaceAddress interfaceAddress;
    public final static String NAME = "InterfaceState";
    
    public InterfaceStateCommand () {
    }
    
    public InterfaceStateCommand(int rId, State s, InterfaceAddress ia) {
        super(rId);
        state = s;
        interfaceAddress = ia;
    }
    
    public InterfaceAddress getAddress() {
        return interfaceAddress;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State s) {
        state=s;
    }
    
    public void setInterfaceAddress(InterfaceAddress l) {
        interfaceAddress = l;
    }
    
    public String toString() {
        return super.toString() + " " + interfaceAddress.toString() + " " + state.toString();
    }
}