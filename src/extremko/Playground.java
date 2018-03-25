package extremko;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author MATEJ BUZAS
 */
public class Playground {
    public ArrayList<Town> towns;
    
    public Playground() throws IOException {
        towns = new ArrayList<Town>();
    }
    
    public void loadMap(String path) throws FileNotFoundException, IOException {
       try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            //first line of text file contains all towns
            String line = br.readLine();
            this.parseTowns(line);                      
            //other lines in text file contains couples of neighbours
            this.parseNeighbours(br);
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
    
    public void parseTowns(String input){
        for (String s: input.split(" ")) {
            towns.add(new Town(s));
        }   
    }
    
    public void parseNeighbours(BufferedReader br) throws IOException{
        String line;
        while ((line = br.readLine()) != null) {
                String[] pair = line.split(" ");
                getTownByName(pair[0]).addNeighbour(getTownByName(pair[1]));
                getTownByName(pair[1]).addNeighbour(getTownByName(pair[0]));                                                       
        }
    }
    
    private Town getTownByName(String name) {
        for (Town t: towns) {
            if (t.getName().equals(name)) return t;
        }
        return null;
    }
    
    public void printMap() {
        for (Town t: towns) {
            System.out.println(t.getName() + ": " + t.printNeighbours());
        }
    }
}
