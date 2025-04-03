package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.Main;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class SelectBranchController {

    @FXML private TableView<Branch> branchTable;
    @FXML private TableColumn<Branch, String> nameColumn;
    @FXML private TableColumn<Branch, String> locationColumn;
    @FXML private TableColumn<Branch, String> phoneColumn;
    @FXML private TableColumn<Branch, String> openingHoursColumn;
    @FXML private Button selectButton;

    private Branch selectedBranch;
    private int orderId = -1;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        openingHoursColumn.setCellValueFactory(new PropertyValueFactory<>("openingHours"));

        branchTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedBranch = newVal;
        });

        selectButton.setOnAction(event -> {
            if (selectedBranch != null) {
                try {
                    String msg = "create order#" + selectedBranch.getName();
                    System.out.println("📤 Sending to server: " + msg);
                    SimpleClient.getClient().sendToServer(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("⚠ Please select a branch.");
            }
        });

        try {
            SimpleClient.getClient().sendToServer("get all branches");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onBranchListReceived(List<Branch> branches) {
        if (branches != null && !branches.isEmpty() && branches.get(0) instanceof Branch) {
            Platform.runLater(() -> branchTable.getItems().setAll(branches));
        }
    }

    @Subscribe
    public void onMessageReceived(String message) {
        if (message.startsWith("orderCreated#")) {
            String[] parts = message.split("#");
            orderId = Integer.parseInt(parts[1]);
            System.out.println("✅ Order created with ID: " + orderId);

            if (orderId == -1) {
                System.out.println("❌ Failed to create order. Check server or DB.");
                return;
            }

            Platform.runLater(() -> {
                try {
                    Main.setContent("SelectItems");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void onClose() {
        EventBus.getDefault().unregister(this);
    }
}