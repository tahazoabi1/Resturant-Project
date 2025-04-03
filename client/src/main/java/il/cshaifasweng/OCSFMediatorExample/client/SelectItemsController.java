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

    private ObservableList<String> menuItems = FXCollections.observableArrayList(); // Observable list for menu
    private List<String> selectedItems = new ArrayList<>(); // Stores selected items
    private String branchName; // Store selected branch

    public void initialize() {
        itemsListView.setItems(menuItems); // Link ListView to observable list
        EventBus.getDefault().register(this); // Register EventBus to receive messages from the server
    }

    // ✅ Server sends menu items -> UI updates
    @Subscribe
    public void onMenuItemsReceived(List<MenuItem> items) {
        Platform.runLater(() -> {
            menuItems.clear();
            for (MenuItem item : items) {
                menuItems.add(item.getName()); // Display item names
            }
            System.out.println("✅ Menu items loaded: " + menuItems);
        });
    }

    // ✅ When a branch is selected, request menu items
    public void setSelectedBranch(String branchName) {
        this.branchName = branchName; // Store branch
        System.out.println("✅ Branch selected: " + branchName);
        loadItemsForBranch(branchName);
    }

    public void loadItemsForBranch(String branchName) {
        try {
            SimpleClient.getClient().sendToServer("get menu items for branch#" + branchName);
            System.out.println("✅ Sent request for menu items of branch: " + branchName);
        } catch (IOException e) {
            System.err.println("❌ Failed to send request: " + e.getMessage());
        }
    }

    // ✅ Add selected item to order
    @FXML
    private void handleAddItem(ActionEvent event) {
        String selectedItem = itemsListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showAlert("No Item Selected", "Please select an item.", Alert.AlertType.WARNING);
            return;
        }

        selectedItems.add(selectedItem); // Add to order
        showAlert("Item Added", selectedItem + " added to your order.", Alert.AlertType.INFORMATION);
    }

    // ✅ Send order to server
    @FXML
    private void handleConfirmOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomizeItems.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) confirmOrderButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}