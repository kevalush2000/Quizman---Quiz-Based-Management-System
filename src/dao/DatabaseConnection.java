package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException; // Import IOException

public class DatabaseConnection {
    // REMOVE the static 'connection' field. Each call to getConnection will create a new one.
    // private static Connection connection = null;

    // Modify getConnection to return a new Connection each time
    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        InputStream input = null; // Declare input outside try for finally block

        try {
            // Load the properties file from the classpath
            input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties");

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                throw new SQLException("config.properties not found. Please ensure it's in your classpath.");
            }

            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            // Create and return a new connection instance every time
            Connection newConnection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established successfully.");
            return newConnection;

        } catch (IOException e) { // Catch IOException for properties loading
            System.err.println("Error loading config.properties: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Error loading database configuration.", e); // Re-throw as SQLException
        } catch (SQLException e) { // Catch SQLException for DriverManager.getConnection
            System.err.println("Error establishing database connection: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the SQLException to the calling method
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("Error closing config.properties input stream: " + e.getMessage());
                }
            }
        }
    }

    // The closeConnection method is no longer needed as 'try-with-resources' handles closing
    // public static void closeConnection() {
    //     // ... (can be removed or modified if you introduce a connection pool later)
    // }
}