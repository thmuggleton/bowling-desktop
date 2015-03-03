package thmuggleton.model.impl;

import java.util.Arrays;

import thmuggleton.Constants;
import thmuggleton.model.Frame;
import thmuggleton.model.exceptions.BowlingException;

/**
 * Model class to represent a game played by a single player. This class is only
 * used by other model classes.
 * 
 * @author Thomas Muggleton
 */
public class GameImpl {

	// Fields
	private AbstractFrame[] frames;
	private int currentFrameIndex;
	private boolean isFirstBall;
	private int lastFrameShots;
	private boolean finished;

	// Default constructor
	protected GameImpl() {
		
		// Create frames
		frames = new AbstractFrame[Constants.NUMBER_OF_FRAMES];
		
		// Create regular frames
		for (int i = 0; i < (frames.length - 1); i++)
			frames[i] = new FrameImpl();
		
		// Create last frame
		frames[Constants.NUMBER_OF_FRAMES - 1] = new LastFrameImpl();
		
		// Initialise remaining fields
		currentFrameIndex = 0;
		isFirstBall = true;
		lastFrameShots = 0;
		finished = false;
	}

	/**
	 * Adds the given score for the next shot in this game.
	 * 
	 * @param frame
	 * 
	 * @return boolean indicating whether the current frame is complete ({@code true})
	 * or not ({@code false}).
	 */
	protected boolean addScore(int score) {

		boolean frameOver = false;
		
		// Validate that all frames have not been played
		if (finished)
			throw new BowlingException(
					"All frames have already been added to this game.");

		// Branch for last frame
		else if (currentFrameIndex == (Constants.NUMBER_OF_FRAMES - 1))
			frameOver = this.setScoreForLastFrame(score);

		// Branch for valid frames other than last frame
		else
			frameOver = this.setScoreForRegularFrame(score);
		
		return frameOver;
	}

	/**
	 * Returns the all the frames currently stored in this game.
	 * 
	 * @return
	 */
	protected Frame[] getFrames() {
		return frames;
	}
	
	/**
	 * Returns the total score for this game.
	 */
	protected int getTotalScore() {
		
		int result = 0;
		
		for (int i = 0; i < frames.length; i++)
			result += frames[i].getTotal();
		
		return result;
	}
	
	/**
	 * Returns {@code true} if all frames are complete and have been added to
	 * the game.
	 * 
	 * @return
	 */
	protected boolean isFinished() {
		return this.finished;
	}
	
	/* ****************
	 *  HELPER METHODS
	 * ****************/
	
	/**
	 * Adds the next score for frames other than the last frame.
	 * 
	 * @param score
	 * @return boolean indicating whether the frame for which this
	 * score has been added is over.
	 */
	private boolean setScoreForRegularFrame(int score) {
		
		boolean frameOver = false;
		
		AbstractFrame currentFrame = frames[currentFrameIndex];
		AbstractFrame previousFrame = (currentFrameIndex > 0) ? frames[currentFrameIndex - 1] : null;
		
		// Branch for first ball
		if (isFirstBall) {
			currentFrame.setFirstBall(score);
			
			this.setBonusForFirstBall(score, previousFrame);
			
			// Increment frame index if strike scored, otherwise set as second ball
			if (currentFrame.isStrike()) {
				currentFrameIndex++;
				frameOver = true;
			}
			else
				isFirstBall = false;
		}
		
		// Branch for second ball
		else {
			currentFrame.setSecondBall(score);
			
			// Add bonus points for previous frame if applicable
			if (previousFrame != null && previousFrame.isStrike())
				previousFrame.addBonusPoints(score);
			
			// Increment frame index and set next frame to first ball
			currentFrameIndex++;
			frameOver = true;
			isFirstBall = true;
		}
		
		return frameOver;
	}

	/**
	 * Sets bonus points for previous frame(s) based on the score for
	 * the first ball in a frame.
	 * 
	 * @param score
	 * @param previousFrame containing the frame immediately preceding
	 * the current frame.
	 */
	private void setBonusForFirstBall(int score, AbstractFrame previousFrame) {
			
		// Add bonus points for previous frame if applicable
		if (previousFrame != null && (previousFrame.isStrike() || previousFrame.isSpare())) {
			previousFrame.addBonusPoints(score);
			
			// Branch for both preceding frames are strikes
			if (currentFrameIndex > 1 && previousFrame.isStrike()) {
				
				// Retrieve frame preceding previous frame
				AbstractFrame precedingFrame = frames[currentFrameIndex - 2];
				
				// Set bonus points for frame prior to previous frame
				if (precedingFrame.isStrike())
					precedingFrame.addBonusPoints(score);
			}
		}
	}

	/**
	 * Sets the score for the last frame or updates field for bonus points
	 * dependent on the which shot is being played in the last frame.
	 * 
	 * @param score
	 * 
	 * @return boolean indicating whether the frame for which this
	 * score has been added is over.
	 */
	private boolean setScoreForLastFrame(int score) {

		// Increment counter to track last frame
		lastFrameShots++;

		// Retrieve frame from array
		AbstractFrame lastFrame = frames[currentFrameIndex];
		AbstractFrame penultimateFrame = frames[currentFrameIndex - 1];
		
		switch (lastFrameShots) {

		// First shot of last frame
		case (1):
			lastFrame.setFirstBall(score);
			this.setBonusForFirstBall(score, penultimateFrame);
			break;

		// Second shot of last frame
		case (2):
			// Set bonus points for previous frame
			if (penultimateFrame.isStrike())
				penultimateFrame.addBonusPoints(score);
			
			// Set points for this frame
			if (lastFrame.isStrike())
				lastFrame.addBonusPoints(score);
			else {
				lastFrame.setSecondBall(score);

				// Notify end of game and increment index if not a spare or strike
				if (! (lastFrame.isStrike() || lastFrame.isSpare()))
					this.finished = true;
			}
			break;

		// Final shot of last frame
		case (3):
			if (lastFrame.isStrike() || lastFrame.isSpare()) {
				lastFrame.addBonusPoints(score);
				this.finished = true;
			} else
				throw new BowlingException(
						"Cannot set score for third shot of last frame if not a strike or spare.");
			break;
		}
		
		return this.finished;
	}

	// OVERRIDEN METHODS FROM OBJECT SUPERCLASS
	
	/**
	 * Returns a String displaying all of the frames in this game.
	 */
	@Override
	public String toString() {
		return frames.toString();
	}
	
	/**
	 * Auto-generated hashCode() method employing all fields.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentFrameIndex;
		result = prime * result + (finished ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(frames);
		result = prime * result + (isFirstBall ? 1231 : 1237);
		result = prime * result + lastFrameShots;
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
		if (!(obj instanceof GameImpl))
			return false;
		GameImpl other = (GameImpl) obj;
		if (currentFrameIndex != other.currentFrameIndex)
			return false;
		if (finished != other.finished)
			return false;
		if (!Arrays.equals(frames, other.frames))
			return false;
		if (isFirstBall != other.isFirstBall)
			return false;
		if (lastFrameShots != other.lastFrameShots)
			return false;
		return true;
	}
}
