package funWithSockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public Server(int port) throws IOException {
		// tries to open up a server on the specified port, if it fails it'll
		// exit
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Connected on port " + port);

		} catch (Exception e) {
			System.out.println("Connection failed on port " + port);
			System.exit(1);
		}

		// tries to initiate a connection to the client by accepting incoming
		// connections
		// the accept method listens for incoming connections
		Socket clientSocket = null;
		try {
			System.out.println("Listening for incoming connections...");
			clientSocket = serverSocket.accept();
			System.out.println("Accepted connection!");
		} catch (Exception e) {
			System.out.println("Client socket failed");
			System.exit(1);
		}

		// once a connection is established the server communicates with the
		// client through the following code
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inputLine, outputLine;

		// initiate conversation with the client
		ConversationProtocol cp = new ConversationProtocol();
		outputLine = cp.processinput(null);
		out.println(outputLine);

		while ((inputLine = in.readLine()) != null) {
			outputLine = cp.processinput(inputLine);
			if (outputLine.equals("end"))
				break;
		}

		out.close();
		in.close();
		clientSocket.close();
		serverSocket.close();
	}

}
