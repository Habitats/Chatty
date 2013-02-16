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
import network.NetworkHandler;
import network.ProgramState;
import network.client.ClientEvent.ClientEvents;

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
		getNetworkHandler().fireClientEvent(new ClientEvent(ClientEvents.STATUS, "Connecting to " + hostname + " on " + port + "..."));
		try {
			socket = new Socket(hostname, port);
			getNetworkHandler().fireClientEvent(new ClientEvent(ClientEvents.CONNECT));
			return socket;
		} catch (UnknownHostException e) {
			getNetworkHandler().fireClientEvent(new ClientEvent(ClientEvents.SHUTDOWN, e, "Unknown host: " + hostname + "!"));
		} catch (IOException e) {
			getNetworkHandler().fireClientEvent(new ClientEvent(ClientEvents.SHUTDOWN, e, "Connection failed."));
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
				getNetworkHandler().fireClientEvent(new ClientEvent(ClientEvents.CHAT_EVENT, chatEvent));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		getNetworkHandler().fireClientEvent(new ClientEvent(ClientEvents.START));
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
			getNetworkHandler().fireClientEvent(new ClientEvent(ClientEvents.DISCONNECT, e));
			return;
		} catch (IOException e) {
			getNetworkHandler().fireClientEvent(new ClientEvent(ClientEvents.CRASH, e));
		} finally {
			getNetworkHandler().fireClientEvent(new ClientEvent(ClientEvents.SHUTDOWN));
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
