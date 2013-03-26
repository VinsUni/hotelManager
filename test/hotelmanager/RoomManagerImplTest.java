/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrej
 */
public class RoomManagerImplTest {
    
    private RoomManagerImpl manager;
    private Connection conn;
    
    public RoomManagerImplTest() {
    }
    
    @Before
    public void setUp() throws SQLException {
        conn = DriverManager.getConnection("jdbc:derby:memory:HotelManagerTest;create=true");
        conn.prepareStatement("CREATE TABLE ROOM ("
                + "id bigint primary key generated always as identity,"
                + "type varchar(255) not null,"
                + "capacity int not null)").executeUpdate();
        manager = new RoomManagerImpl(conn);
    }
    
    @After
    public void tearDown() throws SQLException {
        conn.prepareStatement("DROP TABLE ROOM").executeUpdate();        
        conn.close();
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void createRoom() {
	
        Room room = newRoom(RoomType.bungalow,4);
        manager.createRoom(room);

        Long roomId = room.getId();
        assertNotNull(roomId);
        Room result = manager.getRoom(roomId);
        assertEquals(room, result);
        assertNotSame(room, result);
        assertDeepEquals(room, result);
	
    }
    
    @Test
    public void getRoom() {
        
        assertNull(manager.getRoom(1l));
        
        Room room = newRoom(RoomType.bungalow,4);
        manager.createRoom(room);
        Long roomId = room.getId();

        Room result = manager.getRoom(roomId);
        assertEquals(room, result);
        assertDeepEquals(room, result);
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createRoomNull() {
	
        manager.createRoom(null);
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createRoomNegativeCapacity() {
	
        Room room = newRoom(RoomType.bungalow,-4);
        manager.createRoom(room);
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createRoomZeroCapacity() {
	
        Room room = newRoom(RoomType.bungalow,0);
        manager.createRoom(room);
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createRoomNullType() {
	
        Room room = newRoom(null,4);
        manager.createRoom(room);
	
    }
    
    @Test
    public void updateRoom() {
        Room room = newRoom(RoomType.bungalow,4);
        Room g2 = newRoom(RoomType.apartment,2);
        manager.createRoom(room);
        manager.createRoom(g2);
        Long roomId = room.getId();

        room.setType(RoomType.apartment);
        manager.updateRoom(room);   
        room = manager.getRoom(roomId);     
        assertEquals(RoomType.apartment, room.getType());
        assertEquals(4, room.getCapacity());

        room.setCapacity(2);
        manager.updateRoom(room);    
        room = manager.getRoom(roomId);    
        assertEquals(RoomType.apartment, room.getType());
        assertEquals(2, room.getCapacity());

        // Check if updates didn't affected other records
        assertDeepEquals(g2, manager.getRoom(g2.getId()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomNull() {
	
        manager.updateRoom(null);
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomNullId() {
	
        Room room = newRoom(RoomType.bungalow,4);
        manager.createRoom(room);
        Long roomId = room.getId();
	room = manager.getRoom(roomId);
	room.setId(null);
	manager.updateRoom(room); 
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomDecreaseId() {
	
        Room room = newRoom(RoomType.bungalow,4);
        manager.createRoom(room);
        Long roomId = room.getId();
	room = manager.getRoom(roomId);
	room.setId(roomId - 1);
	manager.updateRoom(room);
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomNullType() {
	
        Room room = newRoom(RoomType.bungalow,4);
        manager.createRoom(room);
        Long roomId = room.getId();
	room = manager.getRoom(roomId);
	room.setType(null);
	manager.updateRoom(room);  
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomZeroCapacity() {
	
        Room room = newRoom(RoomType.bungalow,4);
        manager.createRoom(room);
        Long roomId = room.getId();
	room = manager.getRoom(roomId);
	room.setCapacity(0);
	manager.updateRoom(room);  
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateRoomNegativeCapacity() {
	
        Room room = newRoom(RoomType.bungalow,4);
        manager.createRoom(room);
        Long roomId = room.getId();
	room = manager.getRoom(roomId);
	room.setCapacity(-1);
	manager.updateRoom(room);  
	
    }
    
    @Test
    public void deleteRoom() {

        Room g1 = newRoom(RoomType.bungalow,4);
        Room g2 = newRoom(RoomType.apartment,2);
        manager.createRoom(g1);
        manager.createRoom(g2);
        
        assertNotNull(manager.getRoom(g1.getId()));
        assertNotNull(manager.getRoom(g2.getId()));

        manager.deleteRoom(g1);
        
        assertNull(manager.getRoom(g1.getId()));
        assertNotNull(manager.getRoom(g2.getId()));
                
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deleteRoomNull() {
	
	manager.deleteRoom(null);
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deleteRoomNullId() {
	
        Room room = newRoom(RoomType.bungalow,4); 
	manager.createRoom(room);
        Long roomId = room.getId();
        room = manager.getRoom(roomId);
	room.setId(null);
	manager.deleteRoom(room);
	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deleteRoomWrongId() {
	
        Room room = newRoom(RoomType.bungalow,4); 
	manager.createRoom(room);
        Long roomId = room.getId();
        room = manager.getRoom(roomId);
	room.setId(1l);
	manager.deleteRoom(room);
	
    }
    
    private static Room newRoom(RoomType type, int capacity) {
        
        Room room = new Room();
        room.setType(type);
        room.setCapacity(capacity);
        return room;
        
    }
    
    private void assertDeepEquals(Room expected, Room actual) {
        
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getCapacity(), actual.getCapacity());
        
    }
}