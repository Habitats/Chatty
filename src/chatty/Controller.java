package chatty;

import gui.EventListener;
import gui.MainFrame;
import network.NetworkHandler;

public class Controller {

	private final NetworkHandler networkHandler;
	private final MainFrame gui;

	public Controller(NetworkHandler networkHandler, MainFrame gui) {
		this.networkHandler = networkHandler;
		this.gui = gui;

		EventListener eventListener = gui.getFeedWindow();
		networkHandler.setEventListener(eventListener);
	}

	public void sendMessageToSelf(String msg) {
		gui.getFeedWindow().sendStatusToOwnFeed(msg);
	}

	public void sendMessageToAll(String msg) {
		// TODO: send SELF messages to SERVER, TEMPORARY WORKAROUND

		// if CLIENT, send to SERVER only
		if (networkHandler.getProgramState().isClient())
			networkHandler.getClient().getOutputStream().println(Config.NAME_USER + ": " + msg);

		// if SERVER, send to all CLIENTS and SELF
		else if (networkHandler.getProgramState().isServer()) {
			networkHandler.getServer().broadcastServerMessage(msg);
		}
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;

	}
}
