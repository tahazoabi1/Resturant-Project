package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.MenuItem;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
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
			System.out.println("Received list from server");
			List<?> list = (List<?>) msg;
			if (!list.isEmpty() && list.get(0) instanceof MenuItem) {
				@SuppressWarnings("unchecked")
				List<MenuItem> menuItems = (List<MenuItem>) list;
				System.out.println("Received " + menuItems.size() + " menu items");
				EventBus.getDefault().post(menuItems);
			}
			if (!list.isEmpty() && list.get(0) instanceof Branch) {
				@SuppressWarnings("unchecked")
				List<Branch> branches = (List<Branch>) list;
				System.out.println("Received " + branches.size() + " branches");
				EventBus.getDefault().post(branches);
			}
			else{
				System.err.println("got list but the the wanted one or maybe empty");

			}
		}
		else if (msg instanceof MenuItem) {
			System.out.println("Received single MenuItem");
			EventBus.getDefault().post(msg);
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
