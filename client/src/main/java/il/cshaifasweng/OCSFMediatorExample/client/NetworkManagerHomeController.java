package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;

public class NetworkManagerHomeController {
    @FXML
    private void handleAllReports() {
        NavigationController.getInstance().loadPage("all-reports");
    }

    @FXML private void handleApproveOffers() {
        NavigationController.getInstance().loadPage("approve-offers");
    }
}
