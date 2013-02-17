package gui;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

import msg.ChatEvent;
import msg.ChatEvent.Receipient;
import msg.MessageListener;
import chatty.Config;

public class FeedWindow extends JTextPane implements MessageListener {

	private JScrollPane scrollPane;
	private Receipient rec;

	public FeedWindow(Dimension dim, Receipient rec) {
		this.rec = rec;

		setPreferredSize(dim);
		setMinimumSize(dim);

//		setText("Welcome to " + Config.CHATTY_VER + " (excuse the name) -- a lightweight, easy to use, chat client!");
		setText("Configuring " + Config.CHATTY_VER + "...");
		// auto scroll
		DefaultCaret caret = (DefaultCaret) getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		setBackground(Themes.BACKGROUND);
		setForeground(Themes.FOREGROUND);
		setFont(Config.genFont(12, false));
		setEditable(false);

		setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Themes.BACKGROUND));

		scrollPane = new JScrollPane(this);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setBackground(Themes.FOREGROUND);
	}

	@Override
	public void onChatEvent(ChatEvent chatEvent) {
		try {
			getDocument().insertString(getText().length(), "\n" + chatEvent.getFormattedMessage(), null);
		} catch (BadLocationException e) {
		}
		setCaretPosition(getDocument().getLength());
	}

	@Override
	public void clear() {
		setText("");
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	@Override
	public Receipient getReceipient() {
		return rec;
	}
}
