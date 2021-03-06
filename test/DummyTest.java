

import database.BootstrapDB;
import database.DatabaseHandleTables;
import entities.Building;
import entities.BuildingProgress;
import entities.User;
import extremko.Main;
import extremko.Playground;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import extremko.Town;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;
import repositories.*;

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
    public void setUp() throws SQLException, ClassNotFoundException {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLoadTown() throws IOException, ClassNotFoundException, SQLException{
        DatabaseHandleTables.dropTables();
        DatabaseHandleTables.createTables();
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
    public void testAddUser() throws IOException, ClassNotFoundException, SQLException{
        assertEquals(0,UserRepository.userCount());
        UserRepository.add("stefan");
        assertEquals(true,UserRepository.exists("stefan"));
        assertEquals(1,UserRepository.userCount());
        UserRepository.add("stefann");
        assertEquals(true,UserRepository.exists("stefann"));
        assertEquals(2,UserRepository.userCount());
    }

    @Test
    public void testAddTown() throws IOException, ClassNotFoundException, SQLException{
        User u = UserRepository.getUserByName("stefan");
        UserRepository.getEnemies("stefan");
        //System.out.println(u.getName()+" "+u.getUserID());
        TownRepository.add("mestoStefan",1);
        assertEquals("mestoStefan",TownRepository.getTownNameByUserID(1));
    }

    @Test
    public void testBuildingsCount() throws IOException, ClassNotFoundException, SQLException{
        assertEquals(3,BuildingRepository.buildingCount());
        BuildingRepository.add("Kasarne");
        assertEquals(4,BuildingRepository.buildingCount());
    }

    @Test
    public void testMakeStepsTrue() throws SQLException, ClassNotFoundException {
        Town t = new Town();
        t.setUserID(1);
        t.setTownID(1);
        t.setGold(50);
        t.setArmy(0);
        Main.town = t;
        Building b = new Building(1,"Kasaren",1,15,1);
        assertEquals(true,Main.canMakeStep());
    }

    @Test
    public void testMakeStepsFalse() throws SQLException, ClassNotFoundException {
        Town t = new Town();
        t.setUserID(1);
        t.setTownID(1);
        t.setGold(50);
        t.setArmy(0);
        Main.town = t;
        Building b = new Building(1,"Kasaren",1,15,1);
        BuildingStepRepository.insert(b,t);
        assertEquals(false,Main.canMakeStep());
    }

    @Test
    public void testFinal() throws IOException, SQLException, ClassNotFoundException {
        Playground pg = new Playground();
        String map_path = "map1.txt";
        String username = "matej";
        UserRepository.add(username, map_path);
        User user = UserRepository.getUserByName(username);
        BootstrapDB.initDatabase();
        assertEquals(true,UserRepository.exists(username));
    }

    @Test
    public void testBuildingProgress() throws IOException, ClassNotFoundException, SQLException{
        BuildingProgress bp = new BuildingProgress(1, 1, 10, 5, 1);
        assertEquals("KASAREN",bp.getTypeOfBuidling(bp.getId()));
    }

    @Test
    public void testBuildingProgress2() throws IOException, ClassNotFoundException, SQLException{
        BuildingProgress bp = new BuildingProgress(2, 1, 20, 10, 2);
        assertEquals("ZLATA_BANA",bp.getTypeOfBuidling(bp.getId()));
    }

    @Test
    public void testBuildingProgress3() throws IOException, ClassNotFoundException, SQLException{
        BuildingProgress bp = new BuildingProgress(3, 1, 25, 15, 4);
        assertEquals("HLAVNA_BUDOVA",bp.getTypeOfBuidling(bp.getId()));
    }

    @Test
    public void testBuildingProgress4() throws IOException, ClassNotFoundException, SQLException{
        BuildingProgress bp = new BuildingProgress(0, 1, 25, 15, 4);
        assertEquals("NULL",bp.getTypeOfBuidling(bp.getId()));
    }
}