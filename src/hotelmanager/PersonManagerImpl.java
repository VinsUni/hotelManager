/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

import common.ServiceFailureException;
import common.ValidationException;
import common.IllegalEntityException;
import common.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Andrej
 */
public class PersonManagerImpl implements PersonManager{
    
    public static final Logger logger = Logger.getLogger(HotelManagerImpl.class.getName());
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }
    
    @Override
    public void createPerson(Person person) throws ServiceFailureException {
        checkDataSource();
        validate(person);
        if (person.getId() != null) {
            throw new IllegalArgumentException("person id is already set");
        }       
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO PERSON (name,surname,idCardNumber,email,mobile) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, person.getName());
            st.setString(2, person.getSurname());
            st.setString(3, person.getIdCardNumber());
            st.setString(4, person.getEmail());
            st.setString(5, person.getMobile());
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, person, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            person.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting grave into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    @Override
    public Person getPerson(Long id) throws ServiceFailureException {

        checkDataSource();
        
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,name,surname,idCardNumber,email,mobile FROM person WHERE id = ?");
            st.setLong(1, id);
            return executeQueryForSinglePerson(st);
        } catch (SQLException ex) {
            String msg = "Error when getting person with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    static Person executeQueryForSinglePerson(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Person result = rowToPerson(rs);                
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more bodies with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }
    
    static private Person rowToPerson(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setName(rs.getString("name"));
        person.setSurname(rs.getString("surname"));
        person.setIdCardNumber(rs.getString("idcardnumber"));
        person.setEmail(rs.getString("email"));
        person.setMobile(rs.getString("mobile"));
        return person;
    }
    
    private Person resultSetToPerson(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setName(rs.getString("name"));
        person.setSurname(rs.getString("surname"));
        person.setIdCardNumber(rs.getString("idcardnumber"));
        person.setEmail(rs.getString("email"));
        person.setMobile(rs.getString("mobile"));
        return person;
    }

    @Override
    public void updatePerson(Person person) throws ServiceFailureException {
        checkDataSource();
        validate(person);
        
        if (person.getId() == null) {
            throw new IllegalArgumentException("person id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);            
            st = conn.prepareStatement(
                    "UPDATE Person SET name = ?, surname = ?, idcardnumber = ?, mobile = ?, email = ? WHERE id = ?");
            st.setString(1, person.getName());
            st.setString(2, person.getSurname());
            st.setString(3, person.getIdCardNumber());
            st.setString(4, person.getMobile());
            st.setString(5, person.getEmail());
            st.setLong(6, person.getId());

            int count = st.executeUpdate();
            try {
                DBUtils.checkUpdatesCount(count, person, false);
            } catch(IllegalEntityException ex) {
                throw new IllegalArgumentException(ex);
            }
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating person in the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }        
    }


    @Override
    public void deletePerson(Person person) throws ServiceFailureException {
        checkDataSource();
        if (person == null) {
            throw new IllegalArgumentException("person is null");
        }        
        if (person.getId() == null) {
            throw new IllegalArgumentException("person id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM Person WHERE id = ?");
            st.setLong(1, person.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, person, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting person from the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    static private void validate(Person person) {        
        if (person == null) {
            throw new IllegalArgumentException("grave is null");
        }
        if (person.getName() == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (person.getSurname() == null) {
            throw new IllegalArgumentException("surname is null");
        }
        if (person.getIdCardNumber() == null) {
            throw new IllegalArgumentException("IdCardNumber is null");
        }
    }
}
