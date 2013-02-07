package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

import network.client.ClientEvent;
import network.client.ClientEvent.Event;

import chatty.ChatEvent;

public class InputWindow extends JTextField {

	private final MainFrame mainFrame;

	public InputWindow(Dimension dim, final MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setPreferredSize(dim);
		setMinimumSize(dim);
		addActionListener(new myInputListener());
		
		setBorder(BorderFactory.createEmptyBorder());

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
