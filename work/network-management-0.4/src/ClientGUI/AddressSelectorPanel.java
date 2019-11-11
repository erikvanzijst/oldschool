/*
 * AddressSelectorPanel.java
 *
 * Created on 13 November 2003, 14:20
 */

package ClientGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import java.beans.*;

import logging.OMMLogger;

/**
 *
 * @author  Laurence Crutcher
 */
public class AddressSelectorPanel extends JPanel implements ActionListener, AddressEventListener {
    JLabel selection;    
    String currentString;
    Vector addressList;
    protected EventListenerList listenerList = new EventListenerList();    
    int index = -1;
    private final static String ADDRESSSELECT = "Address Select"; 
     
    
    /** Creates a new instance of AddressSelectorPanel */
    public AddressSelectorPanel(String label, Vector al) {
        addressList = al;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            //Set up the UI for selecting an address.
            JLabel title = new JLabel(label, JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 14));
            title.setForeground(new Color(0x2b29ff));
            
            JLabel routerLabel1 = new JLabel("Enter the address or");
            JLabel routerLabel2 = new JLabel("select one from the list:");

            JComboBox routerSelector = new JComboBox(addressList);
            routerSelector.setActionCommand(ADDRESSSELECT);
            routerSelector.addActionListener(this);          
            routerSelector.setEditable(true);

            //Create the UI for displaying result.
            JLabel selectionLabel = new JLabel(label, JLabel.LEADING); //== LEFT
            
            selection = new JLabel(" ");
            selection.setForeground(Color.black);
            selection.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.black),
                BorderFactory.createEmptyBorder(5,5,5,5)));

            //Lay out everything.
            JPanel routerIdPanel = new JPanel();
            routerIdPanel.setLayout(new BoxLayout(routerIdPanel, BoxLayout.PAGE_AXIS));
            routerIdPanel.add(title);
            routerIdPanel.add(routerLabel1);
            routerIdPanel.add(routerLabel2);
            routerSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
            routerIdPanel.add(routerSelector);

            JPanel selectionPanel = new JPanel(new GridLayout(0, 1));
            selectionPanel.add(selectionLabel);
            selectionPanel.add(selection);

            routerIdPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            selectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            add(routerIdPanel);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(selectionPanel);        
    }
    
            /** Formats and display selected item */
         public void reformat() {
            selection.setForeground(Color.black);
            selection.setText(currentString);
            index = addressList.indexOf(currentString);
            if (index == -1) {
                selection.setForeground(Color.red);
                selection.setText("Error");            
                OMMLogger.logger.warn("illegal argument");                   
             }
            else
                addressEvent();
         }    
    
    public void actionPerformed(ActionEvent e) {
        if (ADDRESSSELECT.equals(e.getActionCommand())) {                   
            JComboBox cb = (JComboBox)e.getSource();
            currentString = (String)cb.getSelectedItem();
//            if (routerList.size() != 0)     // this test just filters out the initial rogue event from selector
            if (currentString != null) {     // user is looking at an empty list
                if (currentString.equals(""))
                    OMMLogger.logger.debug("rogue value from router selector \"" + currentString + "\"");
                else
                    reformat();    
            }
        }        
    }
    
    public int getIndex() {
        return index;
    }   
    
    
    public void addressEvent() {
        OMMLogger.logger.debug("address event");        
        Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==AddressEventListener.class) {
                ((AddressEventListener)listeners[i+1]).addressEvent();
            }	       
        }
    }
    
    public void addAddressEventListener(AddressEventListener ael) {
        listenerList.add(AddressEventListener.class, ael);
    }
    
}
