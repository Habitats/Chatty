package gui;


import gui.RightClickMenu.RightClickType;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public class FeedMouseListener extends MouseAdapter {
	private MainFrame gui;
	private FeedWindow feedWindow;

	public FeedMouseListener(MainFrame gui) {
		this.gui = gui;
		feedWindow = gui.getFeedWindow();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		gui.getRightClickMenu().setVisible(false);
		if (SwingUtilities.isRightMouseButton(e)) {
			// TODO: this is quite sloppy atm
			RightClickType type;
			if (clickedNick(e, gui.getController().getUser().getUsername())){
				System.out.println("CLICKED NICK: " + gui.getController().getUser().getUsername());
				type = RightClickType.NICK;
			}
			else
				type = RightClickType.DEFAULT;
			gui.getRightClickMenu().setLocation(e.getX() + gui.getFeedWindow().getScrollPane().getX(), e.getY() + gui.getFeedWindow().getScrollPane().getY());
			gui.getRightClickMenu().open(type);
		}
	}

	private boolean clickedNick(MouseEvent e, String nick) {
		String cont = feedWindow.getText();
		int mouseClickIndex = feedWindow.viewToModel(e.getPoint());
		int triggerWordIndex = cont.indexOf(nick, mouseClickIndex - nick.length());
		int triggerWordEndIndex = triggerWordIndex + nick.length();
		System.out.println(triggerWordIndex + " <= " + mouseClickIndex + " <= " + triggerWordEndIndex);
		return (triggerWordIndex <= mouseClickIndex && mouseClickIndex <= (triggerWordIndex + nick.length()));
	}
}
