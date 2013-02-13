package gui;

import gui.GBC.Align;
import gui.MenuButton.Type;
import gui.options.OptionsMenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.peer.MenuPeer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import chatty.Chatty;
import chatty.Config;
import chatty.Controller;

public class MainFrame {
	private FeedWindow feedWindow;
	private InputWindow inputWindow;
	private int frameWidth = 600;
	private int frameHeight = 450;
	private Chatty main;
	private OptionsMenu optionsMenu;
	private Controller controller;
	private MenuButton optionsButton;
	
	public MainFrame(Controller controller) {
		Themes.setTheme(Themes.GRAY);
		JFrame frame = new JFrame();
		this.controller = controller;

		JPanel mainPanel = new JPanel();
		JPanel overlayPanel = new JPanel();

		JLayeredPane layeredPane = new JLayeredPane();
		ButtonHandler buttonHandler = new ButtonHandler();
		buttonHandler.addButtonListener(controller);

		int menuSize = 3;

		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setSize(frameWidth, frameHeight);
		overlayPanel.setSize(frameWidth, frameHeight);

		// THIS FUCKS UP SO MUCH
		// frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
		// mainPanel.setPreferredSize(new Dimension(frameWidth,frameHeight));

		mainPanel.setOpaque(false);
		mainPanel.setBackground(Color.blue);
		overlayPanel.setOpaque(false);
		overlayPanel.setBackground(Color.yellow);
		layeredPane.setBackground(Color.red);

		overlayPanel.setLayout(new GridBagLayout());

		feedWindow = new FeedWindow(new Dimension(frameWidth, 200));
		inputWindow = new InputWindow(new Dimension(frameWidth, 20), this);
		optionsMenu = new OptionsMenu(new Dimension((int) (frameWidth * .66), (int) (frameHeight * .33)),buttonHandler);

		Dimension buttonDim = new Dimension(((frameWidth - Config.BORDER_WIDTH * 4) / menuSize), 30);
		MenuButton clientButton = new MenuButton(Type.CLIENT, buttonDim, buttonHandler);
		MenuButton serverButton = new MenuButton(Type.SERVER, buttonDim, buttonHandler);
		optionsButton = new MenuButton(Type.OPTIONS, buttonDim, buttonHandler);

		getController().getNetworkHandler().addNetworkListener(clientButton);
		getController().getNetworkHandler().addNetworkListener(serverButton);
		getController().getNetworkHandler().addNetworkListener(feedWindow);

		JScrollPane scrollPane = new JScrollPane(feedWindow);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setBackground(Themes.FOREGROUND);
		scrollPane.getVerticalScrollBar().setForeground(Themes.FOREGROUND);
		mainPanel.add(scrollPane, new GBC(0, 2, Align.FULL_WIDTH).setSpan(menuSize, 1).setWeight(0, 1));
		mainPanel.add(inputWindow, new GBC(0, 8, Align.FULL_WIDTH_BOTTOM).setSpan(menuSize, 2).setWeight(1, 0));

		mainPanel.add(clientButton, new GBC(0, 0, Align.LEFT).setSpan(1, 1).setWeight(1 / menuSize, 0));
		mainPanel.add(serverButton, new GBC(1, 0, Align.MID).setSpan(1, 1).setWeight(1 / menuSize, 0));
		mainPanel.add(optionsButton, new GBC(2, 0, Align.RIGHT).setSpan(1, 1).setWeight(1 / menuSize, 0));

		overlayPanel.add(optionsMenu, new GBC(0, 0).setIpad((int) (frameWidth * .22), (int) (frameHeight * .12)));

		mainPanel.addKeyListener(new MyKeyListener());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		layeredPane.setOpaque(false);
		layeredPane.add(mainPanel, new Integer(0));
		layeredPane.add(overlayPanel, new Integer(2));

		// DO NOT SET LAYOUT MANAGER TO LAYERED PANE
		// layeredPane.setLayout(new FlowLayout());
		layeredPane.setPreferredSize(new Dimension(frameWidth, frameHeight));

		frame.getContentPane().setBackground(Color.black);
		frame.getContentPane().add(layeredPane);
		frame.getGlassPane().setBackground(Color.yellow);
		frame.getGlassPane().setVisible(true);

		frame.setTitle(Config.CHATTY_VER);
		frame.pack();
		// System.out.println("mainPanel" + mainPanel.getSize());
		// System.out.println("layeredPane" + layeredPane.getSize());
		// System.out.println("frame" + frame.getSize());
		// System.out.println("feedWindow" + feedWindow.getSize());
		// System.out.println("inputwindow" + inputWindow.getSize());

		frame.setLocationRelativeTo(frame.getRootPane());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		 frame.setResizable(false);
		frame.setVisible(true);

		// sets focus to TEXT FIELD
		// inputWindow.requestFocus();
		mainPanel.requestFocus();
	}

	public FeedWindow getFeedWindow() {
		return feedWindow;
	}

	public Controller getController() {
		return controller;
	}

	public OptionsMenu getOptionsMenu() {
		return optionsMenu;
	}
	public MenuButton getOptionsButton() {
		return optionsButton;
	}

}
