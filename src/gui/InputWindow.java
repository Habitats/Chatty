package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import chatty.Config;

public class InputWindow extends JTextField {

	ChatCommand cmd;

	public InputWindow(Dimension dim, final MainFrame mainFrame) {
		setPreferredSize(dim);
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				String msg = ae.getActionCommand();
				// if message is empty, do nothing
				if (msg.length() > 0) {
					// check if it's a CHAT COMMAND
					if (msg.startsWith("!") && ((cmd = ChatCommand.getCmd(msg.split(" ")[0])) != null)) {
						String[] msgArr = msg.split(" ");

						if (cmd == ChatCommand.CHANGE_NICK) {
							Config.NAME_USER = msgArr[0];
							mainFrame.sendMessageToSelf("NICK changed to " + msgArr[0]);
						} else if (cmd == ChatCommand.HELP) {

						} else if (cmd == ChatCommand.QUIT)
							System.exit(0);

					} else
						mainFrame.sendMessageToAll(msg);
					setText("");
				}
			}
		});
	}
}
