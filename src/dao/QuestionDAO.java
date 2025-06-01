package dao;

import model.Question;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    public int createQuestion(Question question) {
        String sql = "INSERT INTO questions (quiz_id, question_text, question_order) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, question.getQuizId());
            stmt.setString(2, question.getText());
            stmt.setInt(3, question.getOrder());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating question failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating question failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Question> getQuestionsByQuiz(int quizId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE quiz_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Question question = new Question(
                    rs.getInt("question_id"),
                    rs.getInt("quiz_id"),
                    rs.getString("question_text"),
                    rs.getInt("question_order")
                );
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
