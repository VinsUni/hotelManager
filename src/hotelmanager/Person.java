/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanager;

/**
 *
 * @author Andrej
 */
public class Person {
    
    private Long id;
    private String name;
    private String surname;
    private String idCardNumber;
    private String mobile;
    private String email;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the idCardNumber
     */
    public String getIdCardNumber() {
        return idCardNumber;
    }

    /**
     * @param idCardNumber the idCardNumber to set
     */
    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
     @Override
    public String toString() {
        return "Person{" + "id=" + getId() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
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
