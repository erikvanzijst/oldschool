

package ClientGUI;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import configuration.*;

import logging.OMMLogger;

public class DynamicTreePanel extends DynamicTree implements DynamicTreeEventListener { 
    private int newNodeSuffix = 1000;
    private NetworkConfiguration networkConfiguration;
    protected EventListenerList listenerList = new EventListenerList(); 
    
    public DynamicTreePanel() {  
        networkConfiguration = new NetworkConfiguration();
    }
       
    
    public void initializeTree(String configFile) {
        networkConfiguration.loadFromFile(configFile);
        populateTree();
        dynamicTreeEvent();
    }
    
    // For a file merge, we just add to the configuration, and then clear and reload tree
    public void addToTree(String file) {
        networkConfiguration.loadFromFile(file);
        clearNodes();
        populateTree();
        dynamicTreeEvent();
    }    
    
    private void populateTree() {
        Vector r = networkConfiguration.getRouters();
        Iterator j = r.iterator();
        try {
            OMMLogger.logger.debug("populateTree(). There are " + r.size() + " routers");
            while (j.hasNext()) {          
                Router router = (Router)j.next();
                DefaultMutableTreeNode tp = addObject(null, new NodeView(NodeView.ROUTER, router.name(), router));                
                Vector v = router.getLinkTreeView();
                Iterator k = v.iterator();
                OMMLogger.logger.debug("populateTree(). There are " + v.size() + " links");
                while (k.hasNext()) {
                    DefaultMutableTreeNode tc = addObject(tp, k.next());
                }
            }
        }
        catch (NoSuchElementException e) {
           OMMLogger.logger.error("exception in populateTree: " + e);
        }    
    }       
     
     public void saveConfiguration(String file) {
         networkConfiguration.saveConfiguration(file);
     }
    
    public void addNode() {
         addObject(new Integer(newNodeSuffix++).toString());
    }
    
    public void removeNode() {
         removeCurrentNode();
    }
    
    public void clearNodes () {
        clear();
    }
    
    public Router getRouter(int routerId) {
        return networkConfiguration.getRouter(routerId);
    }  
    
    public Vector getConnectionSet() {
        return networkConfiguration.getConnectionSet();
    }

    public void dynamicTreeEvent() {
        OMMLogger.logger.debug("dynamic tree event");        
        Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==DynamicTreeEventListener.class) {
                ((DynamicTreeEventListener)listeners[i+1]).dynamicTreeEvent();
            }	       
        }
    }
    
    public void addDynamicTreeEventListener(DynamicTreeEventListener ael) {
        listenerList.add(DynamicTreeEventListener.class, ael);
    }    
        
}
