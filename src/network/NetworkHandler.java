package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import chatty.Config;

import network.client.Client;
import network.server.Server;

import gui.EventListener;
import gui.MainFrame;

public class NetworkHandler implements NetworkListener {

	private MainFrame gui;
	private int port;
	private String hostname;

	private ProgramState programState;
	private Server serverInstance;
	private Client clientInstance;
	private EventListener eventListener;

	public NetworkHandler(int port, String hostname, EventListener eventListener) {
		this.port = port;
		this.hostname = hostname;
		this.eventListener = eventListener;
	}

	@Override
	public void startServer() {
		Thread serverThread;
		try {
			serverInstance = new Server(port, eventListener, this);
			programState = serverInstance;
			serverThread = new Thread((Server) programState);
			serverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startClient(String name) {
		Thread clientThread;
		try {
			clientInstance = new Client(port, hostname, eventListener, name, this);
			programState = clientInstance;
			clientThread = new Thread((Client) programState);
			clientThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCrash() {
	}

	@Override
	public void lostConnection() {
	}

	@Override
	public void serverDisconnect() {
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
