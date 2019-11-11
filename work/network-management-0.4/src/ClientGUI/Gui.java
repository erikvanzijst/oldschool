/*
 * Application.java
 *
 * Created on 13 October 2003, 14:18
 */

/** 
 *
 * @author  Laurence Crutcher
 */
package ClientGUI;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

import configuration.*;
import logging.*;


public class Gui extends javax.swing.JFrame implements ActionListener{
    private static OMMLogger ommLogger;
    private static OMMEnv ommEnv;
    private AboutWindow aboutWindow;
    private OptionsWindow optionsWindow;
    private ConnectWindow connectWindow;
    private DisconnectWindow disconnectWindow;
    private RouterConfigureWindow routerConfigureWindow;
    private RouterResetWindow routerResetWindow;
    private InterfaceConfigureWindow interfaceConfigureWindow;
    private InterfaceResetWindow interfaceResetWindow;
    private RouterUtilizationWindow routerUtilizationWindow;
    private InterfaceUtilizationWindow interfaceUtilizationWindow;
    private Manager manager;   
    private DynamicTreePanel nodeTree;
    private InterfaceTable interfaceTable;
    private TableSorter interfaceTableSorter;
    private ConnectionTable connectionTable;
    private TableSorter connectionTableSorter;
       
    
    public Gui() {      
        super("Overlay Network Manager");     
        
        aboutWindow = new AboutWindow();
        nodeTree = new DynamicTreePanel();
        interfaceTable = new InterfaceTable(nodeTree);
        interfaceTableSorter = new TableSorter(interfaceTable); 
        connectionTable = new ConnectionTable(nodeTree);
        connectionTableSorter = new TableSorter(connectionTable);
        routerUtilizationWindow = new RouterUtilizationWindow();  
        interfaceUtilizationWindow = new InterfaceUtilizationWindow();
        manager = new Manager(nodeTree, interfaceTable, connectionTable, routerUtilizationWindow, interfaceUtilizationWindow);            
        connectWindow = new ConnectWindow(manager);      
        disconnectWindow = new DisconnectWindow(manager);       
        routerConfigureWindow = new RouterConfigureWindow(manager);
        routerResetWindow = new RouterResetWindow(manager);
        interfaceConfigureWindow = new InterfaceConfigureWindow(manager);
        interfaceResetWindow = new InterfaceResetWindow(manager);
        optionsWindow = new OptionsWindow(manager);
        
        initComponents();
    }

    
    private void readConfigurationFile() throws GUIException {
        try {
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                 File file = fc.getSelectedFile();
                 OMMLogger.logger.info("load configuration from file " + file.toString() + " selected");
                 nodeTree.initializeTree(file.toString());
            }
        }
        catch (Exception e) {
            throw new GUIException(e.getMessage());
        }
    }
    
    private void addConfigurationFile() throws GUIException {
        try {
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                 File file = fc.getSelectedFile();
                 OMMLogger.logger.info("add to configuration from file " + file.toString() + " selected");
                 nodeTree.addToTree(file.toString());
            }
        }
        catch (Exception e) {
            throw new GUIException(e.getMessage());
        }        
    }
    
    private void saveConfiguration() throws GUIException {
        try {
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                OMMLogger.logger.info("save configuration to file " + file.toString() + " selected");
                nodeTree.saveConfiguration(file.toString());            
             }
        }
        catch (Exception e) {
            throw new GUIException(e.getMessage());
        }
    }
    
    private void activateNode() {
        try {
            NodeView currentNode = nodeTree.getCurrentNode();
            if (currentNode != null) { 
                OMMLogger.logger.info("activating node " + currentNode.name());
                if (currentNode.isRouter())
                    manager.activateRouter((Router)currentNode.getUserObject());
                else {
                    if (currentNode.isInterface())
                        manager.activateInterface((RInterface)currentNode.getUserObject());
                    else
                        OMMLogger.logger.warn("unknown node type to activate" + currentNode.type());
                }
            }
        }
        catch (ManagerException me) {
            OMMLogger.logger.error("Manager Exception in node activation" + me.getMessage());
        }
    }
    
    private void suspendNode() {
        try {
            NodeView currentNode = nodeTree.getCurrentNode();
            if (currentNode != null) { 
                OMMLogger.logger.info("suspending node " + currentNode.name());
                if (currentNode.isRouter())
                    manager.deactivateRouter((Router)currentNode.getUserObject());
                else {
                    if (currentNode.isInterface())
                        manager.deactivateInterface((RInterface)currentNode.getUserObject());
                    else
                        OMMLogger.logger.warn("unknown node type to suspend" + currentNode.type());
                }
            }
        }
        catch (ManagerException me) {
            OMMLogger.logger.error("Manager Exception in node suspension" + me.getMessage());
        }
    }    
    
    private void toggleDataView() {
        if (dataViewShowing)
            topSplitPane.remove(dataPane);
        else
            topSplitPane.setLeftComponent(dataPane);
        dataViewShowing = !dataViewShowing;       
    }
    
    private void togglePaletteView() {
        if (paletteViewShowing)
            topSplitPane.remove(networkPalette);
        else
            topSplitPane.setRightComponent(networkPalette);
        paletteViewShowing = !paletteViewShowing;     
    }
    
    private void toggleLogView() {
        if (logViewShowing) 
            mainSplitPane.remove(logPane);
        else 
            mainSplitPane.setBottomComponent(logPane);
        logViewShowing = !logViewShowing;       
    }
    
    private void pauseMonitoring() {
        manager.pauseMonitoring();
        pauseMonitoringMenuItem.setEnabled(false);
        resumeMonitoringMenuItem.setEnabled(true);
    }
    
    private void resumeMonitoring() {
        manager.resumeMonitoring();
        pauseMonitoringMenuItem.setEnabled(true);
        resumeMonitoringMenuItem.setEnabled(false);        
    }
 
    
    private void initComponents() {           
        fc = new JFileChooser();        
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        fileAddMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        nodeMenu = new javax.swing.JMenu();
        addMenuItem = new javax.swing.JMenuItem();
        removeMenuItem = new javax.swing.JMenuItem();
        clearMenuItem = new javax.swing.JMenuItem();
        activateMenuItem = new javax.swing.JMenuItem();
        suspendMenuItem = new javax.swing.JMenuItem();
        linkMenu = new javax.swing.JMenu();
        connectMenuItem = new javax.swing.JMenuItem();
        disconnectMenuItem = new javax.swing.JMenuItem();
        routerMenu = new javax.swing.JMenu();
        routerConfigureMenuItem = new javax.swing.JMenuItem();
        routerResetMenuItem = new javax.swing.JMenuItem();
        interfaceMenu = new javax.swing.JMenu();
        interfaceResetMenuItem = new javax.swing.JMenuItem();
        interfaceConfigureMenuItem = new javax.swing.JMenuItem();
        performanceMenu = new javax.swing.JMenu();
        utilizationMenu = new javax.swing.JMenu();
        routerUtilizationMenuItem = new javax.swing.JMenuItem();
        interfaceUtilizationMenuItem = new javax.swing.JMenuItem();    
        viewMenu = new javax.swing.JMenu();
        dataViewMenuItem = new javax.swing.JMenuItem();
        paletteViewMenuItem = new javax.swing.JMenuItem();
        logViewMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        monitoringMenu = new javax.swing.JMenu();
        pauseMonitoringMenuItem = new javax.swing.JMenuItem();
        resumeMonitoringMenuItem = new javax.swing.JMenuItem();
        optionsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();
        
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        
        fileMenu.setText("File");
        openMenuItem.setText("Open");
        openMenuItem.setActionCommand("fileOpen");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if ("fileOpen".equals(evt.getActionCommand()))
                    try {
                        readConfigurationFile();
                    }
                    catch (GUIException e){
                        OMMLogger.logger.error("Exception in configuration from file: " + e);
                    }
            }
            });
        fileMenu.add(openMenuItem);
        
        fileAddMenuItem.setText("Merge");
        fileAddMenuItem.setActionCommand("merge");
        fileAddMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if ("merge".equals(evt.getActionCommand()))
                    try {
                        addConfigurationFile();
                    }
                    catch (GUIException e){
                        OMMLogger.logger.error("Exception in add to configuration from file: " + e);
                    }
            }
            });        
        fileMenu.add(fileAddMenuItem);    
        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);
        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if ("Save As ...".equals(evt.getActionCommand()))
                    try {
                        saveConfiguration();
                    }
                    catch (GUIException e){
                        OMMLogger.logger.error("Exception in save configuration to file: " + e);
                    }
            }
            });        
        fileMenu.add(saveAsMenuItem);
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        
        
        nodeMenu.setText("Node");
        
        addMenuItem.setText("Add");
        addMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeTree.addNode();
            }
        });
        nodeMenu.add(addMenuItem);
        
        removeMenuItem.setText("Remove");
        removeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeTree.removeNode();
            }
        });
        nodeMenu.add(removeMenuItem);
        
        clearMenuItem.setText("Clear");
        clearMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeTree.clearNodes();
            }
        });
        nodeMenu.add(clearMenuItem);
        
        activateMenuItem.setText("Activate");
        activateMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activateNode();
            }
        });
        nodeMenu.add(activateMenuItem);
        
        
        suspendMenuItem.setText("Suspend");
        suspendMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suspendNode();
            }
        });
        nodeMenu.add(suspendMenuItem);
        menuBar.add(nodeMenu);
        
        routerMenu.setText("Router");
        routerConfigureMenuItem.setText("Configure");
        routerConfigureMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routerConfigureWindow.show();
            }
        });        
        routerMenu.add(routerConfigureMenuItem);
        routerResetMenuItem.setText("Reset");
        routerResetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routerResetWindow.show();
            }
        });        
        routerMenu.add(routerResetMenuItem);
        menuBar.add(routerMenu);        

        interfaceMenu.setText("Interface");
        interfaceConfigureMenuItem.setText("Configure");
        interfaceConfigureMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interfaceConfigureWindow.show();
            }
        });        
        interfaceMenu.add(interfaceConfigureMenuItem);
        interfaceResetMenuItem.setText("Reset");
        interfaceResetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interfaceResetWindow.show();
            }
        });        
        interfaceMenu.add(interfaceResetMenuItem);
        menuBar.add(interfaceMenu);
        
        linkMenu.setText("Link");
        connectMenuItem.setText("Connect");
        connectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectWindow.show();        
            }
        });        
        linkMenu.add(connectMenuItem);
        disconnectMenuItem.setText("Disconnect");
        disconnectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectWindow.show();
            }
        });        
        linkMenu.add(disconnectMenuItem);
        menuBar.add(linkMenu);        
        
        performanceMenu.setText("Performance");
        utilizationMenu.setText("Utilization");
        routerUtilizationMenuItem.setText("Router");
        routerUtilizationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routerUtilizationWindow.show();
            }
        });  
        utilizationMenu.add(routerUtilizationMenuItem);
        interfaceUtilizationMenuItem.setText("Interface");
        interfaceUtilizationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interfaceUtilizationWindow.show();
            }
        });         
        utilizationMenu.add(interfaceUtilizationMenuItem);
        performanceMenu.add(utilizationMenu);
        menuBar.add(performanceMenu);
        
        viewMenu.setText("View");
        dataViewMenuItem.setText("Data");
        dataViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleDataView();
            }
        }); 
        viewMenu.add(dataViewMenuItem);
        paletteViewMenuItem.setText("Palette");
        paletteViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togglePaletteView();
            }
        }); 
        viewMenu.add(paletteViewMenuItem);
        logViewMenuItem.setText("Log");
        logViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleLogView();
            }
        });         
        viewMenu.add(logViewMenuItem);
        menuBar.add(viewMenu);
        
        toolsMenu.setText("Tools");
        
        monitoringMenu.setText("Monitoring");
        pauseMonitoringMenuItem.setText("Pause");
        pauseMonitoringMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseMonitoring();
            }
        });     
        monitoringMenu.add(pauseMonitoringMenuItem);
        resumeMonitoringMenuItem.setText("Resume");
        resumeMonitoringMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumeMonitoring();
            }
        });     
        resumeMonitoringMenuItem.setEnabled(false);
        monitoringMenu.add(resumeMonitoringMenuItem);        
        toolsMenu.add(monitoringMenu);
        
        optionsMenuItem.setText("Options");
        optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsWindow.show();
            }
        });        
        toolsMenu.add(optionsMenuItem);
        menuBar.add(toolsMenu);
        
        helpMenu.setText("Help");
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutWindow.show();
            }
        });
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
      
        
        JTree theTree = nodeTree.getTree();
        JScrollPane treeView = new JScrollPane(theTree);          

        JTable managerTable = new JTable(manager);
        JScrollPane managerView = new JScrollPane(managerTable);
        
        JTable interfaceTable = new JTable(interfaceTableSorter);
        interfaceTableSorter.addMouseListenerToHeaderInTable(interfaceTable); 
        JScrollPane interfaceView = new JScrollPane(interfaceTable);
        
        JTable connectionTable = new JTable(connectionTableSorter);
        connectionTableSorter.addMouseListenerToHeaderInTable(connectionTable); 
        JScrollPane connectionView = new JScrollPane(connectionTable);        
               
        networkPalette = new JScrollPane();        
        dataPane = new JTabbedPane();
        JTextArea logArea = new JTextArea();
        logPane = new JScrollPane(logArea);
        
        dataPane.addTab("Network Structure", null, treeView, "This is the network view");
        dataPane.addTab("Manager Table", null, managerView, "This is the management view");
        dataPane.addTab("Interface Table", null, interfaceView, "This is the interface view");
        dataPane.addTab("Connection Table", null, connectionView, "This is the connection view");        
        
        //Add the tabbed panes and palette to a split pane.
        topSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        topSplitPane.setLeftComponent(dataPane);
        topSplitPane.setRightComponent(networkPalette);
        
        Dimension minimumSize = new Dimension(100, 200);
        dataPane.setMinimumSize(minimumSize);
        networkPalette.setMinimumSize(minimumSize);
        topSplitPane.setDividerLocation(500); //XXX: ignored in some releases
        //of Swing. bug 4101306
        //workaround for bug 4101306:
        //treeView.setPreferredSize(new Dimension(100, 100));
        
        topSplitPane.setPreferredSize(new Dimension(1000, 600));

        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);   
        mainSplitPane.setPreferredSize(new Dimension(1000, 700));
        mainSplitPane.setDividerLocation(600);
        mainSplitPane.setTopComponent(topSplitPane);
        mainSplitPane.setBottomComponent(logPane);
        
        //Add the split pane to this frame.
        getContentPane().add(mainSplitPane, BorderLayout.CENTER);
        dataViewShowing = true;
        paletteViewShowing = true;
        logViewShowing = true;
        
        ommLogger.addWindowLogger(logArea);
        
        pack();
        setVisible(true);
    }
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        OMMLogger.logger.info("End of logging. Exit by menu.");
        System.exit(0);
    }
    
    
    private void exitForm(java.awt.event.WindowEvent evt) {
        OMMLogger.logger.info("End of logging. Exit by window close.");        
        System.exit(0);
    }
       
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        ommEnv = new OMMEnv();
        ommLogger = new OMMLogger();                
        Gui gui = new Gui();
        gui.show();
    }
    
    public void actionPerformed(ActionEvent e) {
        OMMLogger.logger.debug("actionPerformed event " + e); 
    }
    
    // Variables declaration

    private JFileChooser fc;
    private JSplitPane topSplitPane;
    private JSplitPane mainSplitPane;
    private JScrollPane logPane;
    private JTabbedPane dataPane;
    private JScrollPane networkPalette;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu nodeMenu;
    private javax.swing.JMenuItem addMenuItem;
    private javax.swing.JMenuItem removeMenuItem;
    private javax.swing.JMenuItem clearMenuItem;   
    private javax.swing.JMenuItem activateMenuItem;
    private javax.swing.JMenuItem suspendMenuItem;
    private javax.swing.JMenu routerMenu;
    private javax.swing.JMenuItem routerConfigureMenuItem;
    private javax.swing.JMenuItem routerResetMenuItem;
    private javax.swing.JMenu interfaceMenu;
    private javax.swing.JMenuItem interfaceResetMenuItem;
    private javax.swing.JMenuItem interfaceConfigureMenuItem;
    private javax.swing.JMenu linkMenu;
    private javax.swing.JMenuItem connectMenuItem;
    private javax.swing.JMenuItem disconnectMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem fileAddMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu performanceMenu;
    private javax.swing.JMenu utilizationMenu;
    private javax.swing.JMenuItem routerUtilizationMenuItem;
    private javax.swing.JMenuItem interfaceUtilizationMenuItem;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenuItem dataViewMenuItem;
    private javax.swing.JMenuItem paletteViewMenuItem;
    private javax.swing.JMenuItem logViewMenuItem;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JMenu monitoringMenu;
    private javax.swing.JMenuItem pauseMonitoringMenuItem;
    private javax.swing.JMenuItem resumeMonitoringMenuItem;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem aboutMenuItem;    
    private javax.swing.JMenuItem contentsMenuItem;    
    boolean dataViewShowing;
    boolean paletteViewShowing;
    boolean logViewShowing;
    
// End of variables declaration
}
