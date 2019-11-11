/*
 * InterfaceConfiguration.java
 *
 * Created on 20 November 2003, 15:37
 */

package ManagementCommands;

import java.io.*;

import configuration.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class ConfigureInterfaceCommand extends ManagementCommand implements Serializable {
    private int metric;
    private InterfaceAddress interfaceAddress;
    public final static String NAME = "ConfigureInterface";   
    
    /** Creates a new instance of InterfaceConfiguration */
    public ConfigureInterfaceCommand() {
    } 
    
    public ConfigureInterfaceCommand(int r, InterfaceAddress ia, int m) {
        super(r);
        interfaceAddress = ia;
        metric = m;
    }
    
    public int getMetric() {
        return metric;
    }
    
    public InterfaceAddress getAddress() {
        return interfaceAddress;
    }
    
    public String toString() {
        return super.toString() + " Interface Address:" + interfaceAddress.toString() + " Metric:" + metric;
    }
    
}
