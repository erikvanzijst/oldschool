/*
 * Class.java
 *
 * Created on 05 October 2003, 15:37
 */

package configuration;

import java.util.*;

import ManagementCommands.*;
// import com.marketxs.management.*;
import logging.OMMLogger;

/**
 *
 * @author  Laurence Crutcher
 */

public class Router extends ManagementInterface {   
        int myNodeId;
        String host;
        Vector rInterface = new Vector();
        State state;

        int utilization;
        
        public Router (int nodeId) {
            myNodeId = nodeId;
            state = new State();
            managementChannelState = new State();
            managementChannelState.setDisconnected();
        }
        
        public Router() {
            state = new State();
            managementChannelState = new State();
            managementChannelState.setDisconnected();            
        }
        
        public int getId() {
            return myNodeId;
        }
        
        public void setId(int r) {
            myNodeId = r;
        }
        
        public String name() {
            return Integer.toString(myNodeId);
        }
        
        public String printRouterState() {
            return state.toString();
        }
        
        
        void addInterface(RInterface i) {
            rInterface.addElement(i);
        }
        
        public Vector getLinks() {
            return rInterface;
        }
        
                        
        void setHost (String s) {
            host = s;
        }
        
        public String getHost() {
            return host;
        }
          
        public void setRouterState(State s) {
            state=s;
        }
        
        public int getUtilization() {
            return utilization;
        }
        
        public void setUtilization(int u) {
            utilization = u;
        }
        
        public void recomposeRouterState(CompositeStateCommand compositeState) {
            utilization = compositeState.getUtilization();
            Iterator i = (compositeState.getLinks()).iterator();
            while (i.hasNext()) {
                RInterface rI = (RInterface)(i.next());
                int m = ConfigurationUtilities.findInterfaceIndex(rInterface, rI.getSrcAddress());
                    if (m != -1) {
//                        RInterface r = (RInterface)rInterface.elementAt(m);
//                        r.setState(rI.getState());
//                        r.setConnectionState(rI.getConnectionState());
//                        rInterface.set(m, r);
                        rInterface.set(m, rI);
                    }
            }
        }
        
        String print() {
            String s = "Router " + name() + ": Administration Parameters \n";
            s+= "Host:" + host + " Management Port:" + managementPort + " Listener Port:" + listenerPort + "\n";
            s+= "HTTP Port:" + httpPort + " HTTP User:" + httpUser + " HTTP Password:" + httpPassword + "\n";
            s+= "Management Domain:" + managementDomain + " Management Agent:" + managementAgent + " Messaging Domain:" + messagingDomain + " MessagingAgent:" + messagingAgent + "\n";
            s+= "Router " + name() + " has " + rInterface.size() + " interfaces.\n";
            Iterator i = rInterface.iterator();
            while (i.hasNext())
                s+= ((RInterface)i.next()).toString() + "\n";
            return s;
        }
        
        public Vector getLinkTreeView() {
            Vector v = new Vector();
            Iterator j = rInterface.iterator();
            while (j.hasNext()) {
                RInterface rI = (RInterface)j.next();
                v.addElement(new NodeView(NodeView.INTERFACE, rI.getName(), rI));
            }
            return v;
        }

}
