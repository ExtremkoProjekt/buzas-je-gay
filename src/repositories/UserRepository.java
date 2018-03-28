package repositories;

import database.DatabaseConnection;
import entities.User;
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
        String sql = "SELECT * FROM USER WHERE NAME = ?";
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }
    
    public static User getUserByName(String name) throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT * FROM USER WHERE NAME = ?";
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next() == false)
            return null;
        
        User user = new User();
        user.setUserID(rs.getInt("USER_ID"));
        user.setName(rs.getString("NAME"));
        rs.close();
        pstmt.close();
        return user; 
    }    

            
    public static int userCount() throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT COUNT(*) U_COUNT FROM USER";
        
        pstmt = c.prepareStatement(sql);
        
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int res = rs.getInt("U_COUNT");
        rs.close();
        pstmt.close();
        return res;
    }
    
    
    public static String getMapByName(String name) throws ClassNotFoundException, SQLException {
        Connection c = DatabaseConnection.getConnection();
        PreparedStatement pstmt;
        String sql = "SELECT TEXTFILE FROM USER WHERE NAME = ?;";
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();
        String fname = "";
        while(rs.next()){
            fname = rs.getString("TEXTFILE");
        }
        return fname;
    }

    public static void add(String userName) throws SQLException, ClassNotFoundException {
        Connection c = DatabaseConnection.getConnection();
        c.setAutoCommit(false);
        String sql = "INSERT INTO USER (NAME) VALUES (?);"; 
        PreparedStatement pstmt;
        pstmt = c.prepareStatement(sql);
        pstmt.setString(1, userName);
        pstmt.executeUpdate();
        pstmt.close();
        c.commit();
        c.setAutoCommit(true);
    }
}
