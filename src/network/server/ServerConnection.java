package network.server;

import gui.FeedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class ServerConnection implements Runnable {

	private Socket clientSocket;
	private FeedListener feedListener;
	private PrintWriter out;
	private Server server;

	public ServerConnection(Socket clientSocket, FeedListener feedListener, Server server) {
		this.clientSocket = clientSocket;
		this.feedListener = feedListener;
		this.server = server;
	}

	@Override
	public void run() {
		String fromUser;
		String fromServer;

		out = null;
		BufferedReader in = null;

		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			server.getOutputStreams().add(out);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// write to the socket
			out.println("Welcome Human, I'm a server!");
			fromServer = "this should only appear once";

			while ((fromUser = in.readLine()) != null) {
				if (fromServer.length() > 0) {
					out.println(fromServer);
					fromServer = "";
				}
				// send message to SERVER (self)
				feedListener.sendMessageToFeed(fromUser);
				server.broadcastMessageToClients(fromUser);
			}

			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("END");
	}

	public PrintWriter getOutputStream() {
		return out;
	}
}
