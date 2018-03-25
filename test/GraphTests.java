import extremko.Playground;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author MATEJ
 */
public class GraphTests {
    
    public GraphTests() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws IOException {
        
          
    }
    
    @Test
    public void testLoadTown() throws IOException{
        Playground pg = new Playground();   
        pg.parseTowns("a b c");
        assertEquals(3, pg.towns.size());
    }
    
    @Test
    public void testLoadMap() throws IOException{
        Playground pg = new Playground();   
        pg.loadMap("map.txt");
        assertEquals(8, pg.towns.size());
        assertNotEquals(2, pg.towns.size());
    }            
    
    @After
    public void tearDown() {
    }
}
