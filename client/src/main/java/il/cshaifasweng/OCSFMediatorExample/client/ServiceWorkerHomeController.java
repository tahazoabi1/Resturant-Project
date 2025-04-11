package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;

public class ServiceWorkerHomeController {
    @FXML
    private void handleOpenComplaints() {
        NavigationController.getInstance().loadPage("open-complaints");
    }

    @FXML private void handleComplaintsHistory() {
        NavigationController.getInstance().loadPage("complaints-history");
    }
}
