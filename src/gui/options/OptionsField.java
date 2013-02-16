package gui.options;

import gui.Themes;
import gui.options.OptionsMenu.Option;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import chatty.Config;

public class OptionsField extends JTextField {
	
	private static final long serialVersionUID = 1L;
	private final Option option;

	public OptionsField(Dimension dim, Option option) {
		this.option = option;
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
		setBackground(Themes.BACKGROUND);
		setForeground(Themes.FOREGROUND);
		setFont(Config.genFont(13, false));
		setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 5));

	}

	public String submit() {
		return getText();
	}
	public Option getOption() {
		return option;
	}
}
