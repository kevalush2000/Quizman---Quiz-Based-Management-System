package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Question;
import model.Quiz;
import dao.QuestionDAO;
import dao.AnswerDAO;
import utils.CodeGenerator;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;

public class CreateQuizController implements Initializable {
    @FXML private VBox questionsContainer;
    @FXML private Button addQuestionButton;
    @FXML private Button finishQuizButton;
    @FXML private Label subjectLabel;

    // For responsive background
    @FXML private StackPane root;
    @FXML private ImageView backgroundImageView;

    private Quiz quiz;
    private String subjectName;

    private QuestionDAO questionDao = new QuestionDAO();
    private AnswerDAO answerDao = new AnswerDAO();

    private List<VBox> questionBlocks = new ArrayList<>();

    private Font pressStartFont;

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
        subjectLabel.setText("Subject: " + subjectName);
        if (pressStartFont != null) subjectLabel.setFont(pressStartFont);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load custom font
        pressStartFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 16);

        // Responsive background
        if (backgroundImageView != null && root != null) {
            backgroundImageView.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student takequiz/quiz answer bg.png")));
            backgroundImageView.fitWidthProperty().bind(root.widthProperty());
            backgroundImageView.fitHeightProperty().bind(root.heightProperty());
            backgroundImageView.setPreserveRatio(false);
        }

        if (pressStartFont != null) {
            subjectLabel.setFont(pressStartFont);
            addQuestionButton.setFont(pressStartFont);
            finishQuizButton.setFont(pressStartFont);
        }

        addQuestionButton.setOnAction(event -> addQuestion());
        finishQuizButton.setOnAction(event -> finishQuiz());
        addQuestion();
    }

    private void addQuestion() {
        int questionNumber = questionBlocks.size() + 1;

        VBox questionBlock = new VBox(10);
        questionBlock.setPadding(new Insets(10, 0, 10, 0));
        questionBlock.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; -fx-padding: 10; -fx-background-color: #f9f9f9;");

        Label qLabel = new Label("Question " + questionNumber + ":");
        qLabel.setStyle("-fx-font-weight: bold;");
        if (pressStartFont != null) qLabel.setFont(pressStartFont);

        TextArea questionField = new TextArea();
        questionField.setPromptText("Enter question text");
        questionField.setPrefRowCount(2);
        questionField.setWrapText(true);
        if (pressStartFont != null) questionField.setFont(pressStartFont);

        questionBlock.getChildren().addAll(qLabel, questionField);

        ToggleGroup group = new ToggleGroup();
        for (int i = 0; i < 4; i++) {
            HBox optionRow = new HBox(5);
            optionRow.setPadding(new Insets(0, 0, 0, 10));

            RadioButton rb = new RadioButton();
            rb.setToggleGroup(group);
            rb.setUserData(i);

            TextField optionField = new TextField();
            optionField.setPromptText("Option " + (char)('A' + i));
            optionField.setPrefWidth(300);
            if (pressStartFont != null) optionField.setFont(pressStartFont);

            optionRow.getChildren().addAll(rb, optionField);
            questionBlock.getChildren().add(optionRow);
        }

        questionsContainer.getChildren().add(questionBlock);
        questionBlocks.add(questionBlock);
    }

    private void saveQuestionBlock(VBox questionBlock) {
        String questionText = null;
        List<String> answerTexts = new ArrayList<>();
        int correctOptionIndex = -1;

        for (javafx.scene.Node node : questionBlock.getChildren()) {
            if (node instanceof TextArea) {
                questionText = ((TextArea) node).getText();
                break;
            }
        }

        for (javafx.scene.Node node : questionBlock.getChildren()) {
            if (node instanceof HBox) {
                HBox optionRow = (HBox) node;
                RadioButton rb = null;
                TextField optionField = null;

                for (javafx.scene.Node subNode : optionRow.getChildren()) {
                    if (subNode instanceof RadioButton) {
                        rb = (RadioButton) subNode;
                    } else if (subNode instanceof TextField) {
                        optionField = (TextField) subNode;
                    }
                }

                if (optionField != null) {
                    answerTexts.add(optionField.getText());
                    if (rb != null && rb.isSelected()) {
                        correctOptionIndex = (int) rb.getUserData();
                    }
                }
            }
        }

        if (questionText == null || questionText.trim().isEmpty()) {
            System.out.println("Skipping empty question block.");
            return;
        }

        Question question = new Question(0, quiz.getQuizId(), questionText.trim(), 0);
        int questionId = questionDao.createQuestion(question);

        if (questionId == -1) {
            System.err.println("Failed to create question in DB: " + questionText);
            return;
        }
        question.setId(questionId);

        for (int i = 0; i < answerTexts.size(); i++) {
            String answerText = answerTexts.get(i);
            if (answerText.trim().isEmpty()) {
                continue;
            }
            boolean isCorrect = (i == correctOptionIndex);
            answerDao.createAnswer(questionId, answerText.trim(), isCorrect, i + 1);
        }
    }

    private void finishQuiz() {
        try {
            for (VBox questionBlock : questionBlocks) {
                saveQuestionBlock(questionBlock);
            }

            String accessCode = CodeGenerator.generateUniqueCode();
            quiz.setAccessCode(accessCode);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuizCreated.fxml"));
            Parent quizCreatedRoot = loader.load();

            QuizCreatedController controller = loader.getController();
            controller.setAccessCode(accessCode);

            Stage stage = (Stage) finishQuizButton.getScene().getWindow();
            stage.setScene(new Scene(quizCreatedRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Quiz Creation Failed");
            alert.setContentText("An error occurred while creating the quiz. Please try again.\n" + e.getMessage());
            alert.showAndWait();
        }
    }
}