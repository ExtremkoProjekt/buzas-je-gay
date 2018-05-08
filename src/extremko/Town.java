package extremko;

import java.util.ArrayList;

/**
 *
 * @author MATEJ BUZAS
 */
public class Town {
    private ArrayList<Town> neighbours;
    private String name;
    private int townID;
    private int army;
    private int userID;
    private int gold;

    public Town(String n) {
        neighbours = new ArrayList<Town>();
        name = n;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }





    public int getTownID() {
        return townID;
    }

    public void setTownID(int townID) {
        this.townID = townID;
    }



    
    public String getName() {
        return name;
    }
    
    public void addNeighbour(Town t) {
        neighbours.add(t);
    }
    
    public String printNeighbours() {
        if (neighbours.size() < 1) 
            return "";
        String result = neighbours.get(0).getName();
        for (int i = 1; i < neighbours.size(); i++) {
            result += " " + neighbours.get(i).getName();
        }
        return result;      
    }
    
}
