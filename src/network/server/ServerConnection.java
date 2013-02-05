package network.server;

import gui.EventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ServerConnection implements Runnable {

	private Socket clientSocket;
	private PrintWriter out;
	private Server server;

	public ServerConnection(Socket clientSocket, EventListener feedListener, Server server) {
		this.clientSocket = clientSocket;
		this.server = server;
	}

	@Override
	public void run() {
		String fromUser;
		// TODO: Needs polishing
		String welcomeMsg = "SERVER: Welcome Human, I'm a server!";

		out = null;
		BufferedReader in = null;

		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			server.getClientConnections().add(new ClientConnection(out, clientSocket));
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// welcome message to new client
			out.println(welcomeMsg);

			while ((fromUser = in.readLine()) != null) {
				// send message to SERVER (self)
				server.getEventListener().sendNormalMessageToOwnFeed(fromUser);

				// broadcast INCOMING message to ALL
				server.broadcastMessageToClients(fromUser);
			}

			out.close();
			in.close();
			clientSocket.close();
		} catch (SocketException e) {
			server.getEventListener().sendErrorToOwnFeed("Server connection dropped!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("END");
	}

	public PrintWriter getOutputStream() {
		return out;
	}
}
