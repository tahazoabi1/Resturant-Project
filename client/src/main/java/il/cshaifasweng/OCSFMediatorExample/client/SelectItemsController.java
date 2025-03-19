package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.util.List;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SelectItemsController {

    @FXML
    private ListView<String> itemsListView;

    @FXML
    private Button confirmButton;

    private String selectedBranch;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this); // Register EventBus listener
    }

    // Method to set the branch selected by the customer
    public void setSelectedBranch(String branch) {
        this.selectedBranch = branch;
        loadItemsForBranch(branch);
    }

    // Send request to the server to get items for the selected branch
    private void loadItemsForBranch(String branch) {
        try {
            String request = "get menu items for branch#" + branch;
            SimpleClient.getClient().sendToServer(request); // Send request to server
            System.out.println("📨 Sent request to server: " + request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle received menu items from the server
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMenuItemsReceived(List<MenuItem> menuItems) {
        System.out.println("✅ Received " + menuItems.size() + " items from the server.");

        ObservableList<String> itemNames = FXCollections.observableArrayList();
        for (MenuItem item : menuItems) {
            itemNames.add(item.getName() + " - $" + item.getPrice());
        }

        itemsListView.setItems(itemNames);
        itemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    // Handle confirmation of selected items
    @FXML
    private void handleConfirmSelection() {
        List<String> selectedItems = itemsListView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            showAlert("Error", "Please select at least one item.", AlertType.ERROR);
            return;
        }

        System.out.println("✅ Selected items: " + selectedItems);
        showAlert("Success", "Items selected successfully!", AlertType.INFORMATION);
    }

    // Utility method to show alerts
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
