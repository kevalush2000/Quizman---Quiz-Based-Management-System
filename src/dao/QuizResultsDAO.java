package dao;

import java.sql.*;

public class QuizResultsDAO {

    public void saveResult(int userId, int quizId, double score) {
        // Check if the user exists
        if (!userExists(userId)) {
            System.err.println("User with ID " + userId + " does not exist.");
            return;
        }

        String sql = "INSERT INTO quiz_results (user_id, quiz_id, score) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, quizId);
            stmt.setDouble(3, score);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean userExists(int userId) {
        String sql = "SELECT 1 FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
