package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import chatty.Config;

public class FeedWindow extends JTextPane implements EventListener {

	public FeedWindow(Dimension dim) {
		setPreferredSize(dim);

		setText("Welcome to  " +  Config.CHATTY_VER + " (excuse the name), a lightweight, easy to use, chat client!");
		// auto scroll
		DefaultCaret caret = (DefaultCaret) getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

//		initStyles();
	}

	private void initStyles() {
		StyleContext sc = new StyleContext();
		final DefaultStyledDocument doc = new DefaultStyledDocument(sc);

		Style error = sc.addStyle("error", null);
		error.addAttribute(StyleConstants.Foreground, Color.RED);

		Style normal = sc.addStyle("normal", null);
		normal.addAttribute(StyleConstants.Foreground, Color.BLACK);

		setStyledDocument(doc);
	}

	public void appendOtherMessage(String msg) {
		appendText(msg);
	}

	public void appendOwnMessage(String msg) {
		appendText(Config.NAME_USER + msg);
	}

	public void appendText(String msg) {
		if (msg.length() > 0)
			setText(getText() + "\n" + msg);
	}

	@Override
	public void sendStatusToOwnFeed(String msg) {
		appendText("STATUS: " + msg);
	}

	@Override
	public void sendNormalMessageToOwnFeed(String msg) {
		appendText(msg);
	}

	@Override
	public void sendErrorToOwnFeed(String msg) {
		appendText("ERROR: " + msg);
	}
}
