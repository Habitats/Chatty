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
					if (msg.startsWith("/") && ((cmd = ChatCommand.getCmd(msg.split(" ")[0])) != null)) {
						String[] msgArr = msg.split(" ");

						if (cmd == ChatCommand.CHANGE_NICK) {
							Config.NAME_USER = msgArr[0];
							mainFrame.getController().sendMessageToSelf("NICK changed to " + msgArr[0]);
						} else if (cmd == ChatCommand.CONNECT) {
							int port = Config.DEFAULT_PORT;
							if (msgArr.length == 3)
								try {
									port = Integer.parseInt(msgArr[2]);
									mainFrame.getController().getNetworkHandler().startClient(msgArr[1], port);
								} catch (NumberFormatException e) {
									mainFrame.getController().sendMessageToSelf("Invalid port or format.");
									return;
								}
						} else if (cmd == ChatCommand.LISTEN_PORT) {
							try {
								int port = Integer.parseInt(msgArr[1]);
								mainFrame.getController().getNetworkHandler().restartServer(port);
							} catch (NumberFormatException e) {
								mainFrame.getController().sendMessageToSelf("Invalid port or format.");
							}
						} else if (cmd == ChatCommand.QUIT)
							System.exit(0);

					} else
						mainFrame.getController().sendMessageToAll(msg);
					setText("");
				}
			}
		});
	}
}
