package network.server;

import java.io.ObjectOutputStream;
import java.net.Socket;

import chatty.User;

public class ClientConnection {

	private Socket clientSocket;
	private ObjectOutputStream objectOutputStream;
	private User user;

	public ClientConnection(ObjectOutputStream objectOutputStream, Socket clientSocket) {
		this.objectOutputStream = objectOutputStream;
		this.clientSocket = clientSocket;
	}

	public synchronized Socket getClientSocket() {
		return clientSocket;

	}

	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}

	public synchronized void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
