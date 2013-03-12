package hotelmanager;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.*;
import static org.junit.Assert.*;

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
    
    private void assertDeepEquals(Person expected, Person actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getIdCardNumber(), actual.getIdCardNumber());
        assertEquals(expected.getMobile(), actual.getMobile());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
