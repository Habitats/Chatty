package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import network.client.ClientEvent;
import network.client.ClientEvent.Event;

import chatty.ChatCommand;
import chatty.ChatEvent;

public class InputWindow extends JTextField {

	ChatCommand cmd;
	private final MainFrame mainFrame;

	public InputWindow(Dimension dim, final MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setPreferredSize(dim);
		addActionListener(new myInputListener());

	}

	private class myInputListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			ChatEvent chatEvent = new ChatEvent(ae.getActionCommand());
			if (chatEvent.getMsgArr().length > 0) {
				if (chatEvent.isCommand()) {
					chatEvent.executeCommand();
					mainFrame.getController().getNetworkHandler().fireClientEvent(new ClientEvent(Event.STATUS, chatEvent.getReturnMsg()));
				} else
					mainFrame.getController().sendMessageToAll(chatEvent.getRaw());
				setText("");
			}
		}
	}
}
