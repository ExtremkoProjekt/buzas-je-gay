package repositories;

import database.DatabaseConnection;
import extremko.Town;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author MATEJ
 */
public class TownRepository {
    //adds town to database
    public static void add(String name, int userID) throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "INSERT INTO TOWN (NAME, USER_ID) VALUES (?, ?);"; 
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setInt(2, userID);
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }

    public static void generateGold(Town t) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "UPDATE TOWN SET GOLD = GOLD + " +
                "(SELECT VALUE FROM TOWN AS T " +
        "JOIN BUILDING_TOWN_RELATION AS BTR ON T.TOWN_ID = BTR.TOWN_ID " +
        "JOIN BUILDING AS B ON BTR.BUILDING_ID = B.BUILDING_ID " +
        "JOIN BUILDING_PROGRESS BP ON B.BUILDING_ID = BP.BUILDING_ID " +
        "AND BP.LEVEL = BTR.LEVEL " +
        "WHERE T.TOWN_ID = ?)";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, t.getTownID());

        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }

    public static void updateArmy(Town t, int soldiers) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "UPDATE TOWN SET ARMY = ARMY + ? " +
                "WHERE TOWN_ID = ?;";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, soldiers);
        pstmt.setInt(2, t.getTownID());
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }

    public static void subtractArmy(Town t, int soldiers) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "UPDATE TOWN SET ARMY = ARMY - ? " +
                "WHERE TOWN_ID = ?;";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, soldiers);
        pstmt.setInt(2, t.getTownID());
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }

    public static void subtractGold(Town t, int ammount) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "UPDATE TOWN SET GOLD = GOLD - ? " +
                "WHERE TOWN_ID = ?;";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, ammount);
        pstmt.setInt(2, t.getTownID());
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }

    public static boolean canBuySoldiers(Town t, int soldiers) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT GOLD FROM TOWN WHERE TOWN_ID = ?";
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, t.getTownID());
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int res = rs.getInt("GOLD");
        rs.close();
        pstmt.close();
        return res >= soldiers * 2; //cena za jedneho vojaka

    }

    public static Town getTownByName(String town_name) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT * FROM TOWN WHERE NAME = ?;";

        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, town_name);
        ResultSet rs = pstmt.executeQuery();

        if (!rs.next())
            return null;

        Town town = new Town(rs.getString("NAME"));
        town.setTownID(rs.getInt("TOWN_ID"));
        town.setArmy(rs.getInt("ARMY"));
        town.setUserID(rs.getInt("USER_ID"));
        town.setGold(rs.getInt("GOLD"));
        rs.close();
        pstmt.close();
        return town;
    }
     
    public static boolean exists(String name) throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT * FROM TOWN WHERE NAME = ?;";
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();  
    }
    
    public static int getGoldAmount(String townName) throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT GOLD FROM TOWN WHERE NAME = ?";
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, townName);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int res = rs.getInt("GOLD");
        rs.close();
        pstmt.close();
        return res;
    }

    public static int getArmyAmount(String townName) throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT ARMY FROM TOWN WHERE NAME = ?";
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, townName);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int res = rs.getInt("ARMY");
        rs.close();
        pstmt.close();
        return res;
    }

    public static String getTownNameByUserID(int userID) throws ClassNotFoundException, SQLException {

        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT NAME FROM TOWN WHERE USER_ID = ?;";

        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, userID);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getString("NAME");
    }

    public static int townCount() throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT COUNT(*) U_COUNT FROM TOWN";

        pstmt = c.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int res = rs.getInt("U_COUNT");
        rs.close();
        pstmt.close();
        return res;
    }

    public static ArrayList<Town> getTowns() throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT TOWN_ID, NAME, GOLD, ARMY, USER_ID " +
                "FROM TOWN";

        pstmt = c.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Town> towns = new ArrayList<>();
        while (rs.next()) {
            Town t = new Town(rs.getString("NAME"));
            t.setTownID(rs.getInt("TOWN_ID"));
            t.setUserID(rs.getInt("USER_ID"));
            t.setArmy(rs.getInt("ARMY"));
            t.setGold(rs.getInt("GOLD"));
            towns.add(t);
        }
        rs.close();
        pstmt.close();
        return towns;
    }

    public static ArrayList<Town> getTownsByUsername(String userName) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT TOWN_ID, T.NAME, GOLD, ARMY, T.USER_ID " +
                "FROM TOWN AS T " +
                "JOIN USER AS U ON T.USER_ID = U.USER_ID " +
                "WHERE U.NAME = ?";

        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, userName);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Town> towns = new ArrayList<>();
        while (rs.next()) {
            Town t = new Town(rs.getString("NAME"));
            t.setTownID(rs.getInt("TOWN_ID"));
            t.setUserID(rs.getInt("USER_ID"));
            t.setArmy(rs.getInt("ARMY"));
            t.setGold(rs.getInt("GOLD"));
            towns.add(t);
        }
        rs.close();
        pstmt.close();
        return towns;
    }
}