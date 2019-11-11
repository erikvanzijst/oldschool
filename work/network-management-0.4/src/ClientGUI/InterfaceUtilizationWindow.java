/*
 * UtilizationWindow.java
 *
 * Created on 10 November 2003, 17:14
 */

package ClientGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;

import JSci.awt.*;
import JSci.swing.*;

import java.util.*;
import java.lang.reflect.Array;

import configuration.*;

/**
 *
 * @author  Laurence Crutcher
 */

public class InterfaceUtilizationWindow extends JFrame {
    private DefaultCategoryGraph2DModel interfaceCategoryModel;
    private JBarGraph interfaceBarGraph;  
    private final JPanel interfaceBarGraphPanel=new JPanel(new JGraphLayout());  
    private JLabel noInterfacesToDisplayTitle;
    private JLabel interfaceTitle;
    final Font titleFont=new Font("Default",Font.BOLD,14);    
    boolean haveInterfaceDataToShow = false;
    String interfaceId[];
    double interfaceUtilization[];
    
    /** Creates a new instance of UtilizationWindow */
    public InterfaceUtilizationWindow() {  
        super("Interface Utilization");           
        addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent evt) {
                     dispose();
              }
        });
        setSize(700,600);
        
         noInterfacesToDisplayTitle=new JLabel("There are no active interfaces in the network at present", JLabel.CENTER);
         noInterfacesToDisplayTitle.setFont(titleFont);         
         interfaceTitle=new JLabel("Interface Utilization", JLabel.CENTER);
         interfaceTitle.setFont(titleFont);             

         if (haveInterfaceDataToShow)
             drawLinkGraph();
         else 
             interfaceBarGraphPanel.add(noInterfacesToDisplayTitle, "Title");                    
                
         resetLayout();
    }
 
    
    private void resetLayout() {
        Container contentPane = getContentPane();        
        interfaceBarGraphPanel.revalidate();
        contentPane.add(interfaceBarGraphPanel);

 //       pack();  // use this to force repaint, but it might compact the window

        repaint();        
    }
    
    private void drawLinkGraph() {
        interfaceBarGraphPanel.removeAll();
        interfaceBarGraphPanel.setOpaque(true);
   //     setSize(700,600);
        interfaceCategoryModel = createInterfaceCategoryData();        

        interfaceBarGraphPanel.add(interfaceTitle,"Title");
        interfaceBarGraphPanel.add(new JBarGraph(interfaceCategoryModel),"Graph");
        interfaceBarGraphPanel.add(new JLabel("Utilization",JLabel.RIGHT),"Y-axis");
        interfaceBarGraphPanel.add(new JLabel("Interface",JLabel.CENTER),"X-axis");         
    }

    public void updatePerformance(Vector l) {
        int m = l.size();
        if (m!=0) {
            haveInterfaceDataToShow = true;
            interfaceId = new String[m];
            interfaceUtilization = new double[m];
            for (int n=0; n<m; n++) {
                RInterface rInterface = (RInterface)l.elementAt(n);
                interfaceId[n]=rInterface.getName();
                interfaceUtilization[n]=(double) rInterface.getUtilization();
            }
            interfaceUtilization[0] += 1; // hack because JBarGraph can't accept all zero values on Y
            drawLinkGraph();
        }
        else {
            if (haveInterfaceDataToShow) {
                haveInterfaceDataToShow = false;
                interfaceBarGraphPanel.removeAll();                
                interfaceBarGraphPanel.add(noInterfacesToDisplayTitle, "Title");        
            }
        }        
        resetLayout();
    }    
        
        private DefaultCategoryGraph2DModel createInterfaceCategoryData() {
                DefaultCategoryGraph2DModel model=new DefaultCategoryGraph2DModel();
                model.setCategories(interfaceId);
                model.addSeries(interfaceUtilization);
                return model;
        }        
        
}
