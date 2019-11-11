/*
 * NodeView.java
 *
 * Created on 16 October 2003, 11:24
 */

package configuration;

/**
 *
 * @author  Laurence Crutcher
 */
public class NodeView {
    String type;
    String name;
    Object object;
    public final static String NETWORK = "Network";
    public final static String ROUTER = "Router";
    public final static String INTERFACE = "Interface";
    public final static String UNKNOWN = "UNKNOWN";
        
    /** Creates a new instance of NodeView */
    public NodeView(String t, String n, Object o) {
        type = t;
        name = n;
        object = o;
    }
    
    public String name() {
        return name;
    }
    
    public String type() {
        return type;
    }
    
    public String toString() {
        return name;
    }
    
    public Object getUserObject() {
        return object;
    }
    
    public boolean isRouter() {
        return (type == ROUTER);
    }
    
    public boolean isNetwork() {
        return (type == NETWORK);
    }
    
    public boolean isInterface() {
        return (type == INTERFACE);
    }
}
