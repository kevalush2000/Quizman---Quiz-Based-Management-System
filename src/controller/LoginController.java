package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import dao.UserDAO;
import java.io.IOException;
import app.Main;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    private UserDAO userDao = new UserDAO();

    @FXML
    public void initialize() {
        // Create default teacher if needed
        userDao.createDefaultTeacherIfNeeded();

        loginButton.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Debug input
        System.out.println("Login attempt for: " + username + " with password: " + password);

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Username and password cannot be empty");
            return;
        }

        try {
            User user = userDao.getUserByUsername(username);

            // Debug user object
            if (user != null) {
                System.out.println("User retrieved from DB: " +
                    user.getUsername() + " | " +
                    user.getPassword() + " | " +
                    user.getEmail() + " | " +
                    user.getRole());
            } else {
                System.out.println("No user found with username: " + username);
            }

            if (user == null) {
                showAlert("Login Failed", "User not found");
                return;
            }

            String storedPassword = user.getPassword();
            if (storedPassword == null) {
                System.out.println("Error: Password is null for user: " + user.getUsername());
                showAlert("Login Error", "Invalid user account configuration");
                return;
            }

            // Debug password comparison
            System.out.println("Comparing:");
            System.out.println("Stored password: " + storedPassword);
            System.out.println("Entered password: " + password);

            if (storedPassword.equals(password)) {
                System.out.println("Passwords match - login successful");
                Main.setCurrentUser(user);
                navigateToChooseOption();
            } else {
                System.out.println("Passwords don't match");
                showAlert("Login Failed", "Invalid password");
            }
        } catch (Exception e) {
            System.out.println("Exception during login: " + e.getMessage());
            e.printStackTrace();
            showAlert("Login Error", "An error occurred during login");
        }
    }

    private void navigateToChooseOption() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SplashScreen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading ChooseOptions.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
