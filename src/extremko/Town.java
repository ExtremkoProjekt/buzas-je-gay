package extremko;

import java.util.ArrayList;

/**
 *
 * @author MATEJ BUZAS
 */
public class Town {
    private ArrayList<Town> neighbours;
    private String name;
    
    public Town(String n) {
        neighbours = new ArrayList<Town>();
        name = n;
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
