/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swingGUI;
import hotelmanager.Room;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;


/**
 *
 * @author Cmato
 */
public class RoomListModel extends DefaultListModel<Object> {

    private List<Room> rooms = new ArrayList<Room>();

    public void addRoom(Room room) {
        rooms.add(room);
        int lastRow = rooms.size() - 1;
	fireContentsChanged(room, lastRow, lastRow);
    }
    
    public void removeRoom(Room room) {
        rooms.remove(room);
        int lastRow = rooms.size() - 1;
	fireContentsChanged(room, lastRow, lastRow);
    }
    
    @Override
    public int getSize() {
	return rooms.size();
    }
    
    @Override
    public Object getElementAt(int index) {
	Room room = rooms.get(index);
	String lineFormat = room.getId()+"    |    "+room.getType()+"  [ "+room.getCapacity()+" ]";
	return lineFormat;
    }

    @Override
    public void addListDataListener(ListDataListener l) {
	super.addListDataListener(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
	super.removeListDataListener(l);
    }
    
}
