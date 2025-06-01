package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.Node;
import model.User;

import app.Main;

public class ChooseRoleController {
    @FXML private StackPane root;
    @FXML private ImageView background;
    @FXML private ImageView logo;
    @FXML private ImageView teacherImage;
    @FXML private ImageView studentImage;
    @FXML private ImageView title;

    @FXML
    public void initialize() {
        // Set images for all ImageViews
        background.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/choose a role assets/bg_space 1.png")));
        logo.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/choose a role assets/QUIZMANLOGO.png")));
        title.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/choose a role assets/Choose a role text.png")));
        teacherImage.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/choose a role assets/TEACHER BUTTON.png")));
        studentImage.setImage(new Image(getClass().getResourceAsStream("/QUIZ ASSETS/choose a role assets/STUDENT BUTTON.png")));

        setupResponsiveDesign();
        setupButtonAnimations();
    }

    private void setupResponsiveDesign() {
        // Background - stretch to fill
        background.fitWidthProperty().bind(root.widthProperty());
        background.fitHeightProperty().bind(root.heightProperty());

        // Logo - at the top, responsive but smaller than buttons
        logo.fitHeightProperty().bind(root.heightProperty().multiply(0.18));
        logo.setPreserveRatio(true);

        // Title - a bit smaller than the buttons
        title.fitHeightProperty().bind(root.heightProperty().multiply(0.10));
        title.setPreserveRatio(true);

        // Teacher and Student images - bigger than the title
        teacherImage.fitHeightProperty().bind(root.heightProperty().multiply(0.38));
        teacherImage.setPreserveRatio(true);
        studentImage.fitHeightProperty().bind(root.heightProperty().multiply(0.38));
        studentImage.setPreserveRatio(true);
    }

    private void setupButtonAnimations() {
        addHoverAnimation(teacherImage);
        addHoverAnimation(studentImage);

        teacherImage.setOnMouseClicked(e -> handleTeacherSelection(e));
        studentImage.setOnMouseClicked(e -> handleStudentSelection(e));
    }

    private void handleTeacherSelection(MouseEvent e) {
        try {
            // Create a temporary teacher user
            User teacher = new User(1, "teacher", "", "teacher", null);
            Main.setCurrentUser(teacher);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseOptions.fxml"));
            Parent teacherRoot = loader.load();
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(teacherRoot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleStudentSelection(MouseEvent e) {
        try {
            // Create a temporary student user
            User student = new User(0, "student", "", "student", null);
            Main.setCurrentUser(student);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudentEnterCode.fxml"));
            Parent studentRoot = loader.load();
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(studentRoot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addHoverAnimation(ImageView img) {
        img.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), img);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });

        img.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), img);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }
}
