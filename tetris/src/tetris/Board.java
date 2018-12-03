package tetris;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	// sets dimensions of tetris board
	private static final int BOARD_WIDTH = 10;
	private static final int BOARD_HEIGHT = 22;
	private Timer timer = new Timer(400, this);
	private boolean isFallingFinished = false;
	private boolean isStarted = false;
	private boolean isPaused = false;
	private int numLinesRemoved = 0;
	private int curX = 0;
	private int curY = 0;
	
	private Shape curPiece;
	private Shape holdPiece;
	private Shape tempPiece;
	private boolean isHold = false;
	private boolean useHold = false;
	private JLabel statusBar;
	
	
	private int firstDrop = 0;
	private Tetrominoes[] board;

	

	public Board(Tetris parent) throws IOException {
		
		
		
		
		setFocusable(true);
		curPiece = new Shape();
		statusBar = parent.getStatusBar();
		statusBar.setText("<html>Score: " + String.valueOf(numLinesRemoved) + "<br>Holding: <br>" + "</html>");
		statusBar.setIcon(noShape);
		
		JLabel label = new JLabel();
		statusBar.add(label);
		statusBar.setHorizontalTextPosition(JLabel.LEFT);
		board = new Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
		clearBoard();
		addKeyListener(new MyTetrisAdapter());
		int x = 400;	//used for speed at which blocks drop
		timer = new Timer(x, this);
		
		
	}
	
	
	
	/*
	public Board(Tetris parent) throws IOException {
		
		//Container pane = new Container();
		
		setFocusable(true);
		curPiece = new Shape();
		statusBar = parent.getStatusBar();
		statusBar.setText("<html>Score: " + String.valueOf(numLinesRemoved) + "<br>Holding: <br>" + "</html>");
		statusBar.setIcon(noShape);
		
		JLabel label = new JLabel();
		statusBar.add(label);
		System.out.println("label not added");
		statusBar.setHorizontalTextPosition(JLabel.LEFT);
		board = new Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
		clearBoard();
		addKeyListener(new MyTetrisAdapter());
		int x = 400;
		timer = new Timer(x, this);

	}
	
	*/
	
	/*
	 * JFrame frame = new JFrame(); frame.setLayout(new GridLayout()); JLabel label
	 * = new JLabel("<html>First line<br>Second line</html>"); frame.add(label);
	 * frame.pack(); frame.setVisible(true); label = parent.getStatusBar();
	 * label.setText("<html>Score: " +
	 * String.valueOf(numLinesRemoved)+"<br>Holding: </html>");
	 */

	public int squareWidth() {
		return (int) getSize().getWidth() / BOARD_WIDTH;
	}

	public int squareHeight() {
		return (int) getSize().getHeight() / BOARD_HEIGHT;
	}

	public Tetrominoes shapeAt(int x, int y) {
		return board[y * BOARD_WIDTH + x];
	}

	private void clearBoard() {
		for (int x = 0; x < BOARD_HEIGHT * BOARD_WIDTH; x++) {
			board[x] = Tetrominoes.NoShape;
		}
	}

	private void pieceDropped() {
		for (int i = 0; i < 4; i++) {
			int x = curX + curPiece.x(i);
			int y = curY - curPiece.y(i);
			board[y * BOARD_WIDTH + x] = curPiece.getShape();
		}

		removeFullLines();

		// difficulty modifier
		if (numLinesRemoved >= 25 && numLinesRemoved < 50) {
			System.out.println("Speed: 1");
			timer.stop();
			timer = new Timer(300, this);
			timer.start();
		} else if (numLinesRemoved > 50 && numLinesRemoved <= 100) {
			System.out.println("Speed: 2");
			timer.stop();
			timer = new Timer(200, this);
			timer.start();
		} else if (numLinesRemoved > 100) {
			System.out.println("Speed: 3");
			timer.stop();
			timer = new Timer(100, this);
			timer.start();
		}
		;

		if (!isFallingFinished) {
			newPiece();
		}
	}

	public void newPiece() {
		curPiece.setRandomShape();
		curX = BOARD_WIDTH / 2 + 1;
		curY = BOARD_HEIGHT - 1 + curPiece.minY();
		useHold = false;

		if (!tryMove(curPiece, curX, curY - 1)) {
			curPiece.setShape(Tetrominoes.NoShape);
			timer.stop();
			isStarted = false;
			statusBar.setText("Game Over");
		}

	}

	// used to Hold a tetromino to use later, found in newer versions of Tetris
	public void holdBlock() {
		if (isHold == false && firstDrop == 0) {
			firstDrop = 1; 
			//firstDrop to 1 makes it so player can't hold/swap twice in a row on the first drop
			holdPiece = curPiece;
			isHold = true;

			//determines the picture to display for the held shape
			if (holdPiece.getShape() == Tetrominoes.ZShape) {
				statusBar.setIcon(zShape);
				System.out.println("z Shape");
				
			} else if (holdPiece.getShape() == Tetrominoes.SShape) {
				statusBar.setIcon(sShape);
				System.out.println("s Shape");
				
			}else if (holdPiece.getShape() == Tetrominoes.LineShape) {
				statusBar.setIcon(lineShape);
				System.out.println("Line Shape");
				
			}else if (holdPiece.getShape() == Tetrominoes.TShape) {
				statusBar.setIcon(tShape);
				System.out.println("t Shape");
				
			}else if (holdPiece.getShape() == Tetrominoes.SquareShape) {
				statusBar.setIcon(squareShape);
				System.out.println("square Shape");
				
			}else if (holdPiece.getShape() == Tetrominoes.LShape) {
				statusBar.setIcon(lShape);
				System.out.println("l Shape");
				
			}else if (holdPiece.getShape() == Tetrominoes.MirroredLShape) {
				statusBar.setIcon(mlShape);
				System.out.println("mirror Shape START");
			}
			
			newPiece();

		} else if (isHold == true && useHold == false) {
			tempPiece = curPiece;
			curPiece = holdPiece;
			holdPiece = tempPiece;
			useHold = true;
			
			if (holdPiece.getShape() == Tetrominoes.ZShape) {
				statusBar.setIcon(zShape);
				
			} else if (holdPiece.getShape() == Tetrominoes.SShape) {
				statusBar.setIcon(zShape);

				
			}else if (holdPiece.getShape() == Tetrominoes.LineShape) {
				statusBar.setIcon(lineShape);

				
			}else if (holdPiece.getShape() == Tetrominoes.TShape) {
				statusBar.setIcon(tShape);

				
			}else if (holdPiece.getShape() == Tetrominoes.SquareShape) {
				statusBar.setIcon(squareShape);

				
			}else if (holdPiece.getShape() == Tetrominoes.LShape) {
				statusBar.setIcon(lShape);
				System.out.println("L Shape RUN");
				
			}else  {
				statusBar.setIcon(mlShape);
				System.out.println("mirror Shape RUN");
			}
			
			
			curX = BOARD_WIDTH / 2 + 1;
			curY = BOARD_HEIGHT - 1 + curPiece.minY();
			
			
		}
	}

	private void oneLineDown() {
		if (!tryMove(curPiece, curX, curY - 1))
			pieceDropped();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (isFallingFinished) {
			isFallingFinished = false;
			newPiece();
		} else {
			oneLineDown();
		}
	}

	private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color color = shape.color;
		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);
		// g.setColor(color.darker());
		g.setColor(color.brighter()); // make the outline "neon"
		// g.setColor(color.WHITE);
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

		for (int i = 0; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; ++j) {
				Tetrominoes shape = shapeAt(j, BOARD_HEIGHT - i - 1);

				if (shape != Tetrominoes.NoShape) {
					drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
				}
			}
		}
		if (curPiece.getShape() != Tetrominoes.NoShape) {
			for (int i = 0; i < 4; ++i) {
				int x = curX + curPiece.x(i);
				int y = curY - curPiece.y(i);
				drawSquare(g, x * squareWidth(), boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
						curPiece.getShape());

			}
		}
	}

	public void start() {
		if (isPaused)
			return;

		isStarted = true;
		isFallingFinished = false;
		numLinesRemoved = 0;
		clearBoard();
		newPiece();
		timer.start();
	}

	public void pause() {
		if (!isStarted)
			return;

		isPaused = !isPaused;

		if (isPaused) {
			timer.stop();
			statusBar.setText("Paused");
		} else {
			timer.start();
			statusBar.setText("Score: " + String.valueOf(numLinesRemoved));

		}

		repaint();
	}

	private boolean tryMove(Shape newPiece, int newX, int newY) {
		Shape shadowPiece = curPiece;
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);

			if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape)
				return false;
		}

		curPiece = newPiece;
		curX = newX;
		curY = newY;
		repaint();

		return true;
	}

	private void removeFullLines() {
		int numFullLines = 0;

		for (int i = BOARD_HEIGHT - 1; i >= 0; --i) {
			boolean lineIsFull = true;

			for (int j = 0; j < BOARD_WIDTH; ++j) {
				if (shapeAt(j, i) == Tetrominoes.NoShape) {
					lineIsFull = false;
					break;
				}
			}

			if (lineIsFull) {
				++numFullLines;

				for (int k = i; k < BOARD_HEIGHT - 1; ++k) {
					for (int j = 0; j < BOARD_WIDTH; ++j) {
						board[k * BOARD_WIDTH + j] = shapeAt(j, k + 1);
					}
				}
			}

			if (numFullLines > 0) {
				numLinesRemoved += numFullLines;
				statusBar.setText("Score: " + String.valueOf(numLinesRemoved));
				isFallingFinished = true;
				curPiece.setShape(Tetrominoes.NoShape);
				repaint();
			}
		}
	}

	private void dropDown() {
		int newY = curY;

		while (newY > 0) {
			if (!tryMove(curPiece, curX, newY - 1))
				break;

			--newY;
		}

		pieceDropped();
	}

	class MyTetrisAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent ke) {
			if (!isStarted || curPiece.getShape() == Tetrominoes.NoShape)
				return;

			int keyCode = ke.getKeyCode();

			if (keyCode == 'p' || keyCode == 'P')
				pause();

			if (isPaused)
				return;

			switch (keyCode) {
			case KeyEvent.VK_LEFT:
				tryMove(curPiece, curX - 1, curY);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(curPiece, curX + 1, curY);
				break;
			case KeyEvent.VK_DOWN:
				tryMove(curPiece.rotateRight(), curX, curY);
				break;
			case KeyEvent.VK_UP:
				tryMove(curPiece.rotateLeft(), curX, curY);
				break;
			case KeyEvent.VK_SPACE:
				dropDown();
				break;
			case 'h':
			case 'H':
				holdBlock();
				break;
			case 'd':
			case 'D':
				oneLineDown();
				break;
			case 'q':
			case 'Q':
				System.exit(0);
				break;
			}
		}
	}

	
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	/*
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	*/
	

	// resize the image icon to look better
	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}

	///// IMAGE IMPORTS///////
	
	// BORDER
	// import shape gif to act as holder
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	InputStream inputNo = classLoader.getResourceAsStream("noShape.gif");
	Image noShapeBig = ImageIO.read(inputNo);
	Image noShapeImg = getScaledImage(noShapeBig, 45, 45);
	ImageIcon noShape = new ImageIcon(noShapeImg);

	// S SHAPE
	InputStream inputSShape = classLoader.getResourceAsStream("sShape - Border.gif");
	Image sShapeBig = ImageIO.read(inputSShape);
	Image sShapeImg = getScaledImage(sShapeBig, 45, 45);
	ImageIcon sShape = new ImageIcon(sShapeImg);

	// Z SHAPE
	InputStream inputZShape = classLoader.getResourceAsStream("zShapeBorder.gif");
	Image zShapeBig = ImageIO.read(inputZShape);
	Image zShapeImg = getScaledImage(zShapeBig, 45, 45);
	ImageIcon zShape = new ImageIcon(zShapeImg);

	// L SHAPE
	InputStream inputLShape = classLoader.getResourceAsStream("lShapeBorder.gif");
	Image lShapeBig = ImageIO.read(inputLShape);
	Image lShapeImg = getScaledImage(lShapeBig, 45, 45);
	ImageIcon lShape = new ImageIcon(lShapeImg);

	// T SHAPE
	InputStream inputTShape = classLoader.getResourceAsStream("tShapeBorder.gif");
	Image tShapeBig = ImageIO.read(inputTShape);
	Image tShapeImg = getScaledImage(tShapeBig, 45, 45);
	ImageIcon tShape = new ImageIcon(tShapeImg);

	// mirror L SHAPE
	InputStream inputMLShape = classLoader.getResourceAsStream("mirrorLShapeBorder.gif");
	Image mlShapeBig = ImageIO.read(inputMLShape);
	Image mlShapeImg = getScaledImage(mlShapeBig, 45, 45);
	ImageIcon mlShape = new ImageIcon(mlShapeImg);

	// square SHAPE
	InputStream inputSquareShape = classLoader.getResourceAsStream("squareBorder.gif");
	Image squareShapeBig = ImageIO.read(inputSquareShape);
	Image squareShapeImg = getScaledImage(squareShapeBig, 45, 45);
	ImageIcon squareShape = new ImageIcon(squareShapeImg);

	// Line SHAPE
	InputStream inputLineShape = classLoader.getResourceAsStream("lineBorder.gif");
	Image lineShapeBig = ImageIO.read(inputLineShape);
	Image lineShapeImg = getScaledImage(lineShapeBig, 45, 45);
	ImageIcon lineShape = new ImageIcon(lineShapeImg);
	
	// LOGO
	InputStream inputLogoShape = classLoader.getResourceAsStream("TetrisLogo.png");
	Image logoShapeBig = ImageIO.read(inputLogoShape);
	Image logoShapeImg = getScaledImage(logoShapeBig, 50, 16);
	ImageIcon logoShape = new ImageIcon(logoShapeImg);

}
