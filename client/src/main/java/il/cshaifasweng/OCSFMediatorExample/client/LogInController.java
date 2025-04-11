package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.Hostess;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.Main.switchScreen;

public class LogInController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button logInButton;


    // Method triggered when the LogIn button is clicked
    @FXML
    private void logIn(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email and password.");
            return;
        }
        else {
            try {
                Main.client.sendToServer("trying To LogIn " + "#" + email + "#" + password);
            } catch (IOException e) {
                System.err.println("Something Error in Login button: " + e.getMessage());
            }
        }
    }

    @Subscribe
    public void authenticateUser(User user) {
        if(user!= null) {
            Main.user = user;
            String role = (Main.user != null) ? Main.user.getRole() : "";
            user.signIn();
            javafx.application.Platform.runLater(() -> {
                NavigationController.getInstance().updateLogInStatus();
                switch (role) {
                    case "Customer":
                        NavigationController.getInstance().loadPage("HomePages/customer-home");
                        showAlert("Success", "Logged in successfully!");
                        break;
                    case "Dietitian":
                        NavigationController.getInstance().loadPage("HomePages/dietitian-home");
                        showAlert("Success", "Logged in successfully!");
                        break;
                    case "Hostess":
                        NavigationController.getInstance().loadPage("HomePages/hostess-home");
                        showAlert("Success", "Logged in successfully!");
                        break;
                    case "Manager":
                        NavigationController.getInstance().loadPage("HomePages/branch-manager-home");
                        showAlert("Success", "Logged in successfully!");
                        break;
                    case "NetworkManager":
                        NavigationController.getInstance().loadPage("HomePages/network-manager-home");
                        showAlert("Success", "Logged in successfully!");
                        break;
                    case "ServiceWorker":
                        NavigationController.getInstance().loadPage("HomePages/service-worker-home");
                        showAlert("Success", "Logged in successfully!");
                        break;
                    default:
                        NavigationController.getInstance().loadPage("first-page");
                        showAlert("Notice", "Role not recognized, redirecting to home.");
                        break;
                }
            });
        }
        else {
            javafx.application.Platform.runLater(() -> {
                showAlert("Invalid", "Something went wrong with email or password. or the user logged in in another client");
            });
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Initialize method to register the controller to listen for events
    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);  // Register the controller to listen for events
    }

    // Cleanup method to unregister the controller from the EventBus to prevent memory leaks
    public void cleanup() {
        EventBus.getDefault().unregister(this);  // Unregister the controller
    }
}
