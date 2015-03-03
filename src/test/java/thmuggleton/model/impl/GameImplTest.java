package thmuggleton.model.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import thmuggleton.Constants;

/**
 * Tests for the Game model class.
 * <p>
 * Adopts BDD naming conventions suggested by <a
 * href="http://dannorth.net/introducing-bdd/">Dan North</a>.
 * <p>
 * Takes a 'classical' TDD approach to focus on results rather than behavioural
 * expectations so that test code is less coupled to the implementation. (See <a
 * href="http://martinfowler.com/articles/mocksArentStubs.html">Mocks Aren't
 * Stubs</a>).
 * <p>
 * Methods in this class assume that the Frame class correctly sets scores and
 * returns the total correctly; this reduces the isolation of tests but adds
 * additional cross-validation and integration testing.
 * 
 * @author Thomas Muggleton
 */
@RunWith(JUnit4.class)
public class GameImplTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	/**
	 * Tests that regular scores are added correctly to the first frame.
	 */
	@Test
	public void shouldSuccessfullyAddScoresForTheFirstFrame() {

		// Record phase
		GameImpl game = new GameImpl();

		// Replay phase
		game.addScore(3);
		game.addScore(4);

		// Verification phase
		assertEquals("First frame should be returned with correct total score",
				7, game.getFrames()[0].getTotal());
	}

	/**
	 * Tests that bonus points are correctly added for a frame in which a strike
	 * was scored.
	 */
	@Test
	public void shouldAddCorrectBonusPointsForStrikeFrame() {
		
		// Record phase
		GameImpl game = new GameImpl();
		game.addScore(10);
		
		// Replay phase
		game.addScore(5);
		game.addScore(3);
		
		// Verify phase
		assertEquals("Strike frame should return score including bonus points",
				18, game.getFrames()[0].getTotal());
		assertEquals("Second frame should return score without bonus points",
				8, game.getFrames()[1].getTotal());
	}
	
	/**
	 * Tests that bonus points are correctly added for a frame in which a spare
	 * was scored.
	 */
	@Test
	public void shouldAddCorrectBonusPointsForSpareFrame() {
		
		// Record phase
		GameImpl game = new GameImpl();
		game.addScore(9);
		game.addScore(1);
		
		// Replay phase
		game.addScore(1);
		game.addScore(8);
		
		// Verify phase
		assertEquals("Spare frame should return score including bonus points",
				11, game.getFrames()[0].getTotal());
		assertEquals("Second frame should return score without bonus points",
				9, game.getFrames()[1].getTotal());
	}
	
	/**
	 * Tests that bonus points are correctly added for successive spares.
	 */
	@Test
	public void shouldAddCorrectBonusPointsForSuccessiveSpares() {
		
		// Record phase
		GameImpl game = new GameImpl();
		
		// First frame
		game.addScore(4);
		game.addScore(6);
		
		// Second frame
		game.addScore(5);
		game.addScore(5);
		
		// Replay phase
		game.addScore(2);
		
		// Verify phase
		assertEquals("First spare frame should return score including bonus points",
				15, game.getFrames()[0].getTotal());
		assertEquals("Second spare frame should return score including bonus points",
				12, game.getFrames()[1].getTotal());
	}
	
	
	/**
	 * Tests that 10 strikes results in a maximum scoring game.
	 */
	@Test
	public void shouldReturnCorrectScoreForMaximumScoringGame() {
		
		// Record phase
		GameImpl game = new GameImpl();
		
		// Replay phase
		for (int i = 0; i < (Constants.NUMBER_OF_FRAMES + 2); i++)
			game.addScore(10);
		
		// Verify phase
		assertEquals("Game should return total indicating maximum scoring game",
				300, game.getTotalScore());
	}
}
