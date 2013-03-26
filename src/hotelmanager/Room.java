/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;
import java.lang.Enum;
/**
 *
 * @author Martin Cmarko
 */
public class Room {
    
    private Long id;
    private RoomType type;
    private int capacity;
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the type
     */
    public RoomType getType() {
        return type;
    }
    
    /**
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(RoomType type) {
        this.type = type;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
     @Override
    public String toString() {
        return "Room{" + "id=" + getId() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = 97 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }  
}
