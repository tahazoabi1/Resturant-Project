package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.server.ConnectToDataBase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class customerRegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField paymentMethodField;

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String paymentMethod = paymentMethodField.getText();

        // Validate input fields
        if (name.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() ||
                email.isEmpty() || password.isEmpty() || paymentMethod.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        // Validate phone number format (digits only)
        if (!phoneNumber.matches("\\d+")) {
            showAlert("Error", "Phone number must contain only digits!");
            return;
        }

        // Register customer in the database
        ConnectToDataBase.addCustomer(name, phoneNumber, address, email, password, paymentMethod);
        showAlert("Success", "Customer registered successfully!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
