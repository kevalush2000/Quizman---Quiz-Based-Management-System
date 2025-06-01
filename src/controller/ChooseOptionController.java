package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.scene.effect.Glow;
import javafx.util.Duration;
import java.io.InputStream;
import java.util.List;
import model.Quiz;
import app.Main;
import dao.QuizDAO;
import java.io.IOException;

public class ChooseOptionController {
    @FXML private StackPane root;
    @FXML private ImageView background;
    @FXML private ImageView backButton;
    @FXML private ImageView title;
    @FXML private ImageView quizPostsButton;
    @FXML private ImageView choosingSubButton;

    private QuizDAO quizDao = new QuizDAO();

    @FXML
    public void initialize() {
        System.out.println("Initializing ChooseOptionController");

        // Load images with error handling
        loadImages();

        // Verify buttons are properly initialized
        if (backButton == null || quizPostsButton == null || choosingSubButton == null) {
            System.err.println("CRITICAL ERROR: One or more buttons not injected!");
            return;
        }

        // Setup responsive design
        setupResponsiveDesign();

        // Setup animations and click handlers
        setupButtonInteractions();
    }

    private void loadImages() {
        System.out.println("Loading images...");

        try {
            // Load back button image first
            Image backImage = loadImage("/QUIZ ASSETS/Choose Option/BACK BUTTON.png");
            if (backImage == null) {
                System.err.println("Back button image failed to load!");
            } else {
                backButton.setImage(backImage);
                System.out.println("Back button image loaded successfully");
            }

            // Load other images
            background.setImage(loadImage("/QUIZ ASSETS/Choose Option/TEACH POV 1 BG.png"));
            title.setImage(loadImage("/QUIZ ASSETS/Choose Option/Choose Options text.png"));

            // Load images for quiz posts and choosing subject buttons
            Image quizPostsImage = loadImage("/QUIZ ASSETS/Choose Option/QUIZ POSTS BUTTON.png");
            if (quizPostsImage == null) {
                System.err.println("Quiz Posts button image failed to load!");
            } else {
                quizPostsButton.setImage(quizPostsImage);
                System.out.println("Quiz Posts button image loaded successfully");
            }

            Image choosingSubImage = loadImage("/QUIZ ASSETS/Choose Option/CHOOSING SUB BUTTON.png");
            if (choosingSubImage == null) {
                System.err.println("Choosing Subject button image failed to load!");
            } else {
                choosingSubButton.setImage(choosingSubImage);
                System.out.println("Choosing Subject button image loaded successfully");
            }

        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Image loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Image not found at: " + path);
                return null;
            }
            return new Image(is);
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            return null;
        }
    }

    private void setupResponsiveDesign() {
        // Background fills screen
        background.fitWidthProperty().bind(root.widthProperty());
        background.fitHeightProperty().bind(root.heightProperty());

        // Back button responsive size
        backButton.fitHeightProperty().bind(root.heightProperty().multiply(0.08));

        // Quiz Posts and Choosing Subject buttons responsive size
        double buttonSizeRatio = 0.25;
        quizPostsButton.fitHeightProperty().bind(root.heightProperty().multiply(buttonSizeRatio));
        choosingSubButton.fitHeightProperty().bind(root.heightProperty().multiply(buttonSizeRatio));
    }

    private void setupButtonInteractions() {
        System.out.println("Setting up button interactions");

        // Back button setup
        if (backButton != null) {
            backButton.setOnMouseEntered(e -> {
                System.out.println("Mouse entered back button");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), backButton);
                st.setToX(1.1);
                st.setToY(1.1);
                st.play();

                Glow glow = new Glow(0.7);
                backButton.setEffect(glow);
            });

            backButton.setOnMouseExited(e -> {
                System.out.println("Mouse exited back button");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), backButton);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
                backButton.setEffect(null);
            });

            // Add click handler
            backButton.setOnMouseClicked(e -> {
                System.out.println("Back button clicked!");
                goBackToRoleSelection();
            });
        } else {
            System.err.println("Back button is null - cannot setup interactions");
        }

        // Quiz Posts button setup
        if (quizPostsButton != null) {
            quizPostsButton.setOnMouseEntered(e -> {
                System.out.println("Mouse entered quiz posts button");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), quizPostsButton);
                st.setToX(1.1);
                st.setToY(1.1);
                st.play();

                Glow glow = new Glow(0.7);
                quizPostsButton.setEffect(glow);
            });

            quizPostsButton.setOnMouseExited(e -> {
                System.out.println("Mouse exited quiz posts button");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), quizPostsButton);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
                quizPostsButton.setEffect(null);
            });

            // Add click handler
            quizPostsButton.setOnMouseClicked(e -> {
    System.out.println("Quiz Posts button clicked!");
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TeacherQuizList.fxml"));
        Parent quizListRoot = loader.load();

        // Pass the quizzes data to the next controller
        TeacherQuizListController controller = loader.getController();
        List<Quiz> quizzes = quizDao.getQuizzesByTeacher(Main.getCurrentUser().getId());
        if (quizzes != null) {
            controller.setQuizzes(quizzes);
        } else {
            System.err.println("Failed to retrieve quizzes for teacher ID: " + Main.getCurrentUser().getId());
        }

        Stage stage = (Stage) quizPostsButton.getScene().getWindow();
        stage.setScene(new Scene(quizListRoot));
        stage.show();
    } catch (IOException ex) {
        ex.printStackTrace();
    }
});
        } else {
            System.err.println("Quiz Posts button is null - cannot setup interactions");
        }

        // Choosing Subject button setup
        if (choosingSubButton != null) {
            choosingSubButton.setOnMouseEntered(e -> {
                System.out.println("Mouse entered choosing subject button");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), choosingSubButton);
                st.setToX(1.1);
                st.setToY(1.1);
                st.play();

                Glow glow = new Glow(0.7);
                choosingSubButton.setEffect(glow);
            });

            choosingSubButton.setOnMouseExited(e -> {
                System.out.println("Mouse exited choosing subject button");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), choosingSubButton);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
                choosingSubButton.setEffect(null);
            });

            // Add click handler
            choosingSubButton.setOnMouseClicked(e -> {
                System.out.println("Choosing Subject button clicked!");
                navigateToChooseSubject();
            });
        } else {
            System.err.println("Choosing Subject button is null - cannot setup interactions");
        }
    }

    private void goBackToRoleSelection() {
        System.out.println("Attempting to navigate back to ChooseRole screen");

        try {
            // Load the ChooseRole FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseRole.fxml"));
            System.out.println("Loading ChooseRole.fxml from: " + getClass().getResource("/view/ChooseRole.fxml"));
            Parent chooseRoleRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();

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

    private void navigateToChooseSubject() {
        System.out.println("Attempting to navigate to ChooseSubject screen");

        try {
            // Load the ChooseSubject FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseSubject.fxml"));
            System.out.println("Loading ChooseSubject.fxml from: " + getClass().getResource("/view/ChooseSubject.fxml"));
            Parent chooseSubjectRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) choosingSubButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(chooseSubjectRoot);
            stage.setScene(scene);
            stage.show();

            System.out.println("Successfully navigated to ChooseSubject screen");
        } catch (IOException e) {
            System.err.println("Failed to load ChooseSubject screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
