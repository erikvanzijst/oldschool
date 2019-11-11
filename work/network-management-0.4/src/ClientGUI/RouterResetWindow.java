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
public class RouterResetWindow extends JFrame implements TableModelListener, ActionListener {
    Manager manager;
    Vector routerObjectList = new Vector();
    Vector routerList = new Vector();
    Router routerObject = null;
    AddressSelectorPanel sourcePanel;
    JPanel optionsPanel = new JPanel();
    ButtonGroup optionsGroup;
    ErrorDialog errorDialog;
        
    private static final String INTERFACECOMMAND = "Interfaces";
    private static final String ROUTINGTABLECOMMAND = "Routing Tables";
    private static final String REBOOTCOMMAND = "Reboot";
    private static final String RESETCOMMAND = "Reset";
    private static final String CANCELCOMMAND = "Cancel";        
             
    
    public void setResetOptionsPane () { 
        JRadioButton interfaceButton;
        JRadioButton routingTableButton;
        JRadioButton rebootButton;
        
        optionsPanel.setOpaque(true);         
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));   
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));             
        JLabel title = new JLabel("Reset Options", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(0x2b29ff));                              

        optionsGroup = new ButtonGroup();
        interfaceButton = new JRadioButton(INTERFACECOMMAND);
        interfaceButton.setActionCommand(INTERFACECOMMAND);
        interfaceButton.addActionListener(this);
        optionsGroup.add(interfaceButton);
        routingTableButton = new JRadioButton(ROUTINGTABLECOMMAND);
        routingTableButton.setActionCommand(ROUTINGTABLECOMMAND);
        routingTableButton.addActionListener(this);
        optionsGroup.add(routingTableButton);
        rebootButton = new JRadioButton(REBOOTCOMMAND);
        rebootButton.setActionCommand(REBOOTCOMMAND);
        rebootButton.addActionListener(this);
        optionsGroup.add(rebootButton);
            
        optionsPanel.add(title);  
        optionsPanel.add(interfaceButton);
        optionsPanel.add(routingTableButton);
        optionsPanel.add(rebootButton);       
    }    

    private void setAddressList() {
        routerList.clear();
        routerObjectList = manager.getRouterData();
        Iterator i = routerObjectList.iterator();
        while (i.hasNext()) {
            routerList.addElement(((Router)i.next()).name());
        }
    }     
    
    public RouterResetWindow(Manager m) {
        //Create and set up the window.
        super("Reset Router");    
   
        manager = m;
        manager.addTableModelListener(this);
        setAddressList();
        
        sourcePanel = new AddressSelectorPanel("Router", routerList);
        setResetOptionsPane();
       
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton connectButton = new JButton(RESETCOMMAND);
        connectButton.setActionCommand(RESETCOMMAND);
        connectButton.addActionListener(this);        
              
        JButton cancelButton = new JButton(CANCELCOMMAND);
        cancelButton.setActionCommand(CANCELCOMMAND);
        cancelButton.addActionListener(this);
       
        buttonPanel.add(connectButton);
        buttonPanel.add(cancelButton);
       
        Container mainPane = getContentPane();
        mainPane.setLayout(new BorderLayout(25, 0));
        mainPane.add(sourcePanel, BorderLayout.LINE_START);
        mainPane.add(optionsPanel, BorderLayout.LINE_END);           
        mainPane.add(buttonPanel, BorderLayout.PAGE_END);
        pack();
    }    
    
    public void tableChanged(TableModelEvent e) {
        OMMLogger.logger.debug("tableChanged " + e);
        setAddressList();        
    }
    
    public void actionPerformed(ActionEvent e) {
        String optionsCommand;
        OMMLogger.logger.debug("action performed: " + e.getActionCommand() + " ParamString: " + e.paramString() );        

        if (RESETCOMMAND.equals(e.getActionCommand())) {   
            try {
                int index = sourcePanel.getIndex();
                if (index != -1) {
                    routerObject = (Router)routerObjectList.elementAt(index);
                    ButtonModel bm = optionsGroup.getSelection();
                    if (bm != null) {
                        optionsCommand = bm.getActionCommand();
                        if (optionsCommand.equals(REBOOTCOMMAND))
                            manager.rebootRouter(routerObject);
                        if (optionsCommand.equals(INTERFACECOMMAND))
                            manager.resetAllInterface(routerObject);
                        if (optionsCommand.equals(ROUTINGTABLECOMMAND))
                            manager.resetRoutingTable(routerObject);
                        dispose(); 
                    }
                    else
                      errorDialog = new ErrorDialog("Specify a reset option");  
                }
                else
                    errorDialog = new ErrorDialog("Specify a router to reset");
            }
            catch (ManagerException me) {
                OMMLogger.logger.error("ManagerException on reset " + me.getMessage());
                errorDialog = new ErrorDialog("Operation failed. Consult the log");
                dispose();
            }
        }
        if (CANCELCOMMAND.equals(e.getActionCommand()))
            dispose();         
    }           
}
    
