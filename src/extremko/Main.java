package extremko;

import database.BootstrapDB;
import database.DatabaseHandleTables;
import entities.Building;
import entities.BuildingStep;
import entities.User;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import repositories.*;

/**
 *
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
        System.out.print("Zadajte vaše meno: " );
        Scanner reader = new Scanner(System.in);

        Playground pg = new Playground();
        String map_path = "";

        String username = reader.next();

        boolean player_exists = UserRepository.exists(username);

        if (player_exists){
            user = UserRepository.getUserByName(username);
            if (user.getUserID() == 1){ // nie computer
                map_path = UserRepository.getMapByName(username);
            }
            else{
                System.out.println("Zle meno!!! Zadajte vase prihlasovacie meno");
                login();
            }
        }
        else{
            // player neexistuje
            if(UserRepository.userCount() > 0){
                System.out.println("Zle meno!!! Zadajte vase prihlasovacie meno");
                login();
            }
            else{
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

        System.out.println("Tvoje mesto " + town.getName() + ". Počet zlata: " + goldAmount);
        System.out.println("--------------------------------------");
        System.out.println("1 - navrat do menu"); 
        System.out.println("2 - vylepsit budovu");
        System.out.println("3 - postav armadu");
        System.out.println("4 - zautoc na mesto");
        System.out.println("5 - preber mesto");
        System.out.println("6 - dalsi krok");
        System.out.print("Tvoja moznost: ");
        int n = reader.nextInt();
        if (n == 1) {
            menu();
        }
        else if(n == 2){
            upgrade_building();
        }
        else if(n == 3){
            build_army();
        }
        else if(n == 4){
            attack_enemy();
        }
        else if(n == 5){
            capture_enemy_town();
        }
        else if(n == 6){
            next_step();
        }
    }

    public static boolean can_make_step() throws SQLException, ClassNotFoundException {
        System.out.println(BuildingStepRepository.count(town) + " <- build | army -> " + ArmyStepRepository.count(town));
        return BuildingStepRepository.count(town) == 0 && ArmyStepRepository.count(town) == 0;
    }

    public static void upgrade_building() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();

        if (!can_make_step()){
            System.out.println("Uz si vykonal akciu, prejdi na dalsi krok");
            town();
        }
        Scanner reader = new Scanner(System.in); 
        System.out.println("Budovy");
        System.out.println("--------------------------------------");

        ArrayList<Building> buildings = BuildingRepository.getTonwBuildingsWithRelations(town.getName());

        for (Building building : buildings) {
            System.out.println(building.getBuildingID() + " - BUDOVA: " +building.getName()+ " LEVEL: " + building.getLevel() + " VYLEPSIT ZA: " + building.getPrice()+ " ZLATA " + " ZA POCET KROKOV " +  building.getSteps());
        }

        System.out.print("Zadaj ID budovy na vylepsienie: ");

        int building_id = reader.nextInt();

        //wtf nechapem kde toto inicializujes
        Building selected_building;

        for (Building building : buildings){
            if(building.getBuildingID() == building_id){
                selected_building = building;

                if(BuildingTownRelationRepository.canUpgradeBuilding(selected_building.getBuildingID(), town.getTownID())){

                    BuildingStepRepository.insert(selected_building, town);

                    System.out.println("Budova zaradena na vylepsenie");
                    town();
                }
                else{
                    System.out.println("Budova sa nedala vylepsit");
                    upgrade_building();
                }

                break;
            }
        }
        town();
    }

    public static void build_army() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        if (!can_make_step()){
            System.out.println("Uz si vykonal akciu, prejdi na dalsi krok");
            town();
        }
        // TODO: ukaz nakup vojakov

        System.out.println("Pocet vojakov v meste: " + town.getArmy() + ". Pocet zlata v meste: " + town.getGold());
        System.out.print("Zadaj pocet vojakov na nakup:");
        Scanner reader = new Scanner(System.in);
        int number_of_soldiers = reader.nextInt();

        if(TownRepository.canBuySoldiers(town, number_of_soldiers)){
            TownRepository.updateArmy(town, number_of_soldiers);
            // TREBA DOKODIT AJ STRHNUTIE Z GOLDU
            System.out.println("Vojaci zaradeny na kupenie");
            town();
        }
        else{
            System.out.println("Vojaci sa nedaju kupit");
            build_army();
        }

    }

    public static void attack_enemy() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        if (!can_make_step()){
            System.out.println("Uz si vykonal akciu, prejdi na dalsi krok");
            town();
        }
        User selected_enemy = choosen_enemy("Utok");

        // TODO: pridaj na kolko krokov moze zautocit ?
        // TODO: najdi najkratsiu cestu


        town();
    }


    public static void capture_enemy_town() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        if (!can_make_step()){
            System.out.println("Uz si vykonal akciu, prejdi na dalsi krok");
            town();
        }
        User selected_enemy = choosen_enemy("Preberanie");

        // TODO: check hlavnu budovu ci je na full ak hej pridaj kroky


        town();
    }

    public static void next_step() throws IOException, InterruptedException, ClassNotFoundException, SQLException {

        // TODO: obnova zlata, zmena remianing steps, vykonaj AI

        TownRepository.generateGold(town);

        if(BuildingStepRepository.count(town) > 0){
            BuildingStep bs = BuildingStepRepository.selectBuildingStep(town);
            BuildingStepRepository.updateSteps(town);
           if(BuildingStepRepository.deleteIfDone(town)){
               BuildingTownRelationRepository.upgradeBuildingLevel(town, bs);
           }
        }

        if(ArmyStepRepository.count(town) > 0){
           ArmyStepRepository.updateSteps(town);
           ArmyStepRepository.deleteIfDone(town);
        }

        makeAISteps();

        town();

    }

    public static void makeAISteps() throws SQLException, ClassNotFoundException {

        ArrayList<User> enemies  = UserRepository.getEnemies(user.getName());
        for (User enemy : enemies){
            String enemy_town_name = TownRepository.getTownNameByUserID(enemy.getUserID());
            Town enemy_town = TownRepository.getTownByName(enemy_town_name);
            TownRepository.generateGold(enemy_town);

            //enemy.simulateAI();
        }
    }


    
    public static void menu() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        System.out.println("Tvoje Mesta");
        System.out.println("--------------------------------------");

        ArrayList<Town> user_towns = TownRepository.getTownsByUsername(user.getName());

        for (Town town : user_towns){
            System.out.println(town.getTownID() +  " - mesto: " + town.getName());
        }

        System.out.print("Vyber mesto podla ID: ");

        int test_id = reader.nextInt();

        for (Town t : user_towns){
            if(t.getTownID() == test_id){
                town = TownRepository.getTownByName(t.getName());
                break;
            }
        }
        town();
    }
    
    public static User choosen_enemy(String option) throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        Scanner reader = new Scanner(System.in); 
        System.out.println("Protihraci - " + option);
        System.out.println("--------------------------------------");

        ArrayList<User> enemies  = UserRepository.getEnemies(user.getName());

        for (User enemy : enemies){
            System.out.println(enemy.getUserID() +  " - " + enemy.getName());
        }

        System.out.print("Vyber protihrada podla ID: ");

        int selected_enemy = reader.nextInt();

        for (User enemy : enemies){
            if(enemy.getUserID() == selected_enemy){
                return enemy;
            }
        }
        return null;
    }
    
    public static void clear() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }
}


