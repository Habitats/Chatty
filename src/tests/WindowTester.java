package tests;

import gui.MyKeyListener;
import gui.Options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import chatty.Config;

public class WindowTester extends JPanel {
	public static void main(String[] args) {
		new WindowTester();
	}

	private int width = 500;
	private int height = 350;

	public WindowTester() {

		JFrame frame = new JFrame();

		setBackground(Color.black);
		setPreferredSize(new Dimension(width, height));

		setLayout(new GridBagLayout());

		add(new Options());

		frame.getContentPane().add(this);
		frame.setTitle(Config.CHATTY_VER);
		// frame.setUndecorated(true);
		frame.pack();

		frame.setLocationRelativeTo(frame.getRootPane());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addKeyListener(new MyKeyListener());

		frame.setResizable(false);
		frame.setVisible(true);
	}

}
