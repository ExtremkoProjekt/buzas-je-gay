
package database;

import java.sql.SQLException;

import repositories.BuildingTownRelationRepository;
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
            BuildingRepository.add("Hlavna budova");
            
        }
    }

    private static void fillBuildingTownRelation() throws SQLException, ClassNotFoundException {
        if(BuildingTownRelationRepository.isEmpty()) {
            BuildingTownRelationRepository.insert();
        }
    }

    private static void fillBuildingProgress() throws SQLException, ClassNotFoundException {

//        for () data in pole ) {
//            if(BuildingProgressRepository.isEmpty()) {
//                BuildingProgressRepository.insert(data.level, data.value, data.staps ... );
//            }
//        }


    }    
    
    public static void initDatabase() throws SQLException, ClassNotFoundException {
        fillBuildings();
        fillBuildingTownRelation();
        fillBuildingProgress();
    }
    
}
