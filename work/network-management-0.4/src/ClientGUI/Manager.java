/*
 * Manager.java
 *
 * Created on 21 oktober 2003, 15:12
 */


package ClientGUI;

import java.io.*;
import java.util.Vector;
import java.util.Iterator;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import configuration.*;
import ManagementCommands.*;
import logging.OMMLogger;
import com.marketxs.management.*;

/**
 *
 * @author  laurence.crutcher
 */


public class Manager extends ManagerTable implements DynamicTreeEventListener, NetworkEventListener {
    Vector linkData = new Vector();
    NetworkIO networkIO;
    DynamicTreePanel nodeTree;
    InterfaceTable interfaceTable;
    ConnectionTable connectionTable;
    RouterUtilizationWindow routerUtilizationWindow;
    InterfaceUtilizationWindow interfaceUtilizationWindow;
    Timer managementTimer;
    Timer performanceTimer;
    private int managerPollPeriod = 5000;
    private int performancePollPeriod = 30000; 

    public Manager(DynamicTreePanel dtp, InterfaceTable it, ConnectionTable ct, RouterUtilizationWindow ruw, InterfaceUtilizationWindow iuw) {
        networkIO = new NetworkIO();
        nodeTree=dtp;
        interfaceTable = it;
        connectionTable = ct;
        routerUtilizationWindow = ruw;
        interfaceUtilizationWindow = iuw;
        nodeTree.addDynamicTreeEventListener(this);
        networkIO.addNetworkEventListener(this);
        managementTimer = new Timer(managerPollPeriod, new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
//                sweepNodeTree();
                sweepManagementChannels();
//                sweepRouterState();
//                sweepCompositeState();
                interfaceTable.update(routerData);
            }
        });
        managementTimer.start(); 
        performanceTimer = new Timer(performancePollPeriod, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                routerUtilizationWindow.updatePerformance(routerData);
                interfaceUtilizationWindow.updatePerformance(linkData);
            }
        });
        performanceTimer.start();
    }
    
    public int getPollPeriod() {
        return managerPollPeriod;
    }
    
    public void setPollPeriod(int i) {
        managerPollPeriod = i;
        managementTimer.setDelay(managerPollPeriod);
    }       
    
    public int getPerformanceUpdatePeriod() {
        return performancePollPeriod;
    }
    
    public void setPerformancePollPeriod(int i) {
        performancePollPeriod = i;
        performanceTimer.setDelay(performancePollPeriod);
    }
    
    public void pauseMonitoring() {
        managementTimer.stop();
        performanceTimer.stop();
    }
    
    public void resumeMonitoring() {
        managementTimer.restart();
        performanceTimer.restart();
    }
    
    private void sweepNodeTree() {
        routerData = this.nodeTree.filter(NodeView.ROUTER);
        linkData = this.nodeTree.filter(NodeView.INTERFACE);
        fireTableChanged(new TableModelEvent(this));
    }
    
    public Vector getRouterData() {
        return routerData;
    }
    
    public Vector getLinkData() {
        return linkData;
    }
    
    private void sweepManagementChannels() {
        Router r;
        Iterator i = routerData.iterator();
        while (i.hasNext()) {
            r = (Router)i.next();
            if (!(r.isManagementChannelConnected())) {
                ManagementClient client = createManagementSession(r.getHost(), r.getManagementPort());
                if (client != null) {
                    r.setManagementClient(client);
                    r.managementChannelIsConnected(true);
                }
            }
        }
    }
    
    private void sweepRouterState() {
        Router r;
        State state = new State();
        Iterator i = routerData.iterator();
        while (i.hasNext()) {
            r = (Router)i.next();            
            OMMLogger.logger.debug("sweepRouterState: Router " + r.name() +
                    " management channel state is " + r.printManagementChannelState());
            if (r.isManagementChannelConnected()) {
                String s = r.getManagementKey();        
                networkIO.read(r.getId(), r.getManagementClient(), s, RouterStateCommand.NAME);                    
            }
        }
    }
    
        
    private void sweepCompositeState() {
        Router r;
        for (int i=0; i<routerData.size(); i++)  { // deliberately don't use iterator, as we want to put the element back
            r = (Router)routerData.elementAt(i);       
            OMMLogger.logger.debug("sweepCompositeState: Router " + r.name() +
                    " management channel state is " + r.printManagementChannelState());
            if (r.isManagementChannelConnected()) {
                    String key = r.getManagementKey();
                    networkIO.read(r.getId(), r.getManagementClient(), key, CompositeStateCommand.NAME);                    
            }
        }
    }
    
    private ManagementClient createManagementSession(String host, int port) {
        try {
            ManagementClient client = new RmiManagementClient(host, port);
            return client;
        }
        catch (ManagementCommunicationException e) {
            return null;
        }
        catch (ManagementException e) {
            return null;
        }
    }
      

    public void activateRouter(Router router) throws ManagerException {
        State state = new State(State.ACTIVE);
        RouterStateCommand routerStateCommand = new RouterStateCommand(router.getId(), state);
        String key =  router.getManagementKey();
        OMMLogger.logger.info("Activate Router:" + routerStateCommand.toString());
        networkIO.write(router.getId(), router.getManagementClient(), key, RouterStateCommand.NAME, routerStateCommand);
   }
    
    public void activateInterface(RInterface rInterface) throws ManagerException {
        activateErikInterface(rInterface);
//        Router router = nodeTree.getRouter(rInterface.getRouterId());             
//        State state = new State(State.ACTIVE);
//        InterfaceStateCommand interfaceStateCommand = new InterfaceStateCommand(router.getId(), state, rInterface.getSrcAddress());        
//        String key = router.getManagementKey();
//        OMMLogger.logger.info("Activate Interface:" + interfaceStateCommand.toString());        
//        networkIO.write(router.getId(), router.getManagementClient(), key, InterfaceStateCommand.NAME, interfaceStateCommand);
   }    
       
    public void deactivateRouter(Router router) throws ManagerException {
        State state = new State(State.INACTIVE);
        RouterStateCommand routerStateCommand = new RouterStateCommand(router.getId(), state);
        String key = router.getManagementKey();  
        OMMLogger.logger.info("Deactivate Router:" + routerStateCommand.toString());        
        networkIO.write(router.getId(), router.getManagementClient(), key, RouterStateCommand.NAME, routerStateCommand);
    }
    
    public void deactivateInterface(RInterface rInterface) throws ManagerException {  
        deactivateErikInterface(rInterface);
//        Router router = nodeTree.getRouter(rInterface.getRouterId());            
//        State state = new State(State.INACTIVE);
//        InterfaceStateCommand interfaceStateCommand = new InterfaceStateCommand(router.getId(), state, rInterface.getSrcAddress());            
//        String s = router.getManagementKey(); 
//        OMMLogger.logger.info("Deactivate Interface:" + interfaceStateCommand.toString());           
//        networkIO.write(router.getId(), router.getManagementClient(), s, InterfaceStateCommand.NAME, interfaceStateCommand);
   }     
    
    public void configureRouter(Router router) throws ManagerException { 
        RouterConfigureCommand routerConfigureCommand = new RouterConfigureCommand(router);    
        String s = router.getManagementKey();           
        OMMLogger.logger.info("Configure Router:" + routerConfigureCommand.toString());  
        networkIO.write(router.getId(), router.getManagementClient(), s, RouterConfigureCommand.NAME, routerConfigureCommand);           
    }
    
    public void rebootRouter(Router router) throws ManagerException {
        RebootRouterCommand rebootRouterCommand = new RebootRouterCommand(router.getId());
        String key = router.getManagementKey();           
        OMMLogger.logger.info("Reboot Router:" + rebootRouterCommand.toString());    
        networkIO.write(router.getId(), router.getManagementClient(), key, RebootRouterCommand.NAME, rebootRouterCommand);     
    }
    
    public void resetInterface(RInterface rInterface) throws ManagerException {
        Router router = nodeTree.getRouter(rInterface.getRouterId());
        ResetInterfaceCommand resetInterfaceCommand = new ResetInterfaceCommand(router.getId(), rInterface.getSrcAddress());
        String key = router.getManagementKey();           
        OMMLogger.logger.info("Reset Interface:" + resetInterfaceCommand.toString()); 
        networkIO.write(router.getId(), router.getManagementClient(), key, ResetInterfaceCommand.NAME, resetInterfaceCommand);      
    }
    
    public void configureInterface(RInterface rInterface) throws ManagerException {
        Router router = nodeTree.getRouter(rInterface.getRouterId());
        ConfigureInterfaceCommand configureInterfaceCommand = new ConfigureInterfaceCommand(router.getId(), rInterface.getSrcAddress(), rInterface.getMetric());
        String key = router.getManagementKey();   
        OMMLogger.logger.info("Configure Interface:" + configureInterfaceCommand.toString());                     
        networkIO.write(router.getId(), router.getManagementClient(), key, ConfigureInterfaceCommand.NAME, configureInterfaceCommand);   
    }
        
    public void resetAllInterface(Router router) throws ManagerException {
        ResetInterfaceCommand resetInterfaceCommand = new ResetInterfaceCommand(router.getId());
        String key = router.getManagementKey();           
        OMMLogger.logger.info("Reset All Interfaces:" + resetInterfaceCommand.toString());  
        networkIO.write(router.getId(), router.getManagementClient(), key, ResetInterfaceCommand.NAME, resetInterfaceCommand);        
    }    
    
    public void resetRoutingTable(Router router) throws ManagerException {
        ResetRoutingTableCommand resetRoutingTableCommand = new ResetRoutingTableCommand(router.getId());
        String key = router.getManagementKey();           
        OMMLogger.logger.info("Reset Routing Table:" + resetRoutingTableCommand.toString());   
        networkIO.write(router.getId(), router.getManagementClient(), key, ResetRoutingTableCommand.NAME, resetRoutingTableCommand);
    }      
    
    public void connectLink(RInterface source, RInterface destination) throws ManagerException {    
        connectErikLink(source, destination);
//        Router router = nodeTree.getRouter(source.getRouterId());
//        LinkConnectCommand linkConnectCommand = new LinkConnectCommand(router.getId(), source, destination);
//        String key = router.getManagementKey();
//        OMMLogger.logger.info("Connect Link:" + linkConnectCommand.toString());    
//        networkIO.invoke(router.getId(), router.getManagementClient(), key, "linkConnect", new Object[]{linkConnectCommand}, new String[]{"ManagementCommands.LinkConnectCommand"});                 
    }

   
    
    public void disconnectLink(RInterface source) throws ManagerException {      
        disconnectErikLink(source);
//        Router router = nodeTree.getRouter(source.getRouterId());
//        LinkDisconnectCommand linkDisconnectCommand = new LinkDisconnectCommand(router.getId(), source);
//        String key = router.getManagementKey();             
//        OMMLogger.logger.info("Disconnect Link:" + linkDisconnectCommand.toString());
//        networkIO.write(router.getId(), router.getManagementClient(), key, LinkDisconnectCommand.NAME, linkDisconnectCommand);       
    }   

        
    public void addDynamicTreeEventListener(DynamicTreeEventListener dtel) {
    }    

    public void dynamicTreeEvent() {
        OMMLogger.logger.debug("dynamicTreeEvent");
        sweepNodeTree();
    }    
    
    public void addNetworkEventListener(NetworkEventListener nel) {
    }
    
    public void networkEvent(Object o) {
        if (o instanceof RouterStateCommand) {
            OMMLogger.logger.info("Received a network event: RouterStateCommand:" + ((RouterStateCommand)o).toString());  
            int routerId = ((RouterStateCommand)o).getRouterId();
            try {
                Router r = ConfigurationUtilities.findRouter(routerData, routerId);
                State state = ((RouterStateCommand)o).getState();
                if (state.isUnknown())
                    r.managementChannelIsConnected(false);
                r.setRouterState(state);      
            }
            catch (ConfigurationException ce) {
                OMMLogger.logger.error("Discarding Event:" + ce);
            }
        }
        
        else if (o instanceof CompositeStateCommand) {
            OMMLogger.logger.info("Received a network event: CompositeStateCommand:" + ((CompositeStateCommand)o).toString());  
            int routerId = ((CompositeStateCommand)o).getRouterId();
            try {
                int index = ConfigurationUtilities.findRouterIndex(routerData, routerId);
                Router r = (Router)routerData.elementAt(index);
                r.recomposeRouterState((CompositeStateCommand)o);   
                routerData.set(index,r);                
            }
            catch (ConfigurationException ce) {
                OMMLogger.logger.error("Discarding Event:" + ce.getMessage());
            }
            catch (ArrayIndexOutOfBoundsException aioobe) {
                OMMLogger.logger.error("Discarding Event:" + aioobe.getMessage());
            }
        }
        
        else if (o instanceof ManagementResponse) {
            OMMLogger.logger.info("Received a network event: ManagementResponse:" + ((ManagementResponse)o).toString());  
        }
        
        else if (o instanceof NetworkException) {
            Throwable t = ((NetworkException)o).getThrowable();
            int rId = ((NetworkException)o).getRouterId();

            if (t!=null)
                t.printStackTrace();
                
            
            
            
            if ((t instanceof ManagementCommunicationException) || (t instanceof ManagementException)) {

                Throwable t1 = ((ManagementException)t).getRoot();
                if (t1 != null) {
                    OMMLogger.logger.warn(t1);
                    t1.printStackTrace();
                } else {
                    OMMLogger.logger.warn(t);
                }                
                
                try {
                    Router r = ConfigurationUtilities.findRouter(routerData, rId);
                    r.managementChannelIsConnected(false);
                    r.setRouterState(new State(State.UNKNOWN));
                }
                catch (ConfigurationException ce) {
                    OMMLogger.logger.error("Unknown router:" + ce);
                } 
            } 
            if (t instanceof ManagerException)
                ;
        }
            
        else
            OMMLogger.logger.warn("Received an unknown network event");
    }
    
    public void connectErikLink(RInterface source, RInterface destination) throws ManagerException {    
        Router srcRouter = nodeTree.getRouter(source.getRouterId());
        Router destRouter = nodeTree.getRouter(destination.getRouterId());
        AddInterface addInterfaceCommand = new AddInterface(srcRouter, destRouter, source, destination);
        String key = srcRouter.getMessagingKey();
        String[] signature = addInterfaceCommand.getSignature();
        Object[] args = addInterfaceCommand.getArgs();
        OMMLogger.logger.info("Add Interface:" + addInterfaceCommand.toString());    
        networkIO.invoke(srcRouter.getId(), srcRouter.getManagementClient(), key, addInterfaceCommand.NAME, args, signature);                 
    }     
    
    
    public void disconnectErikLink(RInterface source) {
        Router srcRouter = nodeTree.getRouter(source.getRouterId());
        RemoveInterface removeInterfaceCommand = new RemoveInterface(source);
        String key = srcRouter.getMessagingKey();
        String[] signature = removeInterfaceCommand.getSignature();
        Object[] args = removeInterfaceCommand.getArgs();
        OMMLogger.logger.info("Remove Interface:" + removeInterfaceCommand.toString());    
        networkIO.invoke(srcRouter.getId(), srcRouter.getManagementClient(), key, removeInterfaceCommand.NAME, args, signature); 
    }
    
    public void activateErikInterface(RInterface source) {
        Router srcRouter = nodeTree.getRouter(source.getRouterId());
        ActivateInterface activateInterfaceCommand = new ActivateInterface(source);
        String key = srcRouter.getMessagingKey();
        String[] signature = activateInterfaceCommand.getSignature();
        Object[] args = activateInterfaceCommand.getArgs();
        OMMLogger.logger.info("Activate Interface:" + activateInterfaceCommand.toString());    
        networkIO.invoke(srcRouter.getId(), srcRouter.getManagementClient(), key, activateInterfaceCommand.NAME, args, signature);         
    }
    
    public void deactivateErikInterface(RInterface source) {
        Router srcRouter = nodeTree.getRouter(source.getRouterId());
        DeactivateInterface deactivateInterfaceCommand = new DeactivateInterface(source);
        String key = srcRouter.getMessagingKey();
        String[] signature = deactivateInterfaceCommand.getSignature();
        Object[] args = deactivateInterfaceCommand.getArgs();
        OMMLogger.logger.info("Deactivate Interface:" + deactivateInterfaceCommand.toString());    
        networkIO.invoke(srcRouter.getId(), srcRouter.getManagementClient(), key, deactivateInterfaceCommand.NAME, args, signature);         
    }
}
