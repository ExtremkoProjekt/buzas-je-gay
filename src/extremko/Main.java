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
        DatabaseHandleTables.dropTables();
        DatabaseHandleTables.createTables();

        reader = new Scanner(System.in);

        rnd = new Random();
        mapCount = 2;

        System.out.println("Vitajte v hre!\n");

        login();
    }

    public static void login() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        printMainSeparator();
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
        printMainSeparator();
        int goldAmount = TownRepository.getGoldAmount(town.getName());

        BuildingStep buildingStep = BuildingStepRepository.selectBuildingStep(town);
        if (buildingStep != null){
            String buildingName = BuildingProgress.getTypeOfBuidling(buildingStep.getBuildingID());
            int remainingSteps = buildingStep.getRemainingSteps();
            System.out.println("Prave staviate "+buildingName+" a zostava vam "+remainingSteps+" krokov");
        }

        ArmyStep armyStep = ArmyStepRepository.selectArmyStep(town);

        if(armyStep != null){
            if(armyStep.getOponentUserID() == 0){
                System.out.println("Momentalne narukuje "+armyStep.getArmy()+" vojakov");

                int kasaren = BuildingTownRelationRepository.getBuildingLevel(town,BuildingProgress.KASAREN);

                int maxArmy = BuildingProgressRepository.maxArmyPerLevel(BuildingProgress.KASAREN,kasaren);
                System.out.println("V jednom kroku dokaze narukovat naraz "+maxArmy);
            }
            else{

                System.out.println("Utocite na mesto: "+TownRepository.getTownNameByUserID(armyStep.getOponentUserID())+" s "+armyStep.getArmy()+" vojakmi");

            }
        }

        System.out.println("Vase mesto '" + town.getName() + "' | Počet zlata: " + goldAmount+ " | Pocet vojakov: "+TownRepository.getArmyAmount(town.getName()));
        System.out.println("--------------------------------------");
        System.out.println("1 - Navrat do menu (vyber mesta)");
        System.out.println("2 - Vylepsit budovu");
        System.out.println("3 - Postav armadu");
        System.out.println("4 - Zautoc na mesto");
        System.out.println("5 - Preber mesto");
        System.out.println("9 - Dalsi krok");
        System.out.print(">> Vasa moznost: ");

        int n = -1;
        reader = new Scanner(System.in);

        try {
            n = reader.nextInt();
        }
        catch (Exception e){
            System.out.println("(!!!) Nezadali ste cislo - musite zadat cislo moznosti zo zoznamu. Vyberte moznost znova!");
            town();
            return;
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
            captureEnemyTown();
        } else if (n == 9) {
            next_step();
        } else if (n == 999) {
            TownRepository.GoldCheat();
            town();
            return;
        } else if (n == 888) {
            TownRepository.ArmyCheat();
            town();
            return;
        }
        else{
            System.out.print("(!!!) Nezadali ste spravnu volbu - musite zadat cislo moznosti zo zoznamu. Vyberte moznost znova!\n");
            town();
        }
    }


    public static boolean canMakeStep() throws SQLException, ClassNotFoundException {
        System.out.println(BuildingStepRepository.count(town) + " <- build | army -> " + ArmyStepRepository.count(town));
        return BuildingStepRepository.count(town) == 0 && ArmyStepRepository.count(town) == 0;
    }

    public static void upgrade_building() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        printMainSeparator();

        if (!canMakeStep()) {
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
        System.out.println("9 - Navrat do menu");

        System.out.print(">> Zadajte ID budovy na vylepsienie, alebo stlacte '9' pre navrat do menu: ");

        int building_id = -1;

        reader = new Scanner(System.in);

        try {
            building_id = reader.nextInt();
        }
        catch (Exception e){
            System.out.println("(!!!) Nezadali ste cislo - musite zadat cislo moznosti zo zoznamu. Vyberte moznost znova!");
            upgrade_building();
            return;
        }

        boolean isBuildingSelected = false;

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
                isBuildingSelected = true;
                break;
            }
        }

        if(isBuildingSelected){
            upgrade_building();
        }
        else{
            if (building_id == 9){
                town();
                return;
            }
            else{
                System.out.println("(!!!) Zadali ste zlu moznost! Musite vybrat spravnu moznost zo zoznamu.");
                town();
                return;
            }
        }
    }

    public static void build_army() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();

        printMainSeparator();
        if (!canMakeStep()) {
            System.out.println("Uz ste vykonali akciu, prejdite na dalsi krok");


            town();
        }
        // TODO: ukaz nakup vojakov

        System.out.println("Pocet vojakov v meste: " + town.getArmy() + " | Pocet zlata v meste: " + TownRepository.getGoldAmount(town.getName()));
        System.out.print(">> Zadajte pocet vojakov na nakup (mozete kupit maximalne " + (TownRepository.getGoldAmount(town.getName())/5) + " vojakov): ");

        Scanner reader = new Scanner(System.in);
        int number_of_soldiers = -1;

        try {
            number_of_soldiers = reader.nextInt();

            if(number_of_soldiers == 0){
                System.out.println("(!!!) Neobjednali ste si ziadnych vojakov.");
                town();
                return;
            }
            if(number_of_soldiers < 0){
                System.out.println("(!!!) Zadali ste zapornu hodnotu. Zadajte znovu, prosim:");
                build_army();
                return;
            }
        }
        catch (Exception e){
            System.out.println("(!!!) Nezadali ste cislo - musite zadat cislo (pocet vojakov). Zadajte znova!");
            build_army();
            return;
        }



        if (TownRepository.canBuySoldiers(town, number_of_soldiers)) {
            // TownRepository.updateArmy(town, number_of_soldiers);
            // TREBA DOKODIT AJ STRHNUTIE Z GOLDU
            System.out.println("Vojaci zaradeny na kupenie");

            ArmyStepRepository.insert(town,0,0,number_of_soldiers,0);
            TownRepository.subtractGold(town,number_of_soldiers*5);
            town();

        } else {
            System.out.println("Vojaci sa nedaju kupit");
            build_army();
        }

    }

    public static void attackEnemy() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        printMainSeparator();
        if (!canMakeStep()) {
            System.out.println("Uz ste vykonali akciu, prejdite na dalsi krok");
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
    }

    public static void captureEnemyTown() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();

        printMainSeparator();
        if (!canMakeStep()) {
            System.out.println("Uz ste vykonali akciu, prejdite na dalsi krok");
            town();
            return;
        }
        else if (BuildingTownRelationRepository.getBuildingLevel(town,BuildingProgress.HLAVNA_BUDOVA)<5) { //vratit na 5
            System.out.println("Este nemozete preberat cudzie mesta!");
            town();
            return;
        }
        else {
            User selected_enemy = choosenEnemy("Preberanie");
            if (selected_enemy != null){
                int armyAmount = choosenArmyAmount();
            }


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
                    if (as.getUserID()!=user.getUserID() && as.getOponentUserID()==user.getUserID()){
                        String attackerTownName = TownRepository.getTownNameByUserID(as.getUserID());
                        System.out.println("Prichadza na teba utok z dediny "+attackerTownName+" s "+as.getArmy()+" vojakmi!");
                        System.out.println("Utok pride za "+as.getRemainingSteps()+"krokov");
                    }
                    ArmyStepRepository.updateAttackSteps(town);
                }
                else{
                    ArmyStep recordOfBattle = ArmyStepRepository.selectArmyStep(town);

                    int attackLevelOfArmy = BuildingTownRelationRepository.getBuildingLevel(town,BuildingProgress.KASAREN);
                    int attackArmy = recordOfBattle.getArmy();
                    int attackArmyAmount = (int)Math.ceil(recordOfBattle.getArmy()*Math.sqrt((double)attackLevelOfArmy));


                    String defendTownName = TownRepository.getTownNameByUserID(recordOfBattle.getOponentUserID());
                    Town defendTown = TownRepository.getTownByName(defendTownName);

                    int defendLevelOfArmy  = BuildingTownRelationRepository.getBuildingLevel(defendTown,BuildingProgress.KASAREN);
                    int defendArmy = TownRepository.getArmyAmount(defendTownName);
                    int defendArmyAmount = (int)Math.ceil(defendArmy*Math.sqrt((double)defendLevelOfArmy));


                    int armyAfterBattle = attackArmyAmount - defendArmyAmount;


                    //REMIZA
                    if (armyAfterBattle == 0){
                        TownRepository.updateArmy(defendTown,-TownRepository.getArmyAmount(defendTownName));

                        if (as.getUserID()!=user.getUserID() && as.getOponentUserID()==user.getUserID()){
                            System.out.println("Zautocila na teba dedina: " +TownRepository.getTownNameByUserID(as.getUserID())+ " a remizoval si!"
                                    +"\nPocet jednotiek supera: "+as.getArmy()
                                    +"\nStratil si vsetky jednotky!");
                        }
                        if(as.getUserID()==user.getUserID()){
                            System.out.println("Remizovali ste boj s: " +defendTownName
                                    +"\nPocet vasich jednotiek: " +recordOfBattle.getArmy()
                                    +"\nPocet jednotiek supera: "+defendArmy
                                    +"\nVratilo sa vam: "+0+" jednotiek");
                        }


                    }

                    //VYHRA
                    else if(armyAfterBattle > 0){
                        TownRepository.updateArmy(town,(int)Math.floor(armyAfterBattle/Math.sqrt((double)attackLevelOfArmy)));
                        TownRepository.setArmy(defendTown,0);


                        if (as.getUserID()!=user.getUserID() && as.getOponentUserID()==user.getUserID()){
                            System.out.println("Zautocila na teba dedina: " +TownRepository.getTownNameByUserID(as.getUserID())+ " a prehral si!"
                                    +"\nPocet jednotiek supera: "+as.getArmy()
                                    +"\nStratil si vsetky jednotky!");
                        }
                        if(as.getUserID()==user.getUserID()){
                            System.out.println("Vyhrali ste boj s: " +defendTownName
                                    +"\nPocet vasich jednotiek: " +recordOfBattle.getArmy()
                                    +"\nPocet jednotiek supera: "+defendArmy
                                    +"\nVratilo sa vam: "+(int)Math.floor(armyAfterBattle/Math.sqrt((double)attackLevelOfArmy))+" jednotiek");
                        }

                    }

                    //PREHRA
                    else{
                        TownRepository.setArmy(defendTown,-(int)Math.floor(armyAfterBattle/Math.sqrt((double)defendLevelOfArmy)));


                        if (as.getUserID()!=user.getUserID() && as.getOponentUserID()==user.getUserID()){
                            System.out.println("Zautocila na teba dedina: " +TownRepository.getTownNameByUserID(as.getUserID())+ " a vyhral si!"
                                    +"\nPocet jednotiek supera: "+as.getArmy()
                                    +"\nZostalo ti "+TownRepository.getArmyAmount(defendTownName)+" jednotiek!");
                        }
                        if(as.getUserID()==user.getUserID()){
                            System.out.println("Prehrali ste boj s: " +defendTownName
                                    +"\nPocet vasich jednotiek: " +recordOfBattle.getArmy()
                                    +"\nPocet jednotiek supera: "+defendArmy
                                    +"\nVratilo sa vam: "+0+" jednotiek");
                        }


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
            rnd = new Random();
            simulateAI(enemy.getUserID(), rnd.nextInt(3));
            doStep(enemy_town);
        }

    }

    public static void simulateAI(int enemyID, int option) throws SQLException, ClassNotFoundException {

        if(option == 0){
            // vylepsi budovu
            String townName = TownRepository.getTownNameByUserID(enemyID);
            Town town = TownRepository.getTownByName(townName);
            ArrayList<Building> buildings = BuildingRepository.getTonwBuildingsWithRelations(townName);
            rnd = new Random();
            int buildingIDToUpgrade = rnd.nextInt(buildings.size());
            Building selected_building;

            for (Building building : buildings) {
                if (building.getBuildingID() == buildingIDToUpgrade) {
                    selected_building = building;

                    if (BuildingTownRelationRepository.canUpgradeBuilding(selected_building.getBuildingID(), town.getTownID())) {

                        BuildingStepRepository.insert(selected_building, town);
                        TownRepository.subtractGold(town, selected_building.getPrice());
                    }
                    break;
                }
            }


        }else if (option == 1){
            // postav vojsko
            String townName = TownRepository.getTownNameByUserID(enemyID);
            Town town = TownRepository.getTownByName(townName);
            if(ArmyStepRepository.count(town)==0){
                rnd = new Random();
                int armyAmountToBuild = rnd.nextInt(50);

                if (TownRepository.canBuySoldiers(town, armyAmountToBuild)) {
                    ArmyStepRepository.insert(town,0,0,armyAmountToBuild,0);
                    TownRepository.subtractGold(town,armyAmountToBuild*5);

                }
            }

        }
        else{
            // zautoc
            String enemyTownName = TownRepository.getTownNameByUserID(enemyID);
            Town enemyTown = TownRepository.getTownByName(enemyTownName);

            if(ArmyStepRepository.count(enemyTown) == 0){
                ArrayList<Town> allTowns = TownRepository.getTowns();
                int defendTownId = enemyID;
                while (defendTownId == enemyID){
                    rnd = new Random();
                    defendTownId = rnd.nextInt(allTowns.size())+1;
                }

                String defendTownName = TownRepository.getTownNameByUserID(defendTownId);
                Town defendTown = TownRepository.getTownByName(defendTownName);

                int armyAmount = TownRepository.getArmyAmount(enemyTownName);
                if(armyAmount > 5){
                    rnd = new Random();
                    int attackArmyAmount = rnd.nextInt(armyAmount)+1;

                    ArmyStepRepository.insert(enemyTown,defendTown.getUserID(),defendTownId,attackArmyAmount,5);
                    //ArmyStepRepository.insert(enemyTown,1,1,attackArmyAmount,5);
                    TownRepository.subtractArmy(enemyTown,attackArmyAmount);
                }
            }



        }

    }

    public static void printSeparator(){
        System.out.println("--------------------------------------");
    }

    public static void printMainSeparator(){
        System.out.println("======================================");
    }




    public static void menu() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        printMainSeparator();
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
            System.out.println("(!!!) Nezadali ste cislo - musite zadat cislo mesta zo zoznamu. Vyberte mesto znova!");
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
            System.out.println("(!!!) Cislo, ktore site zadali, nezodpoveda ziadnemu tvojmu mestu. Vyberte mesto znova!");
            menu();

        }else{
            town();
        }


    }

    public static User choosenEnemy(String option) throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        printMainSeparator();
        Scanner reader = new Scanner(System.in);
        System.out.println("Protihraci - " + option);
        System.out.println("--------------------------------------");

        ArrayList<User> enemies = UserRepository.getEnemies(user.getName());

        for (User enemy : enemies) {
            System.out.println(enemy.getUserID() + " - " + enemy.getName());
        }

        System.out.print(">> Vyberte protihraca podla ID: ");

        int selected_enemy = -1;


        reader = new Scanner(System.in);

        try {
            selected_enemy = reader.nextInt();

            if (selected_enemy < 2 || selected_enemy > 8){
                System.out.println("(!!!) Nezadali ste spravne cislo - musite zadat cislo protihraca zo zoznamu!");
                town();
                return null;
            }
        }
        catch (Exception e){
            System.out.println("(!!!) Nezadali ste cislo v spravnom formate - musite zadat cislo protihraca zo zoznamu!");
            town();
            return null;
        }

        for (User enemy : enemies) {
            if (enemy.getUserID() == selected_enemy) {
                return enemy;
            }
        }
        return null;
    }

    public static int choosenArmyAmount() throws IOException, InterruptedException, SQLException, ClassNotFoundException {
        clear();
        printMainSeparator();
        Scanner reader = new Scanner(System.in);
        int townArmy = TownRepository.getArmyAmount(town.getName());
        System.out.println("K dispozicii mate " + townArmy + " vojakov");
        System.out.println(">> Zadajte pocet vojakov na utok: ");


        int armyAmount = -1;

        reader = new Scanner(System.in);

        try {
            armyAmount = reader.nextInt();
        }
        catch (Exception e){
            System.out.println("(!!!) Nezadali ste cislo v spravnom formate - musite zadat cislo pocet vojakov na boj!");
            town();
        }

        if (townArmy < armyAmount){
            System.out.println(">> Nemate tolko vojska! Zadajte pocet znovu:");
            town();
        }
        if (armyAmount < 0){
            System.out.println("(!!!) Zadali ste zapornu hodnotu! Musite zadat spravny pocet vojakov na utok.");
            town();
        }
        return armyAmount;

    }

    public static void clear() throws IOException, InterruptedException {
        //new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

}


