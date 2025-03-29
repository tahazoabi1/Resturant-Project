package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ConnectToDataBase;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class SimpleServer extends AbstractServer {
	private List<MenuItem> MenuItemDatabase = new ArrayList<>();
	public static ArrayList<ConnectionToClient> Subscribers = new ArrayList<>();

	public SimpleServer(int port) {
		super(port);
	}

	public void sendToAll(Warning warning) {
		try {
			for (ConnectionToClient Subscriber : Subscribers) {
				Subscriber.sendToClient(warning);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		System.out.println("\n====== Server Received Messag11e ======");
		System.out.println("Message: " + msgString);
		System.out.println("From client: " + client.getInetAddress());

		if (msgString.startsWith("#warning")) {
			Warning warning = new Warning("Warning from server!");
			//	Object message=warning.getMessage();
			sendToAll(warning);
			System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
		} else if (msgString.equals("get all MenuItems")) {
			System.out.println("\n====== Processing Get All MenuItems ======");
			try {
				System.out.println("Requesting items from database...");
				List<MenuItem> temp = ConnectToDataBase.getAllMenuItems();
				System.out.println("Retrieved " + (temp != null ? temp.size() : 0) + " items from database");

				if (temp != null && !temp.isEmpty()) {
					System.out.println("Items found:");
					for (MenuItem item : temp) {
						System.out.println("  - " + item.getName() + " ($" + item.getPrice() + ")");
					}
				} else {
					System.out.println("No items found in database");
				}

				client.sendToClient(temp);
				System.out.println("Sent items to client");
			} catch (Exception e) {
				System.err.println("Error retrieving menu items: " + e.getMessage());
				e.printStackTrace();
				try {
					Warning warning = new Warning("Failed to retrieve menu items: " + e.getMessage());
					client.sendToClient(warning);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} else if (msgString.equals("get all branches")) {
			System.out.println("\n====== Processing Get All branches ======");
			try {
				System.out.println("Requesting items from database...");
				List<Branch> temp = ConnectToDataBase.getAllBranches();
				System.out.println("Retrieved " + (temp != null ? temp.size() : 0) + " items from database");

				if (temp != null && !temp.isEmpty()) {
					System.out.println("Items found:");
					for (Branch branch : temp) {
						System.out.println("  - " + branch.getName() + ")");
					}
				} else {
					System.out.println("No items found in database");
				}

				client.sendToClient(temp);
				System.out.println("Sent branches to client");
				System.out.println("Sending branches to client: " + temp);
			} catch (Exception e) {
				System.err.println("Error retrieving branches: " + e.getMessage());
				e.printStackTrace();
				try {
					Warning warning = new Warning("Failed to retrieve branches: " + e.getMessage());
					client.sendToClient(warning);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} else if (msgString.startsWith("Update price")) {
			System.out.println("\n====== Processing Price Update ======");
			try {
				String[] parts = msgString.split("@");
				if (parts.length != 3) {
					throw new IllegalArgumentException("Invalid update format");
				}

				int id = Integer.parseInt(parts[1]);
				double price = Double.parseDouble(parts[2]);

				System.out.println("Updating price for ID: " + id + " to: " + price);

				// Update in database
				ConnectToDataBase.updateMenuItemPrice(id, price);
				System.out.println("Price updated in database");

				// Get updated list and send back to client
				List<MenuItem> updatedItems = ConnectToDataBase.getAllMenuItems();
				client.sendToClient(updatedItems);
				System.out.println("Sent updated list to client");

			} catch (Exception e) {
				System.err.println("Error updating price: " + e.getMessage());
				e.printStackTrace();
				try {
					Warning warning = new Warning("Failed to update price: " + e.getMessage());
					client.sendToClient(warning);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("====== End Price Update ======\n");
		} else if (msgString.startsWith("trying To LogIn")) {
			System.out.println("\n====== Processing Login Update ======");
			try {
				String[] parts = msgString.split("#");
				if (parts.length != 3) {
					throw new IllegalArgumentException("Invalid Email or Password format");
				}

				String email = parts[1];
				String password = parts[2];

				System.out.println("Trying to log in...");

				// Update in database

				User user = ConnectToDataBase.Login(email, password);
				System.out.println("Got the User");

				client.sendToClient(user);
				System.out.println("Sent user to client");

			} catch (Exception e) {
				System.err.println("Error Log in" + e.getMessage());
				e.printStackTrace();
				try {
					Warning warning = new Warning("Failed to Log in " + e.getMessage());
					client.sendToClient(warning);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("====== End Log in ======\n");

			////////////////////me/////////////////
		} else if (msgString.equals("get all complaints")) {
			System.out.println("\n====== Processing Get All Complaints ======");
			try {
				List<Complaint> complaints = ConnectToDataBase.getAllComplaints();
				System.out.println("Fetched complaints from DB: " + complaints);

				client.sendToClient(complaints);
				System.out.println("Sent complaints to client");
			} catch (Exception e) {
				System.err.println("Error retrieving complaints: " + e.getMessage());
				e.printStackTrace();
				try {
					Warning warning = new Warning("Failed to retrieve complaints: " + e.getMessage());
					client.sendToClient(warning);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		} else if (msgString.startsWith("update complaint")) {
			System.out.println("\n====== Processing Complaint Update ======");
			try {
				String[] parts = msgString.split("#");
				if (parts.length != 5) {
					throw new IllegalArgumentException("Invalid update format");
				}

				int complaintId = Integer.parseInt(parts[1]);
				String status = parts[2];
				String solution = parts[3];
				double compensation = Double.parseDouble(parts[4]);

				Complaint complaint = ConnectToDataBase.getComplaintById(complaintId);
				if (complaint == null) {
					throw new Exception("Complaint not found");
				}

				complaint.setStatus(status);
				complaint.setSolution(solution);
				complaint.setCompensationAmount(compensation);

				ConnectToDataBase.updateComplaint(complaint);

				client.sendToClient("Complaint updated successfully.");
				System.out.println("Complaint updated successfully");

			} catch (Exception e) {
				System.err.println("Error updating complaint: " + e.getMessage());
				e.printStackTrace();
				try {
					Warning warning = new Warning("Failed to update complaint: " + e.getMessage());
					client.sendToClient(warning);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} else if (msgString.startsWith("send email#")) {
			try {
				String[] parts = msgString.split("#", 4);
				if (parts.length != 4) {
					throw new IllegalArgumentException("Invalid email format");
				}

				String recipientEmail = parts[1];
				String subject = parts[2];
				String body = parts[3];

				System.out.println("Sending email to: " + recipientEmail);
				System.out.println("Subject: " + subject);
				System.out.println("Body:\n" + body);

				// Send email (you can replace this with a real mail sending method)
				MailUtil.sendEmail(recipientEmail, subject, body);

				client.sendToClient("Email sent successfully.");
			} catch (Exception e) {
				System.err.println("Error sending email: " + e.getMessage());
				e.printStackTrace();
				try {
					Warning warning = new Warning("Failed to send email: " + e.getMessage());
					client.sendToClient(warning);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		System.out.println("Client Disconnected.");
		super.clientDisconnected(client);
	}

	@Override
	protected synchronized void clientConnected(ConnectionToClient client) {
		System.out.println("New Client Connected.");
		super.clientConnected(client);
	}

	private void sendMenuItemListToClient(ConnectionToClient client) {
		try {
			client.sendToClient(this.MenuItemDatabase);
			System.out.format("Sent MenuItem list to client %s\n", client.getInetAddress().getHostAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendWarningToClient(ConnectionToClient client, String message) {
		Warning warning = new Warning(message);
		try {
			client.sendToClient(warning);
			System.out.format("Sent warning to client %s: %s\n", client.getInetAddress().getHostAddress(), message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}