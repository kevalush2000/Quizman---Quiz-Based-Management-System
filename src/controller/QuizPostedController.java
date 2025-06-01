package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

public class QuizPostedController {
    @FXML private Label accessCodeLabel;
    @FXML private ImageView showQuizPostsButton;
    @FXML private ImageView backToRolesButton;

    private String accessCode;

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
        accessCodeLabel.setText("Quiz Code: " + accessCode);
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing QuizPostedController");
        setupButtonInteractions();
    }

    private void setupButtonInteractions() {
        // Show Quiz Posts button setup
        showQuizPostsButton.setOnMouseClicked(e -> {
            System.out.println("Show Quiz Posts button clicked!");
            navigateToTeacherQuizList();
        });

        // Back to Roles button setup
        backToRolesButton.setOnMouseClicked(e -> {
            System.out.println("Back to Roles button clicked!");
            navigateToChooseRole();
        });
    }

    private void navigateToTeacherQuizList() {
        System.out.println("Attempting to navigate to TeacherQuizList screen");

        try {
            // Load the TeacherQuizList FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TeacherQuizList.fxml"));
            System.out.println("Loading TeacherQuizList.fxml from: " + getClass().getResource("/view/TeacherQuizList.fxml"));
            Parent teacherQuizListRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) showQuizPostsButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(teacherQuizListRoot);
            stage.setScene(scene);
            stage.show();

            System.out.println("Successfully navigated to TeacherQuizList screen");
        } catch (IOException e) {
            System.err.println("Failed to load TeacherQuizList screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void navigateToChooseRole() {
        System.out.println("Attempting to navigate to ChooseRole screen");

        try {
            // Load the ChooseRole FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseRole.fxml"));
            System.out.println("Loading ChooseRole.fxml from: " + getClass().getResource("/view/ChooseRole.fxml"));
            Parent chooseRoleRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) backToRolesButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(chooseRoleRoot);
            stage.setScene(scene);
            stage.show();

            System.out.println("Successfully navigated to ChooseRole screen");
        } catch (IOException e) {
            System.err.println("Failed to load ChooseRole screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}