package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

import network.client.ClientEvent;
import network.client.ClientEvent.ClientEvents;

import chatty.ChatCommand;
import chatty.ChatEvent;
import chatty.ChatEvent.Receipient;
import chatty.CommandEvent;
import chatty.Config;

public class InputWindow extends JTextField {

	private final MainFrame mainFrame;

	public InputWindow(Dimension dim, final MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setPreferredSize(dim);
		setMinimumSize(dim);
		addActionListener(new myInputListener());

		setBorder(BorderFactory.createEmptyBorder());
		setBackground(Themes.BACKGROUND);
		setForeground(Themes.FOREGROUND);
		setFont(Config.genFont(12, false));

	}

	private class myInputListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			ChatEvent chatEvent = new ChatEvent(mainFrame.getController().getUser(), Receipient.GLOBAL, ae.getActionCommand());
			if (chatEvent.getMsgArr().length > 0) {
				if (chatEvent.isCommand() && chatEvent.getCommand() == ChatCommand.PRIV_MSG) {
					chatEvent.setRec(Receipient.PRIVATE, chatEvent.getMsgArr()[1]);
					mainFrame.getController().sendChatEvent(chatEvent);
				} else if (chatEvent.isCommand())
					mainFrame.getController().getNetworkHandler().fireClientEvent(new ClientEvent(ClientEvents.COMMAND, chatEvent));
				else
					// mainFrame.getController().sendMessageToAll(chatEvent.getMsg());
					mainFrame.getController().sendChatEvent(chatEvent);
				setText("");
			}
		}
	}
}
