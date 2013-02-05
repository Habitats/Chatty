package gui;

import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

public class FeedWindow extends JTextPane implements FeedListener {

	public FeedWindow(Dimension dim) {
		setPreferredSize(dim);
		setFocusable(false);
		setText("Chatty v0.0...");

		// auto scroll
		DefaultCaret caret = (DefaultCaret) getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	@Override
	public void sendMessageToFeed(String msg) {
		if (msg.length() > 0)
			setText(getText() + "\n" + msg);
	}
}
