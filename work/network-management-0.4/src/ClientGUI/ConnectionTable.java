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
public class ConnectionTable extends AbstractTableModel implements TableModelListener, DynamicTreeEventListener { 
    final String[] columnNames = {"Src Router", "Src Interface", "Dest Router", "Dest Interface", "Status", "Up Time"};
    Vector data = new Vector();   
    DynamicTreePanel nodeTree;
    private static final String blankTableEntry = "   --   ";
    
    /** Creates a new instance of LinkTable*/
    public ConnectionTable(DynamicTreePanel nodes) {
       nodeTree = nodes;
       nodeTree.addDynamicTreeEventListener(this);
       sweepNodeTree();
    }
    
    // Manager calls us with an update on 1 or more connections
    public void update() {
        fireTableChanged(new TableModelEvent(this));        
    }
    
    public void tableChanged(TableModelEvent e) {
        OMMLogger.logger.debug("tableChanged" + e);
    }
    
    private void sweepNodeTree() {
        data = this.nodeTree.getConnectionSet();
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
        Connection connection = (Connection)data.elementAt(rowIndex);
        State state = connection.getState();
        Object returnObject = State.UNKNOWN;
        switch (columnIndex) {
            case 0:
                returnObject = (new Integer(connection.getSrcRouter())).toString();
                break;
            case 1:
                returnObject = (connection.getSrcAddress()).toString();
                break;  
            case 2:
                returnObject = (new Integer(connection.getDestRouter())).toString();
                break;
            case 3:
                returnObject = (connection.getDestAddress()).toString();
                break;         
            case 4:
                returnObject = state.toString();
                break;
            case 5:
                if (state.isConnected())
                    returnObject = (new Long(connection.getUpTime())).toString();
                else
                    returnObject = blankTableEntry;
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
