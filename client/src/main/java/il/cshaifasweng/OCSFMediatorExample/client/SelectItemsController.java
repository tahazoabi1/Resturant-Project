package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectItemsController {

    @FXML
    private ListView<String> itemsListView; // Display menu items

    @FXML
    private Button addItemButton, confirmOrderButton; // Buttons for adding and confirming order

    private MenuItem selectedItem;
    private List<MenuItem> Items;
    private ObservableList<String> menuItems = FXCollections.observableArrayList(); // Observable list for menu
    private List<String> selectedItems = new ArrayList<>(); // Stores selected items
    private String branchName = Main.branch.getName(); // Store selected branch

    public void initialize() {
        Platform.runLater(() -> {
                    itemsListView.setItems(menuItems); // Link ListView to observable list
                });
        loadItemsForBranch(branchName);
        EventBus.getDefault().register(this); // Register EventBus to receive messages from the server
    }

    // ✅ Server sends menu items -> UI updates
    @Subscribe
    public void onMenuItemsReceived(List<MenuItem> items) {
        Platform.runLater(() -> {
            menuItems.clear();
            this.Items = items;
            for (MenuItem item : items) {
                menuItems.add(item.getName()); // Display item names
            }
            System.out.println("✅ Menu items loaded: " + menuItems);
        });
    }

    public void loadItemsForBranch(String branchName) {
        try {
            SimpleClient.getClient().sendToServer("get menu items for branch#" + branchName);
        } catch (IOException e) {
            System.err.println("❌ Failed to send request: " + e.getMessage());
        }
    }


    @FXML
    private void selectItem() {
        // Get the name of the selected item from the ListView
        String selectedItemName = itemsListView.getSelectionModel().getSelectedItem();

        if (selectedItemName == null) {
            showAlert("No Item Selected", "Please select an item from the list.", Alert.AlertType.WARNING);
            return;
        }

        // Find the MenuItem object that matches the selected name
        for (MenuItem item : Items) {
            if (item.getName().equals(selectedItemName)) {
                selectedItem = item; // Save the selected MenuItem object
                System.out.println("Selected item: " + selectedItem.getName());
                break; // Exit the loop once the item is found
            }
        }

        if (selectedItem != null) {
            // Show confirmation or update UI if necessary
            showAlert("Item Selected", selectedItem.getName() + " is now selected.", Alert.AlertType.INFORMATION);
        }
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}