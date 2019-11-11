/*
 * TestAgentMBean.java
 *
 * Created on 21 oktober 2003, 11:41
 */

/**
 *
 * @author  laurence.crutcher
 */

package ManagementServer;

import configuration.*;
import ManagementCommands.*;

public interface TestAgentMBean {
    String getField();
    RouterStateCommand getRouterState();
    void setField(String s);
    void setRouterState(RouterStateCommand routerStateCommand);
    String doSomething(String s);  
    void setInterfaceState(InterfaceStateCommand isc);
    CompositeStateCommand getCompositeState();
    void setRouterConfiguration(RouterConfigureCommand routerConfigureCommand);
//    void setLinkConnect(LinkConnectCommand lc);
    ManagementResponse linkConnect(LinkConnectCommand lc);
    void setLinkDisconnect(LinkDisconnectCommand ld);
    void setRebootRouter(RebootRouterCommand rebootRouter);
    void setResetInterface(ResetInterfaceCommand resetInterface);
    void setResetRoutingTable(ResetRoutingTableCommand resetRoutingTable);
    void setConfigureInterface(ConfigureInterfaceCommand ci);
    
    // Erik's interface
    void addInterface(String src, String dest, String host, Integer port);
    void removeInterface(String src);
    void activateInterface(String src);
    void deactivateInterface(String src);
}
