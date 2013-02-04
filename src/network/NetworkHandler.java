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

	public NetworkHandler(int port, MainFrame gui) {
		this.gui = gui;
		this.port = port;
	}

	public void startServer() {
		Thread serverThread;
		try {
			server = new Server(port, gui.getFeedWindow());

			serverThread = new Thread(server);
			serverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startClient(String name) {
		Thread clientThread;
		try {
			client = new Client(port, gui.getFeedWindow(), name);
			clientThread = new Thread(client);
			// server.clients.add(client);
			clientThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessageToServer(String msg) {

	}

	public Client getClient() {
		return client;
	}
}
