/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrej
 */
public class PersonManagerImpl implements PersonManager{
    
    public static final Logger logger = Logger.getLogger(HotelManagerImpl.class.getName());
    private Connection conn;
    
    public PersonManagerImpl(Connection conn) {
        this.conn = conn;
    } 
    
    @Override
    public void createPerson(Person person) throws ServiceFailureException {
        if (person == null) {
            throw new IllegalArgumentException("person is null");            
        }
        if (person.getId() != null) {
            throw new IllegalArgumentException("person id is already set");            
        }
        if (person.getName() == null) {
            throw new IllegalArgumentException("person name is null");            
        }
        if (person.getSurname() == null) {
            throw new IllegalArgumentException("person surname is null");            
        }
        if (person.getIdCardNumber() == null) {
            throw new IllegalArgumentException("person idCardNumber is null");            
        }
        if (person.getName().equals("")) {
            throw new IllegalArgumentException("person name is empty");            
        }
        if (person.getSurname().equals("")) {
            throw new IllegalArgumentException("person surname is empty");            
        }
        if (person.getIdCardNumber().equals("")) {
            throw new IllegalArgumentException("person idCardNumber is empty");            
        }
        if (person.getEmail() == null) {
            throw new IllegalArgumentException("person emial is null");            
        }
        if (person.getMobile() == null) {
            throw new IllegalArgumentException("person mobile is null");            
        }
        
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO PERSON (name,surname,idCardNumber,email,mobile) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, person.getName());
            st.setString(2, person.getSurname());
            st.setString(3, person.getIdCardNumber());
            st.setString(4, person.getEmail());
            st.setString(5, person.getMobile());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "inserted when trying to insert person " + person);
            }            
            
            ResultSet keyRS = st.getGeneratedKeys();
            person.setId(getKey(keyRS,person));
            
        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when inserting person " + person, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private Long getKey(ResultSet keyRS, Person person) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert person " + person
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert person " + person
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert person " + person
                    + " - no key found");
        }
    }

    @Override
    public Person getPerson(Long id) throws ServiceFailureException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "SELECT id,name,surname,idCardNumber,email,mobile FROM person WHERE id = ?");
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Person person = resultSetToPerson(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + person + " and " + resultSetToPerson(rs));                    
                }  
                
                return person;
            } else {
                return null;
            }
            
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving person with id " + id, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private Person resultSetToPerson(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setName(rs.getString("name"));
        person.setSurname(rs.getString("surname"));
        person.setIdCardNumber(rs.getString("idcardnumber"));
        person.setEmail(rs.getString("emial"));
        person.setMobile(rs.getString("mobile"));
        return person;
    }

    @Override
    public void updatePerson(Person person) throws ServiceFailureException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletePerson(Person person) throws ServiceFailureException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
