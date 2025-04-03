package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Hostess;
import il.cshaifasweng.OCSFMediatorExample.entities.Tables;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.getClient;

public class HostessSelectionController {
    @FXML
    private Label branchNameLabel;
    @FXML
    private ComboBox<String> hostingAreaComboBox;
    @FXML
    private ListView<String> tablesListView;
    private Branch branch;
    private List<Tables> allTables;
    private Tables selectedTable;

    @FXML
    public void initialize() {
        branch = ((Hostess) Main.user).getBranch();
        branchNameLabel.setText(branch.getName());
    }

    @FXML
    private void updateTablesList() {
        String selectedArea = hostingAreaComboBox.getValue();
        if (selectedArea == null) return;

        // Get all tables in the selected area
        allTables = branch.getTables().stream()
                .filter(table -> table.getHostingArea().equalsIgnoreCase(selectedArea))
                .collect(Collectors.toList());

        // Update ListView with reserved & available tables
        tablesListView.setItems(FXCollections.observableArrayList(
                allTables.stream()
                        .map(table -> formatTableEntry(table))
                        .collect(Collectors.toList())));
    }

    private String formatTableEntry(Tables table) {
        String status = table.isReserved() ? " - Reserved" : " - Available";
        return table.getTableName() + " (Capacity: " + table.getCapacity() + ")" + status;
    }

    @FXML
    private void serveTable() throws IOException {
        String selectedEntry = tablesListView.getSelectionModel().getSelectedItem();
        if (selectedEntry == null) {
            showAlert("No table selected", "Please select a table to serve.");
            return;
        }

        // Extract table name from the selected entry
        String selectedTableName = selectedEntry.split(" \\(Capacity:")[0];

        selectedTable = allTables.stream()
                .filter(table -> table.getTableName().equals(selectedTableName) && !table.isReserved())
                .findFirst().orElse(null);

        if (selectedTable != null) {
            try {
                getClient().sendToServer("Update serve table @" + selectedTable.getId() + "@");
                selectedTable.setReserved();
                showAlert("Table Served", "Table " + selectedTableName + " is now reserved.");
                updateTablesList(); // Refresh list after serving
            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw e;
            }
        } else {
            showAlert("Table Already Reserved", "This table is already reserved.");
        }
    }

    @FXML
    private void unserveTable() throws IOException {
        String selectedEntry = tablesListView.getSelectionModel().getSelectedItem();
        if (selectedEntry == null) {
            showAlert("No table selected", "Please select a table to unreserve.");
            return;
        }

        // Extract table name
        String selectedTableName = selectedEntry.split(" \\(Capacity:")[0];

        selectedTable = allTables.stream()
                .filter(table -> table.getTableName().equals(selectedTableName) && table.isReserved())
                .findFirst().orElse(null);

        if (selectedTable != null) {
            try {
                getClient().sendToServer("Update serve table @" + selectedTable.getId() + "@");
                selectedTable.setReserved();
                showAlert("Table Unreserved", "Table " + selectedTableName + " is now available.");
                updateTablesList(); // Refresh list after unserving
            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw e;
            }
        } else {
            showAlert("Table Not Reserved", "This table is already available.");
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
