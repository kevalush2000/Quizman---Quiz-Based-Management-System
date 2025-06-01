package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

public class UserDAO {
    private DatabaseConnection databaseConnection;

    public UserDAO() {
        this.databaseConnection = new DatabaseConnection();
    }

    /**
     * Creates a default teacher account if none exists
     */
    public void createDefaultTeacherIfNeeded() {
        if (!anyTeachersExist()) {
            String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
            try (Connection conn = databaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, "admin");
                stmt.setString(2, "admin123"); // Plain text password
                stmt.setString(3, "admin@quizsystem.com");
                stmt.setString(4, "teacher");
                stmt.executeUpdate();

                System.out.println("Created default teacher account");
            } catch (SQLException e) {
                System.err.println("Failed to create default teacher: " + e.getMessage());
            }
        }
    }

    /**
     * Checks if any teacher accounts exist
     */
    public boolean anyTeachersExist() {
        String sql = "SELECT 1 FROM users WHERE role = 'teacher' LIMIT 1";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a user by username
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Debug: Print all columns from the result set
                System.out.println("Database returned:");
                System.out.println("user_id: " + rs.getObject("user_id"));
                System.out.println("username: " + rs.getObject("username"));
                System.out.println("password: " + rs.getObject("password"));
                System.out.println("email: " + rs.getObject("email"));
                System.out.println("role: " + rs.getObject("role"));

                // Create user object with proper column names
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role")
                );
                return user;
            } else {
                System.out.println("No user found with username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
