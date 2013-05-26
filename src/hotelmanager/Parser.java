/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

import java.io.IOException;

/**
 *
 * @author Cmato
 */
public class Parser {
    
    public RoomType stringToType(String name) throws IllegalArgumentException {
        if (name.equals("apartment")) {
            return RoomType.apartment;
        } else if (name.equals("bungalow")) {
            return RoomType.bungalow;
        } else {
            throw new IllegalArgumentException("wrong type");
        }
    }
    
    public Room parseStringToRoom(String index) throws IOException
    {
	Long roomId = null;
	RoomType roomType = null;
	Integer capacity = null;
	
	String delims = "[ ]+";
	String[] tokens = index.split(delims);
	
	try {
	    roomType = this.stringToType(tokens[2]);
	    roomId = Long.parseLong(tokens[0]);
	    capacity = Integer.parseInt(tokens[4]);
	} catch (NumberFormatException e) {
	    throw new IOException("Wrong input format");
	}
	
	Room roomTmp = new Room();
	roomTmp.setId(roomId);
	roomTmp.setCapacity(capacity);
	roomTmp.setType(roomType);
	
	return roomTmp;
    }
    
    public Person parseStringToPerson(String stringPerson) throws IOException
    {
	Person personTmp = null;
	Long personId = null;
	
	String delims = "[ ]+";
	String[] tokens = stringPerson.split(delims);
	
	try {
	    personId = Long.parseLong(tokens[0]);
	} catch (NumberFormatException e) {
	    throw new IOException("Wrong input format");
	}
	
	String emailTmp = (tokens.length < 5)? "" : tokens[4];
	String mobileTmp = (tokens.length < 6)? "" : tokens[5];
	
	personTmp = new Person();
	personTmp.setId(personId);
	personTmp.setName(tokens[1]);
	personTmp.setSurname(tokens[2]);
	personTmp.setIdCardNumber(tokens[3]);
	personTmp.setEmail(emailTmp);
	personTmp.setMobile(mobileTmp);
	
	return personTmp;
    }
    
}
