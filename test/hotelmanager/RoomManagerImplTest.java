/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

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
    
    public RoomManagerImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        manager = new RoomManagerImpl();
    }
    
    @After
    public void tearDown() {
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
    
    @Test
    public void addRoomWithWrongAttributes() {

        try {
            manager.createRoom(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        Room room = newRoom(RoomType.bungalow,4);
        room.setId(1l);
        try {
            manager.createRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        room = newRoom(RoomType.bungalow,-4);
        try {
            manager.createRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        room = newRoom(RoomType.bungalow,0);
        try {
            manager.createRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        room = newRoom(null,4); 
        try {
            manager.createRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        // these variants should be ok
        room = newRoom(RoomType.apartment,2);
        manager.createRoom(room);
        Room result = manager.getRoom(room.getId()); 
        assertNotNull(result);

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
    
    @Test
    public void updateRoomWithWrongAttributes() {

        Room room = newRoom(RoomType.bungalow,4);
        manager.createRoom(room);
        Long roomId = room.getId();
        
        try {
            manager.updateRoom(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
        
        try {
            room = manager.getRoom(roomId);
            room.setId(null);
            manager.updateRoom(room);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoom(roomId);
            room.setId(roomId - 1);
            manager.updateRoom(room);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoom(roomId);
            room.setType(null);
            manager.updateRoom(room);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoom(roomId);
            room.setCapacity(0);
            manager.updateRoom(room);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoom(roomId);
            room.setCapacity(-1);
            manager.updateRoom(room);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

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
    
    
    @Test
    public void deleteRoomWithWrongAttributes() {

        Room room = newRoom(RoomType.bungalow,4);
        
        try {
            manager.deleteRoom(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room.setId(null);
            manager.deleteRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room.setId(1l);
            manager.deleteRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }        

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