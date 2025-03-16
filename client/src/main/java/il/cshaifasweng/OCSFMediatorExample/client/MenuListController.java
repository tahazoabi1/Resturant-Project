package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import static il.cshaifasweng.OCSFMediatorExample.client.Main.switchScreen;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.getClient;

public class MenuListController {
    @FXML
    private ListView<String> list_of_menu_items;
    public static List<MenuItem> allMenuItems = new ArrayList<>();
    private MenuItem selectedMenuItem = null;
    @FXML
    private AnchorPane menuListPane;
    @FXML
    private Button updateButton;
    @FXML
    private TextField priceField;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
        try {
            getClient().sendToServer("get all MenuItems");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void updateButtonClicked(ActionEvent event) {
        if (this.selectedMenuItem == null) {
            this.showCompletionMessage("Error", "Please select a menu item to update.");
        } else if (this.priceField.getText().isEmpty()) {
            this.showCompletionMessage("Error", "Please enter a new price.");
        } else {
            try {
                double newPrice = Double.parseDouble(this.priceField.getText());
                if (newPrice <= 0) {
                    this.showCompletionMessage("Error", "Price must be greater than zero.");
                    return;
                }

                // Send update request to server
                getClient().sendToServer("Update price @" + this.selectedMenuItem.getId() + "@" + newPrice);
                this.showCompletionMessage("Success", "Price updated successfully!");
            } catch (NumberFormatException e) {
                this.showCompletionMessage("Error", "Invalid price format. Please enter a valid number.");
                this.priceField.clear();
            } catch (IOException e) {
                this.showCompletionMessage("Error", "Failed to send update request to server.");
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void updateTable(MenuItem item) {
        System.out.println("Updating single menu item");
        Platform.runLater(() -> {
            // Find and update the item in the list
            for (int i = 0; i < allMenuItems.size(); i++) {
                if (allMenuItems.get(i).getId() == item.getId()) {
                    allMenuItems.set(i, item);
                    updateListView();
                    break;
                }
            }
        });
    }

    @Subscribe
    public void initTable(List<MenuItem> menuItems) {
        System.out.println("Initializing menu items table");
        allMenuItems = menuItems;
        if (allMenuItems.isEmpty()) {
            switchScreen("primary");
        } else {
            // Display list of menu items
            Platform.runLater(() -> {
                list_of_menu_items.getItems().clear();
                for (MenuItem item : allMenuItems) {
                    ObservableList<String> items = this.list_of_menu_items.getItems();
                    items.add("Item ID: " + item.getId() + " - " + item.getName() + " - $" + item.getPrice());
                }

                // Setup item selection handler
                this.list_of_menu_items.setOnMouseClicked(event -> {
                    String selectedItem = this.list_of_menu_items.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        String[] parts = selectedItem.split(" - ");
                        if (parts.length > 0) {
                            String itemIdString = parts[0].trim();
                            String itemId = itemIdString.substring("Item ID: ".length());
                            int id = Integer.parseInt(itemId);

                            for (MenuItem item : allMenuItems) {
                                if (item.getId() == id) {
                                    this.selectedMenuItem = item;
                                    break;
                                }
                            }
                        }
                    }
                });
            });
        }
    }

    private void updateListView() {
        Platform.runLater(() -> {
            list_of_menu_items.getItems().clear();
            for (MenuItem item : allMenuItems) {
                list_of_menu_items.getItems().add(
                        String.format("Item ID: %d - %s - $%.2f",
                                item.getId(),
                                item.getName(),
                                item.getPrice())
                );
            }
        });
    }

    private void showCompletionMessage(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}