package thmuggleton.controller;

import javax.swing.JOptionPane;

import thmuggleton.model.Match;
import thmuggleton.model.exceptions.BowlingException;
import thmuggleton.view.View;

/**
 * Controls interaction with the bowling match model and view objects.
 * 
 * @author Thomas Muggleton
 */
public class MatchController {

	// Fields
	private Match model;
	private View view;
	private boolean matchBegun;
	
	// Constructor
	protected MatchController(Match model, View view) {
		
		this.model = model;
		this.view = view;
		this.matchBegun = false;
	}
	
	/**
	 * Adds a new player to the model and the view.
	 */
	protected void addPlayer() {

		// Retrieve name from input field
		String playerName = view.getNewPlayerName();
		
		// Show error if name is too long
		if (playerName.length() > View.PLAYER_NAME_MAX_SIZE)
			JOptionPane.showMessageDialog(view.getWindow(), 
					String.format("The name that you entered is too long.%nThe maximum player name size is %d.",
							View.PLAYER_NAME_MAX_SIZE),
					"Cannot add player", JOptionPane.ERROR_MESSAGE);
		
		// Branch for valid name
		else {
			// Try to add new player to model and view
			try {
				model.addPlayer(playerName);
				view.addPlayer(playerName);
			}
			// Display error message if model throws exception
			catch (BowlingException e) {
				JOptionPane.showMessageDialog(view.getWindow(), e.getMessage(),
						"Cannot add player", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Adds a new score to the model and view
	 */
	protected void addScore() {
		
		// Retrieve score details
		int score = view.getScoreEntered();
		
		// Try to add score to model
		try {
			model.addScore(score);
			
			// Disable the addition of further players
			if (!matchBegun) {
				view.disableFurtherPlayers();
				matchBegun = true;
			}
		}
		// Display error message if model throws exception
		catch (BowlingException e) {
			JOptionPane.showMessageDialog(view.getWindow(), e.getMessage(),
					"Cannot add player", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Creates a new match by clearing the model and view.
	 */
	public void createNewMatch() {
		
		// Prompt user if match is in progress
		if(!model.isFinished()) {
			int option = JOptionPane.showConfirmDialog(view.getWindow(),
					String.format("The match is still in progress;%nare you sure you want to clear the current match?"),
					"Match in progress", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			
			// If user presses 'No', return and do not exit
			if (option != JOptionPane.YES_OPTION)
				return;
		}
		
		view.clear();
		model.clear();	
	}
}
