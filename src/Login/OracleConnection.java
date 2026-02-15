package Login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection {

    private static final String HOST = "add-your-JDBC URL"; 
    private static final String USERNAME = "your-username";  
    private static final String PASSWORD = "your-password"; 

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Oracle JDBC Driver not found.");
        }
    }

    // <--- THIS METHOD ALLOWS handleUsers TO GET A CONNECTION
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(HOST, USERNAME, PASSWORD);
    }

    // Optional test main method
    public static void main(String[] args) {
        try (Connection conn = OracleConnection.getConnection()) {
            System.out.println("Connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

