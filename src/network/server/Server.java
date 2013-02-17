package network.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import chatty.User;

import msg.ChatEvent;
import network.NetworkEvent;
import network.NetworkHandler;
import network.ProgramState;
import network.NetworkEvent.NetworkEvents;

public class Server extends ProgramState implements Runnable {

	private List<ClientConnection> clientConnections = Collections.synchronizedList(new ArrayList<ClientConnection>());
	private HashMap<String, User> users = new HashMap<String, User>();

	private boolean listening = true;

	private ServerSocket serverSocket;

	private Socket clientSocket;

	private NetworkHandler networkHandler;
	private User serverUser;

	public Server(int port, NetworkHandler networkHandler) throws IOException {
		setNetworkHandler(networkHandler);
		this.serverUser = new User("SERVER");
		super.port = port;
	}

	private ServerSocket setUpServer(int port) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			return serverSocket;
		} catch (Exception e) {
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.SHUTDOWN_SERVER, e, "Failed to setup server on port " + port + " failed. Exiting..."));
			return null;
		}
	}

	private Socket listenForIncomingConnections(ServerSocket serverSocket) {
		Socket socket = null;
		try {
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.STATUS, "Listening for connections on port " + port + "..."));
			socket = serverSocket.accept();
			return socket;
		} catch (SocketException e) {
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.SHUTDOWN_SERVER, "Server shutting down!"));
			return null;
		} catch (NullPointerException | IOException e) {
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.CRASH, e));
			return null;
		}
	}

	@Override
	public void run() {
		getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.START_SERVER,"Starting server..."));

		// tries to open up a socket on PORT, returns if fail
		if ((serverSocket = setUpServer(port)) == null)
			return;

		setServer(true);
		setClient(false);

		// tries to initiate a connection to the client by accepting incoming
		// connections
		// the accept method listens for incoming connections
		while (listening) {
			clientSocket = listenForIncomingConnections(serverSocket);
			if (clientSocket == null)
				return;
			connectWithClient(clientSocket);
			String clientIp = clientSocket.getRemoteSocketAddress().toString().split("[/:]")[1];
			String localPort = clientSocket.getRemoteSocketAddress().toString().split("[/:]")[2];
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.CLIENT_CONNECT, clientIp + " connected on local port " + localPort + "!"));
			setRunning(true);
		}
	}

	private void connectWithClient(final Socket clientSocket) {
		ServerConnection serverConnection = new ServerConnection(clientSocket, this);
		Thread serverConnectionThread = new Thread(serverConnection);
		serverConnectionThread.start();
	}

	public synchronized NetworkHandler getNetworkHandler() {
		return networkHandler;
	}

	/*
	 * HANDLES OBJECTS
	 */
	public synchronized void broadcastChatEventToClients(ChatEvent chatEvent) {
		ObjectOutputStream currentObjectOutputStream;
		for (ClientConnection clientConnection : clientConnections) {
			if (!clientConnection.getUser().getUsername().equals(chatEvent.getFrom().getUsername())) {
				currentObjectOutputStream = clientConnection.getObjectOutputStream();
				try {
					currentObjectOutputStream.reset();
					currentObjectOutputStream.writeObject(chatEvent);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void broadcastChatEventToAll(ChatEvent chatEvent) {
		getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.CHAT_EVENT, chatEvent));
		broadcastChatEventToClients(chatEvent);
	}

	public synchronized void broadcastChatEvent(ChatEvent chatEvent) {
		broadcastChatEventToAll(chatEvent);
	}

	@Override
	public void kill() {
		setRunning(false);
		listening = false;
		try {
			if (serverSocket != null)
				serverSocket.close();
			for (ClientConnection client : clientConnections) {
				if (client.getClientSocket() != null)
					client.getClientSocket().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setNetworkHandler(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	public synchronized HashMap<String, User> getUsers() {
		return users;
	}

	public List<ClientConnection> getClientConnections() {
		return clientConnections;
	}

	public synchronized void sendPrivateChatEvent(ChatEvent chatEvent) {
		String to = chatEvent.getTo();
		boolean success = false;
		for (ClientConnection clientConnection : clientConnections) {
			// if clientConnection is the RECEIVER or SENDER
			if (clientConnection.getUser().getDisplayName().toLowerCase().equals(to.toLowerCase()))
				try {
					clientConnection.getObjectOutputStream().reset();
					clientConnection.getObjectOutputStream().writeObject(chatEvent);
					success = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		if(!success)
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.STATUS, new ChatEvent(serverUser, chatEvent.getFrom().getDisplayName(), "No such user: " + to)));
	}
	public synchronized User getServerUser() {
		return serverUser;
	}
}
