
package entities;

/**
 *
 * @author MATEJ
 */
public class User {
    private int userID;
    private String name;
    
    public User() {
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }     
    
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }           
}
