package thmuggleton.view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.JFrame;

/**
 * Defines the methods required by the controller to
 * interact with the view.
 * 
 * @author Thomas Muggleton
 */
public interface View {

	/**
	 * Constant for the maximum size for a player name.
	 */
	public static final int PLAYER_NAME_MAX_SIZE = 10;
	
	/**
	 * Colour used to highlight the name of the leader(s) in this View.
	 */
	public static final Color LEADER_COLOUR = Color.YELLOW;
	
	/**
	 * Colour used as the default background colour for a player name
	 * in this View. 
	 */
	public static final Color DEFAULT_PLAYER_COLOUR = Color.WHITE;

	/**
	 * Constant for character to represent a strike.
	 */
	public static final String STRIKE_CHARACTER = "X";
	
	/**
	 * Constant for character to represent a spare.
	 */
	public static final String SPARE_CHARACTER = "/";
	
	/**
	 * Clears the existing match from the view.
	 */
	public void clear();
	
	/**
	 * Adds a new player to this View.
	 * 
	 * @param playerName
	 */
	public void addPlayer(String playerName);
	
	/**
	 * Returns the name entered when a player is added.
	 * @return
	 */
	public String getNewPlayerName();
	
	/**
	 * Returns the score entered.
	 * 
	 * @return
	 */
	public int getScoreEntered();
	
	/**
	 * Returns a BufferedImage of the currently displayed scoreboard.
	 * 
	 * @return
	 */
	public BufferedImage getScoreboardImage();
	
	/**
	 * Returns the window with which an error dialog should be displayed.
	 * 
	 * @return
	 */
	public JFrame getWindow();
	
	/**
	 * Sets the game as complete in the View for the given
	 * player.
	 */
	public void disableFurtherPlayers();

	/**
	 * Updates the View when the bowling match is over.
	 */
	public void setMatchFinished();
	
	/**
	 * Highlights the current leader in this View.
	 * 
	 * @param leaders
	 */
	public void highlightLeaders(Set<String> leaders);
}
