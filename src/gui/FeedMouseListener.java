package gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import chatty.Controller;

public class FeedMouseListener extends MouseAdapter {
	private MainFrame gui;
	private FeedWindow feedWindow;

	public FeedMouseListener(MainFrame gui) {
		this.gui = gui;
		feedWindow = gui.getFeedWindow();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e))
			searchForNick(e, gui.getController().getUser().getName());
	}

	private void searchForNick(MouseEvent e, String nick) {
		String cont = feedWindow.getText();
		int mouseClickIndex = feedWindow.viewToModel(e.getPoint());
		int triggerWordIndex = cont.indexOf(nick, mouseClickIndex - nick.length());
		int triggerWordEndIndex = triggerWordIndex + nick.length();
		System.out.println(triggerWordIndex + " <= " + mouseClickIndex + " <= " + triggerWordEndIndex);
		if (triggerWordIndex <= mouseClickIndex && mouseClickIndex <= (triggerWordIndex + nick.length()))
			System.out.println("CLICKED NICK: " + nick);
	}
}
