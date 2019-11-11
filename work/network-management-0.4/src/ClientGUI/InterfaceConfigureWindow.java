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
import javax.swing.text.*;

import java.text.*;
import java.util.*;

import configuration.*;
import logging.OMMLogger;

/**
 *
 * @author  Laurence Crutcher
 */
public class InterfaceConfigureWindow extends JFrame implements TableModelListener, AddressEventListener {
    Manager manager;
    Vector addressList = new Vector();
    Vector interfaceList = new Vector();
    RInterface currentInterfaceObject = null;
    AddressSelectorPanel sourcePanel;
    JPanel interfaceDetailPanel = new JPanel();
    ErrorDialog errorDialog;      
    private JTextField metricTextField;
    JPanel buttonPanel = new JPanel();    
    
    public void addInterfaceDetailPanel() {
        interfaceDetailPanel.removeAll();
        JPanel interfaceOptionsPanel = new JPanel();
        interfaceOptionsPanel.setLayout(new BoxLayout(interfaceOptionsPanel, BoxLayout.PAGE_AXIS)); 
        int currentMetric;
        
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("Interface Details", JLabel.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(0x2b29ff)); 
        titlePanel.add(title);
        
        JLabel metric = new JLabel("Metric", JLabel.LEFT);
        
        metricTextField = new JTextField();     
        metricTextField.setColumns(10);
        metricTextField.setMinimumSize(new Dimension(400, 30));
        metricTextField.setMaximumSize(new Dimension(500, 30));
        currentMetric = currentInterfaceObject.getMetric();
        metricTextField.setText(new Integer(currentMetric).toString());
        
        metric.setLabelFor(metricTextField);
        
        JPanel metricPanel = new JPanel();
        metricPanel.setLayout(new BoxLayout(metricPanel, BoxLayout.LINE_AXIS));        
        metricPanel.add(metric);
        metricPanel.add(Box.createRigidArea(new Dimension(5, 0)));        
        metricPanel.add(metricTextField);      
        metricPanel.add(Box.createHorizontalGlue());
        
        interfaceOptionsPanel.add(metricPanel);

        interfaceDetailPanel.setLayout(new BoxLayout(interfaceDetailPanel, BoxLayout.PAGE_AXIS));   
        interfaceDetailPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));         
        interfaceDetailPanel.add(titlePanel);
        interfaceDetailPanel.add(interfaceOptionsPanel);
        interfaceDetailPanel.add(Box.createVerticalGlue());
         
        pack();        
    }    

    
    private void setAddressList() {
        addressList.clear();
        interfaceList = manager.getLinkData();
        Iterator i = interfaceList.iterator();
        while (i.hasNext()) {
            addressList.addElement(((RInterface)i.next()).getName());
        }
    }         
    
    public InterfaceConfigureWindow(Manager m) {
        //Create and set up the window.
        super("Configure Interface");    
   
        manager = m;
        m.addTableModelListener(this);
        setAddressList();

        //Create and set up the address panes.
        
        sourcePanel = new AddressSelectorPanel("Interface", addressList);             
        sourcePanel.addAddressEventListener(this);        

        interfaceDetailPanel.setLayout(new BoxLayout(interfaceDetailPanel, BoxLayout.PAGE_AXIS));   
        interfaceDetailPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));  
        
        JPanel buttonPanel = new JPanel();    
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton configureButton = new JButton("Configure");
        configureButton.setActionCommand("Configure");
        configureButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if ("Configure".equals(evt.getActionCommand())) {   
                    try {
                        if (currentInterfaceObject != null) {
                            Integer i = new Integer(metricTextField.getText());
                            currentInterfaceObject.setMetric(i.intValue());
                            manager.configureInterface(currentInterfaceObject);  
                            dispose();
                        }
                        else
                            errorDialog = new ErrorDialog("Specify an interface to configure");
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
        mainPane.add(interfaceDetailPanel, BorderLayout.LINE_END);           
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
            currentInterfaceObject = (RInterface)interfaceList.elementAt(index);
        addInterfaceDetailPanel();       
    }
    
}
