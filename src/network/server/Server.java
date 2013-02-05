package network.server;

import gui.FeedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

import chatty.Config;

import network.NetworkHandler;
import network.ProgramState;
import network.client.Client;

public class Server extends ProgramState implements Runnable {

	private ArrayList<PrintWriter> outputStreams = new ArrayList<PrintWriter>();

	private NetworkHandler networkHandler;
	private FeedListener feedListener;

	private boolean listening = true;

	public Server(int port, FeedListener feedListener, NetworkHandler networkHandler) throws IOException {
		this.networkHandler = networkHandler;
		this.feedListener = feedListener;

		super.port = port;
	}

	private ServerSocket setUpServer(int port) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			feedListener.sendMessageToFeed("Running server on " + port + "!");
			return serverSocket;
		} catch (Exception e) {
			feedListener.sendMessageToFeed("Failed to setup server on port " + port + " failed. Exiting...");
			return null;
		}
	}

	private Socket listenForIncomingConnections(ServerSocket serverSocket) {
		Socket socket = null;
		try {
			feedListener.sendMessageToFeed("Listening for connections...");
			socket = serverSocket.accept();
			return socket;
		} catch (IOException e) {
			feedListener.sendMessageToFeed("ERROR. Server already running?");
			return null;
		}
	}

	@Override
	public void run() {
		setServer(true);
		setClient(false);
		feedListener.sendMessageToFeed("Starting server...");
		// tries to open up a server on the specified port
		ServerSocket serverSocket = setUpServer(port);
		// tries to initiate a connection to the client by accepting incoming
		// connections
		// the accept method listens for incoming connections
		try {
			while (listening) {
				Socket clientSocket = listenForIncomingConnections(serverSocket);
				connectWithClient(clientSocket);
				feedListener.sendMessageToFeed(clientSocket.getRemoteSocketAddress().toString().split("[/:]")[1] + " connected!");
				setRunning(true);
			}
			serverSocket.close();
		} catch (NullPointerException e) {
			feedListener.sendMessageToFeed("CONFLICT! Another server already running?");
			kill();
		} catch (IOException e) {
			feedListener.sendMessageToFeed("Connection dropped. IO ERROR, see trace.");
			kill();
			e.printStackTrace();
		}
	}

	public void broadcastMessageToClients(String msg) {
		for (int i = 0; i < getOutputStreams().size(); i++) {
			PrintWriter currentOut = getOutputStreams().get(i);
			// if(client)
			currentOut.println(msg);
		}
	}

	private void connectWithClient(final Socket clientSocket) {
		ServerConnection serverConnection = new ServerConnection(clientSocket, feedListener, this);
		Thread clientThread = new Thread(serverConnection);
		clientThread.start();
	}

	public ArrayList<PrintWriter> getOutputStreams() {
		return outputStreams;
	}

	@Override
	public void kill() {
		setRunning(false);
		listening = false;
	}
}
