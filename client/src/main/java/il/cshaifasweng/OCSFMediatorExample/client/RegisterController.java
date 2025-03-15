package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.server.ConnectToDataBase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField salaryField;

    @FXML
    private TextField branchField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String salaryStr = salaryField.getText();
        String branchName = branchField.getText(); // Assuming branch is entered as a name

        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() ||
                password.isEmpty() || salaryStr.isEmpty() || branchName.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        if (!phoneNumber.matches("\\d+")) {
            showAlert("Error", "Phone number must contain only digits!");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryStr);
        } catch (NumberFormatException e) {
            showAlert("Error", "Salary must be a valid number!");
            return;
        }

        // Get branch object from the database
        Branch branch = ConnectToDataBase.getBranchByName(branchName);
        if (branch == null) {
            showAlert("Error", "Branch not found!");
            return;
        }

        // Call the method to add worker
        ConnectToDataBase.addWorker(name, phoneNumber, email, password, salary, branch);

        showAlert("Success", "Worker registered successfully!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
