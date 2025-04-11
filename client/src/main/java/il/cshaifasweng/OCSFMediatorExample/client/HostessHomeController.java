package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;

public class HostessHomeController {
    @FXML
    private void handleWalkIn() {
        NavigationController.getInstance().loadPage("walk-in-assignment");
    }

    @FXML private void handleOccupancyMap() {
        NavigationController.getInstance().loadPage("occupancy-map");
    }
}
