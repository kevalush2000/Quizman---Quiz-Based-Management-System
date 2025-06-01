package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import model.Quiz;
import model.Question;
import model.Answer;
import dao.QuestionDAO;
import dao.AnswerDAO;
import java.io.IOException;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

public class ReviewQuizAnswersController {
    @FXML private VBox questionsContainer;
    @FXML private Label quizTitleLabel;
    @FXML private Button backButton;
    @FXML private ImageView logo;
    @FXML private StackPane root;
    @FXML private ImageView backgroundImageView;

    private Quiz quiz;
    private QuestionDAO questionDao = new QuestionDAO();
    private AnswerDAO answerDao = new AnswerDAO();
    private double score;
    private int totalQuestions;

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        quizTitleLabel.setText(quiz.getSubjectName() + " Quiz Answers");
        loadQuestionsAndAnswers();
    }

    private void loadQuestionsAndAnswers() {
        questionsContainer.getChildren().clear();
        List<Question> questions = questionDao.getQuestionsByQuiz(quiz.getQuizId());

        // Load custom font
        Font pressStartFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 16);

        int qNum = 1;
        for (Question question : questions) {
            // Question label
            Label questionLabel = new Label("Q" + qNum + ". " + question.getText());
            questionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #7faaff; -fx-font-size: 16px;");
            if (pressStartFont != null) questionLabel.setFont(pressStartFont);
            questionLabel.setWrapText(true);
            questionLabel.setPadding(new Insets(10, 0, 2, 0));

            VBox answerBox = new VBox(5);
            answerBox.setPadding(new Insets(0, 0, 10, 20));

            List<Answer> answers = answerDao.getAnswersByQuestion(question.getId());
            for (Answer answer : answers) {
                Label answerLabel = new Label(answer.getText());
                if (pressStartFont != null) answerLabel.setFont(pressStartFont);
                answerLabel.setWrapText(true);
                if (answer.isCorrect()) {
                    answerLabel.setStyle("-fx-text-fill: #38ef7d; -fx-font-weight: bold;");
                } else {
                    answerLabel.setStyle("-fx-text-fill: #ff512f;");
                }
                answerBox.getChildren().add(answerLabel);
            }

            questionsContainer.getChildren().addAll(questionLabel, answerBox);
            qNum++;
        }
    }

    @FXML
    private void handleBackToResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuizResults.fxml"));
            Parent resultsRoot = loader.load();

            QuizResultsController controller = loader.getController();
            controller.setQuiz(quiz);
            controller.setScore(score, totalQuestions); // <-- Pass score and total

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(resultsRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        // Responsive background
        if (backgroundImageView != null && root != null) {
            InputStream bgStream = getClass().getResourceAsStream("/QUIZ ASSETS/Student takequiz/quiz answer bg.png");
            if (bgStream == null) {
                System.err.println("Quiz answer background not found!");
            } else {
                backgroundImageView.setImage(new Image(bgStream));
                backgroundImageView.fitWidthProperty().bind(root.widthProperty());
                backgroundImageView.fitHeightProperty().bind(root.heightProperty());
                backgroundImageView.setPreserveRatio(false);
            }
        }

        // Load and set the logo image
        InputStream logoStream = getClass().getResourceAsStream("/QUIZ ASSETS/Student takequiz/QUIZMANLOGO.png");
        if (logoStream == null) {
            System.err.println("Logo image not found! Check the path and filename.");
        } else {
            logo.setImage(new Image(logoStream));
        }
    }

    public void setScoreAndTotal(double score, int totalQuestions) {
        this.score = score;
        this.totalQuestions = totalQuestions;
    }
}
