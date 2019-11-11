/*
 * Class.java
 *
 * Created on 05 October 2003, 16:17
 */

package configuration;

/**
 *
 * @author  Laurence Crutcher
 */

import java.io.*;
import java.util.*;

import logging.OMMLogger;

public class RInterface implements Serializable {
    int routerId;   // This is just a convenience handle
    InterfaceAddress address = new InterfaceAddress();
//    InterfaceAddress destInterfaceId =  new InterfaceAddress();
//    int destRouterId;
    int distance;
    int utilization;
    State interfaceState = new State();
    
    public RInterface() {
        setAddress(State.UNKNOWN);
//        setDestInterface(State.UNKNOWN);
        interfaceState.setUnknown();
    }
    
    public RInterface(String s) {
        setAddress(s);
//        setDestInterface(State.UNKNOWN);
        interfaceState.setUnknown();
    }

    public RInterface(int r) {
        routerId = r;
        setAddress(State.UNKNOWN);
//        setDestInterface(State.UNKNOWN);
        interfaceState.setUnknown();
    }    
    
    public RInterface(InterfaceAddress interfaceAddress) {
        setAddress(interfaceAddress);
//        setDestInterface(State.UNKNOWN);
        interfaceState.setUnknown();
    }
    
    public void setAddress(String s) {
        try {
            address.setAddress(s);
        }
        catch (ConfigurationException ce) {
            OMMLogger.logger.error("Configuration Exception in setAddress" + ce.getMessage());
        }
    }
    
    public void setAddress(InterfaceAddress a) {
        address = a;
    }    
    
//    public void setDestInterface(String s) {
//        try {
//            destInterfaceId.setAddress(s);
//        }
//        catch (ConfigurationException ce) {
//            OMMLogger.logger.error("Configuration Exception in setDestInterface" + ce.getMessage());
//        }
//    }    
//    
//    public void setDestInterface(InterfaceAddress a) {
//        destInterfaceId = a;
//    }
//    
//    public void setDestNode(int d) {
//        destRouterId = d;
//    }
    
    public int getMetric() {
        return distance;
    }
    
    public void setMetric(int d) {
        distance = d;
    }
    
    public String getName() {
        return address.toString();
    }
    
    public boolean equals(InterfaceAddress interfaceAddress) {    // equality is based on the src link address
        return ((address.toString()).equals(interfaceAddress.toString()));
    }
    
    public State getState() {
        return interfaceState;
    }
    
    public void setState(String s) {
        interfaceState.setState(s);
    }
    
    public void setState(State s) {
        interfaceState = s;
    }
    
    
    public int getRouterId() {
        return routerId;
    }
    
    public InterfaceAddress getSrcAddress() {
        return address;
    }
    
//    public InterfaceAddress getDestAddress() {
//        return destInterfaceId;
//    }
    
    public int getUtilization() {
        return utilization;
    }
    
//    public String toString() {
//        return ("Source Interface:" + myInterfaceId.toString() + "  Destination Router:" + destRouterId +
//            "  Destination Interface:" + destInterfaceId.toString() + "  Metric:" + distance + "  Interface State:" + interfaceState.toString() +
//             " Interface Utilization:" + utilization);
//    }
    


        
    
    public String toString() {
        return ("Address:" + address.toString() + 
             "  Metric:" + distance + "  Interface State:" + interfaceState.toString() +
             " Interface Utilization:" + utilization);
    }    
}

