/*
 * RouterConfigure.java
 *
 * Created on 10 November 2003, 12:44
 */

package ManagementCommands;

import java.io.*;
import java.util.*;

import configuration.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class RouterConfigureCommand extends ManagementCommand implements Serializable {
    public final static String NAME = "RouterConfiguration";    
    Vector interfaceAddresses = new Vector();    
    
    /** Creates a new instance of RouterConfigure */
    public RouterConfigureCommand() {
    }
    
    public RouterConfigureCommand(Router router) {
        super(router.getId());
        Vector v = router.getLinks();
        Iterator i = v.iterator();
        while (i.hasNext()) {
            InterfaceAddress ia = ((RInterface)i.next()).getSrcAddress();
            interfaceAddresses.addElement(ia);
        }
    }
    
    public Vector getAddresses() {
        return interfaceAddresses;
    }
    
    public String toString() {
        String s = super.toString();
        Iterator i = interfaceAddresses.iterator();
        while (i.hasNext())
            s+=((i.next()).toString() + " ");
        return s;
    }
        
    
}
