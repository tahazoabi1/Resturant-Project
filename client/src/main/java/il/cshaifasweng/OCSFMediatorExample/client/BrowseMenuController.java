package il.cshaifasweng.OCSFMediatorExample.client;

import com.mysql.cj.xdevapi.Client;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.getClient;

import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class BrowseMenuController {

    @FXML
    private AnchorPane mainAnchor;
    @FXML private ListView<Branch> branchListView;
    @FXML private ListView<MenuItem> menuItemListView;

    // Subscribe to receive the lists of branches and menu items
    @Subscribe
    public void onReceiveBranches(List<Branch> branches) {
        if(branches.get(0) instanceof Branch) {
            System.out.println("Received branches: " + branches.size());
            branchListView.getItems().setAll(branches);
        }
    }

    @Subscribe
    public void onReceiveMenuItems(List<MenuItem> menuItems) {
        if(menuItems.get(0) instanceof MenuItem) {
            System.out.println("Received menu items: " + menuItems.size());
            menuItemListView.getItems().setAll(menuItems);
        }
    }

    @FXML
    private void initialize() throws IOException {
        EventBus.getDefault().register(this);  // Register the controller to listen for events
        try {
            getClient().sendToServer("get all branches");
            getClient().sendToServer("get all MenuItems");
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    // Optional: Display error message if an issue occurs
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
