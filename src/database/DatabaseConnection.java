package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Matej
 */
public class DatabaseConnection {
    
    private static Connection connection;
    
    public static Connection getConnection() throws ClassNotFoundException, SQLException {    
        if (connection == null) {      
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        }
        return connection;
    }
    
}