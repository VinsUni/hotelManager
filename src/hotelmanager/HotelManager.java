/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

import common.IllegalEntityException;
import java.util.List;
import common.ServiceFailureException;

/**
 *
 * @author Martin Cmarko
 */
public interface HotelManager {
    Room findRoomWithPerson(Person person) throws ServiceFailureException, IllegalEntityException;
    
    List<Person> findPersonsInRoom(Room room) throws ServiceFailureException, IllegalEntityException;
    
    void checkIn(Person person, Room room) throws ServiceFailureException, IllegalEntityException;
    
    void checkOut(Person person, Room room) throws ServiceFailureException, IllegalEntityException;
}
