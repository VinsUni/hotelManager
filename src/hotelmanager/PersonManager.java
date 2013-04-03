/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;
import common.ServiceFailureException;

/**
 *
 * @author Andrej
 */
public interface PersonManager {
    
    void createPerson(Person person) throws ServiceFailureException;
    
    Person getPerson(Long id) throws ServiceFailureException;
    
    void updatePerson(Person person) throws ServiceFailureException;
    
    void deletePerson(Person person) throws ServiceFailureException;
    
}
