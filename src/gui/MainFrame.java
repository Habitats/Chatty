package gui;

import gui.GBC.Align;
import gui.MenuButton.Type;
import gui.options.OptionsMenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import msg.ChatEvent.Receipient;
import chatty.Config;
import chatty.Controller;

public class MainFrame {
	private int frameWidth = 1200;
	private int frameHeight = 450;
	private Controller controller;
	private ButtonHandler buttonHandler;

	// TODO: encapsulate this better, shouldn't be fields!
	private RightClickMenu rightClickMenu;
	private MenuButton optionsButton;
	private OptionsMenu optionsMenu;
	private MenuButton clientButton;
	private MenuButton serverButton;

	private FeedWindow feedWindow;
	private FeedWindow feedStatus;
	private FeedWindow feedQuery;

	public MainFrame(Controller controller) {
		Themes.setTheme(Themes.GRAY);
		this.controller = controller;

		rightClickMenu = new RightClickMenu(new Dimension(120, 160));

		JLayeredPane layeredPane = buildLayeredPane();
		JPanel mainPanel = buildMainPanel();
		JPanel overlayPanel = buildOverlayPanel();

		layeredPane.add(mainPanel, new Integer(0));
		layeredPane.add(overlayPanel, new Integer(2));
		layeredPane.add(rightClickMenu, new Integer(3));

		buildFrame(layeredPane);

	}

	private JPanel buildOverlayPanel() {
		JPanel overlayPanel = new JPanel();

		overlayPanel.setLayout(new GridBagLayout());
		overlayPanel.setSize(frameWidth, frameHeight);
		overlayPanel.setOpaque(false);
		overlayPanel.setBackground(Color.yellow);

		optionsMenu = new OptionsMenu(new Dimension((int) (frameWidth * .66), (int) (frameHeight * .33)), buttonHandler);

		overlayPanel.add(optionsMenu, new GBC(0, 0).setIpad((int) (frameWidth * .22), (int) (frameHeight * .12)));

		return overlayPanel;
	}

	private JLayeredPane buildLayeredPane() {
		JLayeredPane layeredPane = new JLayeredPane();

		layeredPane.setBackground(Color.red);
		layeredPane.setOpaque(false);

		// DO NOT SET LAYOUT MANAGER TO LAYERED PANE
		// layeredPane.setLayout(new FlowLayout());
		layeredPane.setPreferredSize(new Dimension(frameWidth, frameHeight));

		return layeredPane;
	}

	private JPanel buildMainPanel() {
		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setSize(frameWidth, frameHeight);

		int menuSize = 3;
		Dimension buttonDim = new Dimension(((frameWidth - Config.BORDER_WIDTH * 4) / menuSize), 30);

		buttonHandler = new ButtonHandler();
		buttonHandler.addButtonListener(controller);

		clientButton = new MenuButton(Type.CLIENT, buttonDim, buttonHandler);
		serverButton = new MenuButton(Type.SERVER, buttonDim, buttonHandler);
		optionsButton = new MenuButton(Type.OPTIONS, buttonDim, buttonHandler);

		feedWindow = new FeedWindow(new Dimension(frameWidth / 3, 200), Receipient.CHANNEL);
		feedWindow.addMouseListener(new FeedMouseListener(this));

		feedStatus = new FeedWindow(new Dimension(frameWidth / 3, 200), Receipient.STATUS);
		feedStatus.addMouseListener(new FeedMouseListener(this));
		
		feedQuery = new FeedWindow(new Dimension(frameWidth / 3, 200), Receipient.QUERY);
		feedQuery.addMouseListener(new FeedMouseListener(this));

		InputWindow inputWindow = new InputWindow(new Dimension(frameWidth, 20), controller.getMessageHandler());
		// TODO: this isn't very pretty
		inputWindow.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				rightClickMenu.setVisible(false);
			}
		});

		// sets focus to TEXT FIELD
		inputWindow.requestFocus();

		// requestFocus();
		// THIS FUCKS UP SO MUCH
		// frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
		// setPreferredSize(new Dimension(frameWidth,frameHeight));

		mainPanel.setOpaque(false);
		mainPanel.setBackground(Color.blue);

		mainPanel.add(feedWindow.getScrollPane(), new GBC(0, 2, Align.LEFT).setSpan(menuSize / 3, 1).setWeight(0, 1));
		mainPanel.add(feedQuery.getScrollPane(), new GBC(1, 2, Align.MID).setSpan(menuSize / 3, 1).setWeight(0, 1));
		mainPanel.add(feedStatus.getScrollPane(), new GBC(2, 2, Align.RIGHT).setSpan(menuSize / 3, 1).setWeight(0, 1));

		mainPanel.add(inputWindow, new GBC(0, 8, Align.FULL_WIDTH_BOTTOM).setSpan(menuSize, 2).setWeight(1, 0));

		mainPanel.add(clientButton, new GBC(0, 0, Align.LEFT).setSpan(1, 1).setWeight(1 / menuSize, 0));
		mainPanel.add(serverButton, new GBC(1, 0, Align.MID).setSpan(1, 1).setWeight(1 / menuSize, 0));
		mainPanel.add(optionsButton, new GBC(2, 0, Align.RIGHT).setSpan(1, 1).setWeight(1 / menuSize, 0));

		mainPanel.addKeyListener(new MyKeyListener());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		return mainPanel;
	}

	private void buildFrame(JLayeredPane layeredPane) {
		JFrame frame = new JFrame();

		frame.getContentPane().setBackground(Color.black);
		frame.getContentPane().add(layeredPane);

		frame.setTitle(Config.CHATTY_VER);
		frame.pack();

		frame.setLocationRelativeTo(frame.getRootPane());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);
		frame.setVisible(true);
	}

	public Controller getController() {
		return controller;
	}

	public MenuButton getOptionsButton() {
		return optionsButton;
	}

	public OptionsMenu getOptionsMenu() {
		return optionsMenu;
	}

	public RightClickMenu getRightClickMenu() {
		return rightClickMenu;
	}

	public MenuButton getClientButton() {
		return clientButton;
	}

	public MenuButton getServerButton() {
		return serverButton;
	}

	public FeedWindow getFeedWindow() {
		return feedWindow;
	}

	public FeedWindow getFeedStatus() {
		return feedStatus;
	}
	public FeedWindow getFeedQuery() {
		return feedQuery;
	}
}
