package thmuggleton.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thmuggleton.Constants;
import thmuggleton.model.Frame;
import thmuggleton.model.Match;
import thmuggleton.model.exceptions.BowlingException;

/**
 * Implementation of the Match interface to represent a match
 * played by a variable number of players. 
 * 
 * @author Thomas Muggleton
 */
public class MatchImpl implements Match {

	// Fields
	private Map<String, GameImpl> match;
	private List<String> players;
	private int currentPlayerIndex = 0;
	private Set<String> leaders;
	private Collection<ChangeListener> changeListeners;
	
	/**
	 * Default constructor
	 */
	public MatchImpl() {
		
		// Initialise fields
		match = new HashMap<String, GameImpl>();
		players = new ArrayList<String>(Constants.MAX_NUMBER_OF_PLAYERS);
		leaders = new HashSet<String>();
		
		// Set change listeners to null until some are registered
		changeListeners = null;
	}
	
	/**
	 * Clears the existing data from this match.
	 */
	@Override
	public void clear() {
		match.clear();
		players.clear();
		leaders.clear();
	}
	
	/**
	 * Adds a new player to the match.
	 * 
	 * @param playerName
	 * @return
	 * @throws BowlingException if maximum number of players has already been added.
	 */
	@Override
	public void addPlayer(String playerName) {
		
		// Validation
		if (match.size() >= Constants.MAX_NUMBER_OF_PLAYERS)
			throw new BowlingException("Cannot add more than "
					+ Constants.MAX_NUMBER_OF_PLAYERS + " players.");
		
		else if (match.containsKey(playerName))
			throw new BowlingException("This player has already been added");
		
		// Branch to add a new player and accompanying game
		else {
			match.put(playerName, new GameImpl());
			players.add(playerName);
		}
	}

	/**
	 * Adds the next score in the match.
	 * 
	 * @param score
	 * 
	 * @return boolean indicating whether the game for the current player is
	 *         complete ({@code true}) or not ({@code false}).
	 */
	@Override
	public boolean addScore(int score) {
		
		// Validate that players have been added
		if (players.size() <= 0)
			throw new BowlingException("No players have been added to this match");
		
		// Retrieve current player
		String currentPlayer = players.get(currentPlayerIndex);
		
		// Retrieve game associated with player and add score
		GameImpl game = match.get(currentPlayer);
		boolean frameComplete = game.addScore(score);
		
		// Increment player index if frame complete, or wrap to first player
		if (frameComplete) {
			currentPlayerIndex++;
			
			if(currentPlayerIndex >= players.size())
				currentPlayerIndex = 0;
		}
		
		// Determine whether this score affects leaders
		this.updateLeaders(currentPlayer, score);
		
		// Notify listeners that a game is complete
		if (game.isFinished())
			this.notifyListeners();
		
		return game.isFinished();
	}

	/**
	 * Returns an array of frames for a given player.
	 * 
	 * @param playerName
	 * @return
	 */
	@Override
	public Frame[] getFrames(String playerName) {
		return match.get(playerName).getFrames();
	}

	/**
	 * Returns the total score for the given player.
	 * 
	 * @param playerName
	 * @return
	 */
	@Override
	public int getTotalScore(String playerName) {
		return match.get(playerName).getTotalScore();
	}

	/**
	 * Returns the player(s) with the highest score in the match.
	 * 
	 * @return
	 */
	@Override
	public Set<String> getLeaders() {
		return leaders;
	}
	
	/**
	 * Returns {@Code true} if all players in this match have
	 * recorded scores for all of their frames.
	 * @return
	 */
	@Override
	public boolean isFinished() {
		
		// Iterate over all games and return false
		// if any are not complete
		for (GameImpl game : match.values()) {
			if (!game.isFinished())
				return false;
		}
		
		// Match is finished
		return true;
	}
	
	/* ****************
	 *  HELPER METHODS
	 * ****************/
	
	/**
	 * Determines who currently has the top score in this
	 * match and updates instance field accordingly. 
	 * 
	 * @param currentPlayer whose score has just changed.
	 * @param score that has just been achieved.
	 */
	private void updateLeaders(String currentPlayer, int score) {
		
		// Retrieve score for player whose score has just changed
		int scoreForCurrentPlayer = this.getTotalScore(currentPlayer);
		
		// If current player has leading score, remove other leaders
		if (leaders.contains(currentPlayer) && score > 0) {
			leaders.clear();
			leaders.add(currentPlayer);
		}
		// Retrieve leading score for other players, if any
		else if (leaders.size() > 0) {
					
			int leadingScore = this.getTotalScore(leaders.iterator().next());
			
			// Branch for current player has a clear lead
			if (scoreForCurrentPlayer > leadingScore) {
				leaders.clear();
				leaders.add(currentPlayer);
			}
			// Branch for current player has equalled current lead
			else if (scoreForCurrentPlayer == leadingScore) {
				leaders.add(currentPlayer);
			}
			// Leaders have not changed; return to avoid notifying listeners
			else
				return;
		}
		// Branch for no leader currently set
		else {
			leaders.add(currentPlayer);
		}
		
		// Notify listeners that current leaders have changed
		this.notifyListeners();
	}
	
	/* **************************
	 *  OBSERVER PATTERN METHODS
	 * **************************/
	
	/**
	 * Adds a change listener to the game; game currently only
	 * notifies listeners when the game is finished.
	 * 
	 * @param listener
	 */
	public void addChangeListener(ChangeListener listener) {
		
		// Initialise collection for first change listener
		if (changeListeners == null)
			changeListeners = new ArrayList<ChangeListener>();
		
		changeListeners.add(listener);
	}
	
	/**
	 * Passes the given change event to all registered change listeners.
	 * 
	 * @param changeEvent
	 */
	private void notifyListeners() {
		
		if (changeListeners != null) {
			for (ChangeListener listener : changeListeners)
				listener.stateChanged(new ChangeEvent(this));
		}
	}

	/* ******************************************
	 *  OVERRIDEN METHODS FROM OBJECT SUPERCLASS
	 * ******************************************/
	
	/**
	 * Auto-generated hashCode() method employing match and leaders fields.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leaders == null) ? 0 : leaders.hashCode());
		result = prime * result + ((match == null) ? 0 : match.hashCode());
		return result;
	}

	/**
	 * Auto-generated equals() method employing match and leaders fields.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MatchImpl))
			return false;
		MatchImpl other = (MatchImpl) obj;
		if (leaders == null) {
			if (other.leaders != null)
				return false;
		} else if (!leaders.equals(other.leaders))
			return false;
		if (match == null) {
			if (other.match != null)
				return false;
		} else if (!match.equals(other.match))
			return false;
		return true;
	}
}
