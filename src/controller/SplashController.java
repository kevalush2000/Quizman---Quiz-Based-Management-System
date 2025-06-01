package controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;

public class SplashController {
    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImage;
    @FXML private ImageView logoImage;
    @FXML private ImageView pressStartImage;

    @FXML
    public void initialize() {
        // Load images
        backgroundImage.setImage(new Image("/QUIZ ASSETS/images/splashbg.gif"));
        logoImage.setImage(new Image("/QUIZ ASSETS/images/QUIZMANLOGO.png"));
        pressStartImage.setImage(new Image("/QUIZ ASSETS/images/PRESS START.png"));

        // Responsive bindings
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        logoImage.fitWidthProperty().bind(rootPane.widthProperty().multiply(0.35));
        logoImage.fitHeightProperty().bind(rootPane.heightProperty().multiply(0.25));
        pressStartImage.fitWidthProperty().bind(rootPane.widthProperty().multiply(0.5));

        // Start both invisible
        logoImage.setOpacity(0);
        pressStartImage.setOpacity(0);

        // Fade in logo
        FadeTransition fadeLogo = new FadeTransition(Duration.seconds(1.5), logoImage);
        fadeLogo.setFromValue(0);
        fadeLogo.setToValue(1);
        fadeLogo.setDelay(Duration.seconds(0.5));

        // Fade in "PRESS START" after logo
        FadeTransition fadePressStart = new FadeTransition(Duration.seconds(1.5), pressStartImage);
        fadePressStart.setFromValue(0);
        fadePressStart.setToValue(1);
        fadePressStart.setDelay(Duration.seconds(2.0));

        fadeLogo.play();
        fadePressStart.play();

        // Add click handler to "Press Start"
        pressStartImage.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseRole.fxml"));
                Parent chooseRoleRoot = loader.load();
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene chooseRoleScene = new Scene(chooseRoleRoot, rootPane.getScene().getWidth(), rootPane.getScene().getHeight());
                stage.setScene(chooseRoleScene);
            } catch (Exception e) {
                e.printStackTrace(); // Remove Platform.exit() for debugging
                // Platform.exit(); // Comment this out for now
            }
        });
    }
}