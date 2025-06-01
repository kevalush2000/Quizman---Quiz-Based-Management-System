package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        String[] createTables = {
            "CREATE TABLE IF NOT EXISTS users (" +
            "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "username VARCHAR(50) NOT NULL UNIQUE, " +
            "password VARCHAR(255) NOT NULL, " +
            "email VARCHAR(100) NOT NULL UNIQUE, " +
            "role ENUM('teacher','student') NOT NULL, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP) ENGINE=InnoDB",
            
            "CREATE TABLE IF NOT EXISTS quizzes (" +
            "quiz_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "teacher_id INT NOT NULL, " +
            "subject_id INT NOT NULL, " +
            "access_code VARCHAR(36) UNIQUE, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (teacher_id) REFERENCES users(user_id) ON DELETE CASCADE) ENGINE=InnoDB",
            
            "CREATE TABLE IF NOT EXISTS questions (" +
            "question_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "quiz_id INT NOT NULL, " +
            "question_text TEXT NOT NULL, " +
            "question_order INT NOT NULL, " +
            "FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE) ENGINE=InnoDB",
            
            "CREATE TABLE IF NOT EXISTS answers (" +
            "answer_id INT AUTO_INCREMENT PRIMARY KEY, " +
            "question_id INT NOT NULL, " +
            "answer_text TEXT NOT NULL, " +
            "is_correct BOOLEAN DEFAULT FALSE, " +
            "answer_order INT NOT NULL, " +
            "FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE) ENGINE=InnoDB"
        };

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            for (String sql : createTables) {
                stmt.executeUpdate(sql);
            }
            
            System.out.println("Database tables initialized successfully");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}