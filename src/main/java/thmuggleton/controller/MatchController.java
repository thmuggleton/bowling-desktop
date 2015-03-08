package thmuggleton.controller;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thmuggleton.Command;
import thmuggleton.Constants;
import thmuggleton.model.Match;
import thmuggleton.model.exceptions.BowlingException;
import thmuggleton.view.View;

/**
 * Controls interaction with the bowling match model and view objects.
 * 
 * @author Thomas Muggleton
 */
public class MatchController implements Controller, ChangeListener {

	// Fields
	private Match model;
	private View view;
	private boolean matchBegun;
	private boolean winnerDeclared;
	
	// Constructor
	protected MatchController(Match model) {
		
		this.model = model;
		this.matchBegun = false;
	}
	
	/**
	 * Sets the given view for this controller.
	 */
	@Override
	public void setView(View view) {
		this.view = view;
	}
	
	/**
	 * Main handler method for action events.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		
		switch (command) {
		
		case (Command.NEW_MATCH) :
			this.createNewMatch();
			break;
			
		case (Command.ADD_PLAYER) :
			this.addPlayer();
			break;
			
		case(Command.ADD_SCORE) :
			this.addScore();
			break;
		}
	}
	
	/**
	 * Reacts to change events in the model.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		
		// Retrieve leaders
		Set<String> leaders = model.getLeaders();
		
		// Iterate over leader(s) to highlight in view
		view.highlightLeaders(leaders);
		
		// Display message if match is over and winner has not been declared
		if (model.isFinished() && !winnerDeclared) {
			view.setMatchFinished();
			this.displayMessageForEndOfMatch(leaders);
			this.winnerDeclared = true;
		}
	}

	/**
	 * Displays a message at the end of the match.
	 * 
	 * @param winners Set containing the winner(s) of this match.
	 */
	private void displayMessageForEndOfMatch(Set<String> winners) {
		
		// Build message
		StringBuilder winnersMessage = new StringBuilder();
		winnersMessage.append("Congratulations, ");
		
		// Create iterator over leaders
		Iterator<String> it = winners.iterator();
		
		// Branch for one winner
		if (winners.size() == 1)
			winnersMessage.append(it.next());
		
		// Branch for tied first place
		else {
			while(it.hasNext())
				winnersMessage.append(it.next() + ", ");
			
			// Remove last comma and space, second-last comma, and insert "and"
			winnersMessage.delete(winnersMessage.lastIndexOf(","), winnersMessage.length());
			winnersMessage.deleteCharAt(winnersMessage.lastIndexOf(","));
			winnersMessage.insert(winnersMessage.lastIndexOf(" "), " and");
		}
		
		winnersMessage.append(", you won!");
		
		JOptionPane.showMessageDialog(view.getWindow(),
				winnersMessage.toString(), "Congratulations!",
				JOptionPane.INFORMATION_MESSAGE, 
				this.createIcon(Constants.PATH_TO_TROPHY_IMAGE));
	}

	/**
	 * Creates an icon from the image to which the path points.
	 * 
	 * @param path a String giving the path to the image file;
	 * this path is relative to the src/main/resources/ directory.
	 * @return Icon object containing the image at the specified
	 * path.
	 */
	private Icon createIcon(String path) {
		
		Icon result = null;
		
		// Retrieve image file resource and create icon
		URL imageURL = this.getClass().getClassLoader().getResource(path);
		if (imageURL != null)
			result = new ImageIcon(imageURL);
		else
			System.err.println("Could not locate image at " + path);
		
		return result;
	}
	
	/**
	 * Adds a new player to the model and the view.
	 */
	private void addPlayer() {

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
	private void addScore() {
		
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
	private void createNewMatch() {
		
		// Prompt user if match is in progress
		if(!model.isFinished()) {
			int option = JOptionPane.showConfirmDialog(view.getWindow(),
					String.format("The match is still in progress;%nare you sure you want to clear the current match?"),
					"Match in progress", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			
			// If user presses 'No', return and do not clear match
			if (option != JOptionPane.YES_OPTION)
				return;
		}
		
		view.clear();
		model.clear();
		this.matchBegun = false;
		this.winnerDeclared = false;
	}
}
