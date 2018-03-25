package extremko;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author MATEJ BUZAS
 */
public class Main {
    private static Playground playground;
    private static Scanner reader;
    
    
    //java -jar nazov.jar
    public static void main(String[] args) throws IOException, InterruptedException {
        reader = new Scanner(System.in); 
        createPlayground();
        play();
        
        
        
    }
    
    public static void play() throws IOException, InterruptedException {  
        menu();
        
        
       
    }
    
    public static void mesto() throws IOException, InterruptedException {  
        clear();
        System.out.println("Tvoje mesto");
        System.out.println("--------------------------------------");
        System.out.println("1 - navrat do menu"); 
        int n = reader.nextInt();
        if (n == 1) {
            play();
        }
       
    }
    
    public static void susedia() throws IOException, InterruptedException {  
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
        System.out.println("Vitaj v hre!");
        System.out.println("--------------------------------------");
        System.out.println("1 - Zobraz svoje mesto");
        System.out.println("2 - Zobraz susedov"); 
        int n = reader.nextInt();
        
        if (n == 1) {
            mesto ();
        }
        else if (n == 2) {
            susedia();
        }
    }
    
    public static void createPlayground() throws IOException {
        String path = "map.txt";
    
        playground = new Playground();
        playground.loadMap(path);
        playground.printMap();
    }
    
    public static void clear() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        
    }

    
    
}


