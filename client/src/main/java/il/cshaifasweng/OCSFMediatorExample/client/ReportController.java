//package il.cshaifasweng.OCSFMediatorExample.client;
//
//import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.chart.*;
//import javafx.collections.FXCollections;
//import javafx.scene.control.cell.PropertyValueFactory;
//
//import java.time.YearMonth;
//import java.util.Arrays;
//
//public class ReportController {
//
//    @FXML private ComboBox<Integer> yearComboBox;
//    @FXML private ComboBox<String> monthComboBox;
//    @FXML private ComboBox<Branch> branchComboBox;
//
//    @FXML private Label deliveryLabel;
//    @FXML private TableView<Visit> visitTable;
//    @FXML private TableColumn<Visit, String> dateColumn;
//    @FXML private TableColumn<Visit, Integer> visitorCountColumn;
//    @FXML private BarChart<String, Number> complaintHistogram;
//
//    @FXML
//    public void initialize() {
//        yearComboBox.setItems(FXCollections.observableArrayList(2024, 2025, 2026));
//        monthComboBox.setItems(FXCollections.observableArrayList(
//                "January", "February", "March", "April", "May", "June",
//                "July", "August", "September", "October", "November", "December"
//        ));
//    }
//
//    @FXML
//    public void generateReport() {
//        System.out.println("Generating Report..."); // This is just a placeholder
//    }
//
//    @FXML
//    public void goBackToSelection() {
//        System.out.println("Going back to selection..."); // This is just a placeholder
//    }
//}
