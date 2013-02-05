package network.server;

import gui.EventListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import network.NetworkHandler;
import network.ProgramState;

public class Server extends ProgramState implements Runnable {

	private NetworkHandler networkHandler;
	private EventListener eventListener;

	private ArrayList<ClientConnection> clientConnections = new ArrayList<ClientConnection>();

	private boolean listening = true;

	private ServerSocket serverSocket;

	private Socket clientSocket;

	public Server(int port, EventListener eventListener, NetworkHandler networkHandler) throws IOException {
		setNetworkHandler(networkHandler);
		setEventListener(eventListener);

		super.port = port;
	}

	public EventListener getEventListener() {
		return eventListener;
	}

	private ServerSocket setUpServer(int port) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			getEventListener().sendStatusToOwnFeed("Running server on " + port + "!");
			return serverSocket;
		} catch (Exception e) {
			getEventListener().sendStatusToOwnFeed("Failed to setup server on port " + port + " failed. Exiting...");
			return null;
		}
	}

	private Socket listenForIncomingConnections(ServerSocket serverSocket) {
		Socket socket = null;
		try {
			getEventListener().sendStatusToOwnFeed("Listening for connections...");
			socket = serverSocket.accept();
			return socket;
		} catch (SocketException e) {
			getEventListener().sendStatusToOwnFeed("Server shutting down!");
			return null;
		} catch (NullPointerException e) {
			getEventListener().sendErrorToOwnFeed("Server already running?");
			return null;
		} catch (IOException e) {
			getEventListener().sendErrorToOwnFeed("IO Error.");
			return null;
		}
	}

	@Override
	public void run() {
		getEventListener().sendStatusToOwnFeed("Starting server...");

		// tries to open up a socket on PORT, returns if fail
		if ((serverSocket = setUpServer(port)) == null)
			return;

		setServer(true);
		setClient(false);

		// tries to initiate a connection to the client by accepting incoming
		// connections
		// the accept method listens for incoming connections
		try {
			while (listening) {
				clientSocket = listenForIncomingConnections(serverSocket);
				if (clientSocket == null)
					return;
				connectWithClient(clientSocket);
				String clientIp = clientSocket.getRemoteSocketAddress().toString().split("[/:]")[1];
				String localPort = clientSocket.getRemoteSocketAddress().toString().split("[/:]")[2];
				getEventListener().sendStatusToOwnFeed(clientIp + " connected on local port " + localPort + "!");
				setRunning(true);
			}
			setRunning(false);
			getEventListener().sendStatusToOwnFeed("Server shutting down!");
		} finally {
			kill();
		}
	}

	private void connectWithClient(final Socket clientSocket) {
		ServerConnection serverConnection = new ServerConnection(clientSocket, getEventListener(), this);
		Thread clientThread = new Thread(serverConnection);
		clientThread.start();
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;
	}

	// broadcasts message to ALL connected OUTPUT streams
	// only SERVER should be calling this
	public void broadcastMessageToClients(String msg) {
		for (int i = 0; i < getClientConnections().size(); i++) {
			PrintWriter currentOut = getClientConnections().get(i).getOutputStream();
			// if(client)
			currentOut.println(msg);
		}
	}
	
	public void broadcastMessageToAll(String msg){
		eventListener.sendNormalMessageToOwnFeed(msg);
		broadcastMessageToClients(msg);
	}

	public void broadcastServerMessage(String msg) {
		broadcastMessageToAll("SERVER: " + msg);
	}

	public void broadcastStatusMessage(String msg) {
		broadcastMessageToAll("STATUS: " + msg);
	}

	@Override
	public void kill() {
		setRunning(false);
		getNetworkHandler().serverDisconnect();
		listening = false;
		try {
			if (serverSocket != null)
				serverSocket.close();
			if (clientSocket != null)
				clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setEventListener(EventListener eventListener) {
		this.eventListener = eventListener;
	}

	public void setNetworkHandler(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	public ArrayList<ClientConnection> getClientConnections() {
		return clientConnections;
	}
}
