package network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import chatty.CommandEvent;

import network.server.ServerEvent.Event;

public class ServerConnection implements Runnable {

	private Socket clientSocket;
	private Server server;
	private String fromUser;
	private String welcomeMsg;

	public ServerConnection(Socket clientSocket, Server server) {
		this.clientSocket = clientSocket;
		this.server = server;
	}

	private void initPrintSteamConnection() throws IOException {
		PrintWriter printWriterOutputStream = new PrintWriter(getClientSocket().getOutputStream(), true);
		getServer().getClientConnections().add(new ClientConnection(printWriterOutputStream, getClientSocket()));
		BufferedReader printWriterInputStream = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));

		// welcome message to new client
		printWriterOutputStream.println(welcomeMsg);

		while ((fromUser = printWriterInputStream.readLine()) != null) {
			// send message to SERVER (self)
			getServer().getNetworkHandler().fireServerEvent(new ServerEvent(Event.MESSAGE, fromUser));

			// broadcast INCOMING message to ALL
			getServer().broadcastMessageToClients(fromUser);
		}

		printWriterOutputStream.close();
		printWriterInputStream.close();
		getClientSocket().close();
	}

	private void initConnection() throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(getClientSocket().getOutputStream());
		getServer().getClientConnections().add(new ClientConnection(objectOutputStream, getClientSocket()));
		ObjectInputStream objectInputStream = new ObjectInputStream(getClientSocket().getInputStream());

		objectOutputStream.writeObject(welcomeMsg);
		CommandEvent chatEvent;

		try {
			while ((chatEvent = (CommandEvent) objectInputStream.readObject()) != null) {
				getServer().getNetworkHandler().fireServerEvent(new ServerEvent(Event.MESSAGE, chatEvent));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		objectOutputStream.close();
		objectInputStream.close();
		getClientSocket().close();
	}

	@Override
	public void run() {
		welcomeMsg = "SERVER: Welcome Human, I'm a server!";
		try {
//			initConnection();
			 initPrintSteamConnection();
		} catch (SocketException e) {
			getServer().getNetworkHandler().fireServerEvent(new ServerEvent(Event.CLIENT_DROPPED));
		} catch (IOException e) {
			getServer().getNetworkHandler().fireServerEvent(new ServerEvent(Event.CRASH, e));
		}

		System.out.println("END");
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
