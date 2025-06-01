package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import model.Quiz;
import app.Main;
import java.io.IOException;
import java.net.URL;

public class QuizResultsController {
    @FXML private VBox root;
    @FXML private Label scoreLabel;
    @FXML private Label messageLabel;
    @FXML private Button showAnswersButton;
    @FXML private Button backToMenuButton;
    @FXML private StackPane stackPane;
    @FXML private ImageView backgroundImageView;
    @FXML private ImageView logoImageView;

    private Quiz quiz;
    double finalScore;
    int totalQuizQuestions;

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setScore(double score, int totalQuestions) {
        this.finalScore = score;
        this.totalQuizQuestions = totalQuestions;
        scoreLabel.setText(String.format("Your score: %.1f%% (%d/%d)",
            score, (int)(score/100 * totalQuestions), totalQuestions));

        if (score >= 80) {
            messageLabel.setText("Excellent work!");
        } else if (score >= 60) {
            messageLabel.setText("Good job!");
        } else {
            messageLabel.setText("Keep practicing!");
        }
    }

    @FXML
    public void initialize() {
        // Responsive background
        backgroundImageView.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student takequiz/space-wallpaper-stars-background.gif")));
        backgroundImageView.fitWidthProperty().bind(stackPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(stackPane.heightProperty());
        backgroundImageView.setPreserveRatio(false);

        // Responsive logo
        logoImageView.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student takequiz/QUIZMANLOGO.png")));
        logoImageView.fitHeightProperty().bind(stackPane.heightProperty().multiply(0.16));
        logoImageView.setPreserveRatio(true);

        // Set font and sky-blue text color
        Font pressStartFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 22);
        if (pressStartFont != null) {
            scoreLabel.setFont(pressStartFont);
            messageLabel.setFont(pressStartFont);
            showAnswersButton.setFont(pressStartFont);
            backToMenuButton.setFont(pressStartFont);

            String gradient = "-fx-text-fill: linear-gradient(to right, #7faaff, #00eaff);";
            scoreLabel.setStyle(gradient);
            messageLabel.setStyle(gradient);
            showAnswersButton.setStyle(gradient);
            backToMenuButton.setStyle(gradient);
        }

        setupButtonHover(showAnswersButton);
        setupButtonHover(backToMenuButton);
    }

    private void setupButtonHover(Button button) {
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), button);
            st.setToX(1.08);
            st.setToY(1.08);
            st.play();
            button.setOpacity(0.85);
        });
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            button.setOpacity(1.0);
        });
    }

    @FXML
    private void handleShowAnswers() {
        if (quiz == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReviewQuizAnswers.fxml"));
            Parent reviewRoot = loader.load();

            ReviewQuizAnswersController controller = loader.getController();
            controller.setQuiz(quiz);
            controller.setScoreAndTotal(finalScore, totalQuizQuestions); // Pass score and total

            Stage stage = (Stage) showAnswersButton.getScene().getWindow();
            stage.setScene(new Scene(reviewRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseRole.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}