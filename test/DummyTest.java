

import extremko.Playground;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;


/**
 *
 * @author Martin
 */
public class DummyTest {

    public DummyTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testLoadTown() throws IOException, ClassNotFoundException, SQLException{
        Playground pg = new Playground();
        pg.parseTowns("");
        assertEquals(0, pg.towns.size());
    }
    
    @Test
    public void testLoadTown2() throws IOException, ClassNotFoundException, SQLException{
        Playground pg = new Playground();   
        pg.parseTowns("a");
        assertEquals(1, pg.towns.size());
    }    
    
    @Test
    public void testLoadMap() throws IOException, FileNotFoundException, ClassNotFoundException, SQLException {
        Playground pg = new Playground();
        pg.loadMap("map1.txt");
        assertEquals(8, pg.towns.size());
        assertNotEquals(2, pg.towns.size());
    }

}
