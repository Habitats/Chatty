package network;

import java.io.IOException;

import network.client.Client;
import network.server.Server;

import gui.MainFrame;

public class NetworkHandler implements NetworkListener {

	private MainFrame gui;
	private int port;
	private Server server;
	private Client client;
	private boolean isClient;
	private boolean isRunning = false;
	private String hostname;

	public NetworkHandler(int port, String hostname, MainFrame gui) {
		this.gui = gui;
		this.port = port;
		this.hostname = hostname;
	}

	public void startServer() {
		Thread serverThread;
		try {
			server = new Server(port, gui.getFeedWindow(), this);

			serverThread = new Thread(server);
			serverThread.start();
			isClient = false;
			isRunning = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startClient(String name) {
		Thread clientThread;
		try {
			client = new Client(port, hostname, gui.getFeedWindow(), name, this);
			clientThread = new Thread(client);
			// server.clients.add(client);
			clientThread.start();
			isClient = true;
			isRunning = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Client getClient() {
		return client;
	}

	public boolean isClient() {
		return isClient;
	}

	public Server getServer() {
		return server;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean value) {
		this.isRunning = value;
	}
}
