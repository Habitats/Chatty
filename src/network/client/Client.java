package network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import network.NetworkHandler;
import network.ProgramState;
import network.client.ClientEvent.Event;

public class Client extends ProgramState implements Runnable {

	private PrintWriter out;
	private NetworkHandler networkHandler;
	private String hostname;
	private Socket echoSocket;
	private BufferedReader in;

	public Client(int port, String hostname, NetworkHandler networkHandler) throws IOException {
		this.hostname = hostname;

		setNetworkHandler(networkHandler);

		super.port = port;
	}

	private Socket setUpConnection(int port, String hostname) {
		Socket socket = null;
		getNetworkHandler().fireClientEvent(new ClientEvent(Event.STATUS, "Connecting to " + hostname + " on " + port + "..."));
		try {
			socket = new Socket(hostname, port);
			getNetworkHandler().fireClientEvent(new ClientEvent(Event.CONNECT));
			return socket;
		} catch (UnknownHostException e) {
			getNetworkHandler().fireClientEvent(new ClientEvent(Event.SHUTDOWN, e, "Unknown host: " + hostname + "!"));
		} catch (IOException e) {
			getNetworkHandler().fireClientEvent(new ClientEvent(Event.SHUTDOWN, e, "Connection failed."));
		}
		return null;
	}

	@Override
	public void run() {
		getNetworkHandler().fireClientEvent(new ClientEvent(Event.START));
		setClient(true);
		setServer(false);

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
			while (isRunning() && (fromServer = in.readLine()) != null) {
				// send message from SERVER to own FEED
				getNetworkHandler().fireClientEvent(new ClientEvent(Event.MESSAGE, fromServer));
			}
		} catch (SocketException e) {
			getNetworkHandler().fireClientEvent(new ClientEvent(Event.DISCONNECT, e));
			return;
		} catch (IOException e) {
			getNetworkHandler().fireClientEvent(new ClientEvent(Event.CRASH, e));
		} finally {
			getNetworkHandler().fireClientEvent(new ClientEvent(Event.SHUTDOWN));
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
			if (out != null)
				out.close();
			if (in != null)
				in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setRunning(false);
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;
	}

	public void setNetworkHandler(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}
}
