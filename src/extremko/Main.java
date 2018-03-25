package extremko;

import java.io.IOException;

/**
 *
 * @author MATEJ BUZAS
 */
public class Main {
    private static Playground playground;

    public static void main(String[] args) throws IOException {
        createPlayground();
    }
    
    public static void createPlayground() throws IOException {
        playground = new Playground();
        playground.printMap();
    }
    
    
}
