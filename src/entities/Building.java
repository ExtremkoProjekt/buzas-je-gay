
package entities;

/**
 *
 * @author MATEJ
 */
public class Building {
    private int buildingID;
 
    private String name;
    
    public Building(String n) {
        name = n;
    }

    public int getBuildingID() {
        return buildingID;
    }

    public String getName() {
        return name;
    }
    
    public void setBuildingID(int buildingID) {
        this.buildingID = buildingID;
    }
        
    
    
}
