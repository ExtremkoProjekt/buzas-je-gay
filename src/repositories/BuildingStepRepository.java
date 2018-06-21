package repositories;

import database.DatabaseConnection;
import entities.Building;
import entities.BuildingStep;
import extremko.Town;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuildingStepRepository {
    public static void insert(Building b, Town t) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "INSERT INTO BUILDING_STEP (USER_ID, TOWN_ID, BUILDING_ID, REMAINING_STEPS) VALUES (?, ?, ?, ?);";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, t.getUserID());
        pstmt.setInt(2, t.getTownID());
        pstmt.setInt(3, b.getBuildingID());
        pstmt.setInt(4, b.getSteps());
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }

    public static int count(Town t) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT COUNT(*) B_COUNT FROM BUILDING_STEP " +
                "WHERE USER_ID = ?";

        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, t.getUserID());

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int res = rs.getInt("B_COUNT");
        rs.close();
        pstmt.close();
        return res;
    }

    public static boolean deleteIfDone(Town t) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "DELETE FROM BUILDING_STEP WHERE REMAINING_STEPS = 0 " +
                "AND TOWN_ID = ?";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, t.getTownID());

        int result = pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
        if (result == 0){
            return false;
        }
        return true;

    }

    public static void updateSteps(Town t) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "UPDATE BUILDING_STEP SET REMAINING_STEPS = REMAINING_STEPS - 1 " +
                "WHERE USER_ID = ?;";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, t.getUserID());
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }

    public static BuildingStep selectBuildingStep(Town t) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "SELECT * FROM BUILDING_STEP " +
                "WHERE USER_ID = ?;";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, t.getUserID());
        ResultSet rs = pstmt.executeQuery();
        BuildingStep bs = null;
        while (rs.next()){
            bs = new BuildingStep();
            bs.setBuildingID(rs.getInt("BUILDING_ID"));
            bs.setRemainingSteps(rs.getInt("REMAINING_STEPS"));
            bs.setTownID(rs.getInt("TOWN_ID"));
            bs.setUserID(rs.getInt("USER_ID"));
        }
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
        return bs;
    }


}
