package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;

import javafx.scene.control.Button;


import java.awt.*;
import java.io.IOException;

public class NavigationController {

    private static NavigationController instance;

    @FXML
    private Text welcomeState;

    @FXML
    private StackPane contentArea; // The dynamic content container

    @FXML
    private Button logInBtn;
    // Constructor
    public NavigationController() {
        instance = this;  // Set the instance on object creation
    }

    @FXML
    private void showMenuList() {
        loadPage("primary");
    }

    @FXML
    private void showSelectBranch() {
        loadPage("selected-branch");
    }

    @FXML
    private void showLogin() {
        loadPage("log-in");
    }

    @FXML
    private void showRegister() {
        loadPage("Register");
    }

    @FXML
    private void showFirstPage() {
        loadPage("first-page");
    }

    // Now, loadPage is no longer static
    public void loadPage(String pageName) {
        try {
            Parent page = FXMLLoader.load(Main.class.getResource((pageName + ".fxml")));
            contentArea.getChildren().setAll(page); // Replace only the center content
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
        loadPage("first-page");
    }

    public void updateLogInStatus(){
        if (Main.user==null){
            logInBtn.setVisible(true);
            welcomeState.setText("");
        }
        else{
            logInBtn.setVisible(false);
            welcomeState.setText(Main.user.getName());

        }
    }
}
