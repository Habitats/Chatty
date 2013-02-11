package network.server;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {

	private PrintWriter outputStream;
	private Socket clientSocket;
	private ObjectOutputStream objectOutputStream;

	public ClientConnection(PrintWriter outputStream, Socket clientSocket) {
		this.outputStream = outputStream;
		this.clientSocket = clientSocket;
	}

	public ClientConnection(ObjectOutputStream objectOutputStream, Socket clientSocket) {
		this.objectOutputStream = objectOutputStream;
		this.clientSocket = clientSocket;
	}

	public PrintWriter getOutputStream() {
		return outputStream;
	}

	public Socket getClientSocket() {
		return clientSocket;

	}

	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}
}
