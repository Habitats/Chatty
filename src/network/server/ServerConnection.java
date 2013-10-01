package network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import msg.ChatEvent;
import msg.ChatEvent.Receipient;
import network.NetworkEvent;
import network.NetworkEvent.NetworkEvents;

public class ServerConnection implements Runnable {

	private Socket clientSocket;
	private Server server;
	private String welcomeMsg;

	public ServerConnection(Socket clientSocket, Server server) {
		this.clientSocket = clientSocket;
		this.server = server;
	}

	private void initConnection() throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(getClientSocket().getOutputStream());
		getServer().getClientConnections().add(new ClientConnection(objectOutputStream, getClientSocket()));
		ObjectInputStream objectInputStream = new ObjectInputStream(getClientSocket().getInputStream());

		objectOutputStream.writeObject(new ChatEvent(getServer().getServerUser(), null, Receipient.STATUS, welcomeMsg));
		ChatEvent chatEvent;

		try {
			while ((chatEvent = (ChatEvent) objectInputStream.readObject()) != null) {
				if (!getServer().getUsers().containsKey(chatEvent.getFrom().getUsername())) {
					getServer().getUsers().put(chatEvent.getFrom().getUsername(), chatEvent.getFrom());
				}
				List<ClientConnection> clientConnections = getServer().getClientConnections();
				synchronized (clientConnections) {
					for (ClientConnection clientConnection : clientConnections) {
						if (clientConnection.getClientSocket() == clientSocket) {
							clientConnection.setUser(chatEvent.getFrom());
							break;
						}
					}
				}
				getServer().getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.CHAT_EVENT, chatEvent));
				if (chatEvent.getReceipient() == Receipient.CHANNEL) {
					getServer().broadcastChatEventToClients(chatEvent);
				} else if (chatEvent.getReceipient() == Receipient.QUERY) {
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
		welcomeMsg = "Welcome Human, I'm a server!";
		try {
			initConnection();
		} catch (SocketException e) {
			getServer().getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.CLIENT_DROPPED, "Socket closed, client dropped!"));
		} catch (IOException e) {
			getServer().getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.CLIENT_DROPPED, "Error reading socket, client dropped!"));
		}
	}

	private Server getServer() {
		return server;
	}

	private Socket getClientSocket() {
		return clientSocket;
	}
}
