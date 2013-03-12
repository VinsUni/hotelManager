/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

/**
 *
 * @author Martin Cmarko
 */
public interface HotelManager {
    
    void createPerson(Person person) throws ServiceFailureException;
    
    Person getPerson(Long id) throws ServiceFailureException;
    
    void updatePerson(Person person) throws ServiceFailureException;
    
    void deletePerson(Person person) throws ServiceFailureException;
    
    void createRoom(Room room) throws ServiceFailureException;
    
    Room getRoom(Long id) throws ServiceFailureException;
    
    void updateRoom(Room room) throws ServiceFailureException;
    
    void deleteRoom(Room room) throws ServiceFailureException;
    
}
