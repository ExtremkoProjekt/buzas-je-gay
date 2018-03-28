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
    
    //java -jar nazov.jar
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        //DatabaseHandleTables.dropTables();
        DatabaseHandleTables.createTables();
        BootstrapDB.initDatabase();
        reader = new Scanner(System.in); 
        // createPlayground();
        // UserRepository.add("Janko", "map2.txt");
        
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
        
        User player = UserRepository.getUserByName(username);
        if(player == null){
            // neexistuje ale existuje s ID 1
            if(UserRepository.userCount() > 0){
                System.out.println("!!!ZLE MENO!!! NEEXISTA" ); 
                login();
            }
            else{
                // neexistuje - vytvor noveho
                map_path = String.format("map%d.txt", 1 + rnd.nextInt(mapCount));
                UserRepository.add(username, map_path);                 
            }
        }
        else if (player.getUserID() == 1){
            map_path = UserRepository.getMapByName(username);
        }
        else if (player.getUserID() != 1){
            System.out.println("!!!ZLE MENO!!! EXISTA ALE ZLA" ); 
            login();
        }
        
        // create graph        
        pg.loadMap(map_path);        
        menu();
       
    }    
    
    
    public static void town() throws IOException, InterruptedException, ClassNotFoundException, SQLException {  
        clear();
        System.out.println("Tvoje mesto");
        System.out.println("--------------------------------------");
        System.out.println("1 - navrat do menu"); 
        System.out.println("2 - zoznam budov"); 
        System.out.println("3 - pocet zlata"); 
        int n = reader.nextInt();
        if (n == 1) {
            menu();
        }
        else if(n == 2){
            buildings();
        }
        else if(n == 3){
            goldAmount();
        }        
    }   
    
    public static void goldAmount() throws IOException, InterruptedException, ClassNotFoundException, SQLException {  
        clear();
        //int goldAmount = TownRepository.get_gold_amount(townname);
        int goldAmount = 50;
        System.out.println("Počet zlata: " + goldAmount);
        town();
    }
    
    public static void buildings() throws IOException, InterruptedException, ClassNotFoundException, SQLException {  
        clear();
        Scanner reader = new Scanner(System.in); 
        System.out.println("Budovy");
        System.out.println("--------------------------------------");
        
        // TODO: vypis vo forcykle budovy a kolko stoji vylepsenie

        ArrayList<String> options = BuildingRepository.printTownBuildings("a");
        
        int ix = 1;
        for (Iterator<String> i = options.iterator(); i.hasNext();) {
            String buildInfo = i.next();
            System.out.println(ix + " - " +buildInfo);
            ix++;
        }
        int n = reader.nextInt();
        
        // TODO: zacni vylepsovat budovu
        if (n == 1) {
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
    
    public static void menu() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        clear();
        System.out.println("Menu");
        System.out.println("--------------------------------------");
        System.out.println("1 - Zobraz svoje mesto");
        System.out.println("2 - Zobraz susedov"); 
        int n = reader.nextInt();
        
        if (n == 1) {
            town();
        }
        else if (n == 2) {
            neighbours();
        }
    }
    
    public static void neighbours() throws IOException, InterruptedException, ClassNotFoundException, SQLException {  
        clear();
        Scanner reader = new Scanner(System.in); 
        System.out.println("Susedia");
        System.out.println("--------------------------------------");
        System.out.println("1 - navrat do menu"); 
        int n = reader.nextInt();
        if (n == 1) {
            menu();
        }
    }    
    
    public static void createPlayground() throws IOException, FileNotFoundException, ClassNotFoundException, SQLException {
        String path = "map.txt";
        playground = new Playground();
        playground.loadMap(path);
        //playground.printMap();
    }
    
    public static void clear() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        
    }

    
    
}


