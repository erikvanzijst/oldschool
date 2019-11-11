/*
 * ConnectWindow.java
 *
 * Created on 03 November 2003, 12:48
 */

package ClientGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

import java.beans.*;

import configuration.*;
import logging.OMMLogger;

/**
 *
 * @author  Laurence Crutcher
 */
public class ConnectWindow extends JFrame implements TableModelListener { 
    Manager manager;
    Vector interfaceList = new Vector();
    Vector addressList = new Vector();
    AddressSelectorPanel sourcePanel;
    AddressSelectorPanel destinationPanel;
    RInterface currentSourceInterface;   
    RInterface currentDestinationInterface;
    ErrorDialog errorDialog;
    private final static String CONNECTCOMMAND = "Connect";
    private final static String CANCELCOMMAND = "Cancel"; 
        
    private void setAddressList() {
        addressList.clear();
        interfaceList = manager.getLinkData();
        Iterator i = interfaceList.iterator();
        while (i.hasNext()) {
            addressList.addElement(((RInterface)i.next()).getName());
        }
    }    
      
    
    public void tableChanged(TableModelEvent e) {
        OMMLogger.logger.debug("tableChanged " + e);
        setAddressList();
    }
       
    
    /** Creates a new instance of ConnectWindow */
    public ConnectWindow(Manager m) {
        super("Connect 2 Interfaces");    
        manager = m;
        m.addTableModelListener(this);
        setAddressList();

        //Create and set up the address panes.
        sourcePanel = new AddressSelectorPanel("Source", addressList);     
        
        destinationPanel = new AddressSelectorPanel("Destination", addressList);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton connectButton = new JButton(CONNECTCOMMAND);
        connectButton.setActionCommand(CONNECTCOMMAND);
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (CONNECTCOMMAND.equals(evt.getActionCommand())) {   
                    try {
                        int sourceIndex = sourcePanel.getIndex();
                        int destinationIndex = destinationPanel.getIndex();
                        if ((sourceIndex != -1) & (destinationIndex != -1)) {
                            currentSourceInterface = (RInterface)interfaceList.elementAt(sourceIndex);
                            currentDestinationInterface = (RInterface)interfaceList.elementAt(destinationIndex);
                            manager.connectLink(currentSourceInterface, currentDestinationInterface); 
                            dispose(); 
                        }
                        else       
                            errorDialog = new ErrorDialog("Specify both addressess to connect");
                    }
                    catch (ManagerException me) {
                        OMMLogger.logger.warn("ManagerException on connect " + me.getMessage());
                        errorDialog = new ErrorDialog("Operation failed. Consult the log.");
                        dispose();
                    }
                }
            }
            });         
              
        JButton cancelButton = new JButton(CANCELCOMMAND);
        cancelButton.setActionCommand(CANCELCOMMAND);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });        
        buttonPanel.add(connectButton);
        buttonPanel.add(cancelButton);

        
        Container mainPane = getContentPane();
        mainPane.setLayout(new BorderLayout(25, 0));
        mainPane.add(sourcePanel, BorderLayout.LINE_START);
        mainPane.add(destinationPanel, BorderLayout.LINE_END);
        mainPane.add(buttonPanel, BorderLayout.PAGE_END);

       
        //Display the window.
        pack();    
        // setVisible(true); leave this to main GUI
    }   
    
}
