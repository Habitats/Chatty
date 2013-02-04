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

import network.client.Client;




public class Server implements Runnable {
	private FeedListener feedListener;
	private int port;
	private boolean listening = true;
	ArrayList<Client> clients = new ArrayList<Client>();
	ArrayList<PrintWriter> outputStreams = new ArrayList<PrintWriter>();

	public Server(int port, FeedListener feedListener) throws IOException {
		this.feedListener = feedListener;
		this.port = port;
	}

	private ServerSocket setUpServer(int port) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Connected on port " + port);
			return serverSocket;
		} catch (Exception e) {
			System.out.println("Connection on port " + port + " failed. Exiting...");
			return null;
		}
	}

	private Socket listenForIncomingConnections(ServerSocket serverSocket) {
		Socket socket = null;
		try {
			System.out.println("Listening for connections...");
			socket = serverSocket.accept();
			System.out.println("Connected!");
			return socket;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void run() {
		feedListener.sendMessageToFeed("Starting server...");
		// tries to open up a server on the specified port
		ServerSocket serverSocket = setUpServer(port);
		// tries to initiate a connection to the client by accepting incoming
		// connections
		// the accept method listens for incoming connections
		while (listening) {
			Socket clientSocket = listenForIncomingConnections(serverSocket);
			connectWithClient(clientSocket);
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void connectWithClient(final Socket clientSocket) {
		ServerConnection serverConnection = new ServerConnection(clientSocket, feedListener, outputStreams);
		Thread clientThread = new Thread(serverConnection);
		clientThread.start();
	}
}
