package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

public class Tetris extends JPanel {

	private static final long serialVersionUID = 1L;
	private Tetromino nextone;
	private Tetromino tetromino;
	private static final int ROWS = 20;
	private static final int COLS = 10;
	private int score = 0;
	private static int scoringFactor = 3;
	private static double speedFactor = 0.5;
	private int lines = 0;
	private static int level = 20;
	private Cell[][] wall = new Cell[ROWS][COLS];
	private boolean STATE = true;
	private boolean pause = false;
	private static int CELL_SIZE = 26;
	public static BufferedImage Z;
	public static BufferedImage S;
	public static BufferedImage J;
	public static BufferedImage L;
	public static BufferedImage O;
	public static BufferedImage I;
	public static BufferedImage T;
	public static BufferedImage tetris;
	public static BufferedImage gameover;
	static {
		try {
			Z = ImageIO.read(Tetris.class.getResource("Z.png"));
			S = ImageIO.read(Tetris.class.getResource("S.png"));
			J = ImageIO.read(Tetris.class.getResource("J.png"));
			T = ImageIO.read(Tetris.class.getResource("T.png"));
			O = ImageIO.read(Tetris.class.getResource("O.png"));
			I = ImageIO.read(Tetris.class.getResource("I.png"));
			L = ImageIO.read(Tetris.class.getResource("L.png"));
			tetris = ImageIO.read(Tetris.class.getResource("tetris1.png"));
			gameover = ImageIO.read(Tetris.class.getResource("gameover.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void action() {
		tetromino = Tetromino.ranShape();
		nextone = Tetromino.ranShape();

		MouseListener ml = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int k = e.getButton();
				mouseClickedAction(k);
				repaint();
			}

		};

		MouseMotionListener mml = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (x > 0 && x < 260 && y > 0 && y < 520) {
					pause = true;
					repaint();
				} else {
					pause = false;
					repaint();
				}

			}
		};

		MouseMotionListener mml1 = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				int[] insd = tetromino.inside();
				if (x >= insd[0] * CELL_SIZE && x <= insd[1] * CELL_SIZE
						&& y >= insd[2] * CELL_SIZE && y <= insd[3] * CELL_SIZE) {
					tetromino = nextone;
					nextone = Tetromino.ranShape();
					score = score - level * scoringFactor;
					repaint();
				}
				repaint();
			}
		};

		MouseWheelListener mwl = new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int k = e.getWheelRotation();
				if (k < 0) {
					spinClockwiseAction();
					repaint();
				} else {
					spinCounterwiseAction();
					repaint();
				}
			}
		};

		this.addMouseListener(ml);
		this.addMouseWheelListener(mwl);
		this.addMouseMotionListener(mml);
		this.addMouseMotionListener(mml1);
		this.setFocusable(true);
		this.requestFocus();

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			int moveIndex = 0;
			int speed = (int) ((55 - level) * (1.2 - speedFactor));

			// Because

			@Override
			public void run() {
				if (STATE) {
					if (moveIndex % speed == 0) {
						moveDownAction();
						moveIndex = 0;
					}
				}
				moveIndex++;
				repaint();
			}
		};
		timer.schedule(task, 10, 20);
	}

	public void mouseClickedAction(int k) {
		switch (k) {
		case MouseEvent.BUTTON1:
			moveLeftAction();
			break;
		case MouseEvent.BUTTON3:
			moveRightAction();
			break;
		}
	}

	public void moveInitAction() {
		STATE = false;
		wall = new Cell[ROWS][COLS];
		tetromino = Tetromino.ranShape();
		nextone = Tetromino.ranShape();
		score = 0;
		lines = 0;
		level = 20;
	}

	public void spinClockwiseAction() {
		Cell[] nCells = tetromino.spin();
		if (nCells == null)
			return;
		for (int i = 0; i < nCells.length; i++) {
			int nRow = nCells[i].getRow();
			int nCol = nCells[i].getCol();
			if (nRow < 0 || nRow >= ROWS || nCol < 0 || nCol >= COLS
					|| wall[nRow][nCol] != null)
				return;
		}
		tetromino.cells = nCells;
	}

	public void spinCounterwiseAction() {
		Cell[] nCells = tetromino.spin1();
		if (nCells == null)
			return;
		for (int i = 0; i < nCells.length; i++) {
			int nRow = nCells[i].getRow();
			int nCol = nCells[i].getCol();
			if (nRow < 0 || nRow >= ROWS || nCol < 0 || nCol >= COLS
					|| wall[nRow][nCol] != null)
				return;
		}
		tetromino.cells = nCells;
	}

	public void moveLeftAction() {
		if (canLeftMove() && !isBottom()) {
			tetromino.moveLeft();
		}
	}

	public void moveRightAction() {
		if (canRightMove() && !isBottom()) {
			tetromino.moveRight();
		}
	}

	public void moveDownAction() {
		if (tetromino == null)
			return;
		if (!isBottom()) {
			tetromino.moveDown();
		}

	}

	public void removeLine() {
		boolean flag = true;
		int rowStart = 20;
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (wall[row][col] == null) {
					flag = false;
					break;
				}
			}
			if (flag) {
				for (int col = 0; col < COLS; col++) {
					wall[row][col] = null;
				}
				rowStart = row;
				score += level * scoringFactor;
				lines += 1;
				if (level == lines) {
					level++;
				}
				for (int row1 = rowStart; row1 > 0; row1--) {
					for (int col1 = 0; col1 < COLS; col1++) {
						wall[row1][col1] = wall[row1 - 1][col1];
					}
				}
			} else {
				flag = true;
			}
		}
	}

	public boolean isBottom() {
		if (tetromino == null)
			return false;
		Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell c = cells[i];
			int col = c.getCol();
			int row = c.getRow();
			if ((row + 1) == ROWS || wall[row + 1][col] != null) {
				for (int j = 0; j < cells.length; j++) {
					Cell cell = cells[j];
					int col1 = cell.getCol();
					int row1 = cell.getRow();
					wall[row1][col1] = cell;
				}
				removeLine();
				tetromino = nextone;
				nextone = Tetromino.ranShape();
				return true;
			}
		}
		return false;
	}

	public boolean canRightMove() {
		if (tetromino == null)
			return false;
		Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell c = cells[i];
			int row = c.getRow();
			int col = c.getCol();
			if ((col + 1) == COLS || wall[row][col + 1] != null)
				return false;
		}
		return true;
	}

	public boolean canLeftMove() {
		if (tetromino == null)
			return false;
		Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell c = cells[i];
			int row = c.getRow();
			int col = c.getCol();
			if (col == 0 || wall[row][col - 1] != null)
				return false;
		}
		return true;
	}

	public void paint(Graphics g) {
		g.drawImage(tetris, 0, 0, null);
		g.translate(15, 15);
		paintWall(g);
		paintTetromino(g);
		paintNextone(g);
		paintTabs(g);
		paintPause(g);
		paintGameOver(g);
	}

	public boolean isGameOver() {
		for (int col = 0; col < COLS; col++) {
			if (wall[0][col] != null)
				return true;
		}
		return false;
	}

	public void paintGameOver(Graphics g) {
		if (isGameOver()) {
			tetromino = null;
			g.drawImage(gameover, -15, -15, null);
			Color color = new Color(0, 71, 157);
			g.setColor(color);
			Font font = new Font(Font.SERIF, Font.BOLD, 30);
			g.setFont(font);
			g.drawString("" + score, 260, 207);
			g.drawString("" + lines, 260, 253);
			g.drawString("" + level, 260, 300);
			STATE = false;
		}
	}

	public void paintPause(Graphics g) {
		if (pause) {
			STATE = false;
			Font f1 = new Font(Font.SERIF, Font.BOLD, 30);
			g.setFont(f1);
			g.drawString("PAUSE", 100, 300);
		} else {
			STATE = true;
		}
	}

	public void paintTabs(Graphics g) {
		int x = 410;
		int y = 160;
		Color color = new Color(0, 0, 0);
		g.setColor(color);
		Font f = new Font(Font.SERIF, Font.BOLD, 28);
		g.setFont(f);
		g.drawString("" + score, x, y);
		y += 56;
		g.drawString("" + lines, x, y);
		y += 56;
		g.drawString("" + level, x, y);
		y += 56;
		g.drawString("scoringFactor:	" + scoringFactor, 275, y);
		y += 56;
		g.drawString("speedFactor:	" + speedFactor, 275, y);
	}

	public void paintNextone(Graphics g) {
		if (nextone == null)
			return;
		Cell[] cells = nextone.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell c = cells[i];
			int row = c.getRow();
			int col = c.getCol() + 9;
			int x = col * CELL_SIZE;
			int y = row * CELL_SIZE;
			g.drawImage(c.getBgImage(), x, y, null);
		}
	}

	public void paintTetromino(Graphics g) {
		if (tetromino == null)
			return;

		Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell c = cells[i];
			int col = c.getCol();
			int row = c.getRow();
			int x = col * CELL_SIZE;
			int y = row * CELL_SIZE;
			g.drawImage(c.getBgImage(), x, y, null);
		}
	}

	public void paintWall(Graphics g) {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Cell cell = wall[row][col];
				int rows = row * CELL_SIZE;
				int cols = col * CELL_SIZE;
				if (cell == null) {
					// System.out.println(0);
				} else {
					g.drawImage(cell.getBgImage(), cols, rows, null);
				}
			}
		}
	}

	public static void initialize() {
		final JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setLocation(600, 300);
		frame.setSize(700, 380);
		frame.getContentPane().setLayout(
				new MigLayout("", "[][][][][][][][][][][]",
						"[][][][][][][][][][][][]"));

		JLabel lblMscoringFactor = new JLabel("ScoringFactor");
		frame.getContentPane().add(lblMscoringFactor, "cell 0 1");
		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(scoringFactor, 1, 10, 1));
		frame.getContentPane().add(spinner, "cell 2 1,alignx center");

		
		JLabel lblNlevelDifficulty = new JLabel("LevelDifficulty");
		frame.getContentPane().add(lblNlevelDifficulty, "cell 4 1");
		final JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(level, 20, 50, 1));
		frame.getContentPane().add(spinner_1, "cell 5 1");

		
		JLabel lblSspeed = new JLabel("SpeedFactor");
		frame.getContentPane().add(lblSspeed, "cell 8 1");
		final JSpinner spinner_2 = new JSpinner();
		spinner_2.setModel(new SpinnerNumberModel(speedFactor, 0.1, 1, 0.1));
		frame.getContentPane().add(spinner_2, "cell 9 1");

		
		JLabel lblSizeOfSquares = new JLabel("Size of squares");
		frame.getContentPane().add(lblSizeOfSquares, "cell 0 3");
		final JSpinner spinner_3 = new JSpinner();
		spinner_3.setModel(new SpinnerNumberModel(CELL_SIZE, 20, 30, 1));
		frame.getContentPane().add(spinner_3, "cell 2 3");



		JLabel lblAllowOneSquare = new JLabel("Allow one square");
		frame.getContentPane().add(lblAllowOneSquare, "cell 0 7");

		final JCheckBox chckbxNewCheckBox = new JCheckBox("Check for yes");
		frame.getContentPane().add(chckbxNewCheckBox, "cell 2 7");

		JLabel lblAllowTwoSquares = new JLabel("Allow two squares?");
		frame.getContentPane().add(lblAllowTwoSquares, "cell 0 8");

		final JCheckBox chckbxCheckForYes_1 = new JCheckBox("Check for yes");
		frame.getContentPane().add(chckbxCheckForYes_1, "cell 2 8");

		JLabel lblAllowThreeSquares = new JLabel("Allow three squares?");
		frame.getContentPane().add(lblAllowThreeSquares, "cell 0 9");

		final JCheckBox chckbxCheckForYes = new JCheckBox("Check for yes");
		frame.getContentPane().add(chckbxCheckForYes, "cell 2 9");

		JButton btnNewButton = new JButton("Save & quit");
		frame.getContentPane().add(btnNewButton, "cell 9 11");

		// Score Difficulty
		spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("value changed: " + spinner.getValue());
				scoringFactor = (Integer) spinner.getValue();
			}
		});
		// Level Difficulty
		spinner_1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("value changed: " + spinner_1.getValue());
				level = (Integer) spinner_1.getValue();
			}
		});
		// Speed
		spinner_2.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("value changed: " + spinner_2.getValue());
				speedFactor = (Double) spinner_2.getValue();
			}
		});
		
		spinner_3.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("value changed: " + spinner_3.getValue());
				CELL_SIZE = (Integer) spinner_3.getValue();
			}
		});


		chckbxNewCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				System.out.println("1-Checked? "
						+ chckbxNewCheckBox.isSelected());
			}
		});

		chckbxCheckForYes_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				System.out.println("2-Checked? "
						+ chckbxCheckForYes_1.isSelected());
			}
		});

		chckbxCheckForYes.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				System.out.println("3-Checked? "
						+ chckbxCheckForYes.isSelected());
			}
		});

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.dispose();
			}
		});
		frame.setVisible(true);
	}

	public static void startTetris() {
		JFrame f = new JFrame();
		Tetris tetris = new Tetris();
		JButton quit = new JButton("QUIT");
		JButton setting = new JButton("Setting");
		setting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initialize();
			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		f.setSize(525, 650);
		f.setLocationRelativeTo(null);
		f.getContentPane().add(tetris);
		f.add("North", setting);
		f.add("South", quit);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		tetris.action();
	}

	public static void main(String[] args) {
		Tetris.startTetris();
	}

}