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

public class RouterUtilizationWindow extends JFrame {
    private DefaultCategoryGraph2DModel routerCategoryModel;  
    private JBarGraph routerBarGraph;
    private final JPanel routerBarGraphPanel=new JPanel(new JGraphLayout());    
    private JLabel noRoutersToDisplayTitle;
    private JLabel routerTitle;    
    final Font titleFont=new Font("Default",Font.BOLD,14);    
    boolean haveRouterDataToShow = false;
    String routerId[];
    double routerUtilization[];
    
    /** Creates a new instance of UtilizationWindow */
    public RouterUtilizationWindow() {  
        super("Router Utilization");           
        addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent evt) {
                     dispose();
              }
        });
        setSize(700,600);

         noRoutersToDisplayTitle=new JLabel("There are no active routers in the network at present", JLabel.CENTER);
         noRoutersToDisplayTitle.setFont(titleFont);         
         routerTitle=new JLabel("Router Utilization", JLabel.CENTER);
         routerTitle.setFont(titleFont);         
         
         if (haveRouterDataToShow)
            drawRouterGraph();
         else 
            routerBarGraphPanel.add(noRoutersToDisplayTitle, "Title");                 
                
         resetLayout();
    }
 
    
    private void resetLayout() {
        Container contentPane = getContentPane();               
        routerBarGraphPanel.revalidate();
        contentPane.add(routerBarGraphPanel);

 //       pack();  // use this to force repaint, but it might compact the window

        repaint();        
    }
    
    private void drawRouterGraph() {
        routerBarGraphPanel.removeAll();
        routerBarGraphPanel.setOpaque(true);
   //     setSize(700,600);
        routerCategoryModel = createRouterCategoryData();        
        routerBarGraph = new JBarGraph(routerCategoryModel);
  //      routerBarGraph.setBounds(10,10,600,250);
        routerBarGraph.setColor(0, Color.green);
        routerBarGraphPanel.add(routerTitle,"Title");
        routerBarGraphPanel.add(routerBarGraph,"Graph");
        routerBarGraphPanel.add(new JLabel("Utilization",JLabel.RIGHT),"Y-axis");
        routerBarGraphPanel.add(new JLabel("Router Node",JLabel.CENTER),"X-axis"); 
    }
    

    public void updatePerformance(Vector r) {
        int i = r.size();
        if (i!=0) {
            haveRouterDataToShow = true;
            routerId = new String[i];
            routerUtilization = new double[i];
            for (int j=0; j<i; j++) {
                Router router = (Router)r.elementAt(j);
                routerId[j]=router.name();
                routerUtilization[j]=(double) router.getUtilization();
            }
            routerUtilization[0] += 1; // hack because JBarGraph can't accept all zero values on Y
            drawRouterGraph();

        }
        else {
            if (haveRouterDataToShow) {
                haveRouterDataToShow = false;
                routerBarGraphPanel.removeAll();                
                routerBarGraphPanel.add(noRoutersToDisplayTitle, "Title");      
            }
        }
        resetLayout();
    }    


        private DefaultCategoryGraph2DModel createRouterCategoryData() {
                DefaultCategoryGraph2DModel model=new DefaultCategoryGraph2DModel();
                model.setCategories(routerId);
                model.addSeries(routerUtilization);
                return model;
        }     
}
