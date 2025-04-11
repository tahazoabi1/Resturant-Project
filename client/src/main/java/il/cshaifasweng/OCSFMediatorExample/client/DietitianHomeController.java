package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;

public class DietitianHomeController {
    @FXML
    private void handleEditMenu() {
        NavigationController.getInstance().loadPage("edit-menu");
    }

    @FXML private void handleViewStatus() {
        NavigationController.getInstance().loadPage("status-of-changes");
    }
}

