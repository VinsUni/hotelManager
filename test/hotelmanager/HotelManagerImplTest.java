package hotelmanager;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.*;
import static org.junit.Assert.*;
import java.lang.Enum;

/**
 *
 * @author Cmato a Andrej
 */
public class HotelManagerImplTest {
    
    private HotelManagerImpl manager;
    
    public HotelManagerImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        manager = new HotelManagerImpl();
    }
    
    @Test
    public void createPerson() {
        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        manager.createPerson(person);

        Long personId = person.getId();
        assertNotNull(personId);
        Person result = manager.getPerson(personId);
        assertEquals(person, result);
        assertNotSame(person, result);
        assertDeepEquals(person, result);
    }
    
    @Test
    public void getPerson() {
        
        assertNull(manager.getPerson(1l));
        
        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        manager.createPerson(person);
        Long personId = person.getId();

        Person result = manager.getPerson(personId);
        assertEquals(person, result);
        assertDeepEquals(person, result);
    }
    
    @Test
    public void addPersonWithWrongAttributes() {

        try {
            manager.createPerson(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        person.setId(1l);
        try {
            manager.createPerson(person);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        person = newPerson(null,"Mrkvička","obc321","tel654","jozko@example.com"); 
        try {
            manager.createPerson(person);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        person = newPerson("Jozko",null,"obc321","tel654","jozko@example.com"); 
        try {
            manager.createPerson(person);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        person = newPerson("Jozko","Mrkvička",null,"tel654","jozko@example.com"); 
        try {
            manager.createPerson(person);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        // these variants should be ok
        person = newPerson("Jozko","Mrkvička","obc321","","jozko@example.com");
        manager.createPerson(person);
        Person result = manager.getPerson(person.getId()); 
        assertNotNull(result);

        person = newPerson("Jozko","Mrkvička","obc321","tel654","");
        manager.createPerson(person);
        result = manager.getPerson(person.getId()); 
        assertNotNull(result);

        person = newPerson("Jozko","Mrkvička","obc321",null,"jozko@example.com");
        manager.createPerson(person);
        result = manager.getPerson(person.getId()); 
        assertNotNull(result);
        assertNull(result.getMobile());

        person = newPerson("Jozko","Mrkvička","obc321","tel654",null);
        manager.createPerson(person);
        result = manager.getPerson(person.getId()); 
        assertNotNull(result);
        assertNull(result.getEmail());

    }
    
    @Test
    public void updatePerson() {
        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        Person g2 = newPerson("Jozefína","Mrkvičková","občan 1.","608telefon","jozinko@example.com");
        manager.createPerson(person);
        manager.createPerson(g2);
        Long personId = person.getId();

        person = manager.getPerson(personId);
        person.setEmail("jozinko@example.com");
        manager.updatePerson(person);        
        assertEquals("Jozko", person.getName());
        assertEquals("Mrkvička", person.getSurname());
        assertEquals("obc321", person.getIdCardNumber());
        assertEquals("tel654", person.getMobile());
        assertEquals("jozinko@example.com", person.getEmail());

        person = manager.getPerson(personId);
        person.setIdCardNumber("občan 1.");
        manager.updatePerson(person);        
        assertEquals("Jozko", person.getName());
        assertEquals("Mrkvička", person.getSurname());
        assertEquals("občan 1.", person.getIdCardNumber());
        assertEquals("tel654", person.getMobile());
        assertEquals("jozinko@example.com", person.getEmail());

        person = manager.getPerson(personId);
        person.setMobile("608telefon");
        manager.updatePerson(person);        
        assertEquals("Jozko", person.getName());
        assertEquals("Mrkvička", person.getSurname());
        assertEquals("občan 1.", person.getIdCardNumber());
        assertEquals("608telefon", person.getMobile());
        assertEquals("jozinko@example.com", person.getEmail());

        person = manager.getPerson(personId);
        person.setName("Jozefína");
        manager.updatePerson(person);        
        assertEquals("Jozefína", person.getName());
        assertEquals("Mrkvička", person.getSurname());
        assertEquals("občan 1.", person.getIdCardNumber());
        assertEquals("608telefon", person.getMobile());
        assertEquals("jozinko@example.com", person.getEmail());

        person = manager.getPerson(personId);
        person.setSurname("Mrkvičková");
        manager.updatePerson(person);        
        assertEquals("Jozefína", person.getName());
        assertEquals("Mrkvičková", person.getSurname());
        assertEquals("občan 1.", person.getIdCardNumber());
        assertEquals("608telefon", person.getMobile());
        assertEquals("jozinko@example.com", person.getEmail());

        // Check if updates didn't affected other records
        assertDeepEquals(g2, manager.getPerson(g2.getId()));
    }
    
    @Test
    public void updatePersonWithWrongAttributes() {

        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        manager.createPerson(person);
        Long personId = person.getId();
        
        try {
            manager.updatePerson(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
        
        try {
            person = manager.getPerson(personId);
            person.setId(null);
            manager.updatePerson(person);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            person = manager.getPerson(personId);
            person.setId(personId - 1);
            manager.updatePerson(person);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            person = manager.getPerson(personId);
            person.setIdCardNumber(null);
            manager.updatePerson(person);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            person = manager.getPerson(personId);
            person.setName(null);
            manager.updatePerson(person);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            person = manager.getPerson(personId);
            person.setSurname(null);
            manager.updatePerson(person);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            person = manager.getPerson(personId);
            person.setIdCardNumber("");
            manager.updatePerson(person);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            person = manager.getPerson(personId);
            person.setName("");
            manager.updatePerson(person);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            person = manager.getPerson(personId);
            person.setSurname("");
            manager.updatePerson(person);        
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }
    
    @Test
    public void deletePerson() {

        Person g1 = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        Person g2 = newPerson("Jozefína","Mrkvičková","občan 1.","608telefon","jozinko@example.com");
        manager.createPerson(g1);
        manager.createPerson(g2);
        
        assertNotNull(manager.getPerson(g1.getId()));
        assertNotNull(manager.getPerson(g2.getId()));

        manager.deletePerson(g1);
        
        assertNull(manager.getPerson(g1.getId()));
        assertNotNull(manager.getPerson(g2.getId()));
                
    }
    
    
    @Test
    public void deletePersonWithWrongAttributes() {

        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        
        try {
            manager.deletePerson(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            person.setId(null);
            manager.deletePerson(person);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            person.setId(1l);
            manager.deletePerson(person);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }        

    }
    
    //<---------------------------------------SART ROOM TEST--------------------------------------------->

    @Test
    public void createRoom() {
        Room room = newRoom(RoomType.bungalow,4);
        manager.createRoom(room);

        Long roomId = room.getId();
        assertNotNull(roomId);
        Room result = manager.getRoom(roomId);
        assertEquals(room, result);
        assertNotSame(room, result);
        assertDeepEqualRoom(room, result);
    }
    
    @Test
    public void getRoom() {
        
        assertNull(manager.getRoom(1l));
        
        Room room = newRoom(RoomType.bungalow,4);
        manager.createRoom(room);
        Long roomId = room.getId();

        Room result = manager.getRoom(roomId);
        assertEquals(room, result);
        assertDeepEqualRoom(room, result);
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

        room = newRoom(RoomType.bungalow,4); 
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

        room = manager.getRoom(roomId);
        room.setType(RoomType.apartment);
        manager.updateRoom(room);        
        assertEquals(RoomType.apartment, room.getType());
        assertEquals(4, room.getCapacity());

        room = manager.getRoom(roomId);
        room.setCapacity(2);
        manager.updateRoom(room);        
        assertEquals(RoomType.apartment, room.getType());
        assertEquals(2, room.getCapacity());

        // Check if updates didn't affected other records
        assertDeepEqualRoom(g2, manager.getRoom(g2.getId()));
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
    
    //<---------------------------------------END ROOM TEST--------------------------------------------->
    
    @After
    public void tearDown() {
    }
    
    private static Person newPerson(String name, String surname, String idCardNumber, String mobile, String email) {
        Person person = new Person();
        person.setEmail(email);
        person.setIdCardNumber(idCardNumber);
        person.setMobile(mobile);
        person.setName(name);
        person.setSurname(surname);
        return person;
    }
    
    private static Room newRoom(RoomType type, int capacity) {
        Room room = new Room();
        room.setType(type);
        room.setCapacity(capacity);
        return room;
    }
    
    private void assertDeepEquals(Person expected, Person actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getIdCardNumber(), actual.getIdCardNumber());
        assertEquals(expected.getMobile(), actual.getMobile());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
    }
    
    private void assertDeepEqualRoom(Room expected, Room actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getCapacity(), actual.getCapacity());
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
