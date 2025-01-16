package il.cshaifasweng.OCSFMediatorExample.client;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PrimaryController {
	@FXML
	private Button menu_list_button;

	@FXML
	void loadMenu(ActionEvent event) {
		try {
			// שליחת בקשה לשרת לקבלת כל המנות
			System.out.println("Requesting all menu items from server...");
			SimpleClient.getClient().sendToServer("get all menu items");

		} catch (IOException e) {
			// טיפול בשגיאה במקרה של כשל בשליחת הבקשה
			System.err.println("Failed to send request to server: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
