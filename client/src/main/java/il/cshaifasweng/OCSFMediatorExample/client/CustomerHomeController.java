package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class CustomerHomeController {

    @FXML
    private void handleBrowseMenu() {
        NavigationController.getInstance().loadPage("SelectItems");
    }

    @FXML
    private void handleOrderHistory() {
        NavigationController.getInstance().loadPage("order-history");
    }

    @FXML
    private void handleReservation() {
        NavigationController.getInstance().loadPage("reservations");
    }
}
