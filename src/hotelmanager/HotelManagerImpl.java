/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

import common.DBUtils;
import common.IllegalEntityException;
import common.ServiceFailureException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Andrej
 */
public class HotelManagerImpl implements HotelManager {
    private static final Logger logger = Logger.getLogger(
            HotelManagerImpl.class.getName());

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
    public Room findRoomWithPerson(Person person) throws ServiceFailureException, IllegalEntityException {
        checkDataSource();        
        if (person == null) {
            throw new IllegalArgumentException("person is null");
        }        
        if (person.getId() == null) {
            throw new IllegalEntityException("person id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT Room.id, type, capacity " +
                    "FROM Room JOIN Person ON Room.id = Person.roomId " +
                    "WHERE Person.id = ?");
            st.setLong(1, person.getId());
            return RoomManagerImpl.executeQueryForSingleRoom(st);
        } catch (SQLException ex) {
            String msg = "Error when trying to find room with person " + person;
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }        
    }

    @Override
    public List<Person> findPersonsNotInRooms() throws ServiceFailureException, IllegalEntityException {
	checkDataSource();    
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,name,surname,idCardNumber,email,mobile " +
                    "FROM Person " +
                    "WHERE roomId is null");
            return PersonManagerImpl.executeQueryForMultiplePersons(st);
        } catch (SQLException ex) {
            String msg = "Error when trying to find free persons ";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }     
    }
    
    @Override
    public List<Person> findPersonsInRoom(Room room) throws ServiceFailureException, IllegalEntityException {
        checkDataSource();        
        if (room == null) {
            throw new IllegalArgumentException("room is null");
        }        
        if (room.getId() == null) {
            throw new IllegalEntityException("room id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT Person.id, name, surname, idcardnumber, mobile, email " +
                    "FROM Person JOIN Room ON Room.id = Person.roomId " +
                    "WHERE Room.id = ?");
            st.setLong(1, room.getId());
            return PersonManagerImpl.executeQueryForMultiplePersons(st);
        } catch (SQLException ex) {
            String msg = "Error when trying to find bodies in room " + room;
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    @Override
    public void checkIn(Person person, Room room) throws ServiceFailureException, IllegalEntityException {
	checkDataSource();
        if (room == null) {
            throw new IllegalArgumentException("room is null");
        }        
        if (room.getId() == null) {
            throw new IllegalEntityException("room id is null");
        }        
        if (person == null) {
            throw new IllegalArgumentException("person is null");
        }        
        if (person.getId() == null) {
            throw new IllegalEntityException("person id is null");
        }        
        Connection conn = null;
        PreparedStatement updateSt = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            checkIfRoomHasSpace(conn, room);
            
            updateSt = conn.prepareStatement(
                    "UPDATE Person SET roomId = ? WHERE id = ? AND roomId IS NULL");
            updateSt.setLong(1, room.getId());
            updateSt.setLong(2, person.getId());
            int count = updateSt.executeUpdate();
            if (count == 0) {
                throw new IllegalEntityException("Person " + person + " not found or it is already placed in some room");
            }
            DBUtils.checkUpdatesCount(count, person, false);            
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when putting person into room";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, updateSt);
        }
    }

    
    private static void checkIfRoomHasSpace(Connection conn, Room room) throws IllegalEntityException, SQLException {
        PreparedStatement checkSt = null;
        try {
            checkSt = conn.prepareStatement(
                    "SELECT capacity, COUNT(Person.id) as personsCount " +
                    "FROM Room LEFT JOIN Person ON Room.id = Person.roomId " +
                    "WHERE Room.id = ? " +
                    "GROUP BY Room.id, capacity");
            checkSt.setLong(1, room.getId());
            ResultSet rs = checkSt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("capacity") <= rs.getInt("personsCount")) {
                    throw new IllegalEntityException("Room " + room + " is already full");
                }
            } else {
                throw new IllegalEntityException("Room " + room + " does not exist in the database");
            }
        } finally {
            DBUtils.closeQuietly(null, checkSt);
        }
    } 
    
    
    @Override
    public void checkOut(Person person, Room room) throws ServiceFailureException, IllegalEntityException {
	checkDataSource();
        if (room == null) {
            throw new IllegalArgumentException("room is null");
        }        
        if (room.getId() == null) {
            throw new IllegalEntityException("room id is null");
        }        
        if (person == null) {
            throw new IllegalArgumentException("person is null");
        }        
        if (person.getId() == null) {
            throw new IllegalEntityException("person id is null");        
        }                             
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "UPDATE Person SET roomId = NULL WHERE id = ? AND roomId = ?");
            st.setLong(1, person.getId());
            st.setLong(2, room.getId());
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, person, false);            
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when putting person into room";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
}
