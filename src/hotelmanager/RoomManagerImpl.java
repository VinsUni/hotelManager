/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

import common.ServiceFailureException;
import common.ValidationException;
import common.IllegalEntityException;
import common.DBUtils;

import static hotelmanager.PersonManagerImpl.logger;
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
public class RoomManagerImpl implements RoomManager {
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
    public void createRoom(Room room) throws ServiceFailureException {
        checkDataSource();
        validate(room);
        if (room.getId() != null) {
            throw new IllegalArgumentException("room id is already set");
        }       
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "INSERT INTO ROOM (type,capacity) VALUES (?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, room.getType().toString());
            st.setInt(2, room.getCapacity());
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, room, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            room.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting person into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    @Override
    public Room getRoom(Long id) throws ServiceFailureException {

        checkDataSource();
        
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,type,capacity FROM room WHERE id = ?");
            st.setLong(1, id);
            return executeQueryForSingleRoom(st);
        } catch (SQLException ex) {
            String msg = "Error when getting room with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    static Room executeQueryForSingleRoom(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Room result = rowToRoom(rs);                
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more bodies with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }
    
    private static Room rowToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getLong("id"));
        room.setCapacity(rs.getInt("capacity"));
        room.setType(nameToType(rs.getString("type")));
        return room;
    }

    @Override
    public void updateRoom(Room room) throws ServiceFailureException {
        checkDataSource();
        validate(room);
        
        if (room.getId() == null) {
            throw new IllegalArgumentException("room id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);            
            st = conn.prepareStatement(
                    "UPDATE Room SET type = ?, capacity = ? WHERE id = ?");
            st.setString(1, room.getType().toString());
            st.setInt(2, room.getCapacity());
            st.setLong(3, room.getId());

            int count = st.executeUpdate();
            try {
                DBUtils.checkUpdatesCount(count, room, false);
            } catch(IllegalEntityException ex) {
                throw new IllegalArgumentException(ex);
            }
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating room in the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }        
    }


    @Override
    public void deleteRoom(Room room) throws ServiceFailureException {
        checkDataSource();
        if (room == null) {
            throw new IllegalArgumentException("room is null");
        }        
        if (room.getId() == null) {
            throw new IllegalArgumentException("room id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM Room WHERE id = ?");
            st.setLong(1, room.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, room, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting room from the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    static private void validate(Room room) {        
        if (room == null) {
            throw new IllegalArgumentException("room is null");
        }
        if (room.getCapacity() <= 0) {
            throw new IllegalArgumentException("capacity is less than 1");
        }
        if (room.getType() == null) {
            throw new IllegalArgumentException("type is null");
        }
    }
    
    private Room resultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getLong("id"));
        room.setType(nameToType(rs.getString("type")));
        room.setCapacity(rs.getInt("capacity"));
        return room;
    }
    
    private static RoomType nameToType(String name) {
        if (name.equals("apartment")) {
            return RoomType.apartment;
        } else if (name.equals("bungalow")) {
            return RoomType.bungalow;
        } else {
            throw new IllegalArgumentException("wrong type");
        }
    }
}
