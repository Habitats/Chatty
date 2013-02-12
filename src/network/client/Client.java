package network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import chatty.ChatEvent;
import network.NetworkHandler;
import network.ProgramState;
import network.client.ClientEvent.Event;

public class Client extends ProgramState implements Runnable {

	private PrintWriter printWriterOutputStream;
	private NetworkHandler networkHandler;
	private String hostname;
	private Socket echoSocket;
	private BufferedReader bufferedReaderInputStream;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

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

	private void initPrintStreamConnection() throws IOException {
		printWriterOutputStream = new PrintWriter(echoSocket.getOutputStream(), true);
		bufferedReaderInputStream = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

		String fromServer;

		setRunning(true);
		// this loop constantly checks for changes a on the socket
		while (isRunning() && (fromServer = bufferedReaderInputStream.readLine()) != null) {
			// send message from SERVER to own FEED
			getNetworkHandler().fireClientEvent(new ClientEvent(Event.MESSAGE, fromServer));
		}
	}

	private void initObjectStreamConnection() throws IOException {
		objectOutputStream = new ObjectOutputStream(echoSocket.getOutputStream());
		objectInputStream = new ObjectInputStream(echoSocket.getInputStream());

		setRunning(true);

		Object objectFromServer;

		try {
			while (isRunning() && ((objectFromServer = objectInputStream.readObject()) != null)) {
				String[] arr = (String[]) objectFromServer;
				getNetworkHandler().fireClientEvent(new ClientEvent(Event.MESSAGE, arr[0]));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		getNetworkHandler().fireClientEvent(new ClientEvent(Event.START));
		setClient(true);
		setServer(false);

		echoSocket = setUpConnection(port, hostname);

		// return if connection went to hell or w/e
		if (echoSocket == null)
			return;

		try {
//			initObjectStreamConnection();
			 initPrintStreamConnection();
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
		return printWriterOutputStream;
	}

	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}

	@Override
	public void kill() {
		// cleanup
		try {
			if (echoSocket != null)
				echoSocket.close();
			if (printWriterOutputStream != null)
				printWriterOutputStream.close();
			if (bufferedReaderInputStream != null)
				bufferedReaderInputStream.close();
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
