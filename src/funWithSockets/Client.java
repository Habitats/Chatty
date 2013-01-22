package funWithSockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public Client(int port) throws IOException {

		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;

//		String hostname = "78.91.48.1";
		String hostname = "localhost";
		try {
			System.out.println("Connecting to " + hostname + " on port " + port);
			echoSocket = new Socket(hostname, port);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			System.out.println("Connected!");

		} catch (UnknownHostException e) {
			System.err.println("don't know about host: " + hostname);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("io error");
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		String fromServer;
		String fromUser;

		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			if (fromServer.equals("end"))
				break;
			fromUser = stdIn.readLine();
			if (fromUser != null) {
				System.out.println("Client: " + fromUser);
				out.println(fromUser);
			}
		}

		out.close();
		in.close();
		stdIn.close();
		echoSocket.close();

	}
}
