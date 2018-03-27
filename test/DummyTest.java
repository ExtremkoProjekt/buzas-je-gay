/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import extremko.Playground;
import java.io.IOException;
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
    public void testLoadTown() throws IOException{
        Playground pg = new Playground();   
        pg.parseTowns("a b c");
        assertEquals(3, pg.towns.size());
    }
    
    @Test
    public void testLoadMap() throws IOException{
        Playground pg = new Playground();   
        pg.loadMap("map1.txt");
        assertEquals(8, pg.towns.size());
        assertNotEquals(2, pg.towns.size());
    }   
}
