package chatty;

import java.io.IOException;
import java.net.InetAddress;
import gui.ButtonEvent;
import gui.ButtonListener;
import gui.MainFrame;
import gui.options.OptionsField;
import msg.ChatEvent;
import msg.MessageHandler;
import network.NetworkHandler;

public class Controller implements ButtonListener {

	private final NetworkHandler networkHandler;
	private MainFrame gui;
	private int port = 7701;
	// private String hostname = "shoopdawhoop.myftp.org";
	public String hostname = "localhost";
	private User user;
	private MessageHandler messageHandler;

	public Controller() {
		networkHandler = new NetworkHandler(this);
		String nickname = Integer.toString((int) (10000 * Math.random()));
		user = new User(nickname);
		messageHandler = new MessageHandler(this);
	}

	private void addMessaegeListeners() {
		messageHandler.addMessageListener(getGui().getFeedWindow());
	}

	private void addNetworkListeners() {
		networkHandler.addNetworkListener(getGui().getClientButton());
		networkHandler.addNetworkListener(getGui().getServerButton());
		networkHandler.addNetworkListener(messageHandler);
	}

	public void startTestInstance(int servers, int clients) {

	}

	public synchronized void sendChatEvent(ChatEvent chatEvent) {

		/*
		 * IF CLIENT
		 */
		if (networkHandler.getProgramState() != null && networkHandler.getProgramState().isRunning() && networkHandler.getProgramState().isClient())
			try {
				System.out.println("Sending: " + chatEvent);
				// See:
				// http://stackoverflow.com/questions/14883073/sending-the-same-objects-with-different-fields-over-an-object-serialized-stream
				networkHandler.getClient().getObjectOutputStream().reset();
				networkHandler.getClient().getObjectOutputStream().writeObject(chatEvent);
				messageHandler.fireChatEventToListeners(chatEvent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		/*
		 * IF SERVER
		 */
		else if (networkHandler.getProgramState() != null && networkHandler.getProgramState().isRunning() && networkHandler.getProgramState().isServer()) {
			networkHandler.getServer().broadcastChatEventToAll(chatEvent);
		}
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;

	}

	public void setGui(MainFrame gui) {
		this.gui = gui;
		addMessaegeListeners();
		addNetworkListeners();
	}

	public MainFrame getGui() {
		return gui;
	}

	private void updateConfig(ButtonEvent event) {
		for (OptionsField field : gui.getOptionsMenu().getOptionFields())
			switch (field.getOption()) {
			case HOSTNAME:
				setHostname(field.submit());
				break;
			case PORT:
				setPort(field.submit());
				break;
			case NICKNAME:
				setNick(field.submit());
				break;
			}
	}

	public String setNick(String nickname) {
		if (nickname.length() == 0 || nickname.equals(user.getUsername()))
			return null;
		if (nickname.length() > 20)
			return String.format("Invalid nickname: \"%s\"", nickname);
		nickname = nickname.replaceAll("\\s", "_");
		getUser().setDisplayName(nickname);
		return String.format("Changed nickname to: \"%s\"", nickname);
	}

	public String setPort(String port) {
		if (port.length() == 0 || port.equals(Integer.toString(getPort())))
			return null;
		for (Character c : port.toCharArray())
			if (!Character.isDigit(c)) {
				return "Invalid port: " + port;
			}
		this.port = Integer.parseInt(port);
		return "Changed port to: " + port;
	}

	public String setHostname(String hostname) {
		if (hostname.length() == 0 || getHostname().contains(hostname))
			return null;
		InetAddress ip;
		String hostnameAndIP = "";
		boolean valid = false;
		try {
			ip = InetAddress.getByName(hostname);
			hostnameAndIP = hostname + String.format(" (%s)", ip.getHostAddress());
			valid = true;
		} catch (Exception e) {
			if (hostname.matches("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b")) {
				valid = true;
			}
		}
		if (valid) {
			this.hostname = hostname;
			return "Changed host to: " + hostnameAndIP;
		}
		return "Invalid hostname: " + hostname;
	}

	private void toggleOptions() {
		if (gui.getOptionsMenu().isVisible()) {
			gui.getOptionsMenu().setVisible(false);
			gui.getOptionsButton().setActive(false);
		} else {
			for (OptionsField str : gui.getOptionsMenu().getOptionFields()) {
				switch (str.getOption()) {
				case HOSTNAME:
					str.setText(hostname);
					break;
				case NICKNAME:
					str.setText(user.getUsername());
					break;
				case PORT:
					str.setText(Integer.toString(getPort()));
					break;
				}
			}
			gui.getOptionsMenu().setVisible(true);
			gui.getOptionsButton().setActive(true);
		}
	}

	@Override
	public void buttonClicked(ButtonEvent event) {
		switch (event.getButton().getType()) {
		case SERVER:
			if (event.getButton().isActive())
				networkHandler.shutDown();
			else
				networkHandler.startServer(port);
			break;
		case CLIENT:
			if (event.getButton().isActive())
				networkHandler.shutDown();
			else
				networkHandler.startClient(hostname, port);
			break;
		case SUBMIT:
			updateConfig(event);
			toggleOptions();
			break;
		case OPTIONS:
			toggleOptions();
			break;
		}
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public User getUser() {
		return user;
	}

	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

}
