package extremko;

import database.DatabaseHandleTables;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import repositories.UserRepository;

/**
 *
 * @author MATEJ BUZAS
 */
public class Main {
    private static Playground playground;
    private static Scanner reader;
    
    
    //java -jar nazov.jar
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        DatabaseHandleTables.dropTables();
        DatabaseHandleTables.createTables();
        reader = new Scanner(System.in); 
        createPlayground();
        UserRepository.add("Janko", "map2.txt");
        //play();
    }
    
    public static void play() throws IOException, InterruptedException {  
        menu();
        
        
       
    }

    public static void login() throws IOException, InterruptedException, ClassNotFoundException, SQLException {  
        clear();
        System.out.println("Vitajte v hre!");
        System.out.println("--------------------------------------");
        System.out.print("Zadajte vaše meno: " );         
        Scanner reader = new Scanner(System.in); 
        String name = reader.next();
        
        // new user
        if(!UserRepository.exists(name)) {
            // create new, assign map
            Random rnd = new Random();
            int mapCount = 5;
            String map_path = String.format("map%d.txt", 1 + rnd.nextInt(mapCount));
            // UserRepository.add(name, map_path);
            menu();
        }
        else{
            menu();
        }
        
       
    }    
    
    public static void town() throws IOException, InterruptedException {  
        clear();
        System.out.println("Tvoje mesto");
        System.out.println("--------------------------------------");
        System.out.println("1 - navrat do menu"); 
        int n = reader.nextInt();
        if (n == 1) {
            play();
        }
       
    }
    
    public static void neighbours() throws IOException, InterruptedException {  
        clear();
        Scanner reader = new Scanner(System.in); 
        System.out.println("Susedia");
        System.out.println("--------------------------------------");
        System.out.println("1 - navrat do menu"); 
        int n = reader.nextInt();
        if (n == 1) {
            play();
        }
    }
    
    public static void menu() throws IOException, InterruptedException {
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
    
    public static void createPlayground() throws IOException {
        String path = "map.txt";
        playground = new Playground();
        playground.loadMap(path);
        //playground.printMap();
    }
    
    public static void clear() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        
    }

    
    
}


