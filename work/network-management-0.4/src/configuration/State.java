/*
 * State.java
 *
 * Created on 24 oktober 2003, 9:18
 */

package configuration;

import java.io.*;

import logging.OMMLogger;

/**
 *
 * @author  laurence.crutcher
 */
public class State implements Serializable {
    String state;
    public final static String ACTIVE = "ACTIVE";
    public final static String INACTIVE = "INACTIVE";
    private final static String DEAD = "DEAD";
    public final static String CONNECTED = "CONNECTED";
    public final static String DISCONNECTED = "DISCONNECTED";
    public final static String UNKNOWN = "UNKNOWN";
    
    /** Creates a new instance of State */
    public State() {
        state = UNKNOWN;
    }
    
    public State(String s) {
        if ((s.equals(ACTIVE)) | (s.equals(INACTIVE)) | (s.equals(DEAD)) | (s.equals(CONNECTED))
            | (s.equals(DISCONNECTED)) | (s.equals(UNKNOWN)))        
            state = s;
        else {
            state=State.UNKNOWN;
            OMMLogger.logger.error("Illegal State");
        }
    }  
    
    public boolean isActive() {
        return (state == ACTIVE);
    }
    
    public boolean isInActive() {
        return (state == INACTIVE);
    }
    
    public boolean isDead() {
        return (state == DEAD);
    }
    
    public boolean isConnected() {
        return (state == CONNECTED);
    }    
    
    public boolean isDisconnected() {
        return (state == DISCONNECTED);
    }    
    
    public boolean isUnknown() {
        return (state == UNKNOWN);
    }
    
    public void setActive() {
        state=ACTIVE;
    }
    
    public void setInactive() {
        state=INACTIVE;
    }
    
    public void setDead() {
        state=DEAD;
    }
    
    public void setConnected() {
        state=CONNECTED;
    }
    
    public void setDisconnected() {
        state=DISCONNECTED;
    }
    
    public void setUnknown() {
        state=UNKNOWN;
    }
    
    public void setState(String s) {
        if ((s.equals(ACTIVE)) | (s.equals(INACTIVE)) | (s.equals(DEAD)) | (s.equals(CONNECTED))
            | (s.equals(DISCONNECTED)) | (s.equals(UNKNOWN)))
            state = s;
        else {
            state=State.UNKNOWN;
            OMMLogger.logger.error("Illegal State");
        }
    }
    
    public String toString() {
        return state;
    }
         
}
