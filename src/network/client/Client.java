package network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import msg.ChatEvent;
import msg.ChatEvent.Receipient;
import network.NetworkEvent;
import network.NetworkHandler;
import network.ProgramState;
import network.NetworkEvent.NetworkEvents;

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
		getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.STATUS, "Connecting to " + hostname + " on " + port + "..."));
		try {
			socket = new Socket(hostname, port);
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.CONNECT));
			return socket;
		} catch (UnknownHostException e) {
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.SHUTDOWN_CLIENT, e, "Unknown host: " + hostname + "!"));
		} catch (IOException e) {
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.SHUTDOWN_CLIENT, e, "Connection failed."));
		}
		return null;
	}

	private void initConnection() throws IOException {
		objectOutputStream = new ObjectOutputStream(echoSocket.getOutputStream());
		objectInputStream = new ObjectInputStream(echoSocket.getInputStream());

		ChatEvent chatEvent;

		setRunning(true);
		try {
			getObjectOutputStream().reset();
			getObjectOutputStream().writeObject(new ChatEvent(getNetworkHandler().getController().getUser(), null, Receipient.SERVER, getNetworkHandler().getController().getUser().getUsername() + " says hi!"));
			while (isRunning() && ((chatEvent = (ChatEvent) objectInputStream.readObject()) != null)) {
				getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.CHAT_EVENT, chatEvent));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.START_CLIENT, "Starting client..."));
		setClient(true);
		setServer(false);

		echoSocket = setUpConnection(port, hostname);

		// return if connection went to hell or w/e
		if (echoSocket == null)
			return;
		getNetworkHandler().getController().getUser().setActivePort(echoSocket.getLocalPort());

		try {
			initConnection();
			// initPrintStreamConnection();
		} catch (SocketException e) {
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.DISCONNECT));
			return;
		} catch (IOException e) {
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.DISCONNECT, "Error reading socket!"));
		} finally {
			getNetworkHandler().fireNetworkEvent(new NetworkEvent(NetworkEvents.SHUTDOWN_CLIENT, "Client shutting down!"));
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
