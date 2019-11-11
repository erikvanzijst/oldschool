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

import configuration.*;
import logging.OMMLogger;

/**
 *
 * @author  Laurence Crutcher
 */
public class DisconnectWindow extends JFrame implements TableModelListener { 
    Manager manager;
    Vector interfaceList = new Vector();
    Vector addressList = new Vector();
    AddressSelectorPanel sourcePanel;
    RInterface currentSourceInterface;   
    ErrorDialog errorDialog;  
    
    private static final String DISCONNECTCOMMAND = "Disconnect";
    private static final String CANCELCOMMAND = "Cancel";
           
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
    public DisconnectWindow(Manager m) {
        //Create and set up the window.
        super("Disconnect Interface");    
        
        manager = m;
        manager.addTableModelListener(this);
        setAddressList();
        
        sourcePanel = new AddressSelectorPanel("Source", addressList);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton connectButton = new JButton(DISCONNECTCOMMAND);
        connectButton.setActionCommand(DISCONNECTCOMMAND);
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (DISCONNECTCOMMAND.equals(evt.getActionCommand())) {   
                    try {
                        int sourceIndex = sourcePanel.getIndex();
                        if (sourceIndex != -1) {
                            currentSourceInterface = (RInterface)interfaceList.elementAt(sourceIndex);                        
                            manager.disconnectLink(currentSourceInterface); 
                            dispose();
                        }
                        else 
                            errorDialog = new ErrorDialog("Specify an address to disconnect"); 
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
        mainPane.add(buttonPanel, BorderLayout.PAGE_END);

       
        //Display the window.
        pack();    
        // setVisible(true); leave this to main GUI
    }
    
}
