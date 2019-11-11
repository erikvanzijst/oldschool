/*
 * AboutWindow.java
 *
 * Created on 03 November 2003, 12:55
 */
 
package ClientGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
/**
 *
 * @author  Laurence Crutcher
 */
public class AboutWindow extends JFrame {
    
    /** Creates a new instance of AboutWindow */
    public AboutWindow() {
        super("About");
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(200,200));
        final JLabel label = new JLabel("<html>Overlay Manager<br>Version 0.1<br>Author: Laurence Crutcher</html>");
        p.add(label, BorderLayout.CENTER);
        getContentPane().add(p, BorderLayout.CENTER);        
        pack();
//      setVisible(true);        
    }
    
}
