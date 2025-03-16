package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.server.ConnectToDataBase;
import javafx.application.Platform;
import java.util.*;

public class browseMenuController {

    @FXML
    private ListView<String> branchListView;
    @FXML
    private ListView<String> menuListView;
    @FXML
    private Label dishNameLabel;
    @FXML
    private Label branchAvailabilityLabel;
    @FXML
    private Label ingredientsLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private ImageView dishImageView;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;

    private ObservableList<String> branches;
    private ObservableList<String> menuItems;
    private Map<String, MenuItem> menuData;

    @FXML
    public void initialize() {
        loadBranchesFromDatabase();
        loadMenuItemsFromDatabase();

        menuListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                displayDishDetails(newVal);
            }
        });

        searchButton.setOnAction(event -> performSearch());
    }

    private void loadBranchesFromDatabase() {
        branches = FXCollections.observableArrayList();
        try {
            List<Branch> branchList = ConnectToDataBase.getAllBranches();
            for (Branch branch : branchList) {
                String branchInfo = branch.getName() + " - " +
                        (branch.getOpeningHours() != null ? branch.getOpeningHours() : "No Hours Available");
                branches.add(branchInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        branchListView.setItems(branches);
    }

    private void loadMenuItemsFromDatabase() {
        menuData = new HashMap<>();
        menuItems = FXCollections.observableArrayList();

        try {
            List<MenuItem> items = ConnectToDataBase.getAllMenuItems();
            System.out.println("DEBUG: Fetched " + items.size() + " menu items from database");

            for (MenuItem item : items) {
                if (item != null) {
                    String dishName = item.getName();
                    String ingredients = item.getIngredients();
                    String price = "$" + item.getPrice();

                    // Branch availability check
                    String branchAvailability = "All Branches";
                    if (item.getBranches() != null && !item.getBranches().isEmpty()) {
                        branchAvailability = item.getBranches().stream()
                                .map(Branch::getName)
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("All Branches");
                    }

                    // Add to UI
                    menuData.put(dishName, item);
                    menuItems.add(dishName);
                }
            }

            System.out.println("DEBUG: Total menu items added to UI: " + menuItems.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ensure UI updates on JavaFX thread
        Platform.runLater(() -> {
            menuListView.setItems(menuItems);
            menuListView.refresh();
        });
    }

    private void displayDishDetails(String dishName) {
        MenuItem dish = menuData.get(dishName);
        if (dish != null) {
            dishNameLabel.setText(dish.getName());
            branchAvailabilityLabel.setText(
                    dish.getBranches() != null ? dish.getBranches().toString() : "All Branches");
            ingredientsLabel.setText(dish.getIngredients());
            priceLabel.setText("$" + dish.getPrice());

            if (dish.getImageUrl() != null && !dish.getImageUrl().isEmpty()) {
                dishImageView.setImage(new Image(dish.getImageUrl()));
            } else {
                dishImageView.setImage(null); // Default if no image
            }
        }
    }

    private void performSearch() {
        String query = searchField.getText().toLowerCase(); // Convert search query to lowercase
        ObservableList<String> filteredMenu = FXCollections.observableArrayList();

        for (Map.Entry<String, MenuItem> entry : menuData.entrySet()) {
            MenuItem dish = entry.getValue();
            String dishName = entry.getKey().toLowerCase(); // Convert dish name to lowercase
            String availability = dish.getBranches() != null ? dish.getBranches().toString().toLowerCase() : "all branches";
            String ingredients = dish.getIngredients().toLowerCase();
            String price = String.valueOf(dish.getPrice());

            // **Split the dish name into words and check if any word contains the query**
            boolean nameMatch = Arrays.stream(dishName.split(" ")).anyMatch(word -> word.contains(query));

            if (nameMatch || availability.contains(query) || ingredients.contains(query) || price.contains(query)) {
                filteredMenu.add(entry.getKey());
            }
        }

        menuListView.setItems(filteredMenu);
    }
}
