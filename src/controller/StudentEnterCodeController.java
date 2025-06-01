package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Quiz;
import dao.QuizDAO;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.ScaleTransition;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.geometry.Pos;

public class StudentEnterCodeController {
    @FXML private StackPane root;
    @FXML private TextField codeField;
    @FXML private ImageView background;
    @FXML private ImageView logo;
    @FXML private ImageView submitButtonImage;

    // Add this field for the error image
    private ImageView errorImageView;

    // Add this field for the purple arrow back button
    private ImageView backArrowImageView;

    private QuizDAO quizDao = new QuizDAO();

    @FXML
    public void initialize() {
        // Set images
        background.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student code/STUDENT ROLE BG.png")));
        background.fitWidthProperty().bind(root.widthProperty());
        background.fitHeightProperty().bind(root.heightProperty());

        logo.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student code/QUIZMANLOGO.png")));
        logo.fitHeightProperty().bind(root.heightProperty().multiply(0.18));
        logo.setPreserveRatio(true);

        submitButtonImage.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student code/ENTER CODE BUTTON.png")));
        submitButtonImage.fitHeightProperty().bind(root.heightProperty().multiply(0.10));
        submitButtonImage.setPreserveRatio(true);

        // Purple arrow back button setup (top left)
        backArrowImageView = new ImageView();
        backArrowImageView.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student code/purplearrow.png")));
        backArrowImageView.setPreserveRatio(true);
        backArrowImageView.fitHeightProperty().bind(root.heightProperty().multiply(0.09));
        backArrowImageView.setStyle("-fx-cursor: hand;");
        backArrowImageView.setRotate(-90); // Rotates the arrow to face left
        // Position at top left using StackPane alignment
        StackPane.setAlignment(backArrowImageView, Pos.TOP_LEFT);
        StackPane.setMargin(backArrowImageView, new javafx.geometry.Insets(24, 0, 0, 24));
        root.getChildren().add(backArrowImageView);

        backArrowImageView.setOnMouseClicked(e -> handleBack());

        // Hover animation for back arrow
        backArrowImageView.setOnMouseEntered(e -> {
            backArrowImageView.setScaleX(1.12);
            backArrowImageView.setScaleY(1.12);
            backArrowImageView.setOpacity(0.8);
        });
        backArrowImageView.setOnMouseExited(e -> {
            backArrowImageView.setScaleX(1.0);
            backArrowImageView.setScaleY(1.0);
            backArrowImageView.setOpacity(1.0);
        });

        // Error image setup (hidden by default)
        errorImageView = new ImageView();
        errorImageView.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student code/CODE ERROR!.png")));
        errorImageView.setPreserveRatio(true);
        errorImageView.fitHeightProperty().bind(root.heightProperty().multiply(0.10));
        errorImageView.setVisible(false);
        root.getChildren().add(errorImageView); // Add to StackPane so it overlays

        // Load and set Press Start 2P font for the input field
        javafx.scene.text.Font pressStartFont = javafx.scene.text.Font.loadFont(
            getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 32
        );
        if (pressStartFont != null) {
            codeField.setFont(pressStartFont);
        } else {
            System.err.println("Press Start 2P font not found!");
        }

        // Hover animation for button
        submitButtonImage.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), submitButtonImage);
            st.setToX(1.08);
            st.setToY(1.08);
            st.play();
            submitButtonImage.setOpacity(0.85);
        });
        submitButtonImage.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), submitButtonImage);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            submitButtonImage.setOpacity(1.0);
        });

        // Submit action
        submitButtonImage.setOnMouseClicked(e -> handleSubmit());
        codeField.setOnAction(e -> handleSubmit());

        codeField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > 4) {
                codeField.setText(newText.substring(0, 4));
            }
        });

        backArrowImageView.setScaleX(-1); // Flips the image horizontally
    }

    private void showErrorImage() {
        errorImageView.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> errorImageView.setVisible(false));
        pause.play();
    }

    private void handleSubmit() {
        String code = codeField.getText().trim();
        if (code.length() != 4) {
            showErrorImage();
            System.err.println("Invalid access code length.");
            return;
        }

        Quiz quiz = quizDao.getQuizByAccessCode(code);
        if (quiz == null) {
            showErrorImage();
            System.err.println("Quiz not found with the provided access code.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TakeQuiz.fxml"));
            Parent takeQuizRoot = loader.load();

            TakeQuizController controller = loader.getController();
            controller.setQuiz(quiz);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(takeQuizRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseRole.fxml"));
            Parent rootNode = loader.load();
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(rootNode));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
