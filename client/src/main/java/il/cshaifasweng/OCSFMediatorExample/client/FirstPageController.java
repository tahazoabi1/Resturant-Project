package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FirstPageController {

    @FXML
    void switchToChooseBranchPage(ActionEvent event) {
        // Use the NavigationController instance to call loadPage
        NavigationController.getInstance().loadPage("select-branch");
    }

    @FXML
    void switchToLogInPage(ActionEvent event) {
        // Use the NavigationController instance to call loadPage
        NavigationController.getInstance().loadPage("log-in");
    }
}
