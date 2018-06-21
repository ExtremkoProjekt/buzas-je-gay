package repositories;

import database.DatabaseConnection;
import entities.BuildingProgress;
import entities.BuildingStep;
import extremko.Town;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuildingTownRelationRepository {
    public static boolean isEmpty() throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT COUNT(*) B_COUNT FROM BUILDING_TOWN_RELATION";

        pstmt = c.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int res = rs.getInt("B_COUNT");
        rs.close();
        pstmt.close();
        return res == 0;
    }

    public static void insert() throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "INSERT INTO BUILDING_TOWN_RELATION (TOWN_ID, BUILDING_ID, LEVEL) " +
                "SELECT TOWN_ID, BUILDING_ID, 1 " +
                "FROM TOWN AS T" +
                "CROSS JOIN BUILDING AS B;";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }

    public static boolean canUpgradeBuilding(int buildingId, int townId) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT GOLD, PRICE " +
                "FROM TOWN AS T JOIN BUILDING_TOWN_RELATION AS BTR ON " +
                "T.TOWN_ID = BTR.TOWN_ID " +
                "JOIN BUILDING  AS B ON " +
                "BTR.BUILDING_ID = B.BUILDING_ID " +
                "JOIN BUILDING_PROGRESS AS BP ON BP.BUILDING_ID = B.BUILDING_ID " +
                "WHERE T.TOWN_ID = ? AND B.BUILDING_ID = ? AND BTR.LEVEL = BP.LEVEL";

        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, townId);
        pstmt.setInt(2, buildingId);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int gold = rs.getInt("GOLD");
        int price = rs.getInt("PRICE");
        rs.close();
        pstmt.close();
        return gold >= price;

    }


    public static void upgradeBuildingLevel(BuildingStep bs) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "update BUILDING_TOWN_RELATION set level = level + 1 where town_id = ? and building_id = ?;";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1,bs.getTownID());
        pstmt.setInt(2,bs.getBuildingID());
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }


    public static int getBuildingLevel(Town town, int building_id) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "select level from BUILDING_TOWN_RELATION where town_id = ? and building_id = ?;";

        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1,town.getTownID());
        pstmt.setInt(2,building_id);
        ResultSet rs = pstmt.executeQuery();

        rs.next();
        int level = rs.getInt("level");

        rs.close();
        pstmt.close();
        return level;
    }
}
