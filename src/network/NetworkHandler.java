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

	public NetworkHandler(int port, String hostname) {
		this.port = port;
		this.hostname = hostname;
	}

	@Override
	public void startServer() {
		startServer(Config.DEFAULT_PORT);
	}

	public void restartServer(int port) {
		if (port == 0)
			port = Config.DEFAULT_PORT;
		getServer().kill();
		startServer(port);
	}

	public void startServer(int port) {
		Thread serverThread;
		try {
			serverInstance = new Server(port, getEventListener(), this);
			programState = serverInstance;
			serverThread = new Thread((Server) programState);
			serverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startClient() {
		startClient(Config.DEFAULT_HOST, Config.DEFAULT_PORT);
	}

	public void startClient(String hostname) {
		startClient(hostname, Config.DEFAULT_PORT);
	}

	@Override
	public void startClient(String hostname, int port) {
		Thread clientThread;
		try {
			clientInstance = new Client(port, hostname, getEventListener(), this);
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

	public EventListener getEventListener() {
		return eventListener;
	}

	public void setEventListener(EventListener eventListener) {
		this.eventListener = eventListener;
	}

}
