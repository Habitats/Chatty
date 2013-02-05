package chatty;

import gui.EventListener;
import gui.MainFrame;

import network.NetworkHandler;

public class Chatty {
	private NetworkHandler networkHandler;

	public static void main(String[] args) {
		new Chatty().run();
	}

	private void run() {
		int port = Config.DEFAULT_PORT;
		String hostname = Config.DEFAULT_HOST;

		MainFrame gui = new MainFrame(this);
		EventListener eventListener = gui.getFeedWindow();
		networkHandler = new NetworkHandler(port, hostname, eventListener);
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;
	}
}
