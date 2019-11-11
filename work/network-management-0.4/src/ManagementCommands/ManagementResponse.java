/*
 * ManagementReply.java
 *
 * Created on 27 November 2003, 10:24
 */

package ManagementCommands;

import java.io.*;

/**
 *
 * @author  Laurence Crutcher
 */
public class ManagementResponse extends ManagementCommand implements Serializable {
    private String result;
    private static String Success = "Success";
    private static String Failure = "Failure";
    
    /** Creates a new instance of ManagementReply */
    public ManagementResponse() {
    }
    
    public ManagementResponse(int rId) {
        super(rId);
    }
    
    public void succeeded() {
        result = Success;
    }
    
    public void failed() {
        result = Failure;
    }
    
    public String toString() {
        return super.toString() + " " + result;
    }
    
}
