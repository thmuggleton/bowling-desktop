package thmuggleton.model.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import thmuggleton.model.exceptions.ScoreException;

/**
 * Tests for the Frame model class.
 * <p>
 * Adopts BDD naming conventions suggested by
 * <a href="http://dannorth.net/introducing-bdd/">Dan North</a>.
 * <p>
 * Takes a 'classical' TDD approach to focus on results rather than behavioural
 * expectations so that test code is less coupled to the implementation. (See
 * <a href="http://martinfowler.com/articles/mocksArentStubs.html">Mocks Aren't Stubs</a>).
 * 
 * @author Thomas Muggleton
 */
@RunWith(JUnit4.class)
public class FrameImplTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	/**
	 * Tests that the frame object returns the correct total for a frame that
	 * did involve a strike or a spare.
	 */
	@Test
	public void shouldReturnCorrectTotalForStandardFrame() {

		// Record phase
		FrameImpl frame = new FrameImpl();
		frame.setFirstBall(3);
		frame.setSecondBall(5);

		// Replay phase
		int total = frame.getTotal();

		// Verify phase
		assertEquals(
				"Total should correspond to addition of first and second score",
				8, total);
	}

	/**
	 * Tests that frame object returns correct total for a frame when a strike
	 * was scored.
	 */
	@Test
	public void shouldReturnCorrectTotalForStrikeFrame() {

		// Record phase
		FrameImpl frame = new FrameImpl();
		frame.setFirstBall(10);
		frame.addBonusPoints(6);

		// Replay phase
		int total = frame.getTotal();

		// Verify phase
		assertEquals(
				"Total should correspond to addition of first score and bonus points",
				16, total);
	}

	/**
	 * Tests that a frame returns the correct total when a spare is scored.
	 */
	@Test
	public void shouldReturnCorrectTotalForSpareFrame() {

		// Record phase
		FrameImpl frame = new FrameImpl();
		frame.setFirstBall(4);
		frame.setSecondBall(6);
		frame.addBonusPoints(10);

		// Replay phase
		int total = frame.getTotal();

		// Verify phase
		assertEquals("Total returned should correspond to score for "
				+ "first two balls and bonus points", 20, total);
	}

	/**
	 * Tests that an exception will be thrown for second ball score set for a
	 * frame in which a strike was scored.
	 */
	@Test
	public void shouldThrowExceptionForSecondBallOnStrikeFrame() {

		// Record phase
		FrameImpl frame = new FrameImpl();
		frame.setFirstBall(10);

		// Replay phase
		exception.expect(ScoreException.class);
		frame.setSecondBall(4);
		frame.addBonusPoints(1);

		// Verify phase
		assertEquals(
				"Frame should only contain score for strike and bonus points",
				11, frame.getTotal());
	}

	/**
	 * Tests that an exception will be thrown if bonus points are set for a
	 * frame in which a strike or a spare was not scored.
	 */
	@Test
	public void shouldThrowExceptionForBonusPointsOnNonFullScoringFrame() {

		// Record phase
		FrameImpl frame = new FrameImpl();
		frame.setFirstBall(3);
		frame.setSecondBall(4);

		// Replay phase
		exception.expect(ScoreException.class);
		frame.addBonusPoints(1);

		// Verify phase
		assertEquals(
				"Frame should only contain score for first and second balls",
				7, frame.getTotal());
	}
}
