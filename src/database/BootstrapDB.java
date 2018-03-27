
package database;

import java.sql.SQLException;
import repositories.BuildingRepository;

/**
 *
 * @author MATEJ
 */
public class BootstrapDB {
    
    private static void fillBuildings() throws SQLException, ClassNotFoundException {
        if (BuildingRepository.buildingCount() == 0) {
            BuildingRepository.add("Kasarne");
            BuildingRepository.add("Zlata bana");
            
        }
    }
    
    public static void initDatabase() throws SQLException, ClassNotFoundException {
        fillBuildings();
        
    }
    
}
