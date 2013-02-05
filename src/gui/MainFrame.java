package gui;

import gui.GBC.Align;
import gui.MenuButton.Type;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import chatty.Chatty;
import chatty.Config;

public class MainFrame extends JPanel {
	private FeedWindow feedWindow;
	private InputWindow inputWindow;
	private int frameWidth = 400;
	private int frameHeight = 350;
	private Chatty main;

	public MainFrame(Chatty main) {
		JFrame frame = new JFrame();

		this.main = main;
		setBackground(Color.black);

		// no effect
		// frame.setSize(new Dimension(frameWidth, frameHeight));

		int menuSize = 3;

		setLayout(new GridBagLayout());

		feedWindow = new FeedWindow(new Dimension(frameWidth, 200));
		inputWindow = new InputWindow(new Dimension(frameWidth, 20), this);

		MenuButton clientButton = new MenuButton(Type.CLIENT, frameWidth / menuSize, feedWindow, main);
		MenuButton serverButton = new MenuButton(Type.SERVER, frameWidth / menuSize, feedWindow, main);
		MenuButton options = new MenuButton(Type.OPTIONS, frameWidth / menuSize, feedWindow, main);

		add(new JScrollPane(feedWindow), new GBC(0, 2, Align.LEFT).setSpan(menuSize, 5));
		add(inputWindow, new GBC(0, 8, Align.MID).setSpan(menuSize, 2));

		add(clientButton, new GBC(0, 0, Align.LEFT).setSpan(1, 1));
		add(serverButton, new GBC(1, 0, Align.RIGHT).setSpan(1, 1));
		add(options, new GBC(2, 0, Align.RIGHT).setSpan(1, 1));

		frame.getContentPane().add(this);
		frame.setTitle(Config.CHATTY_VER);
		// frame.setUndecorated(true);
		frame.pack();

		frame.setLocationRelativeTo(frame.getRootPane());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addKeyListener(new MyKeyListener());

		frame.setResizable(false);
		frame.setVisible(true);
	}

	public FeedWindow getFeedWindow() {
		return feedWindow;
	}

	public void sendMessageToSelf(String msg) {
		feedWindow.sendStatusToOwnFeed(msg);
	}

	public void sendMessageToAll(String msg) {
		// TODO: send SELF messages to SERVER, TEMPORARY WORKAROUND

		// if CLIENT, send to SERVER only
		if (main.getNetworkHandler().getProgramState().isClient())
			main.getNetworkHandler().getClient().getOutputStream().println(Config.NAME_USER + ": " + msg);

		// if SERVER, send to all CLIENTS and SELF
		else if (main.getNetworkHandler().getProgramState().isServer()) {
			main.getNetworkHandler().getServer().broadcastServerMessage(msg);
		}
	}
}
