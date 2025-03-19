package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
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
			System.out.println("\n====== Processing Get All Branches ======");
			try {
				System.out.println("Requesting branches from database...");
				List<Branch> temp = ConnectToDataBase.getAllBranches();
				System.out.println("Retrieved " + (temp != null ? temp.size() : 0) + " branches from database");

				if (temp != null && !temp.isEmpty()) {
					System.out.println("Branches found:");
					for (Branch branch : temp) {
						System.out.println("  - " + branch.getName() + " (" + branch.getClass().getName() + ")");
					}
				} else {
					System.out.println("No branches found in database");
				}

				client.sendToClient(temp);
				System.out.println("✅ Sent branches to client: " + temp.getClass().getName());

			} catch (Exception e) {
				System.err.println("❌ Error retrieving branches: " + e.getMessage());
				e.printStackTrace();
				try {
					Warning warning = new Warning("Failed to retrieve branches: " + e.getMessage());
					client.sendToClient(warning);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		else if (msgString.startsWith("Update price")) {
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
			System.out.println("\n====== Processing Login ======");
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
		}

		else if (msgString.startsWith("Rigister Customer")) {
			System.out.println("\n====== Processing Rigister Customer ======");
			try {
				String[] parts = msgString.split("#");
				if (parts.length != 7) {
					throw new IllegalArgumentException("Invalid Email or Password format");
				}

				String name = parts[1];
				String phoneNumber = parts[2];
				String adress = parts[3];
				String email = parts[4];
				String password = parts[5];
				String paymentMethod = parts[6];

				System.out.println("Rigister Customer...");

				// Update in database

				ConnectToDataBase.addCustomer(name, phoneNumber, adress, email, password, paymentMethod);

				client.sendToClient("Register Completed");

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
		}

		else if (msgString.startsWith("get menu items for branch#")) {
			System.out.println("\n====== Processing Get Menu Items for Branch ======");

			try {
				String[] parts = msgString.split("#");
				if (parts.length != 2) {
					throw new IllegalArgumentException("Invalid request format");
				}

				String branchName = parts[1];
				System.out.println("Fetching menu items for branch: " + branchName);

				List<MenuItem> items = ConnectToDataBase.getMenuItemsByBranch(branchName);
				client.sendToClient(items);
				System.out.println("✅ Sent menu items for branch: " + branchName);

			} catch (Exception e) {
				System.err.println("❌ Error retrieving menu items for branch: " + e.getMessage());
				e.printStackTrace();
				try {
					client.sendToClient(new Warning("Failed to get menu items: " + e.getMessage()));
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