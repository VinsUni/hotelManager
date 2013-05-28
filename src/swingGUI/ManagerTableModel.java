/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swingGUI;

import hotelmanager.Room;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;


public class ManagerTableModel extends AbstractTableModel {
    
    private List<Room> rooms = new ArrayList<Room>();
 
    @Override
    public int getRowCount() {
        return rooms.size();
    }
    
    public List<Room> getRooms() {
        return rooms;
    }
    
    public Room getRoom(int rowIndex){
        Room room = rooms.get(rowIndex);
        return room;
    }
    
    public void removeRoom(Room room) {
        rooms.remove(room);
        int lastRow = rooms.size() - 1;
        fireTableRowsDeleted(lastRow, lastRow);
     }
     
     public void removeAllRooms(){
         rooms.clear();
         fireTableDataChanged();
     }
    
    public void addRoom(Room room) {
        rooms.add(room);
        int lastRow = rooms.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
 
    @Override
    public int getColumnCount() {
        return 5;
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Room room = rooms.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return room.getId();
            case 1:
                return room.getType();
            case 2:
                return room.getCapacity();
	    case 3:
                return new JButton("Check In");
            case 4:
                return new JButton("Check Out");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Id";
            case 1:
                return "Room type";
            case 2:
                return "Capacity";
	    case 3:
                return "Check In";
            case 4:
                return "Check Out";
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return hotelmanager.RoomType.class;
            case 2:
                return Integer.class;
	    case 3:
                return JButton.class;
            case 4:
                return JButton.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
}
