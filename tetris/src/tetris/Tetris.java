package tetris;

//original code from https://www.ssaurel.com/blog/learn-to-create-a-tetris-game-in-java-with-swing/#comments
//s.saurel

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tetris extends JFrame{
	
	private JLabel statusBar;
	
	public Tetris() throws IOException {
		
		statusBar = new JLabel("0");
		add(statusBar, BorderLayout.SOUTH);
		Board board = new Board(this);
		add(board);
		board.start();
		setSize(268, 550);
		setTitle("Tetris");
		board.setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	
	public JLabel getStatusBar() {
		return statusBar;
	}
	
	/*
	public static void main(String[] args) throws IOException {
		Tetris myTetris = new Tetris();
		myTetris.setLocationRelativeTo(null);
		myTetris.setVisible(true);
	}
	*/

}
