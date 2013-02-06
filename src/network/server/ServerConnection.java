package network.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import network.server.ServerEvent.Event;

public class ServerConnection implements Runnable {

	private Socket clientSocket;
	private PrintWriter out;
	private Server server;

	public ServerConnection(Socket clientSocket, Server server) {
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
			out = new PrintWriter(getClientSocket().getOutputStream(), true);
			getServer().getClientConnections().add(new ClientConnection(out, getClientSocket()));
			in = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));

			// welcome message to new client
			out.println(welcomeMsg);

			while ((fromUser = in.readLine()) != null) {
				// send message to SERVER (self)
				getServer().getNetworkHandler().fireServerEvent(new ServerEvent(Event.STATUS, fromUser));

				// broadcast INCOMING message to ALL
				getServer().broadcastMessageToClients(fromUser);
			}

			out.close();
			in.close();
			getClientSocket().close();
		} catch (SocketException e) {
			getServer().getNetworkHandler().fireServerEvent(new ServerEvent(Event.CLIENT_DROPPED));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("END");
	}

	public PrintWriter getOutputStream() {
		return out;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}
}
