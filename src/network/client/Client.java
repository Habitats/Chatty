package network.client;

import gui.EventListener;
import gui.MainFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import chatty.Config;

import network.NetworkHandler;
import network.ProgramState;

public class Client extends ProgramState implements Runnable {

	private EventListener eventListener;
	private PrintWriter out;
	private NetworkHandler networkHandler;
	private String hostname;
	private Socket echoSocket;
	private BufferedReader in;

	public Client(int port, String hostname, EventListener feedListener, NetworkHandler networkHandler) throws IOException {
		this.hostname = hostname;

		setEventListener(feedListener);
		setNetworkHandler(networkHandler);

		super.port = port;
	}

	private Socket setUpConnection(int port, String hostname) {
		Socket socket = null;
		getEventListener().sendStatusToOwnFeed("Connecting to " + hostname + " on " + port + "...");
		try {
			socket = new Socket(hostname, port);
			getEventListener().sendStatusToOwnFeed("Connected!");
			return socket;
		} catch (UnknownHostException e) {
			getEventListener().sendStatusToOwnFeed("Unknown host: " + hostname + "!");
		} catch (IOException e) {
			getEventListener().sendStatusToOwnFeed("Connection failed.");
		}
		return null;
	}

	@Override
	public void run() {
		setClient(true);
		setServer(false);
		getEventListener().sendStatusToOwnFeed("Starting client...");

		// String hostname = "78.91.48.1";

		echoSocket = setUpConnection(port, hostname);

		// return if connection went to hell or w/e
		if (echoSocket == null)
			return;

		in = null;

		try {
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

			String fromServer;

			setRunning(true);
			// this loop constantly checks for changes a on the socket
			while (isRunning() && (fromServer = in.readLine()) != null ) {
				// send message from SERVER to own FEED
				getEventListener().sendNormalMessageToOwnFeed(fromServer);
			}
		} catch (SocketException e) {
			getEventListener().sendErrorToOwnFeed("Connection lost!");
			return;
		} catch (IOException e) {
			getEventListener().sendStatusToOwnFeed("CLIENT CRASHED");
			e.printStackTrace();
		} finally {
			kill();
		}
	}

	public PrintWriter getOutputStream() {
		return out;
	}

	@Override
	public void kill() {
		// cleanup
		try {
			if (echoSocket != null)
				echoSocket.close();
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getNetworkHandler().lostConnection();
		setRunning(false);
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;
	}

	public void setNetworkHandler(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	public EventListener getEventListener() {
		return eventListener;
	}

	public void setEventListener(EventListener eventListener) {
		this.eventListener = eventListener;
	}
}
