package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomizeItemsController {

    @FXML
    private ListView<String> itemsListView;
    @FXML
    private Button addItemButton, changePreferencesButton, confirmOrderButton;

    private ObservableList<String> menuItems = FXCollections.observableArrayList();
    private List<String> selectedItems = new ArrayList<>();

    public void initialize() {
        itemsListView.setItems(menuItems);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onMenuItemsReceived(List<MenuItem> items) {
        Platform.runLater(() -> {
            menuItems.clear();
            for (MenuItem item : items) {
                menuItems.add(item.getName());
            }
        });
    }

    @FXML
    private void handleAddItem(ActionEvent event) {
        String selectedItem = itemsListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            selectedItems.add(selectedItem);
            showAlert("Item Added", selectedItem + " added to your order.", Alert.AlertType.INFORMATION);
        }
        System.out.println("✅ Item added: " + selectedItem);
    }

    @FXML
    private void handleChangePreferences(ActionEvent event) {
        String selectedItem = itemsListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            showAlert("Change Preferences", "You can customize your preferences for " + selectedItem + ".", Alert.AlertType.INFORMATION);
        }
        System.out.println("✅ Change preferences clicked for: " + selectedItem);
    }

    @FXML
    private void handleConfirmOrder(ActionEvent event) {
        if (selectedItems.isEmpty()) {
            showAlert("No Items Ordered", "You haven't selected any items.", Alert.AlertType.WARNING);
            return;
        }
        showAlert("Order Confirmed", "Your order has been placed.", Alert.AlertType.INFORMATION);
        selectedItems.clear();
        System.out.println("✅ Order confirmed!");
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}