package hotelmanager;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import common.DBUtils;
import common.IllegalEntityException;
import static hotelmanager.PersonManagerImplTest.assertPersonCollectionDeepEquals;
import static hotelmanager.PersonManagerImplTest.newPerson;
import static hotelmanager.RoomManagerImplTest.assertRoomDeepEquals;
import static hotelmanager.RoomManagerImplTest.newRoom;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Cmato a Andrej
 */
public class HotelManagerImplTest {
    
    private HotelManagerImpl manager;
    private PersonManagerImpl personManager;
    private RoomManagerImpl roomManager;
    private DataSource ds;
    
    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        //we will use in memory database
        ds.setUrl("jdbc:derby:memory:roommgr-test;create=true");
        //ds.setUrl("jdbc:derby://localhost:1527/test");
        return ds;
    }

    private Room r1, r2, r3, roomWithNullId, roomNotInDB;
    private Person p1, p2, p3, p4, p5, personWithNullId, personNotInDB;
    
    @Test
    public void findRoomWithPerson() {
        
        assertNull(manager.findRoomWithPerson(p1));
        assertNull(manager.findRoomWithPerson(p2));
        assertNull(manager.findRoomWithPerson(p3));
        assertNull(manager.findRoomWithPerson(p4));
        assertNull(manager.findRoomWithPerson(p5));
        
        manager.checkIn(p1, r3);

        assertEquals(r3, manager.findRoomWithPerson(p1));
        assertRoomDeepEquals(r3, manager.findRoomWithPerson(p1));
        assertNull(manager.findRoomWithPerson(p2));
        assertNull(manager.findRoomWithPerson(p3));
        assertNull(manager.findRoomWithPerson(p4));
        assertNull(manager.findRoomWithPerson(p5));
        
        try {
            manager.findRoomWithPerson(null);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        try {
            manager.findRoomWithPerson(personWithNullId);
            fail();
        } catch (IllegalEntityException ex) {}
        
    }
    
    private void prepareTestData() {

        r1 = newRoom(RoomType.apartment, 1);
        r2 = newRoom(RoomType.apartment, 2);
        r3 = newRoom(RoomType.bungalow, 3);
        
        p1 = newPerson("Jozko1","Mrkvička1","obc3211","tel6541","jozko@example.com1");
        p2 = newPerson("Jozko2","Mrkvička2","obc3212","tel6542","jozko@example.com2");
        p3 = newPerson("Jozko3","Mrkvička3","obc3213","tel6543","jozko@example.com3");
        p4 = newPerson("Jozko4","Mrkvička4","obc3214","tel6544","jozko@example.com4");
        p5 = newPerson("Jozko5","Mrkvička5","obc3215","tel6545","jozko@example.com5");
        
        personManager.createPerson(p1);
        personManager.createPerson(p2);
        personManager.createPerson(p3);
        personManager.createPerson(p4);
        personManager.createPerson(p5);
        
        roomManager.createRoom(r1);
        roomManager.createRoom(r2);
        roomManager.createRoom(r3);

        roomWithNullId = newRoom(RoomType.apartment, 1);
        roomNotInDB = newRoom(RoomType.apartment, 1);
        roomNotInDB.setId(r3.getId() + 100);
        personWithNullId = newPerson("Jozko null","Mrkvička null","obc321 null","tel654 null","jozko@example.com null");
        personNotInDB = newPerson("Jozko not in db","Mrkvička not in db","obc321 not in db","tel654 not in db","jozko@example.com not in db");
        personNotInDB.setId(p5.getId() + 100);
        
    }
    
    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds, RoomManager.class.getResource("createTables.sql"));
        manager = new HotelManagerImpl();
        manager.setDataSource(ds);
        personManager = new PersonManagerImpl();
        personManager.setDataSource(ds);
        roomManager = new RoomManagerImpl();
        roomManager.setDataSource(ds);
        prepareTestData();
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds, RoomManager.class.getResource("dropTables.sql"));
    }
    
    @Test
    public void checkIn() {

        assertNull(manager.findRoomWithPerson(p1));
        assertNull(manager.findRoomWithPerson(p2));
        assertNull(manager.findRoomWithPerson(p3));
        assertNull(manager.findRoomWithPerson(p4));
        assertNull(manager.findRoomWithPerson(p5));
        
        manager.checkIn(p1, r3);
        manager.checkIn(p5, r1);
        manager.checkIn(p3, r3);

        List<Person> personsInRoom1 = Arrays.asList(p5);
        List<Person> personsInRoom2 = Collections.emptyList();
        List<Person> personsInRoom3 = Arrays.asList(p1,p3);
        
        assertPersonCollectionDeepEquals(personsInRoom1, manager.findPersonsInRoom(r1));
        assertPersonCollectionDeepEquals(personsInRoom2, manager.findPersonsInRoom(r2));
        assertPersonCollectionDeepEquals(personsInRoom3, manager.findPersonsInRoom(r3));
        
        assertEquals(r3, manager.findRoomWithPerson(p1));
        assertRoomDeepEquals(r3, manager.findRoomWithPerson(p1));
        assertNull(manager.findRoomWithPerson(p2));
        assertEquals(r3, manager.findRoomWithPerson(p3));
        assertRoomDeepEquals(r3, manager.findRoomWithPerson(p3));
        assertNull(manager.findRoomWithPerson(p4));
        assertEquals(r1, manager.findRoomWithPerson(p5));
        assertRoomDeepEquals(r1, manager.findRoomWithPerson(p5));
    
        try {
            manager.checkIn(p1, r3);
            fail();
        } catch (IllegalEntityException ex) {}

        try {
            manager.checkIn(p1, r2);
            fail();
        } catch (IllegalEntityException ex) {}

        try {
            manager.checkIn(null, r2);
            fail();
        } catch (IllegalArgumentException ex) {}

        try {
            manager.checkIn(personWithNullId, r2);
            fail();
        } catch (IllegalEntityException ex) {}

        try {
            manager.checkIn(personNotInDB, r2);
            fail();
        } catch (IllegalEntityException ex) {}

        try {
            manager.checkIn(p2, null);
            fail();
        } catch (IllegalArgumentException ex) {}

        try {
            manager.checkIn(p2, roomWithNullId);
            fail();
        } catch (IllegalEntityException ex) {}

        try {
            manager.checkIn(p2, roomNotInDB);
            fail();
        } catch (IllegalEntityException ex) {}

        // Try to add person to room that is already full
        try {
            manager.checkIn(p2, r1);
            fail();
        } catch (IllegalEntityException ex) {}

        // Check that previous tests didn't affect data in database
        assertPersonCollectionDeepEquals(personsInRoom1, manager.findPersonsInRoom(r1));
        assertPersonCollectionDeepEquals(personsInRoom2, manager.findPersonsInRoom(r2));
        assertPersonCollectionDeepEquals(personsInRoom3, manager.findPersonsInRoom(r3));

        assertEquals(r3, manager.findRoomWithPerson(p1));
        assertNull(manager.findRoomWithPerson(p2));
        assertEquals(r3, manager.findRoomWithPerson(p3));
        assertNull(manager.findRoomWithPerson(p4));
        assertEquals(r1, manager.findRoomWithPerson(p5));        
    }

    @Test
    public void checkOut() {

        manager.checkIn(p1, r3);
        manager.checkIn(p3, r3);
        manager.checkIn(p4, r3);
        manager.checkIn(p5, r1);
        
        assertEquals(r3, manager.findRoomWithPerson(p1));
        assertNull(manager.findRoomWithPerson(p2));
        assertEquals(r3, manager.findRoomWithPerson(p3));
        assertEquals(r3, manager.findRoomWithPerson(p4));
        assertEquals(r1, manager.findRoomWithPerson(p5));

        manager.checkOut(p3, r3);

        List<Person> bodiesInRoom1 = Arrays.asList(p5);
        List<Person> bodiesInRoom2 = Collections.emptyList();
        List<Person> bodiesInRoom3 = Arrays.asList(p1,p4);
        
        assertPersonCollectionDeepEquals(bodiesInRoom1, manager.findPersonsInRoom(r1));
        assertPersonCollectionDeepEquals(bodiesInRoom2, manager.findPersonsInRoom(r2));
        assertPersonCollectionDeepEquals(bodiesInRoom3, manager.findPersonsInRoom(r3));

        assertEquals(r3, manager.findRoomWithPerson(p1));
        assertNull(manager.findRoomWithPerson(p2));
        assertNull(manager.findRoomWithPerson(p3));
        assertEquals(r3, manager.findRoomWithPerson(p4));
        assertEquals(r1, manager.findRoomWithPerson(p5));
                
        try {
            manager.checkOut(p3, r1);
            fail();
        } catch (IllegalEntityException ex) {}

        try {
            manager.checkOut(p1, r1);
            fail();
        } catch (IllegalEntityException ex) {}
        
        try {
            manager.checkOut(null, r2);
            fail();
        } catch (IllegalArgumentException ex) {}

        try {
            manager.checkOut(personWithNullId, r2);
            fail();
        } catch (IllegalEntityException ex) {}

        try {
            manager.checkOut(personNotInDB, r2);
            fail();
        } catch (IllegalEntityException ex) {}

        try {
            manager.checkOut(p2, null);
            fail();
        } catch (IllegalArgumentException ex) {}

        try {
            manager.checkOut(p2, roomWithNullId);
            fail();
        } catch (IllegalEntityException ex) {}

        try {
            manager.checkOut(p2, roomNotInDB);
            fail();
        } catch (IllegalEntityException ex) {}
    
        // Check that previous tests didn't affect data in database
        assertPersonCollectionDeepEquals(bodiesInRoom1, manager.findPersonsInRoom(r1));
        assertPersonCollectionDeepEquals(bodiesInRoom2, manager.findPersonsInRoom(r2));
        assertPersonCollectionDeepEquals(bodiesInRoom3, manager.findPersonsInRoom(r3));

        assertEquals(r3, manager.findRoomWithPerson(p1));
        assertNull(manager.findRoomWithPerson(p2));
        assertNull(manager.findRoomWithPerson(p3));
        assertEquals(r3, manager.findRoomWithPerson(p4));
        assertEquals(r1, manager.findRoomWithPerson(p5));

    }

    
    
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
