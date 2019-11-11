/*
 * CompositeState.java
 *
 * Created on 30 October 2003, 17:49
 */

package ManagementCommands;

import java.io.*;
import java.util.*;

import configuration.*;
/**
 *
 * @author  Laurence Crutcher
 */

public class CompositeStateCommand extends ManagementCommand implements Serializable {
    public final static String NAME = "CompositeState";    
    Vector rInterface = new Vector();
    int utilization;
    
    /** Creates a new instance of CompositeState */
    public CompositeStateCommand() {
    }
    
    public CompositeStateCommand(int rId) {
        super(rId);
    }
    
    public void setLinks(Vector v) {
        rInterface = (Vector)v.clone();
    }
    
    public Vector getLinks() {
        return rInterface;
    }
    
    public int getUtilization() {
        return utilization;
    }
    
    public void setUtilization(int u) {
        utilization = u;
    }
    
    public String toString() {
       String s = new String();
       Iterator i = rInterface.iterator();
       s= super.toString() + " Node Utilization:" + utilization + "\n";       
       while (i.hasNext())
           s+=(((RInterface)i.next()).toString() +"\n");
       return s;
   }   
}
