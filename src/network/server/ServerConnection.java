package network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import chatty.ChatEvent;
import chatty.CommandEvent;
import chatty.User;

import network.server.ServerEvent.ServerEvents;

public class ServerConnection implements Runnable {

	private Socket clientSocket;
	private Server server;
	private String fromUser;
	private String welcomeMsg;
	private User serverUser;

	public ServerConnection(Socket clientSocket, Server server) {
		this.clientSocket = clientSocket;
		this.server = server;
		this.serverUser = new User("SERVER");
	}

	private void initConnection() throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(getClientSocket().getOutputStream());
		getServer().getClientConnections().add(new ClientConnection(objectOutputStream, getClientSocket()));
		ObjectInputStream objectInputStream = new ObjectInputStream(getClientSocket().getInputStream());

		objectOutputStream.writeObject(new ChatEvent(serverUser, serverUser, welcomeMsg));
		ChatEvent chatEvent;

		try {
			while ((chatEvent = (ChatEvent) objectInputStream.readObject()) != null) {
				getServer().getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.CHAT_EVENT, chatEvent));
				getServer().broadcastChatEventToClients(chatEvent);
			}
		} catch (ClassNotFoundException e) {
			System.err.println("CLASS NOT FOUND EXEPTION (serverConnection)");
		}

		objectOutputStream.close();
		objectInputStream.close();
		getClientSocket().close();
	}

	@Override
	public void run() {
		welcomeMsg = "SERVER: Welcome Human, I'm a server!";
		try {
			initConnection();
			// initPrintSteamConnection();
		} catch (SocketException e) {
			getServer().getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.CLIENT_DROPPED));
		} catch (IOException e) {
			getServer().getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.CRASH, e));
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
