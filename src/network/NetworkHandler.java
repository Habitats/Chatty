package network;

import java.io.IOException;

import network.client.Client;
import network.server.Server;

import gui.MainFrame;

public class NetworkHandler implements NetworkListener {

	private MainFrame gui;
	private int port;
	private String hostname;

	private ProgramState programState;
	private Server serverInstance;
	private Client clientInstance;

	public NetworkHandler(int port, String hostname, MainFrame gui) {
		this.gui = gui;
		this.port = port;
		this.hostname = hostname;
	}

	public void startServer() {
		Thread serverThread;
		try {
			serverInstance = new Server(port, gui.getFeedWindow(), this);
			programState = serverInstance;
			serverThread = new Thread((Server) programState);
			serverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startClient(String name) {
		Thread clientThread;
		try {
			clientInstance = new Client(port, hostname, gui.getFeedWindow(), name, this);
			programState = clientInstance;
			clientThread = new Thread((Client) programState);
			clientThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		if (programState != null)
			return programState.isRunning();
		else
			return false;
	}

	public ProgramState getProgramState() {
		return programState;
	}

	public void shutDown() {
		programState.kill();
	}

	public Server getServer() {
		return serverInstance;
	}

	public Client getClient() {
		return clientInstance;
	}
}
