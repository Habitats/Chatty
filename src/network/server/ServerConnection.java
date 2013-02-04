package network.server;

import gui.FeedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class ServerConnection implements Runnable {

	private Socket clientSocket;
	private FeedListener feedListener;
	private PrintWriter out;
	private ArrayList<PrintWriter> outputStreams;

	public ServerConnection(Socket clientSocket, FeedListener feedListener, ArrayList<PrintWriter> outputStreams) {
		this.clientSocket = clientSocket;
		this.feedListener = feedListener;
		this.outputStreams = outputStreams;
	}

	@Override
	public void run() {
		String fromUser;
		String fromServer;

		out = null;
		BufferedReader in = null;

		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			outputStreams.add(out);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// write to the socket
			out.println("Welcome Human, I'm a server!");
			fromServer = "this should only appear once";

			while ((fromUser = in.readLine()) != null) {
				if (fromServer.length() > 0) {
					out.println(fromServer);
					fromServer = "";
				}
				feedListener.sendMessageToFeed(fromUser);
				for (int i = 0; i < outputStreams.size(); i++) {
					PrintWriter currentOut = outputStreams.get(i);
//					if(client)
					currentOut.println(fromUser);
				}
			}

			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("END");
	}

	public PrintWriter getOutputStream() {
		return out;
	}
}
