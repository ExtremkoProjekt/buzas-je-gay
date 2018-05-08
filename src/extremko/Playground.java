package extremko;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import repositories.TownRepository;
import repositories.UserRepository;

/**
 *
 * @author MATEJ BUZAS
 */
public class Playground {
    public ArrayList<Town> towns;

    /**
     * Inicializacia arraylistu towns
     * @throws IOException
     */
    public Playground() throws IOException {
        towns = new ArrayList<Town>();
    }

    /**
     * Otvori subor precita riadky a naplni graf
     * @param path - txt subor
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void loadMap(String path) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
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

    /**
     * Funckia robi viac veci - REFATOROVAT
     *  - vytvori mesta
     *  - prida mesto pouzivatelovi
     *  - pre index > 1 (index 1 je clovek) vytvori AI hracov
     * @param input - mesta oddelene medzerou
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void parseTowns(String input) throws ClassNotFoundException, SQLException{
        int ix = 1;

        if (TownRepository.townCount() > 0){
            towns = TownRepository.getTowns();
        }
        else {
            for (String s : input.split(" ")) {
                towns.add(new Town(s));
                if (ix > 1) {
                    String name = "computer" + ix;
                    if (UserRepository.exists(name)) {
                        ix++;
                        name = "computer" + ix;
                    }
                    UserRepository.add(name);
                }

                // pridam mestu usera pre ix=1 user uz ecistuje
                TownRepository.add(s, ix);
                ix++;
            }
        }

    }

    /**
     * Prida mestam susedov
     * @param br
     * @throws IOException
     */
    public void parseNeighbours(BufferedReader br) throws IOException{
        String line;
        while ((line = br.readLine()) != null) {
                String[] pair = line.split(" ");
                getTownByName(pair[0]).addNeighbour(getTownByName(pair[1]));
                getTownByName(pair[1]).addNeighbour(getTownByName(pair[0]));                                                       
        }
    }

    /**
     * Nazov funkcie hovori za vsetko - mohlo by sa prerobit na DB call
     * @param name
     * @return
     */
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
