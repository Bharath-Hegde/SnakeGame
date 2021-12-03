import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//frame
public class GameFrame extends JFrame implements ActionListener {

	JButton startButton;
	JButton singlePlayerButton;
	JButton multiplayerButton;
	JFrame menuFrame = new JFrame();
	public static JPanel scorePanel;

	String frameTitle;

	JButton createButton(String text, int a1, int a2, int a3, int a4) {

		JButton button = new JButton(text);

		button.setBounds(a1, a2, a3, a4);
		button.addActionListener(this);
		button.setFocusable(false);
		button.setFont(new Font("Comic San", Font.BOLD, 25));
		button.setBackground(Color.green);

		return button;
	}

	GameFrame() {

		// Frame for selecting single/multiplayer
		menuFrame.setVisible(true);
		menuFrame.setTitle("Snake Game");
		menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuFrame.setResizable(true);
		menuFrame.setSize(750, 412);
		menuFrame.setLayout(new BorderLayout());

		// LOADING BAR

		JProgressBar bar = new JProgressBar(0, 100);

		bar.setValue(0); // initial value of progress bar
		bar.setBounds(0, 0, 50, 50); // location and size
		bar.setStringPainted(true); // for text on bar
		bar.setFont(new Font("MV Boli", Font.BOLD, 25));
		bar.setForeground(Color.RED);
		bar.setBackground(Color.BLACK);

		menuFrame.add(bar);

		// Increment loading bar
		int counter = 0;
		while (counter <= 100) {

			bar.setValue(counter);
			try {
				Thread.sleep(23);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			counter += 1;
		}

		// Remove bar after reached 100%
		menuFrame.remove(bar);

		// Set frame icon
		ImageIcon icon = new ImageIcon("../images/snake.png");
		menuFrame.setIconImage(icon.getImage());

		menuFrame.setContentPane(new JLabel(new ImageIcon("../images/bk.png")));
		menuFrame.setLayout(new FlowLayout());

		// Buttons
		startButton = createButton("Start", 200, 200, 300, 50);
		singlePlayerButton = createButton("Single Player", 150, 200, 150, 50);
		multiplayerButton = createButton("Multiplayer", 250, 200, 150, 50);

		menuFrame.add(startButton);
		menuFrame.revalidate();
	}

	void makeGameFrame(int playerNumber) {

		// The main game screen (which starts after choosing a game mode)
		JFrame gFrame = new JFrame();
		int scorePanel_width = 150, gamePanel_width = 600;
		gFrame.setSize(scorePanel_width + gamePanel_width, 625);
		gFrame.setLayout(new BorderLayout());

		// Panel on the right which shows information like score and timer info
		scorePanel = new JPanel();
		scorePanel.setBackground(Color.GRAY);
		gFrame.add(scorePanel, BorderLayout.EAST);

		// Panel on the left where the snake and fruits are shown
		GamePanel gamePanel = new GamePanel(playerNumber);
		gamePanel.setPreferredSize(new Dimension(gamePanel_width, gamePanel_width));
		scorePanel.setPreferredSize(new Dimension(scorePanel_width, scorePanel_width));
		gFrame.add(gamePanel, BorderLayout.WEST); // initializes the window panel in

		if (playerNumber == 1) {
			frameTitle = "Single Player Snakes";
		} else {
			frameTitle = "Multiplayer Snakes";
		}
		gFrame.setTitle(frameTitle); // sets title of the current frame in the title bar
		gFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // by default pressing the close Option will only hide
																// // the
																// windoew but process still runs this line actually
																// closes the program
		gFrame.setResizable(false); // set resize to false
		ImageIcon icon = new ImageIcon("../images/snake.png");
		gFrame.setIconImage(icon.getImage());
		gFrame.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent click) {

		if (click.getSource() == startButton) {
			menuFrame.remove(startButton);
			menuFrame.add(singlePlayerButton);
			menuFrame.add(multiplayerButton);
			SwingUtilities.updateComponentTreeUI(menuFrame);

		} else if (click.getSource() == singlePlayerButton) {
			// Close the menuframe and open the game frame
			menuFrame.dispose();
			makeGameFrame(1);

		} else if (click.getSource() == multiplayerButton) {
			menuFrame.dispose();
			makeGameFrame(2);
		}
	}
}
