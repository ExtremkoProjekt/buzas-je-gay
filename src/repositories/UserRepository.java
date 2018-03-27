package repositories;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author MATEJ
 */
public class UserRepository {
    //adds user to database
     public static void add(String name, String textfile) throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "INSERT INTO USER (NAME, TEXTFILE) VALUES (?, ?);"; 
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, textfile);
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }
    
     //returns true if user with name in parameter exists
    public static boolean exists(String name) throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT * FROM USER WHERE NAME = ?;";
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();  
    }
    
}
