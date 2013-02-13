package network.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import chatty.ChatEvent;
import chatty.CommandEvent;
import chatty.User;

import network.NetworkHandler;
import network.ProgramState;
import network.server.ServerEvent.ServerEvents;

public class Server extends ProgramState implements Runnable {

	private ArrayList<ClientConnection> clientConnections = new ArrayList<ClientConnection>();
	private HashMap<String, User> users = new HashMap<String, User>();

	private boolean listening = true;

	private ServerSocket serverSocket;

	private Socket clientSocket;

	private NetworkHandler networkHandler;

	public Server(int port, NetworkHandler networkHandler) throws IOException {
		setNetworkHandler(networkHandler);

		super.port = port;
	}

	private ServerSocket setUpServer(int port) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			return serverSocket;
		} catch (Exception e) {
			getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.SHUTDOWN, e, "Failed to setup server on port " + port + " failed. Exiting..."));
			return null;
		}
	}

	private Socket listenForIncomingConnections(ServerSocket serverSocket) {
		Socket socket = null;
		try {
			getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.STATUS, "Listening for connections on port " + port + "..."));
			socket = serverSocket.accept();
			return socket;
		} catch (SocketException e) {
			getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.SHUTDOWN, e));
			return null;
		} catch (NullPointerException | IOException e) {
			getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.CRASH, e));
			return null;
		}
	}

	@Override
	public void run() {
		getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.START));

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
				getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.CLIENT_CONNECT, clientIp + " connected on local port " + localPort + "!"));
				setRunning(true);
			}
			setRunning(false);
		} finally {
			getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.SHUTDOWN));
			kill();
		}
	}

	private void connectWithClient(final Socket clientSocket) {
		ServerConnection serverConnection = new ServerConnection(clientSocket, this);
		Thread serverConnectionThread = new Thread(serverConnection);
		serverConnectionThread.start();
	}

	synchronized public NetworkHandler getNetworkHandler() {
		return networkHandler;
	}

	/*
	 * HANDLES OBJECTS
	 */
	public void broadcastChatEventToClients(ChatEvent chatEvent) {
		for (int i = 0; i < getClientConnections().size(); i++) {
			ObjectOutputStream currentObjectOutputStream = getClientConnections().get(i).getObjectOutputStream();
			try {
				currentObjectOutputStream.writeObject(chatEvent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void broadcastChatEventToAll(ChatEvent chatEvent) {
		getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.CHAT_EVENT, chatEvent));
		broadcastChatEventToClients(chatEvent);
	}

	public void broadcastChatEvent(ChatEvent chatEvent) {
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

	public HashMap<String, User> getUsers() {
		return users;
	}

	public ArrayList<ClientConnection> getClientConnections() {
		return clientConnections;
	}

	public void sendPrivateChatEvent(ChatEvent chatEvent) {
		String to = chatEvent.getReceipient().getUsername();
		for (ClientConnection clientConnection : clientConnections) {
			// if clientConnection is the RECEIVER or SENDER
			if (clientConnection.getUser().getName().equals(to) || clientConnection.getUser() == chatEvent.getFrom())
				try {
					clientConnection.getObjectOutputStream().writeObject(chatEvent);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
