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
import chatty.Controller;

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

		MenuButton clientButton = new MenuButton(Type.CLIENT, frameWidth / menuSize,  main);
		MenuButton serverButton = new MenuButton(Type.SERVER, frameWidth / menuSize, main);
		MenuButton options = new MenuButton(Type.OPTIONS, frameWidth / menuSize, main);

		getController().getNetworkHandler().addNetworkListener(clientButton);
		getController().getNetworkHandler().addNetworkListener(serverButton);
		getController().getNetworkHandler().addNetworkListener(feedWindow);
	

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

		addKeyListener(new MyKeyListener());

		frame.setResizable(false);
		frame.setVisible(true);

		// sets focus to TEXT FIELD
		inputWindow.requestFocus();
	}

	public FeedWindow getFeedWindow() {
		return feedWindow;
	}

	public Controller getController() {
		return main.getController();
	}

}
