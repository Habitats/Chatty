package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import network.NetworkEvent;
import network.NetworkListener;
import network.client.ClientEvent;
import network.client.ClientEvent.ClientEvents;
import network.server.ServerEvent;
import network.server.ServerEvent.ServerEvents;

import chatty.ChatEvent;
import chatty.Config;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

	// private void initStyles() {
	// StyleContext sc = new StyleContext();
	// final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
	//
	// Style error = sc.addStyle("error", null);
	// error.addAttribute(StyleConstants.Foreground, Color.RED);
	//
	// Style normal = sc.addStyle("normal", null);
	// normal.addAttribute(StyleConstants.Foreground, Color.BLACK);
	//
	// setStyledDocument(doc);
	// }


	private void appendText(String msg) {
		try {
			// if (msg.length() > 0)
			// setText(getText() + "\n" + (new
			// SimpleDateFormat("hh:mm:ss").format(new Date())) + Config.SEP +
			// msg);
			getDocument().insertString(getText().length(), "\n" + new SimpleDateFormat("hh:mm:ss").format(new Date()) + Config.SEP + msg, null);
			setCaretPosition(getDocument().getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendStatusToOwnFeed(NetworkEvent event) {
		if (event instanceof ServerEvent) {
			ServerEvents serverEvents = (ServerEvents) event.getEvent();
			appendText(serverEvents.getPrefix() + Config.SEP + serverEvents.getMsg());
		} else if (event instanceof ClientEvent) {
			ClientEvents clientEvents = (ClientEvents) event.getEvent();
			appendText(clientEvents.getPrefix() + Config.SEP + clientEvents.getMsg());
		}
	}

	private void sendNormalMessageToOwnFeed(ChatEvent chatEvent) {
		appendText(chatEvent.getFrom().getName() + Config.SEP + chatEvent.getMsg());
	}

	// network stuff

	@Override
	public void serverStatus(ServerEvent event) {
		sendStatusToOwnFeed(event);
	}

	@Override
	public void serverNormalMessage(ServerEvent event) {
		sendNormalMessageToOwnFeed(event.getChatEvent());
	}

	@Override
	public void clientStatus(ClientEvent event) {
		sendStatusToOwnFeed(event);
	}

	@Override
	public void clientMessage(ClientEvent event) {
		sendNormalMessageToOwnFeed(event.getChatEvent());
	}
}
