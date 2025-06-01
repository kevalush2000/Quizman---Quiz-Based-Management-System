package dao;

import model.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Subject subject = new Subject(
                    rs.getInt("subject_id"),
                    rs.getString("subject_name")
                );
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public Subject getSubjectById(int subjectId) {
        String sql = "SELECT * FROM subjects WHERE subject_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Subject subject = new Subject(
                    rs.getInt("subject_id"),
                    rs.getString("subject_name")
                );
                return subject;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
