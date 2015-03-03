package thmuggleton.model.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thmuggleton.Constants;
import thmuggleton.model.Frame;
import thmuggleton.model.exceptions.ScoreException;

/**
 * Provides an implementation of shared functionality in regular frames and
 * the last frame.
 * 
 * @author Thomas Muggleton
 */
public abstract class AbstractFrame implements Frame {

	// Constants
	protected final int FIRST_BALL_INDEX = 0;
	protected final int SECOND_BALL_INDEX = 1;
	
	// Fields
	protected int[] scores;
	protected int total;
	private boolean strike;
	private boolean spare;
	private Collection<ChangeListener> changeListeners;

	/**
	 * Constructor
	 */
	protected AbstractFrame() {
		
		// Initialise all fields
		this.total = SCORE_UNSET;
		this.strike = false;
		this.spare = false;
		
		// Set change listeners to null until some are registered
		changeListeners = null;
	}
	
	/**
	 * Sets the score for the first ball and updates boolean if a strike was
	 * scored.
	 * 
	 * @param score
	 * @throws ScoreException
	 *             if invalid score entered.
	 */
	protected void setFirstBall(int score) {

		// Score validation
		this.validateScore(score);

		// Branch for valid score entered
		this.scores[FIRST_BALL_INDEX] = score;

		if (this.scores[FIRST_BALL_INDEX] == Constants.TOTAL_PINS)
			this.strike = true;
		
		// Update intermediate total
		this.total = this.scores[FIRST_BALL_INDEX];
		
		// Notify listeners of change event
		this.notifyListeners();
	}

	/**
	 * Sets score for second ball and updates boolean if score resulted in a
	 * spare.
	 * 
	 * @param score
	 * @throws ScoreException
	 *             if invalid score entered or frame is a strike.
	 */
	protected void setSecondBall(int score) {

		// Score validation
		this.validateScore(score);

		if ((this.scores[FIRST_BALL_INDEX] + score) > Constants.TOTAL_PINS)
			throw new ScoreException("Total score cannot exceed " + Constants.TOTAL_PINS);
		
		else if (this.isStrike())
			throw new ScoreException("Cannot set second ball for Strike frame");

		// Branch for valid score entered
		else {
			this.scores[SECOND_BALL_INDEX] = score;

			// Branch for spare
			if (this.scores[FIRST_BALL_INDEX] + this.scores[SECOND_BALL_INDEX] == Constants.TOTAL_PINS)
				this.spare = true;

			// Update total
			this.total = this.scores[FIRST_BALL_INDEX] + this.scores[SECOND_BALL_INDEX];
			
			// Notify listeners of change event
			this.notifyListeners();
		}
	}

	/**
	 * Adds bonus points for the frame. This method can be called more than once
	 * to accumulate bonus points in the case of a strike.
	 * 
	 * @param score
	 * 
	 * @throws ScoreException
	 *             if frame was not a strike or a spare.
	 */
	protected abstract void addBonusPoints(int score);

	
	// GETTERS
	@Override
	public int[] getScores() {
		return scores;
	}
	@Override
	public int getTotal() {
		return (total != SCORE_UNSET) ? total : 0;
	}
	@Override
	public boolean isStrike() {
		return strike;
	}
	@Override
	public boolean isSpare() {
		return spare;
	}

	/* ****************
	 *  HELPER METHODS
	 * ****************/
	
	/**
	 * Validates scores input for first and second ball.
	 * 
	 * @param score
	 */
	private void validateScore(int score) {
		if (score < 0 || score > Constants.TOTAL_PINS)
			throw new ScoreException("Score must be between 0 and "
					+ Constants.TOTAL_PINS);
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
	protected void notifyListeners() {
		
		if (changeListeners != null) {
			for (ChangeListener listener : changeListeners)
				listener.stateChanged(new ChangeEvent(this));
		}
	}
	
	// OVERRIDEN METHODS FROM OBJECT SUPERCLASS
	
	/**
	 * Auto-generated hashCode() method employing all fields.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(scores);
		result = prime * result + (spare ? 1231 : 1237);
		result = prime * result + (strike ? 1231 : 1237);
		result = prime * result + total;
		return result;
	}

	/**
	 * Auto-generated equals() method employing all fields.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractFrame))
			return false;
		AbstractFrame other = (AbstractFrame) obj;
		if (!Arrays.equals(scores, other.scores))
			return false;
		if (spare != other.spare)
			return false;
		if (strike != other.strike)
			return false;
		if (total != other.total)
			return false;
		return true;
	}
	
}
