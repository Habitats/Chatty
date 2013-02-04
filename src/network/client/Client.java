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

import network.ServerListener;

public class Client implements Runnable, ServerListener {

	FeedListener feedListener;
	String name;
	private int port;
	private PrintWriter out;

	public Client(int port, FeedListener feedListener, String name) throws IOException {
		this.feedListener = feedListener;
		this.name = name;
		this.port = port;
	}

	private Socket setUpConnection(String hostname, int port) {
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
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onMessage(String msg) {
		feedListener.sendMessageToFeed(msg);
		out.println(msg);
	}

	@Override
	public void run() {
		feedListener.sendMessageToFeed("Starting client...");
		Socket echoSocket = null;

		// String hostname = "78.91.48.1";
		String hostname = "localhost";

		echoSocket = setUpConnection(hostname, port);

		BufferedReader in = null;

		try {
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

			String fromServer;
			String fromUser = "Hello Server!";

			// this loop constantly checks for changes a on the socket
			while ((fromServer = in.readLine()) != null) {
				if (fromUser.length() > 0) {
					System.out.println("Client: " + fromUser);
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
