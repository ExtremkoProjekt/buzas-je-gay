package extremko;

import database.BootstrapDB;
import database.DatabaseHandleTables;
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
import repositories.BuildingRepository;
import repositories.TownRepository;
import repositories.UserRepository;

/**
 *
 * @author MATEJ BUZAS
 */
public class Main {
    private static Playground playground;
    private static Scanner reader;
    
    static Random rnd;
    static int mapCount;
    static String username;
    static String town_name;

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
        username = reader.next();
        
        Playground pg = new Playground();
        String map_path = "";
        
        System.out.println();

        boolean player_exists = UserRepository.exists(username);

        if (player_exists){
            User player = UserRepository.getUserByName(username);
            if (player.getUserID() == 1){ // nie computer
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
            }
        }
        
        // create graph        
        pg.loadMap(map_path);
        BootstrapDB.initDatabase();
        menu();
       
    }    
    
    
    public static void town() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();

        //TODO: int goldAmount = TownRepository.get_gold_amount(town_name);
        int goldAmount = 50;

        System.out.println("Tvoje mesto " + town_name + ". Počet zlata: " + goldAmount);
        System.out.println("--------------------------------------");
        System.out.println("1 - navrat do menu"); 
        System.out.println("2 - vylepsit budovu");
        System.out.println("3 - postav armadu");
        System.out.println("4 - zautoc na mesto");
        System.out.println("5 - preber mesto");
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
    }

    public static void upgrade_building() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        Scanner reader = new Scanner(System.in); 
        System.out.println("Budovy");
        System.out.println("--------------------------------------");
        System.out.println(town_name);

        ArrayList<String> options = BuildingRepository.printTownBuildings(town_name);
        
        int ix = 1;
        for (Iterator<String> i = options.iterator(); i.hasNext();) {
            String buildInfo = i.next();
            System.out.println(ix + " - " + buildInfo);
            ix++;
        }
        int n = reader.nextInt();
        
        // TODO: zacni vylepsovat budovu
        if (n == 1) {
            // TODO: pozri ci ma dost zlata na vylepsenie na dany level
            System.out.println("VYLEPSUJEM BUDOVU: " + n);
            town();
        }
        else if(n == 2){
            System.out.println("VYLEPSUJEM BUDOVU: " + n);
            town();
        }
        
        // TODO: daj stavat budovu na n tahov


        // TODO: vykonaj tahy UI

        town();
    }

    public static void build_army() throws IOException, InterruptedException, ClassNotFoundException, SQLException {

        // TODO: ukaz nakup vojakov

    }

    public static void attack_enemy() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        int enemy_id = enemy_list();

        // TODO: pridaj na kolko krokov moze zautocit ?
        // TODO: najdi najkratsiu cestu

    }


    public static void capture_enemy_town() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        int enemy_id = enemy_list();

        // TODO: check hlavnu budovu ci je na full ak hej pridaj kroky

    }


    
    public static void menu() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();

        // TODO: zobraz podla akutualneho poctu miest pouzivatela
        System.out.println("Tvoje Mesta");
        System.out.println("--------------------------------------");
//        System.out.println("1 - Zobraz svoje mesto");
//        int n = reader.nextInt();

        town_name = "a"; // TODO: zobraz podla akutualneho poctu miest pouzivatela

        ArrayList<Town> user_towns = TownRepository.getTownsByUsername(username);

        for (Town town : user_towns){
            System.out.println(town.getTownID() +  " - nazov mesta " + town.getName());
        }

        int test_id = reader.nextInt();

        for (Town town : user_towns){
            if(town.getTownID() == test_id){
                town_name = town.getName();
                break;
            }
        }
        town();
    }
    
    public static int enemy_list() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        Scanner reader = new Scanner(System.in); 
        System.out.println("Protihraci");
        System.out.println("--------------------------------------");

        ArrayList<User> enemies  = UserRepository.getEnemies(username);

        for (User enemy : enemies){
            System.out.println(enemy.getUserID() +  " - " + enemy.getName());
        }

        return reader.nextInt();
    }    
    
    public static void createPlayground() throws IOException, FileNotFoundException, ClassNotFoundException, SQLException {
        String path = "map1.txt";
        playground = new Playground();
        playground.loadMap(path);
        //playground.printMap();
    }
    
    public static void clear() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        
    }

    
    
}


