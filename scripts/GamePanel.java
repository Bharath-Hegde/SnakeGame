import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class GamePanel extends JPanel implements ActionListener {

	// Setting some game parameters
	Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int max_height = 600, max_width = 600, tile_size = 25;
	public static int delay = 100;
	protected char direction = 'R'; // preset direction

	// Declare array of Snake objects
	Snake[] snakes = new Snake[2];

	// boolean variable to show game is running or not
	private boolean running = false;

	Random random;
	int fruitsEaten;
	private int fruitPosX, fruitPosY;
	public int playerNumber;
	private int playerNumberStart;

	// Timer object to for the usage of time
	private Timer timer;

	// Declare images to be drawn in game panel
	BufferedImage fruitImage;
	BufferedImage snakeImage;
	BufferedImage snakeHeadImage;
	BufferedImage snakeHeadImageU;
	BufferedImage snakeHeadImageD;
	BufferedImage snakeHeadImageL;
	BufferedImage snakeHeadImageR;
	BufferedImage groundImage;

	// Declare labels used in scorePanel
	JLabel[] scoreLabels = new JLabel[2];
	JLabel timeLeftTextLabel = new JLabel();
	JLabel timeLeftLabel = new JLabel();
	JLabel scoreHeadingLabel = new JLabel();

	private String scoreText;
	private String gameOverText;

	// Declaring variables for timer functionality
	long startTime = System.currentTimeMillis();
	long elapsedTime;
	int countDownTime = 30;

	// Constructor
	GamePanel(int playerNumberStart) {

		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new myKeyAdapter());

		this.playerNumberStart = playerNumberStart;
		random = new Random();

		// Load the various images needed
		try {
			fruitImage = ImageIO.read(new File("../images/apple.jpg"));
		} catch (IOException e) {
		}

		try {
			snakeImage = ImageIO.read(new File("../images/snake_body.jpeg"));
		} catch (IOException e) {
		}

		try {
			snakeHeadImageU = ImageIO.read(new File("../images/snake_headU.png"));
		} catch (IOException e) {
		}

		try {
			snakeHeadImageD = ImageIO.read(new File("../images/snake_headD.png"));
		} catch (IOException e) {
		}

		try {
			snakeHeadImageL = ImageIO.read(new File("../images/snake_headL.png"));
		} catch (IOException e) {
		}

		try {
			snakeHeadImageR = ImageIO.read(new File("../images/snake_headR.png"));
		} catch (IOException e) {
		}

		try {
			groundImage = ImageIO.read(new File("../images/grass.jpeg"));
		} catch (IOException e) {
		}

		// Start the game
		startGame(playerNumberStart);

	}

	// function to start a game, parameter i -> number of players
	public void startGame(int i) {
		// set boolean game running to true
		this.running = true;

		// spawn the first fruit
		spawnFruit();
		playerNumber = i;

		if (playerNumberStart != 1) {
			setLabelParameters(scoreHeadingLabel, "LENGTHS: ", Color.BLACK, 19);
		} else {
			setLabelParameters(scoreHeadingLabel, "FRUITS EATEN: ", Color.BLACK, 19);
		}

		// loop through all snakes
		for (int j = 0; j < i; j++) {
			// generate snake head on random square inside our game surface
			snakes[j] = new Snake(j + 1, random.nextInt((int) (max_height / tile_size)) * tile_size,
					random.nextInt((int) (max_width / tile_size)) * tile_size);

			scoreText = "Score " + snakes[j].getName() + ": 0";
			scoreLabels[j] = new JLabel();
			setLabelParameters(scoreLabels[j], scoreText, Color.CYAN, 22);
		}

		GameFrame.scorePanel.add(Box.createHorizontalStrut(1000));
		if (playerNumberStart != 1) {
			setLabelParameters(timeLeftTextLabel, "Remaining: ", Color.RED, 18);
			setLabelParameters(timeLeftLabel, countDownTime + "s", Color.RED, 25);
		}

		// initialise and start timer
		timer = new Timer(delay, this);
		timer.start();
		elapsedTime = 0L;

	}

	void setLabelParameters(JLabel passedLabel, String labelText, Color labelColor, int labelSize) {

		passedLabel.setText(labelText);
		passedLabel.setForeground(labelColor);
		passedLabel.setFont(new Font("Ink Tree", Font.BOLD, labelSize));
		GameFrame.scorePanel.add(passedLabel);
	}

	// moves all snakes
	public void move() {
		// loop through all the snakes
		for (int j = 0; j < playerNumber; j++) {
			// move each snake
			snakes[j].move();
		}
	}

	// function to randomly generate fruits in the game
	public void spawnFruit() {
		// set a random (x,y) within game boundaries
		fruitPosY = random.nextInt((int) (max_height / tile_size)) * tile_size;
		fruitPosX = random.nextInt((int) (max_width / tile_size)) * tile_size;
	}

	// Swing part below
	// function to paint graphic component on screen
	public void paintComponent(Graphics g) {
		// paint graphic component from the super class (JPanel)
		super.paintComponent(g);

		// call the draw function
		draw(g);
	}

	// the actual function which puts things on the screen
	public void draw(Graphics g) {

		if (running) {
			// grass
			for (int i = 0; i < (max_width / tile_size); i++) {
				for (int j = 0; j < (max_height / tile_size); j++) {
					g.drawImage(groundImage, i * tile_size, j * tile_size, (i + 1) * tile_size, (j + 1) * tile_size, 0,
							0, groundImage.getWidth(), groundImage.getHeight(), null);
				}
			}

			// this for loop is for drawing all the snakes on the screen
			// for loop for iterating through all the snakes (players)
			for (int j = 0; j < playerNumber; j++) {
				// for loop to iterate through every point of a snake
				g.setColor(Color.RED);

				g.setFont(new Font("Ink Free", Font.BOLD, 15));
				for (int i = 0; i < snakes[j].getLength(); i++) {
					g.drawString("" + snakes[j].getName(), snakes[j].xPos[1] + tile_size / 2,
							snakes[j].yPos[1] + tile_size / 2);

					// i = 0 --> this is the snake's head
					if (i == 0) {

						switch (snakes[j].getDirection()) {
							case 'R':
								snakeHeadImage = snakeHeadImageR;
								break;
							case 'L':
								snakeHeadImage = snakeHeadImageL;
								break;
							case 'U':
								snakeHeadImage = snakeHeadImageU;
								break;
							case 'D':
								snakeHeadImage = snakeHeadImageD;
								break;
							default:
								break;
						}
						g.drawImage(snakeHeadImage, snakes[j].xPos[i], snakes[j].yPos[i], snakes[j].xPos[i] + tile_size,
								snakes[j].yPos[i] + tile_size, 0, 0, snakeHeadImage.getWidth(),
								snakeHeadImage.getHeight(), null);
					}
					// i != 0 --> this is the snake's body
					else {
						g.drawImage(snakeImage, snakes[j].xPos[i], snakes[j].yPos[i], snakes[j].xPos[i] + tile_size,
								snakes[j].yPos[i] + tile_size, 0, 0, snakeImage.getWidth(), snakeImage.getHeight(),
								null);
					}
				}
			}

			// Fruit
			g.drawImage(fruitImage, fruitPosX, fruitPosY, fruitPosX + tile_size, fruitPosY + tile_size, 0, 0,
					fruitImage.getWidth(), fruitImage.getHeight(), null);

		} else {

			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 75));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("Game Over", (max_width - metrics2.stringWidth("Game Over")) / 2, max_height / 2);
			if (playerNumberStart != 1) {

				if (snakes[0].checkHeadOn() || snakes[1].checkHeadOn()) {
					if (snakes[0].getLengthBeforeHeadOn() > snakes[1].getLengthBeforeHeadOn()) {
						gameOverText = "Player 1 wins!";
					} else if (snakes[0].getLengthBeforeHeadOn() < snakes[1].getLengthBeforeHeadOn()) {
						gameOverText = "Player 2 wins!";
					} else {
						gameOverText = "Draw!";
					}
				} else {
					if (snakes[0].getLength() > snakes[1].getLength()) {
						gameOverText = "Player 1 wins!";
					} else if (snakes[0].getLength() < snakes[1].getLength()) {
						gameOverText = "Player 2 wins!";
					} else {
						gameOverText = "Draw!";
					}
				}
				// gameover
				g.setColor(Color.cyan);
				g.drawString(gameOverText, (max_width - metrics2.stringWidth(gameOverText)) / 2,
						max_height / 2 + tile_size * 4);
			}
		}

		// Display scores
		for (int r = 0; r < playerNumber; r++) {
			if (playerNumberStart == 1) {
				scoreText = "" + snakes[r].getLength();
			} else {
				scoreText = "Player " + snakes[r].getName() + ": " + snakes[r].getLength();
			}
			scoreLabels[r].setText(scoreText);
		}

		if (playerNumberStart != 1) {
			if (running) {
				timeLeftLabel.setText(((countDownTime * 1000 - (int) elapsedTime) / 1000) + "s");
			} else {
				timeLeftLabel.setText("" + 0);
			}
		}

	}

	public void gameOver(Graphics g) {
	}

	// implements actionPerformed method from ActionListener class
	public void actionPerformed(ActionEvent e) {
		// for every action event, all snakes have to be moved
		if (running) {
			this.move();

			// terminates the game if number of players left is 1
			if (playerNumberStart == 1) {
				running = snakes[0].selfCollision(snakes[0]);
			} else {

				snakes[0].selfCollision(snakes[0]);
				snakes[0].selfCollision(snakes[1]);
				snakes[1].selfCollision(snakes[1]);
				snakes[1].selfCollision(snakes[0]);

				elapsedTime = (new Date()).getTime() - startTime;
				if (snakes[0].checkDeath() || snakes[1].checkDeath() || elapsedTime > countDownTime * 1000) {
					running = false;
				}
			}

			// checks if any snake has eaten a fruit. If yes, a new fruit is spawned
			for (int ii = 0; ii < playerNumber; ii++) {
				if (snakes[ii].checkFood(fruitPosX, fruitPosY)) {
					this.spawnFruit();
				}

			}
			// repaints the screen, so that the updated frame is displayed
			repaint();
		}
	}

	// inner class to handle all keyboard inputs
	public class myKeyAdapter extends KeyAdapter {

		// implements keyPressed method from KeyAdapter class
		@Override
		public void keyPressed(KeyEvent e) {
			// key event e is the event of a keyboard key being pressed
			switch (e.getKeyCode()) {
				// get keycode fetches what key has been pressed from the user keyboard
				// the switch case sets the new direction according to the keypress
				case KeyEvent.VK_LEFT:
					snakes[0].setDirection('L');
					break;
				case KeyEvent.VK_RIGHT:
					snakes[0].setDirection('R');
					break;
				case KeyEvent.VK_UP:
					snakes[0].setDirection('U');
					break;
				case KeyEvent.VK_DOWN:
					snakes[0].setDirection('D');
					break;

				default:
					break;
			}

			// same piee of code for the second player
			switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					snakes[1].setDirection('L');
					break;
				case KeyEvent.VK_D:
					snakes[1].setDirection('R');
					break;
				case KeyEvent.VK_W:
					snakes[1].setDirection('U');
					break;
				case KeyEvent.VK_S:
					snakes[1].setDirection('D');
					break;

				default:
					break;
			}

		}
	}
}
