package dao;

import model.Answer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDAO {

    public int createAnswer(int questionId, String text, boolean isCorrect, int order) {
        String sql = "INSERT INTO answers (question_id, answer_text, is_correct, answer_order) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, questionId);
            stmt.setString(2, text);
            stmt.setBoolean(3, isCorrect);
            stmt.setInt(4, order);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating answer failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating answer failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Answer> getAnswersByQuestion(int questionId) {
        List<Answer> answers = new ArrayList<>();
        String sql = "SELECT * FROM answers WHERE question_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Answer answer = new Answer(
                    rs.getInt("answer_id"),
                    rs.getInt("question_id"),
                    rs.getString("answer_text"),
                    rs.getBoolean("is_correct"),
                    rs.getInt("answer_order")
                );
                answers.add(answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

    public boolean isAnswerCorrect(int answerId) {
        String sql = "SELECT is_correct FROM answers WHERE answer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, answerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("is_correct");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
