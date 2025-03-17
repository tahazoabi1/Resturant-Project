package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.scene.control.Alert;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

public class SimpleClient extends AbstractClient {
	private static SimpleClient client = null;
	public static String newHost = "localhost";
	public static int newPort = 3000;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.println("Received message from server of type: " + msg.getClass().getName());

		if (msg instanceof Warning) {
			System.out.println("Received warning from server");
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		}

		if (msg instanceof List<?>) {
			List<?> list = (List<?>) msg;

			if (!list.isEmpty()) {
				Object firstElement = list.get(0);
				System.out.println("First element in list is of type: " + firstElement.getClass().getName()); // Debugging line

				if (firstElement instanceof MenuItem) {
					List<MenuItem> menuItems = (List<MenuItem>) list;
					System.out.println("Received " + menuItems.size() + " menu items");
					EventBus.getDefault().post((menuItems));
				}
				else if (firstElement instanceof Branch) {
					List<Branch> branches = (List<Branch>) list;
					System.out.println("Received " + branches.size() + " branches");
					EventBus.getDefault().post(branches);
				}
				else {
					System.err.println("Received an unknown list type: " + firstElement.getClass().getName());
				}
			} else {
				System.err.println("Received an empty list from the server");
			}
		}
		else if (msg instanceof MenuItem) {
			System.out.println("Received single MenuItem");
			EventBus.getDefault().post(msg);
		}
		else if (msg instanceof User){
			System.out.println("Received User from the server");
			EventBus.getDefault().post(msg);
		}
		else if (msg instanceof String){
			String msgString = msg.toString();
			if(msgString.equals("Register Completed"));
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Registered Completed");
				alert.setContentText("Registered Successfully");
				alert.show();
			}
		}

		else{
			System.err.println("Got nothing from the server");
		}
	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient(newHost, newPort);
		}
		return client;
	}

	@Override
	protected void connectionEstablished() {
		super.connectionEstablished();
		System.out.println("Connected to server - connection established!");
	}

	@Override
	protected void connectionClosed() {
		super.connectionClosed();
		System.out.println("Connection closed!");
	}

	public void sendToServer(String msg) throws IOException {
		System.out.println("Sending to server: " + msg);
		super.sendToServer(msg);
	}
}
