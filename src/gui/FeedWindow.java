package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

import msg.ChatEvent;
import msg.MessageListener;
import chatty.Config;

public class FeedWindow extends JTextPane implements MessageListener {

	public FeedWindow(Dimension dim) {

		setPreferredSize(dim);
		setMinimumSize(dim);

		setText("Welcome to " + Config.CHATTY_VER + " (excuse the name) -- a lightweight, easy to use, chat client!");
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

	@Override
	public void onChatEvent(ChatEvent chatEvent) {
		try {
			getDocument().insertString(getText().length(), "\n" + chatEvent.getMsg(), null);
		} catch (BadLocationException e) {
		}
		setCaretPosition(getDocument().getLength());
	}

	@Override
	public void clear() {
		setText("");
	}

	@Override
	public ChatEvent newChatEvent(ActionEvent ae) {
		return null;
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

}
