package tetris;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.util.*;

public class Menu extends JFrame {
	FlowLayout experimentLayout = new FlowLayout();
	JButton startBtn = new JButton("Start Game");
	
	public Menu(String name) {
		super(name);
	}

	
	public void addComponentsToPane(final Container pane) throws IOException {
		final JPanel compsToExperiment = new JPanel();
		compsToExperiment.setBorder(new EmptyBorder(10,10,10,10));
		compsToExperiment.setLayout(experimentLayout);
		//experimentLayout.setAlignment(FlowLayout.TRAILING);
		
		JPanel controls = new JPanel();
		controls.setLayout(new FlowLayout());
		controls.setBorder(new EmptyBorder(10,10,10,10));
		
		
		JLabel logoHold = new JLabel();
		ImageIcon i = logoImage();
		logoHold.setIcon(i);
		
		compsToExperiment.add(logoHold);
		controls.add(startBtn);
		
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent f) {
				try {
					Tetris myTetris = new Tetris();
					myTetris.setLocationRelativeTo(null);
					myTetris.setVisible(true);
					System.out.println("Loading Tetris");
				} catch (IOException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		pane.add(compsToExperiment);
		pane.add(controls, BorderLayout.SOUTH);
		
	}
	
	private static void createAndShowGUI() throws IOException {
		Menu frame = new Menu("T E T R I S");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addComponentsToPane(frame.getContentPane());
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) throws IOException {
		createAndShowGUI();
	}
	
	
	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}

	///// IMAGE IMPORTS///////
	
	public ImageIcon logoImage() throws IOException{
	// BORDER
	// import shape gif to act as holder
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	InputStream inputNo = classLoader.getResourceAsStream("TetrisLogo.png");
	Image noShapeBig = ImageIO.read(inputNo);
	Image noShapeImg = getScaledImage(noShapeBig, 400, 129);
	ImageIcon noShape = new ImageIcon(noShapeImg);
	return noShape;
	}
}