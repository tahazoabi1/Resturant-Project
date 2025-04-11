package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.HashMap;

import static il.cshaifasweng.OCSFMediatorExample.client.Main.user;

public class NavigationController {

    private static NavigationController instance;

    @FXML
    private Button profileBtn, logInBtn, logOutBtn, registerBtn;

    @FXML
    private Text welcomeState;

    @FXML
    private StackPane contentArea; // Dynamic content container

    @FXML
    private VBox navigationContainer;

    @FXML
    private HBox navBar;

    @FXML
    private ToggleButton toggleButton;

    private boolean isCollapsed = false;
    private HashMap<String, Parent> pageCache = new HashMap<>(); // Page caching

    // Constructor
    public NavigationController() {
        instance = this;
    }

    @FXML
    private void toggleNavigationBar() {
        double targetHeight = isCollapsed ? navBar.prefHeight(-1) : 0;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new KeyValue(navBar.maxHeightProperty(), targetHeight, Interpolator.EASE_BOTH))
        );
        timeline.play();

        if (!isCollapsed) {
            navBar.setManaged(false);
            navBar.setVisible(false);
            toggleButton.setText("☰");
        } else {
            navBar.setVisible(true);
            navBar.setManaged(true);
            toggleButton.setText("✖");
        }

        isCollapsed = !isCollapsed;
    }

    private void resetDynamicUI() {
        // Remove any dynamically added buttons or elements
        navBar.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().equals("dynamic"));
    }

    @FXML
    public void loadPage(String pageName) {
        try {
            // Clear the content area to remove previous content
            contentArea.getChildren().clear();

            resetDynamicUI();


            // Load the new page
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName + ".fxml"));
            Parent page = loader.load();

            // Add the new page to the content area
            contentArea.getChildren().setAll(page);

            // Optional: Add fade transition for a smooth effect
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), contentArea);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();

            // Ensure global CSS is applied once
            Scene currentScene = contentArea.getScene();
            if (currentScene != null && currentScene.getStylesheets().isEmpty()) {
                currentScene.getStylesheets().add(getClass().getResource("/CSS/global-file.css").toExternalForm());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigate(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String role = (user != null) ? user.getRole() : "";

        // Handle pre-defined buttons
        switch (clickedButton.getId()) {

            case "logOutBtn":
                handleLogout();
                break;
            case "menuBtn":
                loadPage("CustomerTasks/SelectItems");
                break;
            case "registerBtn":
                loadPage("UserTasks/customer-register");
                break;
            case "homeBtn":
                if (user == null) {
                    loadPage("first-page");
                } else {
                    switch (role) {
                        case "Customer":
                            loadPage("HomePages/customer-home");
                            break;
                        case "Dietitian":
                            loadPage("HomePages/dietitian-home");
                            break;
                        case "Hostess":
                            loadPage("HomePages/hostess-home");
                            break;
                        case "Manager":
                            loadPage("HomePages/branch-manager-home");
                            break;
                        case "NetworkManager":
                            loadPage("HomePages/network-manager-home");
                            break;
                        case "ServiceWorker":
                            loadPage("HomePages/service-worker-home");
                            break;
                        default:
                            loadPage("first-page"); // fallback
                    }
                }
                break;
            case "profileBtn":
                loadPage("UserTasks/profile");
                break;
            case "cartBtn":
                loadPage("CustomerTasks/cart");
                break;
            case "reservationBtn":
                loadPage("CustomerTasks/reservations");
                break;
            case "logInBtn":
                loadPage("UserTasks/log-in");
                break;

            case "orderHistoryBtn":
                loadPage("CustomerTasks/order-history");
                break;
            case "complaintBtn":
                loadPage("CustomerTasks/submit-complaint");
                break;
            case "cancelOrderBtn":
                loadPage("CustomerTasks/cancel-order");
                break;
            case "walkinBtn":
                loadPage("CustomerTasks/walk-in-assignment");
                break;
            case "occupancyMapBtn":
                loadPage("HostessTasks/occupancy-map");
                break;
            case "branchReportBtn":
                loadPage("ManagerTasks/branch-report");
                break;
            case "manageTablesBtn":
                loadPage("ServiceTasks/manage-tables");
                break;
            case "branchComplaintsBtn":
                loadPage("ServiceTasks/branch-complaints");
                break;
            case "allReportsBtn":
                loadPage("ManagerTasks/all-reports");
                break;
            case "createReportBtn":
                loadPage("ManagerTasks/create-report");
                break;
            case "approveOffersBtn":
                loadPage("NetworkManagerTasks/approve-offers");
                break;
            case "editMenuBtn":
                loadPage("DietitianTasks/edit-menu");
                break;
            case "statusBtn":
                loadPage("DietitianTasks/status-of-changes");
                break;
            case "openComplaintsBtn":
                loadPage("ServiceTasks/open-complaints");
                break;
            case "complaintsHistoryBtn":
                loadPage("CustomerTasks/complaints-history");
                break;
            default:
                System.out.println("Unknown button ID: " + clickedButton.getId());
                break;
        }
    }

    @FXML
    private void handleLogout() {
        user.signOut();
        try {
            SimpleClient.getClient().sendToServer("logOut#" + user.getEmail() + "#" + user.getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user = null;
        updateLogInStatus();
        loadPage("HomePages/home-page");
    }

    @FXML
    public void initialize() {
        instance = this;
        toggleNavigationBar();
        Platform.runLater(() -> loadPage("first-page"));
        updateLogInStatus();
    }

    public void updateLogInStatus() {
        boolean isLoggedIn = (user != null);

        logInBtn.setVisible(!isLoggedIn);
        logInBtn.setManaged(!isLoggedIn);

        registerBtn.setVisible(!isLoggedIn);
        registerBtn.setManaged(!isLoggedIn);

        logOutBtn.setVisible(isLoggedIn);
        logOutBtn.setManaged(isLoggedIn);

        profileBtn.setManaged(isLoggedIn);
        profileBtn.setVisible(isLoggedIn);

        welcomeState.setText(isLoggedIn ? user.getName() : "");

        // 🧠 Clear previous dynamic buttons (if re-logging or switching users)
        navBar.getChildren().removeIf(node -> node.getUserData() != null && node.getUserData().equals("dynamic"));

        if (!isLoggedIn) return;

        String role = (user != null) ? user.getRole() : ""; // You may need to adjust based on your actual User class

        switch (role) {
            case "Customer":
                addButton("Menu", "menuBtn");
                addButton("Branch", "branchBtn");
                addButton("Reservations", "reservationBtn");
                addButton("MyOrders", "orderHistoryBtn");
                addButton("Complain", "complaintBtn");
                break;
            case "Hostess":
                addButton("Reserve", "walkinBtn");
                addButton("Map", "occupancyMapBtn");
                addButton("LogOut", "logOutBtn");
                break;
            case "Manager":
                addButton("Reports", "branchReportBtn");
                addButton("Complaints", "branchComplaintsBtn");
                break;
            case "NetworkManager":
                addButton("Reports", "allReportsBtn");
                addButton("Approve Offers", "approveOffersBtn");
                break;
            case "Dietitian":
                addButton("Edit", "editMenuBtn");
                addButton("Status", "statusBtn");
                break;
            case "ServiceWorker":
                addButton("Open Complaints", "openComplaintsBtn");
                addButton("Complaints History", "complaintsHistoryBtn");
                break;
            default:
                addButton("Menu", "menuBtn");
                addButton("Branch", "branchBtn");
                break;
        }
    }

    private void addButton(String text, String id) {
        Button button = new Button(text);
        button.setId(id);
        button.getStyleClass().add("nav-button");
        button.setOnAction(this::navigate);
        button.setUserData("dynamic");
        navBar.getChildren().add(button);
    }

    public static NavigationController getInstance() {
        return instance;
    }
}
