/*
 * ManagerException.java
 *
 * Created on 23 oktober 2003, 15:26
 */

package ClientGUI;

/**
 *
 * @author  laurence.crutcher
 */
public class ManagerException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>ManagerException</code> without detail message.
     */
    public ManagerException() {
    }
    
    
    /**
     * Constructs an instance of <code>ManagerException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ManagerException(String msg) {
        super(msg);
    }
}
