/*
 * ManagerTable.java
 *
 * Created on 26 November 2003, 16:00
 */

package ClientGUI;

import java.util.Vector;

import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;

import configuration.*;


/**
 *
 * @author  Laurence Crutcher
 */
public class ManagerTable extends AbstractTableModel implements TableModelListener {
    final String[] columnNames = {"Router", "Mgmt Status", "Host", "Node Status"};
    Vector routerData = new Vector();
    
    /** Creates a new instance of ManagerTable */
    public ManagerTable() {
    }
    
    public int getColumnCount() {
        return columnNames.length;        
    }

        
    public String getColumnName(int col) {        
        return columnNames[col];             
    }
    
    public int getRowCount() {
        return routerData.size();        
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        Router r = (Router)(routerData.elementAt(rowIndex));
        Object returnObject = State.UNKNOWN;
        switch (columnIndex) {
            case 0:
                returnObject = r.name(); break;
            case 1:
                returnObject = r.printManagementChannelState();
                break;
            case 2:
                returnObject = r.getHost();
                break;
            case 3:
                returnObject = r.printRouterState();
                break;
        }
        return returnObject;        
    }
    
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }
    
}
