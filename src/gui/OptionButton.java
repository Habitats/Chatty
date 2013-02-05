package gui;

import java.awt.Choice;
import java.awt.Dimension;

public class OptionButton extends Button {
	private Choice type;
	private int optionButtonWidth = 40;
	private int optionButtonHeight = 20;

	public OptionButton(String text, Dimension dim) {
		super(text, dim);
		setPreferredSize(dim);
	}

	public OptionButton(Choice type) {
		this.type = type;
		setPreferredSize(super.dim);
	}
}
