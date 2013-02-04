package funWithSockets;

import gui.FeedListener;
import gui.MainFrame;

import java.io.IOException;
import java.util.Scanner;


import network.NetworkHandler;
import network.NetworkListener;
import network.client.Client;
import network.server.Server;

public class FunWithSockets {
	private NetworkHandler networkHandler;

	public static void main(String[] args) {
		new FunWithSockets().run();
	}

	private void run() {
		int port = 7701;

		MainFrame gui = new MainFrame(this);
		FeedListener feedListener = gui.getFeedWindow();
		networkHandler = new NetworkHandler(port, gui);
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;
	}
}
