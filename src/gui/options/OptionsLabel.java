package gui.options;

import gui.options.OptionsMenu.Option;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import chatty.Config;

public class OptionsLabel extends JLabel {
	public OptionsLabel(Dimension dim, Option option) {
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		setHorizontalAlignment(SwingConstants.RIGHT);
		setText(option.getOption());
		setForeground(Config.TEXT_COLOR_DEFAULT_MENU);
		setBackground(Config.BUTTON_COLOR_DEFAULT);
		setOpaque(true);
	}
}
