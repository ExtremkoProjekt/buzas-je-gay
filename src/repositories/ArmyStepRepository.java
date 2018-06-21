package repositories;

import database.DatabaseConnection;
import extremko.Town;
import entities.ArmyStep;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArmyStepRepository {

    public static int count(Town t) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT COUNT(*) B_COUNT FROM ARMY_STEP " +
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
        String sql = "DELETE FROM ARMY_STEP WHERE ARMY = 0 " +
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

    public static void updateSteps(Town t, int armyAmount) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "UPDATE ARMY_STEP SET ARMY = ARMY - ?" +
                "WHERE USER_ID = ?;";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1,armyAmount);
        pstmt.setInt(2, t.getUserID());
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }

    public static ArmyStep selectArmyStep(Town town) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "SELECT * FROM ARMY_STEP " +
                "WHERE USER_ID = ?;";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, town.getUserID());
        ResultSet rs = pstmt.executeQuery();
        ArmyStep as = null;
        while (rs.next()) {
            as = new ArmyStep();
            as.setArmy(rs.getInt("ARMY"));
            as.setOponentTownID(rs.getInt("OPONENT_USER_ID"));
            as.setOponentUserID(rs.getInt("OPONENT_TOWN_ID"));
            as.setRemainingSteps(rs.getInt("REMAINING_STEPS"));
            as.setTownID(rs.getInt("TOWN_ID"));
            as.setUserID(rs.getInt("USER_ID"));
        }
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
        return as;

    }

    public static void insert(Town town, int oponent_user_id, int oponent_town_id, int amount, int remainSteps) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "INSERT INTO ARMY_STEP (USER_ID,TOWN_ID,OPONENT_USER_ID,OPONENT_TOWN_ID,ARMY,REMAINING_STEPS) VALUES (?,?,?,?,?,?);";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1,town.getUserID());
        pstmt.setInt(2,town.getTownID());
        pstmt.setInt(3,oponent_user_id);
        pstmt.setInt(4,oponent_town_id);
        pstmt.setInt(5,amount);
        pstmt.setInt(6,remainSteps);

        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);

    }
}
