package network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import chatty.User;

import msg.ChatEvent;
import msg.ChatEvent.Receipient;
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
				if (!getServer().getUsers().containsKey(chatEvent.getFrom().getUsername())) {
					getServer().getUsers().put(chatEvent.getFrom().getUsername(), chatEvent.getFrom());
				}
				List<ClientConnection> clientConnections = getServer().getClientConnections();
				synchronized (clientConnections) {
					for (ClientConnection clientConnection : clientConnections) {
//						System.out.print("server port: " + clientConnection.getClientSocket().getPort());
//						System.out.println("Socket info: " + clientConnection.getClientSocket());
//						System.out.println(" - client port: " + chatEvent.getFrom().getActivePort());
						if (clientConnection.getClientSocket() == clientSocket) {
							clientConnection.setUser(chatEvent.getFrom());
							break;
						}
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

	private Server getServer() {
		return server;
	}

	private void setServer(Server server) {
		this.server = server;
	}

	private Socket getClientSocket() {
		return clientSocket;
	}
}
