package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;

public class BranchManagerHomeController {
    @FXML
    private void handleReports() {
        NavigationController.getInstance().loadPage("branch-report");
    }

    @FXML private void handleComplaints() {
        NavigationController.getInstance().loadPage("branch-complaints");
    }
}
