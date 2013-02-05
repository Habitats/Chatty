package gui;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

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
