/*
 * ErrorDialog.java
 *
 * Created on 10 November 2003, 11:28
 */

package ClientGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author  Laurence Crutcher
 */

 public class ErrorDialog extends JDialog {
    /** Creates a new instance of ErrorDialog */

    public ErrorDialog(String bodyMessage) {  
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JOptionPane pane = new JOptionPane();
        pane.showMessageDialog(null, bodyMessage);
        setContentPane(pane);    
    }
}
