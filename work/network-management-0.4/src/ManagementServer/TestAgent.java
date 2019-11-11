/*
 * TestAgent.java
 *
 * Created on 21 oktober 2003, 11:41
 */

/**
 *
 * @author  laurence.crutcher
 */

package ManagementServer;

import java.util.*;

import configuration.*;
import ManagementCommands.*;

public class TestAgent extends Router implements TestAgentMBean {  
    private String s;
    private int routerId;
    private State routerState;
    private Vector rInterfaceSet = new Vector();

    String className = this.getClass().getName();        
    boolean DEBUG = true;
    
    /** Creates a new instance of TestAgent */
    public TestAgent(String rId) {
        routerId = (new Integer(rId)).intValue();
        rInterfaceSet.addElement(new RInterface("1.2"));
        System.out.println(this.getClass().getName() + " routerId:" + routerId);
        routerState = new State(State.INACTIVE);
    }
    
    public RouterStateCommand getRouterState() {
        RouterStateCommand routerStateCommand = new RouterStateCommand(routerId, routerState);
        System.out.println(className + ": getRouterState: returning " + routerStateCommand.toString());
        return routerStateCommand;
    }
    
    public CompositeStateCommand getCompositeState() {
        CompositeStateCommand compositeStateCommand = new CompositeStateCommand(routerId);
        compositeStateCommand.setLinks(rInterfaceSet);       
        int u = getUtilization();
        compositeStateCommand.setUtilization(u);
        System.out.println(className + ": getCompositeState: " + compositeStateCommand.toString());
        if (u==100)
            setUtilization(1);
        else
            setUtilization(u+1);
        return compositeStateCommand;
    }
    
    public String getField() {
        return s;
    }
    
    public String doSomething(String test) {
        return test;
    }
    
    public void setField(String s) {
        this.s = s;
    }
    
    public void setRouterState(RouterStateCommand routerStateCommand) {
        System.out.println(className + ": setRouterState: received " + routerStateCommand.toString());
        if (routerStateCommand.getRouterId() == routerId)
            routerState = routerStateCommand.getState();
    }
    
    public void setRouterConfiguration(RouterConfigureCommand routerConfigureCommand) {
        System.out.println(className + ": setRouterConfiguration: received " + routerConfigureCommand.toString());        
        rInterfaceSet.clear();
        Vector v = routerConfigureCommand.getAddresses();
        Iterator i = v.iterator();
        while (i.hasNext()) 
             rInterfaceSet.addElement(new RInterface((InterfaceAddress)i.next()));
    }
    
    public void setInterfaceState(InterfaceStateCommand isc) {
        int index;
        System.out.println(className + ": setInterfaceState: received " + isc.toString());
        if ((index=ConfigurationUtilities.findInterfaceIndex(rInterfaceSet, isc.getAddress())) != -1)
            ((RInterface)rInterfaceSet.elementAt(index)).setState(isc.state);
        else
            System.out.println(className + ": no record of this interface");
    }     
    
    public void setConfigureInterface(ConfigureInterfaceCommand ci) {
        int index;
        System.out.println(className + ": setConfigureInterface: received " + ci.toString()); 
        if ((index=ConfigurationUtilities.findInterfaceIndex(rInterfaceSet, ci.getAddress())) != -1)
            ((RInterface)rInterfaceSet.elementAt(index)).setMetric(ci.getMetric());
        else
            System.out.println(className + ": no record of this interface");        
    }
    
//    public void setLinkConnect(LinkConnectCommand lc) {
//        int index;
//        System.out.println(className + ": setLinkConnect: received " + lc.toString());
//        if ((index=ConfigurationUtilities.findInterfaceIndex(rInterface, lc.getSource())) != -1) {
//                ((RInterface)rInterface.elementAt(index)).setDestInterface(lc.getDestination());
//                ((RInterface)rInterface.elementAt(index)).setConnectionState(new State(State.CONNECTED));
//        }
//    }     
         
    public ManagementResponse linkConnect(LinkConnectCommand lc) {
        int index;
        System.out.println(className + ": linkConnect: invoked " + lc.toString());
//        if ((index=ConfigurationUtilities.findInterfaceIndex(rInterface, lc.getSource())) != -1) {
//                ((RInterface)rInterface.elementAt(index)).setDestInterface(lc.getDestination());
//                ((RInterface)rInterface.elementAt(index)).setConnectionState(new State(State.CONNECTED));
//        }
        ManagementResponse managementResponse = new ManagementResponse(lc.getRouterId());
        managementResponse.setCommandId(lc.getCommandId());        
        try {
            RInterface rInterface = ConfigurationUtilities.findInterface(rInterfaceSet, lc.getSource());
            Connection connection = new Connection();
            connection.setDestAddress(lc.getDestination());
            connection.setState(new State(State.CONNECTED));
            managementResponse.succeeded();
        }
        catch (ConfigurationException ce) {
            System.out.println(className + ": configuration exception in linkConnect: " + ce.getMessage());
            managementResponse.failed();
        }
        return managementResponse;
    }    
    
    public void setLinkDisconnect(LinkDisconnectCommand ld) {
//        int index;
//        System.out.println(className + ": setLinkDisconnect: received " + ld.toString());
//        if ((index=ConfigurationUtilities.findInterfaceIndex(rInterface, ld.getSource())) != -1) {
//                ((RInterface)rInterface.elementAt(index)).setConnectionState(new State(State.DISCONNECTED));
//        }
    }      
    
    public void setRebootRouter(RebootRouterCommand rebootRouter) {
        System.out.println(className + ": reboot command received for router ID " + rebootRouter.toString());
    }
    
    public void setResetInterface(ResetInterfaceCommand resetInterface) {
        System.out.println(className + ": reset interface command received: " + resetInterface.toString());        
    }
    
    public void setResetRoutingTable(ResetRoutingTableCommand resetRoutingTable) {
        System.out.println(className + ": reset routing table command received for router ID " + resetRoutingTable.toString());                
    }
    
    
    // Erik's interface
    
    public void addInterface(String src, String dest, String host, Integer port) {
        System.out.println(className + ": add Interface command received: " + src + " " + dest + " " + host + " " + port);
    }
    
    public void removeInterface(String src) {
        System.out.println(className + ": remove Interface command received: " + src);
    }
    
    public void activateInterface(String src) {
        System.out.println(className + ": activate Interface command received: " + src);        
    }
    
    public void deactivateInterface(String src) {
        System.out.println(className + ": deactivate Interface command received: " + src);        
    }
    
}
