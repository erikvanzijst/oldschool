package ClientGUI;

import java.util.Vector;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.net.*;

import configuration.*;
import logging.OMMLogger;

class MyRenderer extends DefaultTreeCellRenderer {
    ImageIcon interfaceIcon;
    ImageIcon routerIcon;
    ImageIcon networkIcon;
    java.net.URL imageURL;

    public MyRenderer() {
        try {
            imageURL = new URL(OMMEnv.iconURL + "bluearrow.gif");
            interfaceIcon = new ImageIcon(imageURL);            
            imageURL = new URL(OMMEnv.iconURL + "tower01.gif");
            routerIcon = new ImageIcon(imageURL);  
            imageURL = new URL(OMMEnv.iconURL + "network1.gif");
            networkIcon = new ImageIcon(imageURL);           
        }
        catch (MalformedURLException mue) {
            OMMLogger.logger.fatal("Can't load icons " + mue.getMessage());
        }
    }

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
       
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        NodeView nodeView = (NodeView)(node.getUserObject());
        if (nodeView.isNetwork())
            setIcon(networkIcon);
        if (nodeView.isRouter())
            setIcon(routerIcon);
        if (nodeView.isInterface())
            setIcon(interfaceIcon);

        return this;
    }
}

public class DynamicTree { 
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    public DynamicTree() {      
        NodeView node = new NodeView(NodeView.NETWORK, "Network1", null);
        rootNode = new DefaultMutableTreeNode(node);
        treeModel = new DefaultTreeModel(rootNode);
    
        tree = new JTree(treeModel);
        
        tree.setCellRenderer(new MyRenderer());
        
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);      
    }
    
    public JTree getTree() {
        return tree;
    }
    
    /** Remove all nodes except the root node. */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    public NodeView getCurrentNode() {
        synchronized (rootNode) {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            return (NodeView)currentNode.getUserObject();
        }
        else
            return null;
        }
    }
    
    
    public NodeView getCurrentParent() {
        synchronized (rootNode) {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)(currentNode.getParent());
            return (NodeView)parentNode.getUserObject();
        }
        else
            return null;
        }
    }
    
    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        } 

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addObject(Object child) {
        synchronized (rootNode) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();
        NodeView newNode;
        Object newNodeObject = null;
        String nodeType=NodeView.UNKNOWN;

        try {
            if (parentPath == null) {
                parentNode = rootNode;
            } else {
                parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
            }
 
            NodeView parentNodeView = (NodeView)(parentNode.getUserObject());
            if (parentNodeView.isNetwork()) {
                nodeType = NodeView.ROUTER;
                newNodeObject = new Router(new Integer((String)child).parseInt((String)child));
            }
            else {
                if (parentNodeView.isRouter()) {
                    nodeType = NodeView.INTERFACE;
                    newNodeObject = new RInterface((String)child);
                }
                else {
                    if (parentNodeView.isInterface()) {
                        System.out.println("Can't add a node to an interface"); // should throw this
                    }
                }
            }
            if (nodeType != NodeView.UNKNOWN) {
                newNode = new NodeView(nodeType, (String)child, newNodeObject);       
                return addObject(parentNode, newNode, true);
            }
            else
                return null;
        }
        catch (Exception e) {
            OMMLogger.logger.warn("exception in addObject(): " + e);
        }
        return null;
        }
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child) {
        return addObject(parent, child, false);
    }

    public synchronized DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child, 
                                            boolean shouldBeVisible) {
        synchronized (rootNode) {
        try {                                   
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
            if (parent == null) {
                parent = rootNode;
            }
            treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
            // Make sure the user can see the new node.
            if (shouldBeVisible) {
                tree.scrollPathToVisible(new TreePath(childNode.getPath()));
            }
            return childNode;
        }
        catch (Exception e) {
            OMMLogger.logger.error("exception in addObject(DefaultMutableTreeNode, Object, boolean) " + e);
        }
       return null;
        }
    }
       
     
// filter() returns the actual routers or links in a Vector
    
     public synchronized Vector filter(String s) {
        NodeView nodeView;
        Vector v = new Vector();
            synchronized (rootNode) {
            try {
                v.clear();
                int numberRouters = treeModel.getChildCount(rootNode);
                for (int i=0; i<numberRouters; i++) {
                    Object routerNode = treeModel.getChild(rootNode, i);
                    if (s==NodeView.ROUTER) {
                        nodeView = (NodeView)((DefaultMutableTreeNode)routerNode).getUserObject();
                        v.addElement(nodeView.getUserObject());
                        OMMLogger.logger.debug("added a router in filter");
                    }
                    else {
                        if (s==NodeView.INTERFACE) {
                            int numberLinks = treeModel.getChildCount(routerNode);
                            OMMLogger.logger.debug("found " + numberLinks + " interfaces in filter");                            
                            for (int j=0; j<numberLinks; j++) {
                                Object linkNode = treeModel.getChild(routerNode, j);
                                nodeView = (NodeView)((DefaultMutableTreeNode)linkNode).getUserObject();
                                v.addElement(nodeView.getUserObject());
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                OMMLogger.logger.error("exception in filter() " + e);
            }
        return v;
            }
    }   
}
