/*
 * Connection.java
 *
 * Created on 2 december 2003, 12:05
 */

package configuration;

import java.util.Date;

import logging.OMMLogger;

/**
 *
 * @author  laurence.crutcher
 */
public class Connection {
    Date creationDate;
    State state;      
    InterfaceAddress srcAddress;
    InterfaceAddress destAddress;
    int srcRouterId;
    int destRouterId;
    int distance;
    
    /** Creates a new instance of Connection */
    public Connection() {
        creationDate = new Date();
        state = new State();
        srcAddress = new InterfaceAddress();
        destAddress = new InterfaceAddress();
    }
        
    public InterfaceAddress getSrcAddress() {
        return srcAddress;
    }
    
    public void setSrcAddress(InterfaceAddress a) {
        srcAddress = a;
    }
    
    public InterfaceAddress getDestAddress() {
        return destAddress;
    }    
    
    public void setDestAddress(InterfaceAddress a) {
        destAddress = a;
    }  
    
    public int getSrcRouter() {
        return srcRouterId;
    }
    
    public void setSrcRouter(int rId) {
        srcRouterId = rId;
    }
    
    public int getDestRouter() {
        return destRouterId;
    }
    
    public void setDestRouter(int rId) {
        destRouterId = rId;
    }    
    
    public State getState() {
        return state;
    }  
    
    public void setState(State s) {
        state = s;
    }    
    
    public void setDistance(int d) {
        distance = d;
    }
    
    public long getUpTime() {
        Date now = new Date();
        return (now.getTime() - creationDate.getTime());
    }
    
    public String toString() {
        return ("Connection. src router:" + srcRouterId + " address:" + srcAddress.toString() + " dest router:" + destRouterId 
            + " address:" + destAddress.toString());
    }
}
