package thmuggleton.view.impl;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import thmuggleton.Constants;
import thmuggleton.model.Match;
import thmuggleton.view.View;

/**
 * Defines the view class to display the scoreboard.
 * 
 * @author Thomas Muggleton
 */
public class Scoreboard extends JPanel {
	
	/**
	 * Auto-generated serial version ID. 
	 */
	private static final long serialVersionUID = 821700465025358729L;
	
	// Fields
	private Match model;
	private Map<String, PlayerScorePanel> playerPanels;

	/**
	 * Constructor.
	 * 
	 * @param model object containing match data.
	 */
	public Scoreboard(Match model) {

		// Store model in field
		this.model = model;

		// Create scoreboard and set layout for maximum number of players
		this.setLayout(new GridLayout(Constants.MAX_NUMBER_OF_PLAYERS, 1));
		this.setBackground(Color.WHITE);
		
		// Create score display maps
		playerPanels = new HashMap<String, PlayerScorePanel>();
	}
	
	/**
	 * Clears the existing scoreboard.
	 */
	protected void clearScoreboard() {
		playerPanels.clear();
		this.removeAll();
	}

	/**
	 * Updates scoreboard when new players are added.
	 * 
	 * @param playerName String to be added to the scoreboard
	 * for the new player.
	 */
	protected void addPlayer(String playerName) {
		
		// Create new panel
		PlayerScorePanel newPanel = new PlayerScorePanel(model, playerName);
		
		// Add new entry to Map and scoreboard display
		this.playerPanels.put(playerName, newPanel);
		this.add(newPanel);
	}

	/**
	 * Resets the colour of all player labels to the default
	 * colour.
	 */
	public void resetPlayerColours() {
		
		for (PlayerScorePanel panel : playerPanels.values())
			panel.highlightPlayer(View.DEFAULT_PLAYER_COLOUR);		
	}
	
	/**
	 * Highlights the panel for the given player with the given
	 * colour.
	 * 
	 * @param playerName String for the name of the player to highlight.
	 * @param colour Color object containing the colour with which to
	 * highlight the given player.
	 */
	protected void highlightPanel(String playerName, Color colour) {
		playerPanels.get(playerName).highlightPlayer(colour);
	}
}
