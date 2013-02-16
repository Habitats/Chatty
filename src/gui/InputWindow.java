package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

import msg.MessageHandler;
import chatty.Config;

public class InputWindow extends JTextField {
	
	private static final long serialVersionUID = 1L;

	public InputWindow(Dimension dim, MessageHandler messageHandler) {
		setPreferredSize(dim);
		setMinimumSize(dim);
		addActionListener(new myInputListener(messageHandler));

		setBorder(BorderFactory.createEmptyBorder());
		setBackground(Themes.BACKGROUND);
		setForeground(Themes.FOREGROUND);
		setFont(Config.genFont(12, false));

	}

	private class myInputListener implements ActionListener {
		MessageHandler messageHandler;

		public myInputListener(MessageHandler messageHandler) {
			this.messageHandler = messageHandler;
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			messageHandler.newChatEvent(ae);
			setText("");
		}
	}
}
