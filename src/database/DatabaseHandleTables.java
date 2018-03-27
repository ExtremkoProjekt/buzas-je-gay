package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 *
 * @author MATEJ
 */

public class DatabaseHandleTables {

    public static void createTables() throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        Statement stmt;     
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS USER " +
                   "(USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "TEXTFILE TEXT"    
                + ");";
        stmt.executeUpdate(sql);
        sql = "CREATE TABLE IF NOT EXISTS TOWN " +
                "(TOWN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "GOLD INTEGER DEFAULT 50, "
                + "USER_ID, "
                + "FOREIGN KEY(USER_ID) REFERENCES USER(USER_ID)"
                + ");";
        stmt.executeUpdate(sql);
        sql = "CREATE TABLE IF NOT EXISTS BUILDING " +
                "(BUILDING_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT"
                + ");";
        stmt.executeUpdate(sql);
        sql = "CREATE TABLE IF NOT EXISTS BUILDING_PROGRESS " +
                "(BUILDING_ID INTEGER,"
                + "LEVEL INTEGER, "
                + "PRICE INTEGER, "
                + "VALUE INTEGER,"
                + "STEPS INTEGER, "
                + "FOREIGN KEY(BUILDING_ID) REFERENCES BUILDING(BUILDING_ID)"
                + ");";
        stmt.executeUpdate(sql);
        sql = "CREATE TABLE IF NOT EXISTS BUILDING_TOWN_RELATION " +
                "(TOWN_ID INTEGER, "
                + "BUILDING_ID INTEGER, "
                + "LEVEL INTEGER, "
                + "FOREIGN KEY(BUILDING_ID) REFERENCES BUILDING(BUILDING_ID)"
                + "FOREIGN KEY(TOWN_ID) REFERENCES TOWN(TOWN_ID)"
                + ");";
        stmt.executeUpdate(sql);
        stmt.close();
        c.commit();         
    }

    public static void dropTables() throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        Statement stmt;     
        stmt = c.createStatement();
        String sql = "DROP TABLE IF EXISTS USER; "
                + "DROP TABLE IF EXISTS TOWN" ;
                 
     
        stmt.executeUpdate(sql);
        stmt.close();
        c.commit(); 
        
        
    }  
}

