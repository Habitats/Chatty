package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import network.NetworkListener;
import network.client.ClientEvent;
import network.server.ServerEvent;

import chatty.Config;

public class FeedWindow extends JTextPane implements NetworkListener {

	public FeedWindow(Dimension dim) {

		setPreferredSize(dim);
		setMinimumSize(dim);

		setText("Welcome to  " + Config.CHATTY_VER + " (excuse the name), a lightweight, easy to use, chat client!");
		// auto scroll
		DefaultCaret caret = (DefaultCaret) getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		setBackground(Themes.BACKGROUND);
		setForeground(Themes.FOREGROUND);
		setFont(Config.genFont(12, false));
		setEditable(false);

		setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Themes.BACKGROUND));

		// initStyles();
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

	private void appendText(String msg) {
		if (msg.length() > 0)
			setText(getText() + "\n" + msg);
	}

	private void sendStatusToOwnFeed(String msg) {
		appendText("STATUS: " + msg);
	}

	private void sendNormalMessageToOwnFeed(String msg) {
		appendText(msg);
	}

	private void sendErrorToOwnFeed(String msg) {
		appendText("ERROR: " + msg);
	}

	// network stuff

	@Override
	public void serverStart(ServerEvent event) {
		sendStatusToOwnFeed(event.getMsg());
	}

	@Override
	public void serverShutDown(ServerEvent event) {
		sendStatusToOwnFeed(event.getMsg());
	}

	@Override
	public void clientDropped(ServerEvent event) {
		sendErrorToOwnFeed(event.getMsg());
	}

	@Override
	public void serverCrashed(ServerEvent event) {
		sendErrorToOwnFeed(event.getMsg());
	}

	@Override
	public void serverStatus(ServerEvent event) {
		sendStatusToOwnFeed(event.getMsg());
	}
	
	@Override
	public void serverNormalMessage(ServerEvent event) {
		sendNormalMessageToOwnFeed(event.getMsg());
	}

	@Override
	public void clientStart(ClientEvent event) {
		sendStatusToOwnFeed(event.getMsg());
	}

	@Override
	public void clientShutDown(ClientEvent event) {
		sendStatusToOwnFeed(event.getMsg());
	}

	@Override
	public void clientCrashed(ClientEvent event) {
		sendErrorToOwnFeed(event.getMsg());
	}

	@Override
	public void clientConnect(ClientEvent event) {
		sendStatusToOwnFeed(event.getMsg());
	}

	@Override
	public void clientStatus(ClientEvent event) {
		sendStatusToOwnFeed(event.getMsg());
	}

	@Override
	public void clientMessage(ClientEvent event) {
		sendNormalMessageToOwnFeed(event.getMsg());
	}
}
