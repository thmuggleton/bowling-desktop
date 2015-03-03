package thmuggleton.model.impl;

import java.util.Arrays;

import thmuggleton.model.Frame;

/**
 * Model class to represent the last frame.
 * 
 * @author Thomas Muggleton
 */
public class LastFrameImpl extends AbstractFrame {

	// Constants
	private final int THIRD_BALL_INDEX = 2;

	/**
	 * Constructor
	 */
	protected LastFrameImpl() {

		// Call superclass constructor
		super();
		
		// Initialise all fields
		this.scores = new int[Frame.LAST_FRAME];
		
		for (int i = 0; i < scores.length; i++)
			scores[i] = SCORE_UNSET;
	}
	
	/**
	 * Sets bonus points for the last frame and updates total for frame.
	 * 
	 * @param score
	 */
	@Override
	protected void addBonusPoints(int score) {

		// Branch for first bonus shot after strike on last frame
		if (this.isStrike() && this.scores[SECOND_BALL_INDEX] == SCORE_UNSET)
			this.scores[SECOND_BALL_INDEX] = score;
		
		// Branch for final shot of last frame
		else {
			this.scores[THIRD_BALL_INDEX] = score;
		}
		
		// Update total
		this.total = 0;
		for (int recordedScore : scores) {
			// Only add scores that have been set to total
			if (recordedScore != SCORE_UNSET)
				total += recordedScore;
		}
		
		// Notify listeners of change event
		this.notifyListeners();
	}
	
	// OVERRIDEN METHODS FROM OBJECT SUPERCLASS
	
	@Override
	public String toString() {
		return String.format("Ball 1: %d; Ball 2: %d; Ball 3: %d; Total: %d",
				this.scores[FIRST_BALL_INDEX], this.scores[SECOND_BALL_INDEX], 
				this.scores[THIRD_BALL_INDEX], this.total);
	}
	
	/**
	 * Auto-generated hash code method.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(scores);
		result = prime * result + total;
		return result;
	}

	/**
	 * Auto-generated hash code method.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FrameImpl))
			return false;
		FrameImpl other = (FrameImpl) obj;
		if (!Arrays.equals(scores, other.scores))
			return false;
		if (total != other.total)
			return false;
		return true;
	}
}
