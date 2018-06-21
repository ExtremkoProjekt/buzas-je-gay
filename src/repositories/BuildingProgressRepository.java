package repositories;

import database.DatabaseConnection;
import entities.BuildingProgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;

public class BuildingProgressRepository {

    public static boolean isEmpty() throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT COUNT(*) B_COUNT FROM BUILDING_PROGRESS";

        pstmt = c.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int res = rs.getInt("B_COUNT");
        rs.close();
        pstmt.close();
        return res == 0;
    }

    public static void insert(BuildingProgress bp) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "INSERT INTO BUILDING_PROGRESS (BUILDING_ID, LEVEL, PRICE, VALUE, STEPS) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1, bp.getId());
        pstmt.setInt(2, bp.getLevel());
        pstmt.setInt(3, bp.getPrice());
        pstmt.setInt(4, bp.getValue());
        pstmt.setInt(5, bp.getSteps());
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }


    public static int buildingMaxLevel(int building_id) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(level) AS maxLevel FROM BUILDING_PROGRESS WHERE building_id = ?;";
        c.setAutoCommit(false);

        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setInt(1,building_id);

        ResultSet rs = pstmt.executeQuery();
        int result = -1;
        while(rs.next()){
            result = rs.getInt("maxLevel");
        }

        return result;
    }
}
