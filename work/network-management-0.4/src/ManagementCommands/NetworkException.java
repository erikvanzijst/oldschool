/*
 * NetworkException.java
 *
 * Created on 25 November 2003, 20:10
 */

package ManagementCommands;


/**
 *
 * @author  Laurence Crutcher
 */
public class NetworkException {
    Throwable throwable;
    int routerId;
    
    /** Creates a new instance of NetworkException */
    public NetworkException(Throwable t, int r) {
        throwable = t;
        routerId = r;
    }
    
    public Throwable getThrowable() {
        return throwable;
    }
    
    public int getRouterId() {
        return routerId;
    }
    
    public String toString() {
        return (new Integer(routerId)).toString() + ": " + throwable.toString();
    }
    
    
}
