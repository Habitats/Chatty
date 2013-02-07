package gui.options;

import gui.options.OptionsMenu.Option;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class OptionsField extends JTextField {
	private final Option option;

	public OptionsField(Dimension dim, Option option) {
		this.option = option;
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);

		setBorder(BorderFactory.createEmptyBorder());
	}

	public String submit() {
		return getText();
	}
	public Option getOption() {
		return option;
	}
}
