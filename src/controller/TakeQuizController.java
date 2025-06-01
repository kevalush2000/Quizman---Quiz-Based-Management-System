package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import model.Quiz;
import model.Question;
import model.Answer;
import dao.QuestionDAO;
import dao.AnswerDAO;
import dao.QuizResultsDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public class TakeQuizController {
    @FXML private StackPane root;
    @FXML private ImageView backgroundImageView;
    @FXML private ImageView logoImageView;
    @FXML private Label questionNumber;
    @FXML private Label questionText;
    @FXML private Label choice1, choice2, choice3, choice4;
    @FXML private Rectangle rect113, rect114, rect115, rect116;
    @FXML private Button nextButton;
    @FXML private Button submitButton;

    private Quiz quiz;
    private List<Question> questions;
    private int currentIndex = 0;
    private Map<Integer, Integer> userAnswers = new HashMap<>();
    private List<List<Answer>> allAnswers;

    private Rectangle[] rects;
    private Label[] choices;

    @FXML
    public void initialize() {
        // Set responsive background
        backgroundImageView.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student takequiz/space-wallpaper-stars-background.gif")));
        backgroundImageView.fitWidthProperty().bind(root.widthProperty());
        backgroundImageView.fitHeightProperty().bind(root.heightProperty());
        backgroundImageView.setPreserveRatio(false);

        // Set responsive logo
        logoImageView.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student takequiz/QUIZMANLOGO.png")));
        logoImageView.fitHeightProperty().bind(root.heightProperty().multiply(0.16));
        logoImageView.setPreserveRatio(true);

        rects = new Rectangle[]{rect113, rect114, rect115, rect116};
        choices = new Label[]{choice1, choice2, choice3, choice4};

        // Add click listeners for choices
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            rects[i].setOnMouseClicked(e -> selectChoice(idx));
            choices[i].setOnMouseClicked(e -> selectChoice(idx));
        }

        nextButton.setOnAction(e -> showNextQuestion());
        submitButton.setOnAction(e -> handleSubmit());

        submitButton.setDisable(true); // Only enabled on last question
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        loadQuestions();
        showQuestion(0);
    }

    private void loadQuestions() {
        questions = new QuestionDAO().getQuestionsByQuiz(quiz.getQuizId());
        allAnswers = new java.util.ArrayList<>();
        for (Question q : questions) {
            allAnswers.add(new AnswerDAO().getAnswersByQuestion(q.getId()));
        }
    }

    private void showQuestion(int idx) {
        Question q = questions.get(idx);
        questionNumber.setText("QUESTION " + (idx + 1));
        questionText.setText(q.getText());

        List<Answer> answers = allAnswers.get(idx);
        for (int i = 0; i < 4; i++) {
            choices[i].setText(answers.get(i).getText());
            rects[i].setOpacity(1.0);
            choices[i].setOpacity(1.0);
            rects[i].setStrokeWidth(0);
        }

        // Highlight selected answer if any
        Integer selected = userAnswers.get(q.getId());
        if (selected != null) {
            for (int i = 0; i < 4; i++) {
                if (answers.get(i).getId() == selected) {
                    rects[i].setStroke(Color.LIME);
                    rects[i].setStrokeWidth(5);
                }
            }
        }

        // Button logic
        nextButton.setDisable(idx == questions.size() - 1);
        submitButton.setDisable(idx != questions.size() - 1);
    }

    private void selectChoice(int idx) {
        Question q = questions.get(currentIndex);
        int answerId = allAnswers.get(currentIndex).get(idx).getId();
        userAnswers.put(q.getId(), answerId);

        // Visual feedback
        for (int i = 0; i < 4; i++) {
            rects[i].setStrokeWidth(0);
        }
        rects[idx].setStroke(Color.LIME);
        rects[idx].setStrokeWidth(5);
    }

    private void showNextQuestion() {
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            showQuestion(currentIndex);
        }
    }

    @FXML
    private void handleSubmit() {
        int correct = 0;
        AnswerDAO answerDao = new AnswerDAO();
        for (Map.Entry<Integer, Integer> entry : userAnswers.entrySet()) {
            if (answerDao.isAnswerCorrect(entry.getValue())) {
                correct++;
            }
        }

        double score = (double) correct / questions.size() * 100;
        new QuizResultsDAO().saveResult(app.Main.getCurrentUser().getId(), quiz.getQuizId(), score);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuizResults.fxml"));
            Parent resultsRoot = loader.load();

            QuizResultsController controller = loader.getController();
            controller.setScore(score, questions.size());
            controller.setQuiz(quiz);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(resultsRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}