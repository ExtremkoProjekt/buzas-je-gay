package repositories;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author MATEJ
 */
public class UserRepository {
    //adds user to database and returns user_id from 
     public static int add(String name, String textfile) throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "INSERT INTO USER (NAME, TEXTFILE) VALUES (?, ?);"; 
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, name);
        pstmt.setString(2, textfile);
        pstmt.executeUpdate();
        c.commit();
        c.setAutoCommit(true);
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getInt(1);   
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
