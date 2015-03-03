package thmuggleton.model.impl;

import java.util.Arrays;

import thmuggleton.Constants;
import thmuggleton.model.Frame;
import thmuggleton.model.exceptions.ScoreException;

/**
 * Model class to represent regular frames. All attributes are stored in fields
 * which increases memory usage but removes the need for calculations when
 * get...() methods are called.
 * 
 * @author Thomas Muggleton
 */
public class FrameImpl extends AbstractFrame {

	// Fields
	private int bonusPoints;
	
	/**
	 * Constructor
	 */
	protected FrameImpl() {

		// Call superclass constructor
		super();
		
		// Initialise all fields
		this.scores = new int[Frame.REGULAR_FRAME];
		
		for (int i = 0; i < scores.length; i++)
			scores[i] = SCORE_UNSET;
		
		this.bonusPoints = 0;
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
	@Override
	protected void addBonusPoints(int score) {
		
		// Score validation
		if (score < 0 || score > Constants.MAX_BONUS_POINTS)
			throw new ScoreException("Bonus points must be between 0 and "
					+ Constants.MAX_BONUS_POINTS);

		// Throw exception if not a strike or spare
		else if (!(this.isStrike() || this.isSpare()))
			throw new ScoreException("Cannot score bonus points"
					+ " on frame that was not a strike or spare");

		// Set bonus points for regular frame and update total
		else {
			this.bonusPoints += score;
			
			// Branch for strike
			if (this.isStrike())
				this.total = scores[FIRST_BALL_INDEX] + this.bonusPoints;
			
			// Branch for spare
			else
				this.total = scores[FIRST_BALL_INDEX] + scores[SECOND_BALL_INDEX] + this.bonusPoints;
			
			// Notify listeners of change event
			this.notifyListeners();
		}
	}
	
	// OVERRIDEN METHODS FROM OBJECT SUPERCLASS
	
	@Override
	public String toString() {
		return String.format("Ball 1: %d; Ball 2: %d; Total: %d",
				this.scores[FIRST_BALL_INDEX], this.scores[SECOND_BALL_INDEX], this.total);
	}

	/**
	 * Auto-generated hash code method.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bonusPoints;
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
		if (bonusPoints != other.bonusPoints)
			return false;
		if (!Arrays.equals(scores, other.scores))
			return false;
		if (total != other.total)
			return false;
		return true;
	}
}
