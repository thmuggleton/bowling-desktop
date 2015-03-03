package thmuggleton.model;

import javax.swing.event.ChangeListener;

/**
 * Defines a read-only interface to interact with model objects
 * representing frames.
 * 
 * @author Thomas Muggleton
 */
public interface Frame {

	/**
	 * Constant for the number of possible shots in a regular frame.
	 */
	public static final int REGULAR_FRAME = 2;
	
	/**
	 * Constant for the number of possible shots in a regular frame.
	 */
	public static final int LAST_FRAME = 3; 
	
	/**
	 * Constant indicating that the score in this frame
	 * has not yet been set.
	 */
	public static final int SCORE_UNSET = -1;
	
	/**
	 * Returns the scores for each shot in the frame.
	 * 
	 * @return
	 */
	public int[] getScores();

	/**
	 * Returns the total score for the frame.
	 * 
	 * @return
	 */
	public int getTotal();

	/**
	 * Returns {@code true} if a strike was scored for this frame.
	 * 
	 * @return
	 */
	public boolean isStrike();

	/**
	 * Returns {@code true} if a spare was scored for this frame.
	 * 
	 * @return
	 */
	public boolean isSpare();
	
	/**
	 * Adds a change listener to this Frame which will be informed of
	 * change events.
	 * 
	 * @param listener
	 */
	public void addChangeListener(ChangeListener listener);
}
