/*
 * TestServer.java
 *
 * Created on 21 oktober 2003, 11:11
 */
package ManagementServer;

import com.marketxs.management.*;


/**
 *
 * @author  laurence.crutcher
 */
public class TestServer {
    
    /** Creates a new instance of TestServer */
    public TestServer() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: TestServer <routerId>");
            System.exit(0);
        }
        try {
            System.setProperty("com.marketxs.log.stacktrace.debug", "true");
            
            ManagementHelper.runRmiConnector(1095);
            ManagementHelper.runHtmlAdaptor("administrator", "secret", 8082);
            
            ManagementHelper.register("Test:type=testagent", new TestAgent(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
