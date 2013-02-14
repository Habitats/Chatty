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
import chatty.ChatEvent.Receipient;

import network.server.ServerEvent.ServerEvents;

public class ServerConnection implements Runnable {

	private Socket clientSocket;
	private Server server;
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

		objectOutputStream.writeObject(new ChatEvent(serverUser, null, Receipient.PRIVATE, welcomeMsg));
		ChatEvent chatEvent;

		try {
			while ((chatEvent = (ChatEvent) objectInputStream.readObject()) != null) {
				System.out.println("Reading [S]: " + chatEvent);
				if (!getServer().getUsers().containsKey(chatEvent.getFrom().getName())) {
					getServer().getUsers().put(chatEvent.getFrom().getName(), chatEvent.getFrom());
				}
				for (ClientConnection clientSocket : getServer().getClientConnections()) {
//					System.out.print("server port: " + clientSocket.getClientSocket().getPort());
//					System.out.println(" - client port: " + chatEvent.getFrom().getActivePort());
					if (chatEvent.getFrom().getActivePort() == clientSocket.getClientSocket().getPort()) {
//						System.out.println("Match! Client: " + chatEvent.getFrom().getName() + " - connected on: " + clientSocket.getClientSocket().getPort());
						clientSocket.setUser(chatEvent.getFrom());
					}
				}
				getServer().getNetworkHandler().fireServerEvent(new ServerEvent(ServerEvents.CHAT_EVENT, chatEvent));
				if (chatEvent.getReceipient() == Receipient.GLOBAL) {
					getServer().broadcastChatEventToClients(chatEvent);
				} else if (chatEvent.getReceipient() == Receipient.PRIVATE) {
					getServer().sendPrivateChatEvent(chatEvent);
				}
				chatEvent = null;
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
