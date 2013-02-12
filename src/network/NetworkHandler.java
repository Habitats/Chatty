package network;

import java.io.IOException;
import java.util.ArrayList;

import chatty.Controller;

import network.client.Client;
import network.client.ClientEvent;
import network.client.ClientEvent.ClientEvents;
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
	private final Controller controller;

	public NetworkHandler(Controller controller) {
		this.controller = controller;
	}

	public void restartServer(int port) {
		if (port == 0)
			port = controller.getPort();
		getServer().kill();
		startServer(port);
	}

	public void startServer(int port) {
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

	public void restartClient(String hostname, int port) {
		getProgramState().kill();
		startClient(hostname, port);
	}

	public void startClient(String hostname, int port) {
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

	synchronized public void fireServerEvent(ServerEvent event) {
		networkEvents.add(event);
		for (NetworkListener listener : networkListeners) {
			switch (event.getEvent()) {
			case START:
				if (event.getMsg() == null)
					event.setMsg("Starting server...");
				listener.serverStatus(event);
				break;
			case CLIENT_DROPPED:
				if (event.getMsg() == null)
					event.setMsg("Client connection dropped!");
				listener.serverStatus(event);
				break;
			case SHUTDOWN:
				if (event.getMsg() == null)
					event.setMsg("Server shutting down!");
				System.out.println("dicks");
				listener.serverStatus(event);
				break;
			case CLIENT_CONNECT:
				if (event.getMsg() == null)
					event.setMsg("Client connected!");
				listener.serverStatus(event);
				break;
			case CRASH:
				if (event.getMsg() == null) {
					event.setMsg("Server crashed!");
					event.getException().printStackTrace();
				}
				listener.serverStatus(event);
				break;
			case STATUS:
				listener.serverStatus(event);
				break;
			case CHAT_EVENT:
				listener.serverNormalMessage(event);
				break;
			default:
				break;
			}
		}
	}

	synchronized public void fireClientEvent(ClientEvent event) {
		if (event.getEvent() == ClientEvents.COMMAND)
			controller.executeChatCommand(event.getChatEvent());
		else {
			networkEvents.add(event);
			for (NetworkListener listener : networkListeners) {
				switch (event.getEvent()) {
				case START:
					if (event.getMsg() == null)
						event.setMsg("Client starting...");
					listener.clientStatus(event);
					break;
				case CONNECT:
					if (event.getMsg() == null)
						event.setMsg("Client connected!");
					listener.clientStatus(event);
					break;
				case SHUTDOWN:
					if (event.getMsg() == null)
						event.setMsg("Client shutting down!");
					listener.clientStatus(event);
					break;
				case CRASH:
					if (event.getMsg() == null)
						event.setMsg("Client crashed!");
					listener.clientStatus(event);
					break;
				case STATUS:
					listener.clientStatus(event);
					break;
				case CHAT_EVENT:
					listener.clientMessage(event);
					break;
				default:
					break;
				}
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
