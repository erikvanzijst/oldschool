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
public class InterfaceResetWindow extends JFrame implements TableModelListener { 
    Manager manager;
    Vector interfaceList = new Vector();
    Vector addressList = new Vector();
    AddressSelectorPanel sourcePanel;
    RInterface currentSourceInterface;   
    ErrorDialog errorDialog; 
    
    private static final String RESETCOMMAND = "Reset";
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
    public InterfaceResetWindow(Manager m) {
        //Create and set up the window.
        super("Reset Interface");    
        
        manager = m;
        manager.addTableModelListener(this);
        setAddressList();
        
        sourcePanel = new AddressSelectorPanel("Source", addressList);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton connectButton = new JButton(RESETCOMMAND);
        connectButton.setActionCommand(RESETCOMMAND);
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (RESETCOMMAND.equals(evt.getActionCommand())) {   
                    try {
                        int sourceIndex = sourcePanel.getIndex();
                        if (sourceIndex != -1) {
                            currentSourceInterface = (RInterface)interfaceList.elementAt(sourceIndex);                        
                            manager.resetInterface(currentSourceInterface); 
                            dispose();
                        }
                        else 
                            errorDialog = new ErrorDialog("Specify an address to reset"); 
                    }
                    catch (ManagerException me) {
                        OMMLogger.logger.error("ManagerException on interface reset " + me.getMessage());
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
