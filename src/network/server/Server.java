package network.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import chatty.CommandEvent;

import network.NetworkHandler;
import network.ProgramState;
import network.server.ServerEvent.Event;

public class Server extends ProgramState implements Runnable {

	private ArrayList<ClientConnection> clientConnections = new ArrayList<ClientConnection>();

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
			getNetworkHandler().fireServerEvent(new ServerEvent(Event.SHUTDOWN, e, "Failed to setup server on port " + port + " failed. Exiting..."));
			return null;
		}
	}

	private Socket listenForIncomingConnections(ServerSocket serverSocket) {
		Socket socket = null;
		try {
			getNetworkHandler().fireServerEvent(new ServerEvent(Event.STATUS, "Listening for connections on port " + port + "..."));
			socket = serverSocket.accept();
			return socket;
		} catch (SocketException e) {
			getNetworkHandler().fireServerEvent(new ServerEvent(Event.SHUTDOWN, e));
			return null;
		} catch (NullPointerException | IOException e) {
			getNetworkHandler().fireServerEvent(new ServerEvent(Event.CRASH, e));
			return null;
		}
	}

	@Override
	public void run() {
		getNetworkHandler().fireServerEvent(new ServerEvent(Event.START));

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
				getNetworkHandler().fireServerEvent(new ServerEvent(Event.CLIENT_CONNECT, clientIp + " connected on local port " + localPort + "!"));
				setRunning(true);
			}
			setRunning(false);
		} finally {
			getNetworkHandler().fireServerEvent(new ServerEvent(Event.SHUTDOWN));
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
	public void broadcastChatEventToClients(CommandEvent chatEvent) {
		for (int i = 0; i < getClientConnections().size(); i++) {
			ObjectOutputStream currentObjectOutputStream = getClientConnections().get(i).getObjectOutputStream();
			try {
				currentObjectOutputStream.writeObject(chatEvent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void broadcastChatEventToAll(CommandEvent chatEvent) {
		getNetworkHandler().fireServerEvent(new ServerEvent(Event.OBJECT, chatEvent));
		broadcastChatEventToClients(chatEvent);
	}

	/*
	 * HANDLES CLEAR TEXT ONLY
	 */
	public void broadcastMessageToClients(String msg) {
		for (int i = 0; i < getClientConnections().size(); i++) {
			PrintWriter currentOut = getClientConnections().get(i).getOutputStream();
			currentOut.println(msg);
		}
	}

	private void broadcastMessageToAll(String msg) {
		getNetworkHandler().fireServerEvent(new ServerEvent(Event.MESSAGE, msg));
		broadcastMessageToClients(msg);
	}

	public void broadcastServerMessage(String msg) {
		CommandEvent chatEvent = new CommandEvent(msg);
		broadcastMessageToAll("SERVER: " + msg);
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

	public ArrayList<ClientConnection> getClientConnections() {
		return clientConnections;
	}
}
