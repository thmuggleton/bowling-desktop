package thmuggleton.view.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import thmuggleton.Command;
import thmuggleton.model.Match;
import thmuggleton.view.View;

/**
 * Defines the main window for the GUI.
 * 
 * @author Thomas Muggleton
 */
public class MainWindow extends JFrame implements View {

	/**
	 * Auto-generated serial version ID. 
	 */
	private static final long serialVersionUID = -8706859592151454646L;
	
	// Window constants
	private final Dimension MINIMUM_FRAME_SIZE = new Dimension(800, 500);
	private final String WINDOW_TITLE = "Bowling";

	// Fields
	private MenuBar menuBar;
	private AddPlayerPanel addPlayerPanel;
	private AddScoresPanel addScoresPanel;
	private Scoreboard scoreboard;

	/**
	 * Constructor
	 * 
	 * @param controllers Map between command Strings and their handlers.
	 * @param model object containing match data.
	 */
	public MainWindow(Map<String, ? extends ActionListener> controllers, Match model) {
		
		// Set window size and positional parameters
		this.setWindowParameters(controllers);

		// Call helper method to layout GUI components
		this.layoutComponents(controllers, model);
		
		// Determine component sizes and display the window
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * Clears the existing scoreboard and creates a new scoreboard.
	 */
	@Override
	public void clear() {
		this.scoreboard.clearScoreboard();
		this.addScoresPanel.enableInput(false);
		this.addPlayerPanel.enableInput(true);
		this.repaint();
	}
	
	/**
	 * Adds a new player to this View.
	 * 
	 * @param playerName String containing the name
	 * of the new player.
	 */
	@Override
	public void addPlayer(String playerName) {
		
		// Add new player name to scoreboard
		scoreboard.addPlayer(playerName);
		addScoresPanel.enableInput(true);
		this.pack();
	}
	
	/**
	 * Returns the name entered when a player is added.
	 * 
	 * @return
	 */
	@Override
	public String getNewPlayerName() {
		return addPlayerPanel.getEnteredName();
	}

	/**
	 * Returns the score entered.
	 * 
	 * @return
	 */
	@Override
	public int getScoreEntered() {
		return addScoresPanel.getScore();
	}
	
	/**
	 * Returns a BufferedImage of the currently displayed scoreboard.
	 * 
	 * @return
	 */
	@Override
	public BufferedImage getScoreboardImage() {
		
		// Retrieve width and height of scoreboard
		int width = scoreboard.getWidth();
		int height = scoreboard.getHeight();
		
		// Create a BufferedImage and paint all component
		// of the scoreboard
		BufferedImage scoreboardImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = scoreboardImage.createGraphics();
		scoreboard.paintAll(graphics);
		
		// Return buffered image of the scoreboard
		return scoreboardImage;
	}
	
	/**
	 * Returns this window.
	 * 
	 * @return
	 */
	@Override
	public JFrame getWindow(){
		return this;
	}
	
	/**
	 * Disables the addition of further players.
	 */
	@Override
	public void disableFurtherPlayers() {
		addPlayerPanel.enableInput(false);
	}
	
	/**
	 * Updates the View when the bowling match is over.
	 */
	@Override
	public void setMatchFinished() {
		this.addScoresPanel.enableInput(false);
	}
	
	/**
	 * Highlights the current leader(s).
	 * 
	 * @param leaders Set containing current leader(s).
	 */
	@Override
	public void highlightLeaders(Set<String> leaders) {
		// Reset all players to default
		scoreboard.resetPlayerColours();
		
		for (String leader : leaders)
			scoreboard.highlightPanel(leader, View.LEADER_COLOUR);
	}
	
	/*
	 * **********************
	 *     HELPER METHODS
	 * **********************
	 */
	
	/**
	 * Sets the title, default close behaviour, size, and position of the
	 * window.
	 * 
	 * @param controller to pass to custom window listener
	 */
	private void setWindowParameters(Map<String, ? extends ActionListener> controllers) {

		// Set window title
		this.setTitle(WINDOW_TITLE);

		// Enable custom behaviour when user closes the window
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new MainWindowListener(controllers.get(Command.EXIT)));

		// Retrieve centre point of screen
		Point screenCentre = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();

		// Calculate x and y coordinates of upper-left corner of window
		int x = (int) (screenCentre.getX() - (MINIMUM_FRAME_SIZE.getWidth() / 2));
		int y = (int) (screenCentre.getY() - (MINIMUM_FRAME_SIZE.getHeight() / 2));

		// Set location and minimum size for frame
		this.setLocation(x, y);
		this.setMinimumSize(MINIMUM_FRAME_SIZE);
	}

	/**
	 * Lays out all of the components for the GUI.
	 * 
	 * @param controllers Map between command Strings and their handlers.
	 * @param model object containing match data.
	 */
	private void layoutComponents(Map<String, ? extends ActionListener> controllers, Match model) {
		
		// Create and add menu bar
		menuBar = new MenuBar(controllers);
		this.add(menuBar, BorderLayout.NORTH);
		
		// Create centre-panel for input and scoreboard
		JPanel centrePanel = new JPanel(new BorderLayout());
		
		// Add panel for user input
		centrePanel.add(createUserInputPanel(controllers), BorderLayout.NORTH);
		
		// Add scoreboard
		this.scoreboard = new Scoreboard(model);
		centrePanel.add(scoreboard, BorderLayout.CENTER);
		
		// Add centre-panel to main window
		this.add(centrePanel, BorderLayout.CENTER);
	}
	
	/**
	 * Lays out components for the top panel and adds these to the GUI.
	 * 
	 * @param controllers Map between command Strings and their handlers.
	 * @return
	 */
	private JPanel createUserInputPanel(Map<String, ? extends ActionListener> controllers) {
		
		// Create panel to return
		JPanel result = new JPanel();
		
		// Create panel with textfield and button to add players
		addPlayerPanel = new AddPlayerPanel(controllers.get(Command.ADD_PLAYER));
		addScoresPanel = new AddScoresPanel(controllers.get(Command.ADD_SCORE));
		
		result.add(addPlayerPanel);
		result.add(addScoresPanel);
		
		return result;
	}
	
	/**
	 * Defines behaviour in response to user interaction with the main window.
	 * 
	 * @author Thomas Muggleton
	 */
	private class MainWindowListener extends WindowAdapter {
		
		// Fields
		private ActionListener controller;
		
		/**
		 * Constructor
		 * 
		 * @param controller handler for window events.
		 */
		public MainWindowListener(ActionListener controller) {
			super();
			this.controller = controller;
		}
		
		/**
		 * Directs action event to controller when the user closes the window.
		 */
		@Override
		public void windowClosing(WindowEvent e) {
			controller.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, Command.EXIT));
		}
	}
}
