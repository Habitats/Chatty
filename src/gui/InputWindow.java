package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class InputWindow extends JTextField {

	public InputWindow(Dimension dim, final MainFrame mainFrame) {
		setPreferredSize(dim);
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.sendMessageToFeed(arg0.getActionCommand());
				setText("");
			}
		});
	}
}
