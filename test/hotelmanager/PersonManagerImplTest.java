/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

import common.ValidationException;
import common.IllegalEntityException;
import common.DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.dbcp.BasicDataSource;

import static org.junit.Assert.*;

/**
 *
 * @author Andrej
 */
public class PersonManagerImplTest {
    
    private PersonManagerImpl manager;
    //private Connection conn;
    private DataSource ds;

    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        //we will use in memory database
        ds.setUrl("jdbc:derby:memory:hotelmanager-test;create=true");
        return ds;
    }
    
    public PersonManagerImplTest() {
    }
    
    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,HotelManager.class.getResource("createTables.sql"));
        manager = new PersonManagerImpl();
        manager.setDataSource(ds);
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,HotelManager.class.getResource("dropTables.sql"));
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
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
    
    @Test(expected = IllegalArgumentException.class)
    public void createPersonNull() {
        manager.createPerson(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createPersonNullName() {
        Person person = newPerson(null,"Mrkvička","obc321","tel654","jozko@example.com"); 
        manager.createPerson(person);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createPersonNullSurname() {
        Person person = newPerson("Jozko",null,"obc321","tel654","jozko@example.com"); 
        manager.createPerson(person);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createPersonNullIdCardNumber() {
        Person person = newPerson("Jozko","Mrkvička",null,"tel654","jozko@example.com"); 
        manager.createPerson(person);
    }
    
    @Test
    public void addPersonWithValidAttributes() {
        // these variants should be ok
        Person person = newPerson("Jozko","Mrkvička","obc321","","jozko@example.com");
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
        Person g2 = newPerson("Michal","Chobot","prekuaz no. 4","123mobil","michal@chobot.net");
        manager.createPerson(person);
        manager.createPerson(g2);
        Long personId = person.getId();

        person.setEmail("jozinko@example.com");
        manager.updatePerson(person);
        person = manager.getPerson(personId);        
        assertEquals("Jozko", person.getName());
        assertEquals("Mrkvička", person.getSurname());
        assertEquals("obc321", person.getIdCardNumber());
        assertEquals("tel654", person.getMobile());
        assertEquals("jozinko@example.com", person.getEmail());

        person.setIdCardNumber("občan 1.");
        manager.updatePerson(person);        
        person = manager.getPerson(personId);
        assertEquals("Jozko", person.getName());
        assertEquals("Mrkvička", person.getSurname());
        assertEquals("občan 1.", person.getIdCardNumber());
        assertEquals("tel654", person.getMobile());
        assertEquals("jozinko@example.com", person.getEmail());

        person.setMobile("608telefon");
        manager.updatePerson(person);        
        person = manager.getPerson(personId);
        assertEquals("Jozko", person.getName());
        assertEquals("Mrkvička", person.getSurname());
        assertEquals("občan 1.", person.getIdCardNumber());
        assertEquals("608telefon", person.getMobile());
        assertEquals("jozinko@example.com", person.getEmail());

        person.setName("Jozefína");
        manager.updatePerson(person);        
        person = manager.getPerson(personId);
        assertEquals("Jozefína", person.getName());
        assertEquals("Mrkvička", person.getSurname());
        assertEquals("občan 1.", person.getIdCardNumber());
        assertEquals("608telefon", person.getMobile());
        assertEquals("jozinko@example.com", person.getEmail());

        person.setSurname("Mrkvičková");
        manager.updatePerson(person);       
        person = manager.getPerson(personId); 
        assertEquals("Jozefína", person.getName());
        assertEquals("Mrkvičková", person.getSurname());
        assertEquals("občan 1.", person.getIdCardNumber());
        assertEquals("608telefon", person.getMobile());
        assertEquals("jozinko@example.com", person.getEmail());

        // Check if updates didn't affected other records
        assertDeepEquals(g2, manager.getPerson(g2.getId()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updatePersonNull() {
        manager.updatePerson(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updatePersonNullId() {
        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        manager.createPerson(person);
        Long personId = person.getId();
        
        person = manager.getPerson(personId);
        person.setId(null);
        manager.updatePerson(person); 
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updatePersonWrongId() {
        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        manager.createPerson(person);
        Long personId = person.getId();
        
        person = manager.getPerson(personId);
        person.setId(personId - 1);
        manager.updatePerson(person); 
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updatePersonNullIdCardNumber() {
        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        manager.createPerson(person);
        Long personId = person.getId();
        
        person = manager.getPerson(personId);
        person.setIdCardNumber(null);
        manager.updatePerson(person); 
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updatePersonNullName() {
        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        manager.createPerson(person);
        Long personId = person.getId();
        
        person = manager.getPerson(personId);
        person.setName(null);
        manager.updatePerson(person); 
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updatePersonNullSurname() {
        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        manager.createPerson(person);
        Long personId = person.getId();
        
        person = manager.getPerson(personId);
        person.setSurname(null);
        manager.updatePerson(person); 
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
    
    @Test(expected = IllegalArgumentException.class)
    public void deletePersonNull() { 
        manager.deletePerson(null);
    }    
    
    @Test(expected = IllegalArgumentException.class)
    public void deletePersonNullId() {
        Person person = newPerson("Jozko","Mrkvička","obc321","tel654","jozko@example.com");
        manager.createPerson(person);
        Long personId = person.getId();
        
        person = manager.getPerson(personId);
        person.setId(null);
        manager.deletePerson(person);
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
    
    private void assertDeepEquals(Person expected, Person actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getIdCardNumber(), actual.getIdCardNumber());
        assertEquals(expected.getMobile(), actual.getMobile());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
    }
}