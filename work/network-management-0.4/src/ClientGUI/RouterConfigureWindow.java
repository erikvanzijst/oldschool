/*
 * RouterConfigureWindow.java
 *
 * Created on 07 November 2003, 10:32
 */

package ClientGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

import configuration.*;
import logging.OMMLogger;

/**
 *
 * @author  Laurence Crutcher
 */
public class RouterConfigureWindow extends JFrame implements TableModelListener, AddressEventListener {
    Manager manager;
    Vector linkList = new Vector();
    Vector routerList = new Vector();
    Router currentRouterObject = null;
    AddressSelectorPanel sourcePanel;
    JPanel routerDetailPanel = new JPanel();
    ErrorDialog errorDialog;      
    
    public void addRouterDetailPanel() {
        Vector links;
        JPanel interfaceSetPanel = new JPanel();
        interfaceSetPanel.setLayout(new BoxLayout(interfaceSetPanel, BoxLayout.PAGE_AXIS));                  
        
        routerDetailPanel.removeAll();        
        if (currentRouterObject != null) {
            links = currentRouterObject.getLinks();

            JLabel title = new JLabel("Router Details", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 14));
            title.setForeground(new Color(0x2b29ff));                              
  
            Iterator i = links.iterator();
            while (i.hasNext()) {               
                JPanel interfacePanel = new JPanel();         
                interfacePanel.setLayout(new BoxLayout(interfacePanel, BoxLayout.LINE_AXIS));
                JLabel interfaceLabel = new JLabel("Interface", JLabel.LEADING); 
                JLabel iface = new JLabel(((RInterface)i.next()).getName());
                iface.setForeground(Color.black);
                iface.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.black),
                    BorderFactory.createEmptyBorder(5,5,5,5)));                
                interfacePanel.add(interfaceLabel);
                interfacePanel.add(Box.createRigidArea(new Dimension(5, 0)));                
                interfacePanel.add(iface);     
                interfaceSetPanel.add(interfacePanel);
                interfaceSetPanel.add(Box.createRigidArea(new Dimension(0, 5)));                 
            }
            
            routerDetailPanel.add(title);  
            routerDetailPanel.add(interfaceSetPanel);
        }
        pack();        
    }    

    private void setAddressList() {
        routerList.clear();
        linkList = manager.getRouterData();
        Iterator i = linkList.iterator();
        while (i.hasNext()) {
            routerList.addElement(((Router)i.next()).name());
        }
    }     
    
    public RouterConfigureWindow(Manager m) {
        //Create and set up the window.
        super("Configure Router");    
   
        manager = m;
        manager.addTableModelListener(this);
        setAddressList();

        //Create and set up the address panes.
        
        sourcePanel = new AddressSelectorPanel("Router", routerList);             
        sourcePanel.addAddressEventListener(this);        

        routerDetailPanel.setLayout(new BoxLayout(routerDetailPanel, BoxLayout.PAGE_AXIS));   
        routerDetailPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));  
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton configureButton = new JButton("Configure");
        configureButton.setActionCommand("Configure");
        configureButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if ("Configure".equals(evt.getActionCommand())) {   
                    try {
                        if (currentRouterObject != null) {
                            manager.configureRouter(currentRouterObject);  
                            dispose();
                        }
                        else
                            errorDialog = new ErrorDialog("Specify a router to configure");
                    }
                    catch (ManagerException me) {
                        OMMLogger.logger.error("ManagerException on configure " + me.getMessage());
                        errorDialog = new ErrorDialog("Operation failed. Consult the log");
                        dispose();
                    }
                }
            }
            });         
              
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });        
        buttonPanel.add(configureButton);
        buttonPanel.add(cancelButton);

        
        Container mainPane = getContentPane();
        mainPane.setLayout(new BorderLayout(25, 0));
        mainPane.add(sourcePanel, BorderLayout.LINE_START);
        mainPane.add(routerDetailPanel, BorderLayout.LINE_END);           
        mainPane.add(buttonPanel, BorderLayout.PAGE_END);
        pack();
    }    
    
    public void tableChanged(TableModelEvent e) {
        OMMLogger.logger.debug("tableChanged " + e);
        setAddressList();        
    }
    
    public void addAddressEventListener(AddressEventListener ael) {
    }
    
    public void addressEvent() {
        int index = sourcePanel.getIndex();
        if (index != -1)
            currentRouterObject = (Router)linkList.elementAt(index);
        addRouterDetailPanel();
    } 
    
}
