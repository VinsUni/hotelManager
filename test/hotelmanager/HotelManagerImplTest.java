package hotelmanager;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Cmato
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
