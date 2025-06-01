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

import app.Main;

import java.io.IOException;
import model.Quiz;
import model.Subject;
import dao.QuizDAO;
import dao.SubjectDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ChooseSubjectController {
    @FXML private StackPane root;
    @FXML private ImageView background;
    @FXML private ImageView backButton;
    @FXML private ImageView title;
    @FXML private ImageView scienceButton;
    @FXML private ImageView mathButton;
    @FXML private ImageView historyButton;

    private QuizDAO quizDao = new QuizDAO();
    private SubjectDAO subjectDao = new SubjectDAO();

    @FXML
    public void initialize() {
        loadImages();
        setupResponsiveDesign();
        setupButtonInteractions();
    }

    private void loadImages() {
        background.setImage(loadImage("/QUIZ ASSETS/Choose a Subject assets/TEACH POV 1 BG.png"));
        backButton.setImage(loadImage("/QUIZ ASSETS/Choose Option/BACK BUTTON.png"));
        title.setImage(loadImage("/QUIZ ASSETS/Choose a Subject assets/CS Title.png"));
        scienceButton.setImage(loadImage("/QUIZ ASSETS/Choose a Subject assets/SCIENCE.png"));
        mathButton.setImage(loadImage("/QUIZ ASSETS/Choose a Subject assets/MATH.png"));
        historyButton.setImage(loadImage("/QUIZ ASSETS/Choose a Subject assets/HISTORY.png"));
    }

    private void setupResponsiveDesign() {
        background.fitWidthProperty().bind(root.widthProperty());
        background.fitHeightProperty().bind(root.heightProperty());
    }

    private void setupButtonInteractions() {
        applyButtonEffects(scienceButton);
        scienceButton.setOnMouseClicked(e -> handleSubjectSelection(1, "Science"));

        applyButtonEffects(mathButton);
        mathButton.setOnMouseClicked(e -> handleSubjectSelection(2, "Mathematics"));

        applyButtonEffects(historyButton);
        historyButton.setOnMouseClicked(e -> handleSubjectSelection(3, "History"));

        applyButtonEffects(backButton);
        backButton.setOnMouseClicked(e -> handleBackButton());
    }

    private void applyButtonEffects(ImageView button) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.05);
        st.setToY(1.05);

        button.setOnMouseEntered(e -> {
            st.playFromStart();
            button.setEffect(new Glow(0.3));
        });
        button.setOnMouseExited(e -> {
            st.setRate(-1);
            st.play();
            button.setEffect(null);
        });
    }

    private void handleSubjectSelection(int subjectId, String subjectName) {
        try {
            if (Main.currentUser == null || Main.currentUser.getId() <= 0) {
                showAlert(AlertType.ERROR, "Login Required", "Please log in as a teacher to create a quiz.");
                return; // Stop execution if no valid user is logged in
            }

            Subject subject = subjectDao.getSubjectById(subjectId);
            if (subject == null) {
                showAlert(AlertType.ERROR, "Subject Not Found", "The selected subject does not exist.");
                return;
            }

            Quiz quiz = new Quiz();
            quiz.setTeacherId(Main.currentUser.getId());
            quiz.setSubjectId(subject.getId());
            quiz.setSubjectName(subjectName);

            int quizId = quizDao.createQuiz(quiz);
            if (quizId == -1) {
                System.err.println("Failed to create quiz: DAO returned -1");
                showAlert(AlertType.ERROR, "Quiz Creation Failed", "Could not create quiz. Please try again.");
                return;
            }

            quiz.setQuizId(quizId);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateQuiz.fxml"));
            Parent createQuizRoot = loader.load();

            CreateQuizController controller = loader.getController();
            controller.setQuiz(quiz);
            controller.setSubjectName(subjectName);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(createQuizRoot));
            stage.show();
        } catch (IOException ex) {
            System.err.println("Error loading CreateQuiz.fxml: " + ex.getMessage());
            showAlert(AlertType.ERROR, "Navigation Error", "Could not load the quiz creation screen. Please check FXML path.");
            ex.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Error in handleSubjectSelection:");
            showAlert(AlertType.ERROR, "Application Error", "An unexpected error occurred during quiz creation.");
            ex.printStackTrace();
        }
    }

    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseOptions.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading ChooseOptions.fxml: " + e.getMessage());
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Could not go back to options. Please check FXML path.");
        }
    }

    private Image loadImage(String imagePath) {
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is == null) {
                System.err.println("Image not found: " + imagePath);
                return null;
            }
            return new Image(is);
        } catch (IOException e) {
            System.err.println("Error loading image: " + imagePath + " - " + e.getMessage());
            return null;
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
