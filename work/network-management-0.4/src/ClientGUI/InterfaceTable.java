/*
 * LinkView.java
 *
 * Created on 27 October 2003, 22:23
 */

package ClientGUI;

import java.util.Vector;
import java.util.Iterator;
import javax.swing.table.*; 
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.event.*;

import configuration.*;
import logging.OMMLogger;

/**
 *
 * @author  Laurence Crutcher
 */
public class InterfaceTable extends AbstractTableModel implements TableModelListener, DynamicTreeEventListener { 
    final String[] columnNames = {"Interface", "Status"};
    Vector data = new Vector();   
    DynamicTreePanel nodeTree;
    private static final String blankTableEntry = "--";
    
    /** Creates a new instance of LinkTable*/
    public InterfaceTable(DynamicTreePanel nodes) {
       nodeTree = nodes;
       nodeTree.addDynamicTreeEventListener(this);
       sweepNodeTree();
    }
    
    // Manager calls us with an update on 1 or more routers
    public void update(Vector v) {
        Iterator i = v.iterator();
        while (i.hasNext()) {
            Router r = (Router)i.next();       
            Vector j = r.getLinks();
            Iterator k = j.iterator();
            while (k.hasNext()) {
                RInterface rInterface = (RInterface)k.next();
                InterfaceAddress interfaceAddress = rInterface.getSrcAddress();
                int m = ConfigurationUtilities.findInterfaceIndex(data, interfaceAddress);
                if (m != -1)
                    data.set(m, rInterface);
            }
        }
        fireTableChanged(new TableModelEvent(this));        
    }
    
    public void tableChanged(TableModelEvent e) {
        OMMLogger.logger.debug("tableChanged" + e);
    }
    
    private void sweepNodeTree() {
        data.clear();
        data = this.nodeTree.filter(NodeView.INTERFACE);
        fireTableChanged(new TableModelEvent(this));
    }    
    
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public int getRowCount() {
        return data.size();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {      
        RInterface rInterface = (RInterface)data.elementAt(rowIndex);
        Object returnObject = State.UNKNOWN;
        switch (columnIndex) {
            case 0:
                returnObject = rInterface.getName(); break;
            case 1:
                returnObject = (rInterface.getState()).toString();
                break;                       
        }
        return returnObject;       
    }

    
    public void addDynamicTreeEventListener(DynamicTreeEventListener dtel) {        
    }
    
    public void dynamicTreeEvent() {
        OMMLogger.logger.debug("dynamicTreeEvent");    
        sweepNodeTree();          
    }
    
}
