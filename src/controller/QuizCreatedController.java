package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class QuizCreatedController {
    @FXML private VBox root;
    @FXML private Label accessCodeLabel;
    @FXML private Button viewQuizzesButton;
    @FXML private Button backToMenuButton;

    public void setAccessCode(String accessCode) {
        accessCodeLabel.setText("Access Code: " + accessCode);
    }

    @FXML
    private void handleViewQuizzes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TeacherQuizList.fxml"));
            Parent teacherQuizListRoot = loader.load();

            // Call refreshQuizList to populate the list
            TeacherQuizListController controller = loader.getController();
            controller.refreshQuizList();

            Stage stage = (Stage) viewQuizzesButton.getScene().getWindow();
            stage.setScene(new Scene(teacherQuizListRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading TeacherQuizList.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseOptions.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ChooseOptions.fxml: " + e.getMessage());
        }
    }
}
