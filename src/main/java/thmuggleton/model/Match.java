package thmuggleton.model;

import java.util.Set;

import javax.swing.event.ChangeListener;

import thmuggleton.model.exceptions.BowlingException;


/**
 * Defines an interface to interact with a model object representing a bowling
 * match.
 * 
 * @author Thomas Muggleton
 */
public interface Match {

	/**
	 * Clears the existing data from this match.
	 */
	public void clear();
	
	/**
	 * Adds a new player to the match.
	 * 
	 * @param playerName
	 * @return
	 * @throws BowlingException if maximum number of players has already been added.
	 */
	public void addPlayer(String playerName) throws BowlingException;
	
	/**
	 * Adds the next score in the match.
	 * 
	 * @param score
	 * 
	 * @return boolean indicating whether the game for the current player is
	 *         complete ({@code true}) or not ({@code false}).
	 */
	public boolean addScore(int score);
	
	/**
	 * Returns an array of frames for a given player.
	 * 
	 * @param playerName
	 * @return
	 */
	public Frame[] getFrames(String playerName);
	
	/**
	 * Returns the total score for the given player.
	 * 
	 * @param playerName
	 * @return
	 */
	public int getTotalScore(String playerName);
	
	/**
	 * Returns the player(s) with the highest score in the match.
	 * 
	 * @return
	 */
	public Set<String> getLeaders();
	
	/**
	 * Returns {@Code true} if all players in this match have
	 * recorded scores for all of their frames.
	 * @return
	 */
	public boolean isFinished();
	
	/**
	 * Adds a change listener to this Match which will be informed of
	 * change events.
	 * 
	 * @param listener
	 */
	public void addChangeListener(ChangeListener listener);
}
