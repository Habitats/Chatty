package network.client;

import gui.FeedListener;
import gui.MainFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import network.NetworkHandler;
import network.ServerListener;

public class Client implements Runnable, ServerListener {

	private FeedListener feedListener;
	private String name;
	private int port;
	private PrintWriter out;
	private NetworkHandler networkHandler;
	private String hostname;

	public Client(int port, String hostname, FeedListener feedListener, String name, NetworkHandler networkHandler) throws IOException {
		this.feedListener = feedListener;
		this.name = name;
		this.port = port;
		this.hostname = hostname;
		this.networkHandler = networkHandler;
	}

	private Socket setUpConnection(int port, String hostname) {
		Socket socket = null;
		System.out.println("Connecting to " + hostname + " on " + port);
		try {
			socket = new Socket(hostname, port);
			feedListener.sendMessageToFeed("Connected!");
			return socket;
		} catch (UnknownHostException e) {
			feedListener.sendMessageToFeed("don't know about host: " + hostname);
		} catch (IOException e) {
			feedListener.sendMessageToFeed("Connection failed.");
			networkHandler.setRunning(false);
		}
		return null;
	}

	@Override
	public void onMessage(String msg) {
		// feedListener.sendMessageToFeed(msg);
		out.println(msg);
	}

	@Override
	public void run() {
		feedListener.sendMessageToFeed("Starting client...");
		Socket echoSocket = null;

		// String hostname = "78.91.48.1";

		echoSocket = setUpConnection(port, hostname);

		// return if connection went to hell or w/e
		if (echoSocket == null)
			return;

		BufferedReader in = null;

		try {
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

			String fromServer;
			String fromUser = "";

			// this loop constantly checks for changes a on the socket
			while ((fromServer = in.readLine()) != null) {
				if (fromUser.length() > 0) {
					out.println(fromUser);
					fromUser = "";
				}
				feedListener.sendMessageToFeed(fromServer);
			}

			out.close();
			in.close();
			echoSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("END");
	}

	public PrintWriter getOutputStream() {
		return out;
	}

}
