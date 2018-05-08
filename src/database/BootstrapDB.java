
package database;

import java.sql.SQLException;
import entities.BuildingProgress;
import repositories.BuildingProgressRepository;
import repositories.BuildingTownRelationRepository;
import repositories.BuildingRepository;
import java.util.ArrayList;

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

        int numberOfLevels = 5;
        ArrayList<BuildingProgress> rows = new ArrayList<BuildingProgress>();

        // [ KASARNE ]
        for(int i = 1; i <= numberOfLevels; i++){
            BuildingProgressRepository.insert(new BuildingProgress(1, i, i * 15, i * i * 5, i - 1));
        }

        // [ ZLATA BANA ]
        for(int i = 1; i <= numberOfLevels; i++){
            BuildingProgressRepository.insert(new BuildingProgress(2, i, i * 25, i * 5, 2));
        }

        // [ HLAVNA BUDOVA ]
        for(int i = 1; i <= numberOfLevels; i++){
            if (i == numberOfLevels){
                BuildingProgressRepository.insert(new BuildingProgress(3, i, i * 20, 0, i - 1));
            }
            else{ // ak je Hlavna budova == level 5, value zvys na 1 (inak 0)
                BuildingProgressRepository.insert(new BuildingProgress(3, i, i * 20, 1, i - 1));
            }
        }
    }    
    
    public static void initDatabase() throws SQLException, ClassNotFoundException {
        fillBuildings();
        fillBuildingTownRelation();
        fillBuildingProgress();
    }
}