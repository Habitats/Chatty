package network;

import java.io.IOException;
import java.util.ArrayList;

import chatty.Config;

import network.client.Client;
import network.client.ClientEvent;
import network.server.Server;
import network.server.ServerEvent;

public class NetworkHandler {


	private ProgramState programState;
	private Server serverInstance;
	private Client clientInstance;

	// store all listeners
	private ArrayList<NetworkListener> networkListeners = new ArrayList<NetworkListener>();
	// store all events for loggins purposes
	private ArrayList<NetworkEvent> networkEvents = new ArrayList<NetworkEvent>();

	public void startServer() {
		startServer(Config.DEFAULT_PORT);
	}

	public void restartServer(int port) {
		if (port == 0)
			port = Config.DEFAULT_PORT;
		getServer().kill();
		startServer(port);
	}

	private void startServer(int port) {
		Thread serverThread;
		try {
			serverInstance = new Server(port, this);
			programState = serverInstance;
			serverThread = new Thread((Server) programState);
			serverThread.start();
		} catch (IOException e) {
			programState = null;
			e.printStackTrace();
		}
	}

	public void startClient() {
		startClient(Config.DEFAULT_HOST, Config.DEFAULT_PORT);
	}

	public void startClient(String hostname) {
		startClient(hostname, Config.DEFAULT_PORT);
	}

	private void startClient(String hostname, int port) {
		Thread clientThread;
		try {
			clientInstance = new Client(port, hostname, this);
			programState = clientInstance;
			clientThread = new Thread((Client) programState);
			clientThread.start();
		} catch (IOException e) {
			programState = null;
			e.printStackTrace();
		}
	}

	public void addNetworkListener(NetworkListener listener) {
		networkListeners.add(listener);
	}

	public void fireServerEvent(ServerEvent event) {
		networkEvents.add(event);
		for (NetworkListener listener : networkListeners) {
			switch (event.getEvent()) {
			case START:
				if (event.getMsg() == null)
					event.setMsg("Starting server...");
				listener.serverStart(event);
				break;
			case CLIENT_DROPPED:
				if (event.getMsg() == null)
					event.setMsg("Client connection dropped!");
				listener.serverStart(event);
				break;
			case SHUTDOWN:
				if (event.getMsg() == null)
					event.setMsg("Server shutting down!");
				listener.serverShutDown(event);
				break;
			case CLIENT_CONNECT:
				if (event.getMsg() == null)
					event.setMsg("Client connected!");
				listener.clientDropped(event);
				break;
			case CRASH:
				if (event.getMsg() == null)
					event.setMsg("Server crashed!");
				listener.serverCrashed(event);
				break;
			case STATUS:
				listener.serverStatus(event);
				break;
			default:
				break;
			}
		}
	}

	public void fireClientEvent(ClientEvent event) {
		networkEvents.add(event);
		for (NetworkListener listener : networkListeners) {
			switch (event.getEvent()) {
			case START:
				if (event.getMsg() == null)
					event.setMsg("Client starting...");
				listener.clientStart(event);
				break;
			case CONNECT:
				if (event.getMsg() == null)
					event.setMsg("Client connected!");
				listener.clientStart(event);
				break;
			case SHUTDOWN:
				if (event.getMsg() == null)
					event.setMsg("Client shutting down!");
				listener.clientShutDown(event);
				break;
			case CRASH:
				if (event.getMsg() == null)
					event.setMsg("Client crashed!");
				listener.clientCrashed(event);
				break;
			case STATUS:
				listener.clientStatus(event);
				break;
			case MESSAGE:
				listener.clientMessage(event);
				break;
			default:
				break;
			}
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
