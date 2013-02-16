package gui.options;

import gui.Themes;
import gui.options.OptionsMenu.Option;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import chatty.Config;

@SuppressWarnings("serial")
public class OptionsLabel extends JLabel {
	public OptionsLabel(Dimension dim, Option option) {
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		setHorizontalAlignment(SwingConstants.RIGHT);
		setText(option.getOption().toUpperCase());
		setFont(Config.genFont(13, true));
		setForeground(Themes.FOREGROUND);
		setBackground(Themes.BUTTON_COLOR_HOVER);
		setOpaque(true);
	}
}
