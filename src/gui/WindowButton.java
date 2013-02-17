package gui;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import chatty.Config;

import msg.ChatEvent.Receipient;

public class WindowButton extends Button {

	public WindowButton(final Receipient rec, Dimension buttonDim, final WindowMenu windowMenu) {
		setText(rec.name());
		setPreferredSize(buttonDim);
		setFont(Config.genFont(13, true));
		final WindowButton button = this;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				windowMenu.show(rec, button);
			}
		});
	}

	public WindowButton(Dimension buttonDim) {
		setPreferredSize(buttonDim);
	}
}
