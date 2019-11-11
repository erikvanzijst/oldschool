/*
 * GUIExceptionHandler.java
 *
 * Created on 23 oktober 2003, 15:19
 */

package ClientGUI;

/**
 *
 * @author  laurence.crutcher
 */
public class GUIException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>GUIExceptionHandler</code> without detail message.
     */
    public GUIException() {
    }
    
    
    /**
     * Constructs an instance of <code>GUIExceptionHandler</code> with the specified detail message.
     * @param msg the detail message.
     */
    public GUIException(String msg) {
        super(msg);
    }
}
