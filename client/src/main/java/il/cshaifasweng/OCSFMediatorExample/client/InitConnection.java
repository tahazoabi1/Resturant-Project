package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import static il.cshaifasweng.OCSFMediatorExample.client.Main.switchScreen;

public class InitConnection {
    @FXML
    private TextField host;
    @FXML
    private TextField port;


    @FXML
    void initConnection(ActionEvent event) {
        SimpleClient.newHost = this.host.getText();
        try {
            SimpleClient.newPort = Integer.parseInt(this.port.getText());
        } catch (NumberFormatException e) {
            showError("Invalid Port", "Please enter a valid port number.");
            return;
        }

        // יצירת לקוח
        Main.client = SimpleClient.getClient();

        try {
            // פתיחת חיבור לשרת
            Main.client.openConnection();
            System.out.println("Connected to server at " + SimpleClient.newHost + ":" + SimpleClient.newPort);

            switchScreen("Menu List");
        } catch (IOException e) {
            showError("Connection Failed", "Failed to connect to the server. Please check the host and port.");
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

