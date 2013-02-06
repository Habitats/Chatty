package chatty;

import gui.MainFrame;
import network.NetworkHandler;
import network.client.ClientEvent;
import network.client.ClientEvent.Event;

public class Controller {

	private final NetworkHandler networkHandler;
	private MainFrame gui;

	public Controller() {
		networkHandler = new NetworkHandler(Config.DEFAULT_PORT, Config.DEFAULT_HOST);
	}

	public void sendMessageToSelf(String msg) {
		getNetworkHandler().fireClientEvent(new ClientEvent(Event.STATUS, msg));
	}

	public void sendMessageToAll(String msg) {
		// TODO: send SELF messages to SERVER, TEMPORARY WORKAROUND

		// if CLIENT, send to SERVER only
		if (networkHandler.getProgramState() != null && networkHandler.getProgramState().isRunning() && networkHandler.getProgramState().isClient())
			networkHandler.getClient().getOutputStream().println(Config.NAME_USER + ": " + msg);

		// if SERVER, send to all CLIENTS and SELF
		else if (networkHandler.getProgramState() != null && networkHandler.getProgramState().isRunning() && networkHandler.getProgramState().isServer()) {
			networkHandler.getServer().broadcastServerMessage(msg);
		}
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;

	}

	public void addGui(MainFrame gui) {
		this.gui = gui;
	}
}
