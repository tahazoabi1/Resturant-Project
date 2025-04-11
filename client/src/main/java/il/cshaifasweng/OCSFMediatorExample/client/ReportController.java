//package il.cshaifasweng.OCSFMediatorExample.client;
//
//import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
//import il.cshaifasweng.OCSFMediatorExample.server.ConnectToDataBase;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.scene.chart.BarChart;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
//import javafx.scene.control.*;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.cell.PropertyValueFactory;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//import javafx.event.ActionEvent;
//import javafx.scene.input.MouseEvent;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ReportController {
//
//    @FXML
//    private ComboBox<String> branchSelector;
//
//    @FXML
//    private Label monthlyDeliveriesLabel;
//    @FXML
//    private TableView<Map<String, String>> dailyVisitTable;
//
//    @FXML
//    private TableColumn<Map<String, String>, String> dateColumn;
//
//    @FXML
//    private TableColumn<Map<String, String>, String> visitorCountColumn;
//
//    private ObservableList<String> branchNames = FXCollections.observableArrayList();
//    private List<Branch> branchList;
//
//    @FXML
//    public void initialize() {
//        loadBranchesFromDatabase();
//        branchSelector.setItems(branchNames);
//        branchSelector.setOnAction(this::handleBranchSelection);
//
//        // Initialize TableView columns
//        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Date")));
//        visitorCountColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Visitors")));
//
//        // Initialize the complaint status chart with default categories
//        complaintStatusChart.getData().clear();  // Clear any existing data
//
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName("Complaints Status");
//
//        // Adding empty categories with count 0
//        series.getData().add(new XYChart.Data<>("Pending", 0));
//        series.getData().add(new XYChart.Data<>("Resolved", 0));
//        series.getData().add(new XYChart.Data<>("Closed", 0));
//
//        complaintStatusChart.getData().add(series);
//    }
//
//
//
//    private void loadBranchesFromDatabase() {
//        Session session = null;
//        Transaction transaction = null;
//
//        try {
//            session = ConnectToDataBase.getSession();
//            transaction = session.beginTransaction();
//
//            branchList = session.createQuery("FROM Branch", Branch.class).getResultList();
//
//            if (branchList.isEmpty()) {
//                showAlert("No Branches Found", "No branches exist in the database.");
//            }
//
//            for (Branch branch : branchList) {
//                branchNames.add(branch.getName());
//            }
//
//            transaction.commit();
//
//        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
//            showAlert("Database Error", "Failed to load branches from database: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            if (session != null) session.close();
//        }
//    }
//
//    private void handleBranchSelection(ActionEvent event) {
//        monthlyDeliveriesLabel.setVisible(true);
//        dailyVisitTable.setVisible(true);
//
//        String selectedBranchName = branchSelector.getSelectionModel().getSelectedItem();
//
//        if (selectedBranchName != null) {
//            Branch selectedBranch = branchList.stream()
//                    .filter(branch -> branch.getName().equals(selectedBranchName))
//                    .findFirst()
//                    .orElse(null);
//
//            if (selectedBranch != null) {
//                loadMonthlyDeliveriesCount(selectedBranch.getId());
//                loadDailyVisitCounts(selectedBranch.getId());
//                loadComplaintStatusCounts(selectedBranch.getId());
//            }
//        }
//    }
//
//    private void loadMonthlyDeliveriesCount(int branchId) {
//        Session session = null;
//        Transaction transaction = null;
//        int deliveryCount = 0;
//
//        try {
//            session = ConnectToDataBase.getSession();
//            transaction = session.beginTransaction();
//
//            LocalDate firstDayOfMonth = YearMonth.now().atDay(1);
//            LocalDate lastDayOfMonth = YearMonth.now().atEndOfMonth();
//
//            System.out.println("Fetching deliveries for Branch ID " + branchId + " between " + firstDayOfMonth + " and " + lastDayOfMonth);
//
//            deliveryCount = ((Long) session.createQuery(
//                            "SELECT COUNT(o) FROM Order o WHERE o.branch.id = :branchId AND o.orderDate BETWEEN :startDate AND :endDate")
//                    .setParameter("branchId", branchId)
//                    .setParameter("startDate", firstDayOfMonth)
//                    .setParameter("endDate", lastDayOfMonth)
//                    .getSingleResult()).intValue();
//
//            transaction.commit();
//
//            System.out.println("Delivery Count for Branch ID " + branchId + ": " + deliveryCount);
//
//            if (monthlyDeliveriesLabel != null) {
//                monthlyDeliveriesLabel.setText("Deliveries This Month: " + deliveryCount);
//            }
//
//        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
//            showAlert("Database Error", "Failed to load deliveries count: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            if (session != null) session.close();
//        }
//    }
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    private void loadDailyVisitCounts(int branchId) {
//        Session session = null;
//        Transaction transaction = null;
//        ObservableList<Map<String, String>> data = FXCollections.observableArrayList();
//
//        try {
//            session = ConnectToDataBase.getSession();
//            transaction = session.beginTransaction();
//
//            LocalDate firstDayOfMonth = YearMonth.now().atDay(1);
//            LocalDate lastDayOfMonth = YearMonth.now().atEndOfMonth();
//
//            List<Object[]> results = session.createQuery(
//                            "SELECT r.date, COUNT(r) FROM ReservationReport r WHERE r.branch.id = :branchId AND r.date BETWEEN :startDate AND :endDate GROUP BY r.date ORDER BY r.date")
//                    .setParameter("branchId", branchId)
//                    .setParameter("startDate", firstDayOfMonth)
//                    .setParameter("endDate", lastDayOfMonth)
//                    .getResultList();
//
//            if (results.isEmpty()) {
//                System.out.println("No reservations found for the selected branch and date range.");
//            } else {
//                for (Object[] result : results) {
//                    LocalDate date = (LocalDate) result[0];
//                    Long visitorCount = (Long) result[1];
//
//                    Map<String, String> row = new HashMap<>();
//                    row.put("Date", date.toString());
//                    row.put("Visitors", visitorCount.toString());
//                    data.add(row);
//                }
//            }
//
//            transaction.commit();
//            dailyVisitTable.setItems(data);
//
//        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
//            e.printStackTrace();
//        } finally {
//            if (session != null) session.close();
//        }
//    }
//    @FXML
//    private BarChart<String, Number> complaintStatusChart;
//
//    private void loadComplaintStatusCounts(int branchId) {
//        Session session = null;
//        Transaction transaction = null;
//
//        try {
//            session = ConnectToDataBase.getSession();
//            transaction = session.beginTransaction();
//
//            LocalDate firstDayOfMonth = YearMonth.now().atDay(1);
//            LocalDate lastDayOfMonth = YearMonth.now().atEndOfMonth();
//
//            List<Object[]> results = session.createQuery(
//                            "SELECT c.status, COUNT(c.id) FROM Complaint c WHERE c.branch.id = :branchId AND c.complaintDate BETWEEN :startDate AND :endDate GROUP BY c.status ORDER BY c.status")
//                    .setParameter("branchId", branchId)
//                    .setParameter("startDate", firstDayOfMonth)
//                    .setParameter("endDate", lastDayOfMonth)
//                    .getResultList();
//
//            transaction.commit();
//
//            // Clear previous data before displaying new data
//            complaintStatusChart.getData().clear();
//            complaintStatusChart.setVisible(true);
//
//            NumberAxis yAxis = (NumberAxis) complaintStatusChart.getYAxis();
//            yAxis.setAutoRanging(true);
//            yAxis.setTickUnit(1);
//            yAxis.setForceZeroInRange(false);
//
//            List<String> statusOrder = List.of("Pending", "Resolved", "Closed");
//
//            XYChart.Series<String, Number> series = new XYChart.Series<>();
//            series.setName("Complaints Status");
//
//            Map<String, Long> statusCounts = new HashMap<>();
//            for (Object[] result : results) {
//                String status = (String) result[0];
//                Long count = ((Number) result[1]).longValue();
//                statusCounts.put(status, count);
//            }
//
//            for (String status : statusOrder) {
//                Long count = statusCounts.getOrDefault(status, 0L);
//                XYChart.Data<String, Number> data = new XYChart.Data<>(status, count);
//                series.getData().add(data);
//            }
//
//            complaintStatusChart.getData().add(series);
//
//            // Force layout update
//            complaintStatusChart.applyCss();
//            complaintStatusChart.layout();
//
//        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
//            e.printStackTrace();
//        } finally {
//            if (session != null) session.close();
//        }
//    }
//
//    @FXML
//    private Button allBranchesButton;
//
//    @FXML
//    private void loadAllBranchesReport(ActionEvent event) {
//        Session session = null;
//        Transaction transaction = null;
//
//        try {
//            session = ConnectToDataBase.getSession();
//            transaction = session.beginTransaction();
//
//            LocalDate firstDayOfMonth = YearMonth.now().atDay(1);
//            LocalDate lastDayOfMonth = YearMonth.now().atEndOfMonth();
//
//            // Fetching complaints from ALL branches and grouping them by status
//            List<Object[]> results = session.createQuery(
//                            "SELECT c.status, COUNT(c) FROM Complaint c WHERE c.complaintDate BETWEEN :startDate AND :endDate GROUP BY c.status")
//                    .setParameter("startDate", firstDayOfMonth)
//                    .setParameter("endDate", lastDayOfMonth)
//                    .getResultList();
//
//            transaction.commit();
//
//            // Hide Deliveries and Visitors sections
//            monthlyDeliveriesLabel.setVisible(false);
//            dailyVisitTable.setVisible(false);
//
//            // Clear previous data before displaying new data ✅
//            complaintStatusChart.getData().clear();
//
//            if (results.isEmpty()) {
//                System.out.println("No complaints found for the entire chain in the selected month.");
//                complaintStatusChart.setVisible(false);  // Hide chart if no complaints found
//            } else {
//                complaintStatusChart.setVisible(true);  // Make chart visible
//
//                // Create a single series for the chart ✅
//                XYChart.Series<String, Number> series = new XYChart.Series<>();
//                series.setName("All Branches - Complaints Status");
//
//                for (Object[] result : results) {
//                    String status = (String) result[0];
//                    Long count = (Long) result[1];
//                    series.getData().add(new XYChart.Data<>(status, count));
//                }
//
//                complaintStatusChart.getData().add(series);  // ✅ Replace old series with the new one
//                series.getData().forEach(data ->
//                        data.getNode().setStyle("-fx-bar-fill: orange;")
//                );
//
//                complaintStatusChart.applyCss();
//                complaintStatusChart.layout();
//
//            }
//
//        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
//            e.printStackTrace();
//        } finally {
//            if (session != null) session.close();
//        }
//    }
//
//
//    @FXML
//    private void goBack(ActionEvent event) {
//        System.out.println("Going back to the previous screen...");
//        Main.switchScreen("navigation");
//    }
//}