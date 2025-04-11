package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.getClient;

public class AddDishController {

    @FXML
    private TextField PrefrencesField;

    @FXML
    private TextField ingredientsField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private RadioButton DeliveryRdaioId;

    @FXML
    private RadioButton ChainDishRdioId;

    @FXML
    private VBox branchSelectionBox;

    private static List<Branch> Branches;

    private static List<Branch> selectedBranches = new ArrayList<>();

    @FXML
    private void initialize() throws IOException {
        EventBus.getDefault().register(this);  // Register the controller to listen for events
        try {
            getClient().sendToServer("get all branches");
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        ChainDishRdioId.setOnAction(this::ChainDishToggled);
    }

    @FXML
    void onSaveClicked(ActionEvent event) {
        String name = nameField.getText();
        String preferences = PrefrencesField.getText();
        String ingredients = ingredientsField.getText();
        Boolean isDelivery = DeliveryRdaioId.isSelected();
        Boolean isChain = ChainDishRdioId.isSelected();
        double price;

        try {
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid price");
            return;
        }

        if (name.isEmpty() || preferences.isEmpty() || ingredients.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        StringBuilder branchIds = new StringBuilder();
        if (!isChain) {
            for (int i = 0; i < selectedBranches.size(); i++) {
                branchIds.append(selectedBranches.get(i).getId());
                if (i < selectedBranches.size() - 1) {
                    branchIds.append("@");
                }
            }
        }

        String message = String.format("additem#%s#%s#%s#%.2f#%s#%s#%s",
                name,
                ingredients,
                preferences,
                price,
                isChain.toString(),
                isDelivery.toString(),
                branchIds.toString()
        );

        try {
            SimpleClient.getClient().sendToServer(message);
            showAlert("Success", "Dish added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to send data to server.");
        }
    }


    @Subscribe
    public void onReceiveBranches(List<Branch> branches) {
        if (branches != null && !branches.isEmpty() && branches.get(0) instanceof Branch) {
            System.out.println("Received branches: " + branches.size());
            Platform.runLater(() -> {
                this.Branches = branches;
                generateBranchCheckboxes(); // optionally call a method to update UI
            });
        }
    }

    private void ChainDishToggled(ActionEvent event) {
        boolean isChain = ChainDishRdioId.isSelected();
        branchSelectionBox.setVisible(!isChain);
    }

    private void generateBranchCheckboxes() {
        branchSelectionBox.getChildren().clear();
        selectedBranches.clear();

        for (Branch branch : Branches) {
            CheckBox checkBox = new CheckBox(branch.getName() + " (ID: " + branch.getId() + ")");
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    selectedBranches.add(branch);
                } else {
                    selectedBranches.remove(branch);
                }
            });
            branchSelectionBox.getChildren().add(checkBox);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
