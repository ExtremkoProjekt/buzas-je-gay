package repositories;

import database.DatabaseConnection;

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
}
