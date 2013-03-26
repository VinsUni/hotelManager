/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

import static hotelmanager.PersonManagerImpl.logger;
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
public class RoomManagerImpl implements RoomManager {
    public static final Logger logger = Logger.getLogger(HotelManagerImpl.class.getName());
    private Connection conn;
    
    public RoomManagerImpl(Connection conn) {
        this.conn = conn;
    } 
    
    @Override
    public void createRoom(Room room) throws ServiceFailureException {
        if (room == null) {
            throw new IllegalArgumentException("room is null");            
        }
        if (room.getId() != null) {
            throw new IllegalArgumentException("room id is already set");            
        }
        if (room.getCapacity() < 1) {
            throw new IllegalArgumentException("room capacity is negative or zero");            
        }
        if (room.getType() == null) {
            throw new IllegalArgumentException("room type is null");            
        }
        
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO ROOM (type,capacity) VALUES (?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, room.getType().toString());
            st.setInt(2, room.getCapacity());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows "
                        + "inserted when trying to insert room " + room);
            }            
            
            ResultSet keyRS = st.getGeneratedKeys();
            room.setId(getKey(keyRS,room));
            
        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when inserting room " + room, ex);
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

    private Long getKey(ResultSet keyRS, Room room) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert room " + room
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert room " + room
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert room " + room
                    + " - no key found");
        }
    }

    @Override
    public Room getRoom(Long id) throws ServiceFailureException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "SELECT id,type,capacity FROM room WHERE id = ?");
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Room room = resultSetToRoom(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + room + " and " + resultSetToRoom(rs));                    
                }  
                
                return room;
            } else {
                return null;
            }
            
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving room with id " + id, ex);
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
    
    private Room resultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getLong("id"));
        room.setType(nameToType(rs.getString("type")));
        room.setCapacity(rs.getInt("capacity"));
        return room;
    }
    
    private RoomType nameToType(String name) {
        if (name.equals("apartment")) {
            return RoomType.apartment;
        } else if (name.equals("bungalow")) {
            return RoomType.bungalow;
        } else {
            throw new IllegalArgumentException("wrong type");
        }
    }

    @Override
    public void updateRoom(Room room) throws ServiceFailureException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteRoom(Room room) throws ServiceFailureException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
