package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complaint;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.getClient;

public class ComplaintController {

	@FXML
	private ListView<String> list_of_complaints;

	@FXML
	private TextField solutionField;

	@FXML
	private Button updateButton;

	private List<Complaint> allComplaints = new ArrayList<>();
	private Complaint selectedComplaint;

	@FXML
	private TextField compensationField;

	@FXML
	public void initialize() {
		EventBus.getDefault().register(this);
		loadComplaints();

		list_of_complaints.setOnMouseClicked(this::handleComplaintSelection);
	}

	private void loadComplaints() {
		try {
			System.out.println("Requesting all complaints from server...");
			getClient().sendToServer("get all complaints");
		} catch (IOException e) {
			System.err.println("Failed to send request to server: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Subscribe
	public void initComplaintTable(List<Complaint> complaints) {
		Platform.runLater(() -> {
			allComplaints = complaints;
			list_of_complaints.getItems().clear();

			for (Complaint c : complaints) {
				String display = "Complaint ID: " + c.getId() +
						" | " + c.getMessage() +
						" | Status: " + c.getStatus() +
						(c.getSolution() != null && !c.getSolution().isEmpty()
								? " | Solution: " + c.getSolution()
								: "");

				list_of_complaints.getItems().add(display);
			}

			System.out.println("Complaints loaded: " + complaints.size());
		});
	}

	@FXML
	void handleComplaintSelection(MouseEvent event) {
		String selectedText = list_of_complaints.getSelectionModel().getSelectedItem();
		if (selectedText != null) {
			int selectedIndex = list_of_complaints.getSelectionModel().getSelectedIndex();
			selectedComplaint = allComplaints.get(selectedIndex);
			solutionField.setText(selectedComplaint.getSolution() != null ? selectedComplaint.getSolution() : "");
		}
	}

	@FXML
	void updateButtonClicked(ActionEvent event) {
		if (selectedComplaint == null) {
			System.out.println("No complaint selected.");
			return;
		}

		String solution = solutionField.getText().trim();
		String compensationText = compensationField.getText().trim();
		Double compensation = null;

		if (solution.isEmpty()) {
			System.out.println("Solution field is empty!");
			return;
		}

		if (!compensationText.isEmpty()) {
			try {
				compensation = Double.parseDouble(compensationText);
			} catch (NumberFormatException e) {
				System.out.println("Invalid compensation amount!");
				return;
			}
		}

		try {
			// Compose message with compensation
			String responseMessage = "update complaint#" + selectedComplaint.getId() +
					"#Answered#" + solution + "#" + (compensation != null ? compensation : "0.0");

			getClient().sendToServer(responseMessage);

			// Update local object
			selectedComplaint.setStatus("Answered");
			selectedComplaint.setSolution(solution);
			selectedComplaint.setCompensationAmount(compensation);

			// Update UI
			int selectedIndex = list_of_complaints.getSelectionModel().getSelectedIndex();
			String updatedDisplay = "Complaint ID: " + selectedComplaint.getId() +
					" | " + selectedComplaint.getMessage() +
					" | Status: " + selectedComplaint.getStatus() +
					" | Solution: " + selectedComplaint.getSolution() +
					(compensation != null ? " | Compensation: $" + compensation : "");

			list_of_complaints.getItems().set(selectedIndex, updatedDisplay);

			// Optionally send email
			sendEmailToClient(selectedComplaint);

			// Cleanup
			list_of_complaints.getSelectionModel().clearSelection();
			selectedComplaint = null;
			solutionField.clear();
			compensationField.clear();

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Update Successful");
			alert.setHeaderText(null);
			alert.setContentText("Complaint answered and email sent to client.");
			alert.showAndWait();

		} catch (IOException e) {
			System.err.println("Error sending solution to server: " + e.getMessage());
		}
	}

	private void sendEmailToClient(Complaint complaint) {
		String customerEmail = "doniakhamishu@gmail.com";  // TEMP: hardcoded for testing
//		String customerEmail = complaint.getCustomer().getEmail();
		String subject = "Response to Your Complaint (ID: " + complaint.getId() + ")";
		String message = "Dear Customer,\n\n"
				+ "Your complaint has been reviewed.\n"
				+ "Solution: " + complaint.getSolution() + "\n";

		if (complaint.getCompensationAmount() != null && complaint.getCompensationAmount() > 0) {
			message += "You have been granted a compensation of $" + complaint.getCompensationAmount() + ".\n";
		}

		message += "\nThank you for your patience.\n\nBest regards,\nCustomer Service Team";

		// This assumes you have a method to send emails on the server side.
		try {
			getClient().sendToServer("send email#" + customerEmail + "#" + subject + "#" + message);
		} catch (IOException e) {
			System.err.println("Failed to send email: " + e.getMessage());
		}
	}

}
