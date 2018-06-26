package extremko;

import database.BootstrapDB;
import database.DatabaseHandleTables;
import entities.*;

import java.io.IOException;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import repositories.*;

/**
 * @author MATEJ BUZAS
 */
public class Main {
    private static Playground playground;
    private static Scanner reader;

    static Random rnd;
    static int mapCount;
    static User user;
    static Town town;

    //java -jar nazov.jar
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        //DatabaseHandleTables.dropTables();
        DatabaseHandleTables.createTables();

        reader = new Scanner(System.in);

        rnd = new Random();
        mapCount = 2;

        System.out.println("Vitajte v hre!");
        System.out.println("--------------------------------------");
        login();
    }

    public static void login() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        System.out.print(">> Zadajte vaše meno: ");
        Scanner reader = new Scanner(System.in);

        Playground pg = new Playground();
        String map_path = "";

        String username = reader.next();

        boolean player_exists = UserRepository.exists(username);

        if (player_exists) {
            user = UserRepository.getUserByName(username);
            if (user.getUserID() == 1) { // nie computer
                map_path = UserRepository.getMapByName(username);
            } else {
                System.out.println("(!!!) Zle meno! Musite zadat vase prihlasovacie meno.");
                login();
            }
        } else {
            // player neexistuje
            if (UserRepository.userCount() > 0) {
                System.out.println("(!!!) Zle meno! Musite zadat vase prihlasovacie meno");
                login();
            } else {
                // vytvor hraca s ID 1 - pouzivatela
                map_path = "map1.txt";
                UserRepository.add(username, map_path);
                user = UserRepository.getUserByName(username);
            }
        }

        // create graph        
        pg.loadMap(map_path);
        BootstrapDB.initDatabase();
        menu();
    }


    public static void town() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        int goldAmount = TownRepository.getGoldAmount(town.getName());

        System.out.println("Tvoje mesto " + town.getName() + ". Počet zlata: " + goldAmount+ " Pocet vojakov: "+TownRepository.getArmyAmount(town.getName()));
        System.out.println("--------------------------------------");
        System.out.println("1 - navrat do menu");
        System.out.println("2 - vylepsit budovu");
        System.out.println("3 - postav armadu");
        System.out.println("4 - zautoc na mesto");
        System.out.println("5 - preber mesto");
        System.out.println("6 - dalsi krok");
        System.out.print("Tvoja moznost: ");

        int n = -1;
        reader = new Scanner(System.in);

        try {
            n = reader.nextInt();
        }
        catch (Exception e){
            System.out.print("(!!!) Nezadali ste cislo - musite zadat cislo moznosti zo zoznamu. Vyberte moznost znova!\n");
            menu();
        }

        if (n == 1) {
            menu();
        } else if (n == 2) {
            upgrade_building();
        } else if (n == 3) {
            build_army();
        } else if (n == 4) {
            attackEnemy();
        } else if (n == 5) {
            capture_enemy_town();
        } else if (n == 6) {
            next_step();
        }
        else{
            System.out.print("(!!!) Nezadali ste spravnu volbu - musite zadat cislo moznosti zo zoznamu. Vyberte moznost znova!\n");
            town();
        }
    }

    public static boolean can_make_step() throws SQLException, ClassNotFoundException {
        System.out.println(BuildingStepRepository.count(town) + " <- build | army -> " + ArmyStepRepository.count(town));
        return BuildingStepRepository.count(town) == 0 && ArmyStepRepository.count(town) == 0;
    }

    public static void upgrade_building() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();

        if (!can_make_step()) {
            System.out.println(">> Uz si vykonal akciu, prejdi na dalsi krok");
            town();
        }
        Scanner reader = new Scanner(System.in);
        System.out.println("Budovy");
        System.out.println("--------------------------------------");

        ArrayList<Building> buildings = BuildingRepository.getTonwBuildingsWithRelations(town.getName());

        for (Building building : buildings) {
            System.out.println(building.getBuildingID() + " - BUDOVA: " + building.getName() + " LEVEL: " + building.getLevel() + " VYLEPSIT ZA: " + building.getPrice() + " ZLATA " + " ZA POCET KROKOV " + building.getSteps());
        }

        System.out.print("Zadaj ID budovy na vylepsienie: ");

        int building_id = reader.nextInt();

        Building selected_building;

        for (Building building : buildings) {
            if (building.getBuildingID() == building_id) {
                selected_building = building;

                if (BuildingTownRelationRepository.canUpgradeBuilding(selected_building.getBuildingID(), town.getTownID())) {

                    BuildingStepRepository.insert(selected_building, town);

                    TownRepository.subtractGold(town, selected_building.getPrice());
                    System.out.println("Budova zaradena na vylepsenie");
                    town();
                } else {
                    System.out.println("Budova sa nedala vylepsit");
                    //upgrade_building();
                }

                break;
            }
        }
        town();
    }

    public static void build_army() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        if (!can_make_step()) {
            System.out.println("Uz si vykonal akciu, prejdi na dalsi krok");
            town();
        }
        // TODO: ukaz nakup vojakov

        System.out.println("Pocet vojakov v meste: " + town.getArmy() + ". Pocet zlata v meste: " + TownRepository.getGoldAmount(town.getName()));
        System.out.print("Zadaj pocet vojakov na nakup:");
        Scanner reader = new Scanner(System.in);
        int number_of_soldiers = reader.nextInt();

        if (TownRepository.canBuySoldiers(town, number_of_soldiers)) {
            // TownRepository.updateArmy(town, number_of_soldiers);
            // TREBA DOKODIT AJ STRHNUTIE Z GOLDU
            System.out.println("Vojaci zaradeny na kupenie");

            ArmyStepRepository.insert(town,0,0,number_of_soldiers,0);
            TownRepository.subtractGold(town,number_of_soldiers*2);
            town();

        } else {
            System.out.println("Vojaci sa nedaju kupit");
            build_army();
        }

    }

    public static void attackEnemy() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        if (!can_make_step()) {
            System.out.println("Uz si vykonal akciu, prejdi na dalsi krok");
            town();
        }
        User selected_enemy = choosenEnemy("Utok");
            int armyAmount = choosenArmyAmount();
            String town_name = TownRepository.getTownNameByUserID(selected_enemy.getUserID());
            Town oponentTown = TownRepository.getTownByName(town_name);
            ArmyStepRepository.insert(town,oponentTown.getUserID(),oponentTown.getTownID(),armyAmount,5);
            TownRepository.subtractArmy(town,armyAmount);
            town();
        // TODO: pridaj na kolko krokov moze zautocit ?
        // TODO: najdi najkratsiu cestu
        town();
    }

    public static void capture_enemy_town() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        if (!can_make_step()) {
            System.out.println("Uz si vykonal akciu, prejdi na dalsi krok");
            town();
        }
        else if (BuildingTownRelationRepository.getLevelOfMainBuilding(town.getTownID()) != 1) { //vratit na 5
            System.out.println("Este nemozete preberat cudzie mesta!");
            town();
        }
        else {
            User selected_enemy = choosenEnemy("Preberanie");
            int armyAmount = choosenArmyAmount();

        }




        town();
    }

    public static void next_step() throws IOException, InterruptedException, ClassNotFoundException, SQLException {

        // TODO: obnova zlata, zmena remianing steps, vykonaj AI

        TownRepository.generateGold(town);

        doStep(town);

        makeAISteps();

        town();

    }

    public static void doStep(Town town) throws ClassNotFoundException, SQLException {

        if (BuildingStepRepository.count(town) > 0) {
            BuildingStep bs = BuildingStepRepository.selectBuildingStep(town);
            BuildingStepRepository.updateSteps(town);

            if (BuildingStepRepository.deleteIfDone(town)) {
                BuildingTownRelationRepository.upgradeBuildingLevel(bs);
            }

        }

        if (ArmyStepRepository.count(town) > 0) {
            ArmyStep as = ArmyStepRepository.selectArmyStep(town);
            if (as.getOponentUserID() == 0) {
                // neutocim, vylepsijem vojakov
                int buildingLevel = BuildingTownRelationRepository.getBuildingLevel(town, BuildingProgress.KASAREN);
                int maxArmy = BuildingProgressRepository.maxArmyPerLevel(BuildingProgress.KASAREN,buildingLevel);

                int armyAmount = (as.getArmy() > maxArmy)? maxArmy : as.getArmy();

                TownRepository.updateArmy(town, armyAmount);


                //ArmyStepRepository.insert(town,as.getOponentUserID(),as.getOponentTownID(),as.getArmy()-armyAmount,0);
                ArmyStepRepository.updateSteps(town,armyAmount);
                ArmyStepRepository.deleteIfDone(town);


            } else {
                if(as.getRemainingSteps() > 0){
                    ArmyStepRepository.updateAttackSteps(town);
                }
                else{
                    ArmyStep recordOfBattle = ArmyStepRepository.selectArmyStep(town);

                    int attackLevelOfArmy = BuildingTownRelationRepository.getBuildingLevel(town,BuildingProgress.KASAREN);
                    int attackArmyAmount = (int)Math.ceil(recordOfBattle.getArmy()*Math.sqrt((double)attackLevelOfArmy));


                    String defendTownName = TownRepository.getTownNameByUserID(recordOfBattle.getOponentUserID());
                    Town defendTown = TownRepository.getTownByName(defendTownName);

                    int defendLevelOfArmy  = BuildingTownRelationRepository.getBuildingLevel(defendTown,BuildingProgress.KASAREN);
                    int defendArmyAmount = (int)Math.ceil(TownRepository.getArmyAmount(defendTownName)*Math.sqrt((double)defendLevelOfArmy));


                    int armyAfterBattle = attackArmyAmount - defendArmyAmount;






                    //REMIZA
                    if (armyAfterBattle == 0){

                        TownRepository.updateArmy(town,-recordOfBattle.getArmy());
                        TownRepository.updateArmy(defendTown,-TownRepository.getArmyAmount(defendTownName));
                        System.out.println("Remizoval si boj s: " +defendTownName
                                +"\nPocet tvojich jednotiek: " +recordOfBattle.getArmy()
                                +"\nPocet jednotiek obrany: "+TownRepository.getArmyAmount(defendTownName)
                                +"\nVratilo sa ti: "+0+" jednotiek");


                    }
                    //VYHRA
                    else if(armyAfterBattle > 0){
                        TownRepository.updateArmy(town,(int)Math.floor(armyAfterBattle/Math.sqrt((double)attackLevelOfArmy)));
                        TownRepository.updateArmy(defendTown,-TownRepository.getArmyAmount(defendTownName));
                        System.out.println("Vyhral si boj s: " +defendTownName
                                +"\nPocet tvojich jednotiek: " +recordOfBattle.getArmy()
                                +"\nPocet jednotiek obrany: "+TownRepository.getArmyAmount(defendTownName)
                                +"\nVratilo sa ti: "+(int)Math.floor(armyAfterBattle/Math.sqrt((double)attackLevelOfArmy))+" jednotiek");


                    }
                    //PREHRA
                    else{
                        TownRepository.updateArmy(town,-recordOfBattle.getArmy());
                        TownRepository.updateArmy(defendTown,-(int)Math.floor(armyAfterBattle/Math.sqrt((double)defendLevelOfArmy)));
                        System.out.println("Prehral si boj s: " +defendTownName
                                +"\nPocet tvojich jednotiek: " +recordOfBattle.getArmy()
                                +"\nPocet jednotiek obrany: "+TownRepository.getArmyAmount(defendTownName)
                                +"\nVratilo sa ti: "+0+" jednotiek");
                    }

                    ArmyStepRepository.deleteIfDoneAttack(town);








                }

                // simuluj utocenie
            }
        }
    }

    public static void makeAISteps() throws SQLException, ClassNotFoundException {

        ArrayList<User> enemies = UserRepository.getEnemies(user.getName());

        for (User enemy : enemies) {
            String enemy_town_name = TownRepository.getTownNameByUserID(enemy.getUserID());
            Town enemy_town = TownRepository.getTownByName(enemy_town_name);
            TownRepository.generateGold(enemy_town);
            simulateAI(enemy.getUserID(), rnd.nextInt(3));
        }

    }

    public static void simulateAI(int enemyID, int option) throws SQLException, ClassNotFoundException {

        if(option == 0){
            // vylepsi budovu
            String townName = TownRepository.getTownNameByUserID(enemyID);
            Town town = TownRepository.getTownByName(townName);
            ArrayList<Building> buildings = BuildingRepository.getTonwBuildingsWithRelations(townName);

            int buildingIDToUpgrade = rnd.nextInt(buildings.size());
            Building selected_building;

            for (Building building : buildings) {
                if (building.getBuildingID() == buildingIDToUpgrade) {
                    selected_building = building;

                    if (BuildingTownRelationRepository.canUpgradeBuilding(selected_building.getBuildingID(), town.getTownID())) {

                        BuildingStepRepository.insert(selected_building, town);
                        TownRepository.subtractGold(town, selected_building.getPrice());
                    } else {
                        doStep(town);
                    }

                    break;
                }
            }


        }else if (option == 1){
            // postav vojsko
            String townName = TownRepository.getTownNameByUserID(enemyID);
            Town town = TownRepository.getTownByName(townName);

            int armyAmountToBuild = rnd.nextInt(50);

            if (TownRepository.canBuySoldiers(town, armyAmountToBuild)) {
                ArmyStepRepository.insert(town,0,0,armyAmountToBuild,0);
                TownRepository.subtractGold(town,armyAmountToBuild*2);

            } else {
                doStep(town);
            }
        }
        else{
            // zautoc


        }

    }

    public static void printSeparator(){
        System.out.println("--------------------------------------");
    }


    public static void menu() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        System.out.println("Zoznam vasich miest:");
        printSeparator();



        ArrayList<Town> user_towns = TownRepository.getTownsByUsername(user.getName());

        for (Town town : user_towns) {
            System.out.println(town.getTownID() + " - mesto: " + town.getName());
        }

        System.out.print(">> Vyberte mesto podla ID: ");


        reader = new Scanner(System.in);
        int test_id = -1;
        boolean town_found = false;
        boolean correct_input = false;

        try {
            test_id = reader.nextInt();
            correct_input = true;
        }
        catch (Exception e){
            System.out.print("(!!!) Nezadali ste cislo - musite zadat cislo mesta zo zoznamu. Vyberte mesto znova!\n");
            menu();
        }

        if (!correct_input || test_id == -1){ // Ak pouzivatel zadal zly vstup, program nemoze pokracovat
            return;
        }

        for (Town t : user_towns) {
            if (t.getTownID() == test_id) {
                town = TownRepository.getTownByName(t.getName());
                town_found = true;
                break;
            }
        }
        if (!town_found){
            System.out.print("(!!!) Cislo, ktore site zadali, nezodpoveda ziadnemu tvojmu mestu. Vyberte mesto znova");
            menu();

        }else{
            town();
        }


    }

    public static User choosenEnemy(String option) throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        Scanner reader = new Scanner(System.in);
        System.out.println("Protihraci - " + option);
        System.out.println("--------------------------------------");

        ArrayList<User> enemies = UserRepository.getEnemies(user.getName());

        for (User enemy : enemies) {
            System.out.println(enemy.getUserID() + " - " + enemy.getName());
        }

        System.out.print("Vyber protihrada podla ID: ");

        int selected_enemy = reader.nextInt();

        for (User enemy : enemies) {
            if (enemy.getUserID() == selected_enemy) {
                return enemy;
            }
        }
        return null;
    }

    public static int choosenArmyAmount() throws IOException, InterruptedException, SQLException, ClassNotFoundException {
        clear();
        Scanner reader = new Scanner(System.in);
        int townArmy = TownRepository.getArmyAmount(town.getName());
        System.out.println("K dispozicii mas "+townArmy+" vojakov");
        System.out.println("Zadaj pocet vojakov na utok: ");
        int armyAmount = reader.nextInt();

        if ( townArmy < armyAmount){
            System.out.println("Nemas tolko vojska... Zadaj pocet znovu");
            choosenArmyAmount();
        }
        return armyAmount;

    }

    public static void clear() throws IOException, InterruptedException {
        //new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

}


