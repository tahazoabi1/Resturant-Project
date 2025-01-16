package il.cshaifasweng.OCSFMediatorExample.client;


import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static il.cshaifasweng.OCSFMediatorExample.client.App.switchScreen;

public class SecondaryController {

    @FXML
    private void switchToPrimary() {
        switchScreen("Tables");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
