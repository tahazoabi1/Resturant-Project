package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.getClient;

public class SelectBranchController {

    @FXML
    private TabPane tabPane; // TabPane where tabs will be added
    @FXML
    private Button selectBranchButton; // Button to select branch

    // Button to request branches from the server
    @FXML
    private Button loadBranchesButton;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
        try {
            getClient().sendToServer("get all branches");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // Once the server responds with the list of branches, populate the TabPane
    public void populateTabs(List<Branch> branches) {
        tabPane.getTabs().clear(); // Clear previous tabs if any
        for (Branch branch : branches) {
            Tab tab = new Tab(branch.getName());

            // Create content for the tab
            AnchorPane branchInfo = new AnchorPane();
            branchInfo.setPrefSize(750, 450);

            // Display branch name and description
            Label nameLabel = new Label("Name: " + branch.getName());
            Label descriptionLabel = new Label("Description: " + branch.getDescription());
            nameLabel.setLayoutX(20);
            nameLabel.setLayoutY(20);
            descriptionLabel.setLayoutX(20);
            descriptionLabel.setLayoutY(50);

            // Display the image if it exists
            if (branch.getImageUrl() != null && !branch.getImageUrl().isEmpty()) {
                Image image = new Image("file:" + branch.getImageUrl()); // Assuming the image URL is a local path
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setLayoutX(300);
                imageView.setLayoutY(20);
                branchInfo.getChildren().add(imageView);
            }

            // Add the labels and image to the tab content
            branchInfo.getChildren().addAll(nameLabel, descriptionLabel);

            // Set the content of the tab
            tab.setContent(branchInfo);

            // Add the tab to the TabPane
            tabPane.getTabs().add(tab);
        }
    }

    @Subscribe
    public void onBranchesReceived(List<Branch> branches) {
        // Once the branches are received, populate the tabs
        populateTabs(branches);
    }

    public void dispose() {
        // Unregister from EventBus to prevent memory leaks
        EventBus.getDefault().unregister(this);
    }
}
