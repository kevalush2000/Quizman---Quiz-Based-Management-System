package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;


public class Main extends Application {
    public static User currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("QuizMan(Quiz Para Kang Usman)");

        // Set the window icon
        primaryStage.getIcons().add(
            new Image(getClass().getResourceAsStream("/QUIZ ASSETS/Student takequiz/QUIZMANLOGO.png"))
        );

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
