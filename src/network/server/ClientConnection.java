package network.server;

import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
	
	private PrintWriter outputStream;
	private Socket clientSocket;

	public ClientConnection(PrintWriter outputStream,Socket clientSocket){
		this.outputStream = outputStream;
		this.clientSocket = clientSocket;
	}

	public PrintWriter getOutputStream() {
		return outputStream;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}
}
