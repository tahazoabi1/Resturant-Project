/**
 * Sample Skeleton for 'Dietitian.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Request;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
//import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

public class DietitianController {

    @FXML // fx:id="colPreferences"
    private TableColumn<?, ?> colPreferences; // Value injected by FXMLLoader

    @FXML // fx:id="colIngredients"
    private TableColumn<?, ?> colIngredients; // Value injected by FXMLLoader

    @FXML // fx:id="colName"
    private TableColumn<?, ?> colName; // Value injected by FXMLLoader

    @FXML // fx:id="colPrice"
    private TableColumn<?, ?> colPrice; // Value injected by FXMLLoader

    @FXML // fx:id="colType"
    private TableColumn<?, ?> colId; // Value injected by FXMLLoader
    @FXML
    private TableColumn<?, ?> colBranchDish;

    @FXML
    private TableColumn<?, ?> colChainDish;

    @FXML
    private TableColumn<?, ?> colDelivery;
    @FXML // fx:id="dishTable"
    private TableView<MenuItem> dishTable; // Value injected by FXMLLoader
    public static List<Branch> branches;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colIngredients.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
        colPreferences.setCellValueFactory(new PropertyValueFactory<>("preferences"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colChainDish.setCellValueFactory(new PropertyValueFactory<>("chainDish"));
        colDelivery.setCellValueFactory(new PropertyValueFactory<>("deliveryAvailable"));
//        try {
//            SimpleClient.getClient().sendToServer("get all branches");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //  colType.setCellValueFactory(new PropertyValueFactory<>("image_url"));

        loadMenuItemsFromDB();
    }
    @Subscribe
    private void loadBranch(List<Branch> branches) {
        this.branches = branches;
    }


    private void loadMenuItemsFromDB() {
        try {
            SimpleClient.getClient().sendToServer("get all MenuItems");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onMenuItemsReceived(List<MenuItem> menuItems) {
        if(menuItems.getFirst() instanceof MenuItem) {
            Platform.runLater(() -> {
                dishTable.getItems().setAll(menuItems);
            });
        }

    }

    @FXML
    void onAddDish(ActionEvent event) {
        try {
            EventBus.getDefault().unregister(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/il/cshaifasweng/OCSFMediatorExample/client/DietitianTasks/AddDish.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Dish");
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onConvertDishType(ActionEvent event) {
        MenuItem selectedItem = dishTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            System.out.println("No item selected!");
            return;
        }

        Boolean currentChain = selectedItem.getChainDish();
        if (currentChain == null) currentChain = false;

        // Toggle the chainDish value
        selectedItem.setChainDish(!currentChain);

        try {
            // Send the update message to the server
            SimpleClient.getClient().sendToServer("#updateMenuItemType:" + selectedItem.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Refresh the table view
        dishTable.refresh();
    }

    @FXML
    void onEditDish(ActionEvent event) {
        MenuItem selectedItem = dishTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            System.out.println("No item selected!");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(selectedItem.getIngredients());
        dialog.setTitle("Edit Ingredients");
        dialog.setHeaderText("Edit ingredients for: " + selectedItem.getName());
        dialog.setContentText("New Ingredients:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newIngredients -> {
            selectedItem.setIngredients(newIngredients);
            try {
                SimpleClient.getClient().sendToServer("#updateIngredients:" + selectedItem.getId() + "," + newIngredients);
            } catch (IOException e) {
                e.printStackTrace();
            }
            dishTable.refresh(); // تحديث الجدول
        });


    }

    @FXML
    void onLogout(ActionEvent event) {

    }

    @FXML
    void onRemoveDish(ActionEvent event) {
        MenuItem selectedItem = dishTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            try {
                SimpleClient.getClient().sendToServer("#deleteMenuItem:" + selectedItem.getId());

                dishTable.getItems().remove(selectedItem);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No item selected.");
        }
    }

    @FXML
    void onUpdatePrice(ActionEvent event)
    {
        MenuItem selectedItem = dishTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            System.out.println("No item selected.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedItem.getPrice()));
        dialog.setTitle("Update Price");
        dialog.setHeaderText("Update price for: " + selectedItem.getName());
        dialog.setContentText("New Price:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(priceStr -> {
            try {
                double newPrice = Double.parseDouble(priceStr);
                // Send request to manager for approval
                Request request = new Request(selectedItem, newPrice, Main.user);
                SimpleClient.getClient().sendToServer(request);
                System.out.println("Request sent to manager.");
                loadMenuItemsFromDB();
            } catch (NumberFormatException e) {
                System.out.println("Invalid price input.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



    }
    @Subscribe
    public void onManagerResponse(String response) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Manager Response");
            alert.setContentText(response);
            alert.showAndWait();
            if (response.contains("successfully")) {
                loadMenuItemsFromDB(); // refresh table
            }
        });
    }
    //newwwwwwwwwwwwwwwww
//        if (selectedItem  == null) {
//            this.showCompletionMessage("Error", "Please select a menu item to update.");
//        } else if (colPrice.getText().isEmpty()) {
//            this.showCompletionMessage("Error", "Please enter a new price.");
//        } else {
//            try {
//                double newPrice = Double.parseDouble(colPrice.getText());
//                if (newPrice <= 0) {
//                    this.showCompletionMessage("Error", "Price must be greater than zero.");
//                    return;
//                }
//
//                // Send update request to server
//                getClient().sendToServer("Update price @" + colId.getText() + "@" + newPrice);
//                this.showCompletionMessage("Success", "Price updated successfully!");
//            } catch (NumberFormatException e) {
//                this.showCompletionMessage("Error", "Invalid price format. Please enter a valid number.");
//               selectedItem.getPrice().clear();
//            } catch (IOException e) {
//                this.showCompletionMessage("Error", "Failed to send update request to server.");
//                e.printStackTrace();
//            }
//        }


    private void showCompletionMessage(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}