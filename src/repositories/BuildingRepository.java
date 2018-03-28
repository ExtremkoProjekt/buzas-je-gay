package repositories;

import database.DatabaseConnection;
import entities.Building;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author MATEJ
 */
public class BuildingRepository {
    public static void add(String name) throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "INSERT INTO BUILDING (NAME) VALUES (?);"; 
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }
    
    public static ArrayList<Building> getTownBuildings(String townName) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT T.NAME AS TOWN_NAME, T.BUILDING_ID FROM TOWN T "
                + "JOIN BUILDING_TOWN_RELATION TR ON T.TOWN_ID = TR.TOWN_ID "
                + "JOIN BUILDING B ON TR.BUILDING_ID = B.BUILDING_ID "
                + "WHERE T.NAME = ?";
        
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, townName);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Building> buildings = new ArrayList<>();
        while (rs.next()) {
                
                Building t = new Building(rs.getString("TOWN_NAME"));
                t.setBuildingID(rs.getInt("BUILDING_ID"));
        
                buildings.add(t); 
        }
      rs.close();
      pstmt.close();
      return buildings;
    }
    
    public static ArrayList<String> printTownBuildings(String townName) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT T.NAME AS TOWN_NAME, B.NAME AS BUILDING_NAME, PRICE, TR.LEVEL FROM TOWN AS T "
                + "JOIN BUILDING_TOWN_RELATION AS TR ON T.TOWN_ID = TR.TOWN_ID "
                + "JOIN BUILDING AS B ON TR.BUILDING_ID = B.BUILDING_ID "
                + "JOIN BUILDING_PROGRESS AS BP ON BP.BUILDING_ID = B.BUILDING_ID "
                + "WHERE T.NAME = ? AND TR.LEVEL = BP.LEVEL";
        
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, townName);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<String> buildings = new ArrayList<>();
        while (rs.next()) {
                
                String s = "BUDOVA: " +rs.getString("BUILDING_NAME")+ " LEVEL: " + rs.getString("LEVEL") + " VYLEPSIT ZA: " + rs.getString("PRICE")+ " ZLATA ";
               
        
                buildings.add(s); 
        }
      rs.close();
      pstmt.close();
      return buildings;
    }
    
    
    public static int buildingCount() throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT COUNT(*) B_COUNT FROM BUILDING";
        
        pstmt = c.prepareStatement(sql);
        
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int res = rs.getInt("B_COUNT");
        rs.close();
        pstmt.close();
        return res;
    }
    
    
    
    
    
}
