
package entities;

/**
 *
 * @author MATEJ
 */
public class Building {
    private int buildingID;
 
    private String name;
    private int level;
    private int price;
    private int steps;
    
    public Building(String n) {
        name = n;
    }

    public Building() {
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
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
