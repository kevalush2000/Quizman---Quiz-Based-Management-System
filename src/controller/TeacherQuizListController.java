package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Quiz;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import dao.QuizDAO;
import app.Main;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.effect.Glow;

public class TeacherQuizListController {
    @FXML private StackPane root;
    @FXML private ImageView background;
    @FXML private ImageView logo;
    @FXML private ImageView title;
    @FXML private ImageView backButton;
    @FXML private ScrollPane quizScrollPane;
    @FXML private VBox quizListContainer;

    private List<Quiz> quizzes;
    private Font gameFont;

    @FXML
    public void initialize() {
        try {
            // Load custom font
            InputStream fontStream = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (fontStream != null) {
                gameFont = Font.loadFont(fontStream, 18);
            } else {
                System.err.println("Font file not found");
                gameFont = Font.font("Arial", 18);
            }

            // Responsive background
            background.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Quiz Posts/QUIZ BG.png")));
            background.fitWidthProperty().bind(root.widthProperty());
            background.fitHeightProperty().bind(root.heightProperty());

            // Responsive logo and title
            logo.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Quiz Posts/QUIZMANLOGO.png")));
            logo.fitHeightProperty().bind(root.heightProperty().multiply(0.18));
            logo.setPreserveRatio(true);

            title.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Quiz Posts/QUIZ POSTS TEXT.png")));
            title.fitHeightProperty().bind(root.heightProperty().multiply(0.11));
            title.setPreserveRatio(true);

            // Responsive back button
            backButton.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Quiz Posts/BACK TO ROLES BUTTON.png")));
            backButton.fitHeightProperty().bind(root.heightProperty().multiply(0.09));
            backButton.setPreserveRatio(true);

            // Responsive quiz list area
            quizScrollPane.prefWidthProperty().bind(root.widthProperty().multiply(0.85));
            quizScrollPane.prefHeightProperty().bind(root.heightProperty().multiply(0.55));
            quizListContainer.setSpacing(30);
            quizListContainer.setAlignment(Pos.CENTER);

            // Back button click
            backButton.setOnMouseClicked(e -> handleBack());

            // Back button hover animation
            backButton.setOnMouseEntered(e -> {
                backButton.setScaleX(1.08);
                backButton.setScaleY(1.08);
                backButton.setOpacity(0.85);
            });
            backButton.setOnMouseExited(e -> {
                backButton.setScaleX(1.0);
                backButton.setScaleY(1.0);
                backButton.setOpacity(1.0);
            });

        } catch (Exception e) {
            System.err.println("Error loading resources: " + e.getMessage());
        }
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
        loadQuizzes();
    }

    private void loadQuizzes() {
        quizListContainer.getChildren().clear();

        if (quizzes == null || quizzes.isEmpty()) {
            Text noQuizzes = new Text("No quizzes available.");
            noQuizzes.setFont(gameFont);
            noQuizzes.setStyle("-fx-fill: white;");
            quizListContainer.getChildren().add(noQuizzes);
            return;
        }

        for (Quiz quiz : quizzes) {
            HBox quizBox = new HBox();
            quizBox.setSpacing(0);
            quizBox.setAlignment(Pos.CENTER);
            quizBox.setStyle("-fx-background-color: rgba(186,104,200,0.7); -fx-background-radius: 18; -fx-border-color: #ffb7ff; -fx-border-width: 2; -fx-border-radius: 18;");
            quizBox.prefWidthProperty().bind(quizListContainer.widthProperty().multiply(0.98));

            // Subject icon
            ImageView subjectIcon = new ImageView();
            String subjectImagePath = "/QUIZ ASSETS/Quiz Posts/" + quiz.getSubjectName().toLowerCase() + "logo.png";
            InputStream subjectStream = getClass().getResourceAsStream(subjectImagePath);
            if (subjectStream != null) {
                subjectIcon.setImage(new Image(subjectStream));
            }
            subjectIcon.setFitHeight(40);
            subjectIcon.setPreserveRatio(true);

            // Quiz info
            Text quizName = new Text("  " + quiz.getSubjectName() + " Quiz #" + quiz.getQuizId() + "  ");
            quizName.setFont(gameFont);
            quizName.setStyle("-fx-fill: white;");

            Text choicesText = new Text("  Number of choices: " + quiz.getQuestionCount() + "  ");
            choicesText.setFont(gameFont);
            choicesText.setStyle("-fx-fill: white;");

            Text codeText = new Text("  Code Number: " + quiz.getAccessCode() + "  ");
            codeText.setFont(gameFont);
            codeText.setStyle("-fx-fill: white;");

            // Add vertical separators
            Text sep1 = new Text("|");
            sep1.setFont(gameFont);
            sep1.setStyle("-fx-fill: #fff0ff;");
            Text sep2 = new Text("|");
            sep2.setFont(gameFont);
            sep2.setStyle("-fx-fill: #fff0ff;");

            quizBox.getChildren().addAll(subjectIcon, quizName, sep1, choicesText, sep2, codeText);

            // Add glow hover effect
            Glow glow = new Glow(0.5);
            quizBox.setOnMouseEntered(e -> quizBox.setEffect(glow));
            quizBox.setOnMouseExited(e -> quizBox.setEffect(null));

            quizListContainer.getChildren().add(quizBox);
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseOptions.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshQuizList() {
        quizListContainer.getChildren().clear();
        List<Quiz> quizzes = QuizDAO.getAllQuizzesForTeacher(Main.getCurrentUser().getId());

        if (quizzes == null || quizzes.isEmpty()) {
            Text noQuizzes = new Text("No quizzes available.");
            noQuizzes.setFont(gameFont);
            noQuizzes.setStyle("-fx-fill: white;");
            quizListContainer.getChildren().add(noQuizzes);
            return;
        }

        for (Quiz quiz : quizzes) {
            HBox quizBox = new HBox();
            quizBox.setSpacing(10);
            quizBox.setAlignment(Pos.CENTER);
            quizBox.setStyle("-fx-background-color: rgba(186,104,200,0.7); -fx-background-radius: 18; -fx-border-color: #ffb7ff; -fx-border-width: 2; -fx-border-radius: 18;");
            quizBox.prefWidthProperty().bind(quizListContainer.widthProperty().multiply(0.98));

            Text quizInfo = new Text("Quiz #" + quiz.getQuizId() + " - " + quiz.getSubjectName() + " - Code: " + quiz.getAccessCode());
            quizInfo.setFont(gameFont);
            quizInfo.setStyle("-fx-fill: white;");

            quizBox.getChildren().add(quizInfo);
            quizListContainer.getChildren().add(quizBox);
        }
    }
}