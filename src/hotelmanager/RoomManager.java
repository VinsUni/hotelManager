/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

/**
 *
 * @author Andrej
 */
public interface RoomManager {
    
    void createRoom(Room room) throws ServiceFailureException;
    
    Room getRoom(Long id) throws ServiceFailureException;
    
    void updateRoom(Room room) throws ServiceFailureException;
    
    void deleteRoom(Room room) throws ServiceFailureException;
    
}
