package repositories;

import database.DatabaseConnection;
import entities.BuildingProgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        //pstmt.setString(1, bp.g);
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }
}
