package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
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
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

import java.io.IOException;

public class NavigationController {



    private static NavigationController instance;
    @FXML
    public Button profileBtn;

    @FXML
    private Text welcomeState;

    @FXML
    private StackPane contentArea; // The dynamic content container

    @FXML
    private Button logInBtn;

    @FXML
    private Button logOutBtn;

    @FXML
    private Button registerBtn;


    @FXML
    private VBox navigationContainer;

    @FXML
    private HBox navBar;

    @FXML
    private ToggleButton toggleButton;

    private boolean isCollapsed = false;

    private boolean isSidebarCollapsed = true;

    // Constructor
    public NavigationController() {
        instance = this;  // Set the instance on object creation
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

    @FXML
    private void showMenuList() {
        loadPage("browseMenu");
    }

    @FXML
    private void showSelectBranch() {
        loadPage("SelectBranch");
    }

    @FXML
    private void showLogin() {
        loadPage("log-in");
    }

    @FXML
    private void showRegister() {
        loadPage("customer-rigister");
    }

    @FXML
    private void showHomePage() {
        loadPage("home-page");
    }

    @FXML
    private void showLogOut() {
        Main.user.signOut();
        Main.user = null;
        updateLogInStatus();
        loadPage("first-page");
    }

    @FXML
    public void loadPage(String pageName) {
        try {
            // Load the FXML page
            Parent page = FXMLLoader.load(Main.class.getResource((pageName + ".fxml")));

            // Set the loaded page to the content area (replace only the center content)
            contentArea.getChildren().setAll(page);
            Scene currentScene = contentArea.getScene();
            if (currentScene != null) {
                currentScene.getStylesheets().add(getClass().getResource("/CSS/global-file.css").toExternalForm());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get instance method (optional)
    public static NavigationController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        instance = this;
        toggleNavigationBar();
        Platform.runLater(() -> loadPage("first-page"));
    }

    public void updateLogInStatus() {
        if (Main.user == null) {
            logInBtn.setVisible(true);
            logInBtn.setManaged(true);

            registerBtn.setVisible(true);
            registerBtn.setManaged(true);

            logOutBtn.setVisible(false);
            logOutBtn.setManaged(false);

            profileBtn.setManaged(false);
            profileBtn.setVisible(false);

            welcomeState.setText("");
        } else {
            logInBtn.setVisible(false);
            logInBtn.setManaged(false);

            registerBtn.setVisible(false);
            registerBtn.setManaged(false);

            logOutBtn.setVisible(true);
            logOutBtn.setManaged(true);

            profileBtn.setManaged(true);
            profileBtn.setVisible(true);


            welcomeState.setText(Main.user.getName());
        }
    }


    public void showCart(ActionEvent event) {
    }

    public void showReservations(ActionEvent event) {
    }

    public void showProfilePage(ActionEvent event) {
    }
}