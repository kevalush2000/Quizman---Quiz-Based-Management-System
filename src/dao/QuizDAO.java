package dao;

import model.Quiz;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuizDAO {

    public int createQuiz(Quiz quiz) {
        // Check if the subject exists
        if (!subjectExists(quiz.getSubjectId())) {
            System.err.println("Subject with ID " + quiz.getSubjectId() + " does not exist.");
            return -1; // Indicate failure
        }

        // Generate a unique access code if not already set in the quiz object
        String accessCode = quiz.getAccessCode();
        if (accessCode == null || accessCode.isEmpty()) {
             accessCode = generateUniqueAccessCode();
             quiz.setAccessCode(accessCode); // Set it back to the quiz object
        }

        String sql = "INSERT INTO quizzes (teacher_id, subject_id, access_code) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, quiz.getTeacherId());
            stmt.setInt(2, quiz.getSubjectId());
            stmt.setString(3, accessCode); // Use the generated/set access code

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating quiz failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated quiz_id
                } else {
                    throw new SQLException("Creating quiz failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating quiz: " + e.getMessage());
            e.printStackTrace();
            return -1; // Indicate failure
        }
    }

    private boolean subjectExists(int subjectId) {
        String sql = "SELECT 1 FROM subjects WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Quiz> getQuizzesByTeacher(int teacherId) {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT q.*, s.subject_name, " +
                    "(SELECT COUNT(*) FROM questions WHERE quiz_id = q.quiz_id) as question_count " +
                    "FROM quizzes q JOIN subjects s ON q.subject_id = s.subject_id " +
                    "WHERE q.teacher_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Quiz quiz = new Quiz(
                    rs.getInt("quiz_id"),
                    rs.getInt("teacher_id"),
                    rs.getInt("subject_id"),
                    rs.getString("access_code"),
                    rs.getString("subject_name")
                );
                quiz.setQuestionCount(rs.getInt("question_count"));
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    private String generateUniqueAccessCode() {
        // Generate a random UUID and take the first 4 characters
        return UUID.randomUUID().toString().substring(0, 4).toUpperCase(); // Added .toUpperCase() for consistency
    }

    public Quiz getQuizByAccessCode(String code) {
        String sql = "SELECT q.*, s.subject_name, " +
                     "(SELECT COUNT(*) FROM questions WHERE quiz_id = q.quiz_id) as question_count " +
                     "FROM quizzes q JOIN subjects s ON q.subject_id = s.subject_id " +
                     "WHERE q.access_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Quiz quiz = new Quiz(
                    rs.getInt("quiz_id"),
                    rs.getInt("teacher_id"),
                    rs.getInt("subject_id"),
                    rs.getString("access_code"),
                    rs.getString("subject_name")
                );
                quiz.setQuestionCount(rs.getInt("question_count"));
                return quiz;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Not found
    }

    // --- THIS IS THE METHOD YOUR CONTROLLER CALLS ---
    public static List<Quiz> getAllQuizzesForTeacher(int teacherId) {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT q.*, s.subject_name, " +
                    "(SELECT COUNT(*) FROM questions WHERE quiz_id = q.quiz_id) as question_count " +
                    "FROM quizzes q JOIN subjects s ON q.subject_id = s.subject_id " +
                    "WHERE q.teacher_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Quiz quiz = new Quiz(
                    rs.getInt("quiz_id"),
                    rs.getInt("teacher_id"),
                    rs.getInt("subject_id"),
                    rs.getString("access_code"),
                    rs.getString("subject_name")
                );
                quiz.setQuestionCount(rs.getInt("question_count"));
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }
}
