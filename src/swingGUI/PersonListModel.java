/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swingGUI;

import hotelmanager.Person;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;


public class PersonListModel extends DefaultListModel<Object>  {
    
    private List<Person> persons = new ArrayList<Person>();

    public void addPerson(Person person) {
        persons.add(person);
        int lastRow = persons.size() - 1;
	fireContentsChanged(person, lastRow, lastRow);
    }
    
    public void removePerson(Person person) {
        persons.remove(person);
        int lastRow = persons.size() - 1;
	fireContentsChanged(person, lastRow, lastRow);
    }
    
    @Override
    public int getSize() {
	return persons.size();
    }
    
    @Override
    public Object getElementAt(int index) {
	Person person = persons.get(index);
	String lineFormat = person.getId()+" "+person.getName()+" "+person.getSurname()+" "+person.getIdCardNumber()+" "+person.getEmail()+" "+person.getMobile();
	return lineFormat;
    }

    @Override
    public void addListDataListener(ListDataListener l) {
	super.addListDataListener(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
	super.removeListDataListener(l);
    }
    
}
