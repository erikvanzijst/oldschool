/*
 * OptionsWindow.java
 *
 * Created on 13 November 2003, 23:55
 */

package ClientGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;

import logging.OMMLogger;

/**
 *
 * @author  Laurence Crutcher
 */
public class OptionsWindow extends JFrame {
    JTextField managerPollTextField;
    JTextField performancePollTextField;
    Manager manager;
    int currentManagerPollPeriod;
    int currentPerformancePollPeriod;
    
    private static final String OKCOMMAND = "OK";
    private static final String CANCELCOMMAND = "Cancel";

    /** Creates a new instance of OptionsWindow */
    public OptionsWindow(Manager m) {
        super("Options");
        manager = m;  
        JPanel optionsPanel = new JPanel(new SpringLayout());
        JLabel managerPoll = new JLabel("Manager Poll", JLabel.TRAILING);
        optionsPanel.add(managerPoll);
        managerPollTextField = new JTextField(10);
        currentManagerPollPeriod = manager.getPollPeriod();
        managerPollTextField.setText(new Integer(currentManagerPollPeriod).toString());
        managerPoll.setLabelFor(managerPollTextField);
        optionsPanel.add(managerPollTextField);
        JLabel performancePoll = new JLabel("Performance Poll", JLabel.TRAILING);
        optionsPanel.add(performancePoll);
        performancePollTextField = new JTextField(10);
        currentPerformancePollPeriod = manager.getPerformanceUpdatePeriod();
        performancePollTextField.setText(new Integer(currentPerformancePollPeriod).toString());        
        performancePoll.setLabelFor(performancePollTextField);
        optionsPanel.add(performancePollTextField);        

        SpringUtilities.makeCompactGrid(optionsPanel,
                                        2, 2, //rows, cols
                                        6, 6,        //initX, initY
                                        6, 6);       //xPad, yPad
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton(OKCOMMAND);
        okButton.setActionCommand(OKCOMMAND);
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (OKCOMMAND.equals(evt.getActionCommand())) {   
                    Integer i = new Integer(managerPollTextField.getText());
                    Integer j = new Integer(currentManagerPollPeriod);
                    if (!(i.equals(j))) {
                        OMMLogger.logger.info("change of manager poll period from " + j + " to " + i);
                        currentManagerPollPeriod = i.intValue();
                        manager.setPollPeriod(currentManagerPollPeriod);
                    }
                    i = new Integer(performancePollTextField.getText());
                    j = new Integer(currentPerformancePollPeriod);
                    if (!(i.equals(j))) {
                        OMMLogger.logger.info("change of performance poll period from " + j + " to " + i);
                        currentPerformancePollPeriod = i.intValue();
                        manager.setPerformancePollPeriod(currentPerformancePollPeriod);
                    }
                    dispose();
                 }
            }
        });         
            
        buttonPanel.add(okButton);   
              
        JButton cancelButton = new JButton(CANCELCOMMAND);
        cancelButton.setActionCommand(CANCELCOMMAND);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        }); 
        
        buttonPanel.add(cancelButton);
        
        optionsPanel.setOpaque(true);
        buttonPanel.setOpaque(true);
               
        Container mainPane = getContentPane();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
        mainPane.add(optionsPanel, BorderLayout.PAGE_START);         
        mainPane.add(buttonPanel, BorderLayout.PAGE_END);      
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);        
        pack();
    }
    
}
