package thmuggleton.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import thmuggleton.Constants;
import thmuggleton.model.exceptions.BowlingException;

/**
 * Tests for the Match model class.
 * <p>
 * Adopts BDD naming conventions suggested by <a
 * href="http://dannorth.net/introducing-bdd/">Dan North</a>.
 * <p>
 * Takes a 'classical' TDD approach to focus on results rather than behavioural
 * expectations so that test code is less coupled to the implementation. (See <a
 * href="http://martinfowler.com/articles/mocksArentStubs.html">Mocks Aren't
 * Stubs</a>).
 * <p>
 * Similarly to other model test classes, these tests assume the correct
 * functioning of dependent classes. Again, this reduces the isolation of tests
 * but adds additional cross-validation and integration testing.
 * 
 * @author Thomas Muggleton
 */
@RunWith(JUnit4.class)
public class MatchImplTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	/**
	 * Tests that the maximum number of players can be added, after which an exception
	 * should occur.
	 * <p>
	 * Coverage: addPlayer() method.
	 */
	@Test
	public void shouldAddUpToTheMaximumNumberOfPlayersThenThrowException(){
		
		/* **************
		 *  Record phase
		 * **************/
		MatchImpl match = new MatchImpl();
		
		// Create array containing maximum number of players
		String[] testPlayers = new String[Constants.MAX_NUMBER_OF_PLAYERS];
		for (int i = 0; i < testPlayers.length; i++)
			testPlayers[i] = "Test" + i;
		
		/* **************
		 *  Replay phase
		 * **************/
		// Add maximum number of players
		for (int i = 0; i < testPlayers.length; i++)
			match.addPlayer(testPlayers[i]);
		
		// Expect exception when a further player is added
		exception.expect(BowlingException.class);
		match.addPlayer("Superfluous player");
	}
	
	/**
	 * Tests that duplicate players cannot be added without resulting in an
	 * exception.
	 * <p>
	 * Coverage: addPlayer() method.
	 */
	@Test
	public void shouldNotAllowPlayersWithTheSameNameToBeAdded() {
		
		/* **************
		 *  Record phase
		 * **************/
		String playerName = "Duplicate player";
		
		MatchImpl match = new MatchImpl();
		match.addPlayer(playerName);
		
		/* **************
		 *  Replay phase
		 * **************/
		exception.expect(BowlingException.class);
		match.addPlayer(playerName);
	}
	
	/**
	 * Tests that scores that do not result in bonus points are added correctly.
	 * <p>
	 * Coverage: addPlayer(), addScore(), getFrames() and isFinished() methods.
	 */
	@Test
	public void shouldAddScoresThatDoNotResultInBonusPointsCorrectly() {
		
		/* **************
		 *  Record phase
		 * **************/
		String playerName = "Test player";
		
		MatchImpl match = new MatchImpl();
		match.addPlayer("Test player");
		
		/* **************
		 *  Replay phase
		 * **************/
		
		// First frame
		match.addScore(4);
		match.addScore(3);
		
		// Second frame
		match.addScore(1);
		match.addScore(8);
		
		// Third frame
		match.addScore(0);
		match.addScore(0);
		
		/* **************
		 *  Verify phase
		 * **************/
		assertEquals("Score for first frame should reflect expected score.",
				7, match.getFrames(playerName)[0].getTotal());
		
		assertEquals("Score for second frame should reflect expected score.",
				9, match.getFrames(playerName)[1].getTotal());
		
		assertEquals("Score for no scoring frame should be zero.",
				0, match.getFrames(playerName)[2].getTotal());
		
		assertFalse("Match should return that it is not finished",
				match.isFinished());
	}
	
	/**
	 * Tests that scores that result in bonus points are added correctly.
	 * <p>
	 * Coverage: addPlayer(), addScore(), getFrames() and isFinished() methods.
	 */
	@Test
	public void shouldAddScoresThatResultInBonusPointsCorrectly() {
		
		/* **************
		 *  Record phase
		 * **************/
		String playerName = "Test player";
		
		MatchImpl match = new MatchImpl();
		match.addPlayer("Test player");
		
		/* **************
		 *  Replay phase
		 * **************/
		
		// First frame (strike)
		match.addScore(10);
		
		// Second frame
		match.addScore(8);
		match.addScore(1);
		
		// Third frame (spare)
		match.addScore(9);
		match.addScore(1);
		
		// Fourth frame (strike)
		match.addScore(10);
		
		/* **************
		 *  Verify phase
		 * **************/
		assertEquals("Score for first strike frame should include bonus points.",
				19, match.getFrames(playerName)[0].getTotal());
		
		assertEquals("Score for second frame should reflect expected score.",
				9, match.getFrames(playerName)[1].getTotal());
		
		assertEquals("Score for spare frame should include bonus points.",
				20, match.getFrames(playerName)[2].getTotal());
		
		assertEquals("Score for second strike frame should not include bonus points since no further scores were added.",
				10, match.getFrames(playerName)[3].getTotal());
		
		assertFalse("Match should return that it is not finished",
				match.isFinished());
	}
	
	/**
	 * Tests that an exception is thrown if scores are added before any
	 * players are added.
	 * <p>
	 * Coverage: addScore() method.
	 */
	@Test
	public void shouldThrowExceptionIfScoreAddedForPlayerNotInMatch() {
		
		/* **************
		 *  Record phase
		 * **************/
		MatchImpl match = new MatchImpl();
		
		/* **************
		 *  Replay phase
		 * **************/
		exception.expect(BowlingException.class);
		match.addScore(10);
	}
	
	/**
	 * Tests that total scores and the winner of a match are correctly returned.
	 * <p>
	 * Coverage: 	addPlayer(), addScore(), getTotalScore(), getLeaders() and 
	 * 				isFinished() methods.
	 */
	@Test
	public void shouldReturnCorrectWinnerForMatch() {
		
		/* **************
		 *  Record phase
		 * **************/
		String playerOne = "Alice";
		String playerTwo = "Bob";
		
		MatchImpl match = new MatchImpl();
		match.addPlayer(playerOne);
		match.addPlayer(playerTwo);
		
		/* **************
		 *  Replay phase
		 * **************/
		
		// Add scores for all frames except last frame
		for (int i = 0; i < (Constants.NUMBER_OF_FRAMES - 1); i++) {
			// Player one frames (all strikes)
			match.addScore(10);
			
			// Player two frames (all nines)
			match.addScore(9);
			match.addScore(0);
		}
		
		// Add final three strikes for player one
		match.addScore(10);
		match.addScore(10);
		match.addScore(10);
		
		// Add final score for player two
		// Player two frames (all nines)
		match.addScore(9);
		match.addScore(0);
		
		/* **************
		 *  Verify phase
		 * **************/
		assertEquals("Player one should have total score for perfect game (300)",
				300, match.getTotalScore(playerOne));
		assertEquals("Player two should have total score after all nines (90)",
				90, match.getTotalScore(playerTwo));
		
		assertTrue("Match should return that it is finished",
				match.isFinished());
		
		assertTrue("Player one should be among the winners returned",
				match.getLeaders().contains(playerOne));
		assertEquals("Only player one should be returned as the winner",
				1, match.getLeaders().size());
	}
	
	/**
	 * Tests that multiple winners are returned when final scores are tied.
	 * <p>
	 * Coverage: 	addPlayer(), addScore(), getTotalScore(), getLeaders()
	 * 				and isFinished() methods.
	 */
	@Test
	public void shouldReturnCorrectWinnerForTiedMatch() {
		
		/* **************
		 *  Record phase
		 * **************/
		String[] winners = {"Alice", "Bob", "Charlie"};
		String[] losers = {"Grumpy", "Happy", "Dopey"};
		
		MatchImpl match = new MatchImpl();
		
		for (String winner : winners)
			match.addPlayer(winner);
		
		for (String loser : losers)
			match.addPlayer(loser);
		
		/* **************
		 *  Replay phase
		 * **************/
		
		// 9 Frames for all players (all 5 for winners; all 4 for others)
		for (int i = 0; i < (Constants.NUMBER_OF_FRAMES - 1); i++) {
			
			for (int j = 0; j < winners.length; j++) {
				match.addScore(5);
				match.addScore(5);
			}
			
			for (int j = 0; j < losers.length; j++) {
				match.addScore(4);
				match.addScore(4);
			}
		}
		
		// Add final frames for winners
		for (int j = 0; j < winners.length; j++) {
			match.addScore(5);
			match.addScore(5);
			match.addScore(5);
		}
		
		// Add final frames for losers
		for (int j = 0; j < losers.length; j++) {
			match.addScore(4);
			match.addScore(4);
		}
		
		
		/* **************
		 *  Verify phase
		 * **************/
		assertEquals("Winners should have total for knocking down 5 pins with every shot (150).",
				150, match.getTotalScore(winners[0]));
		assertEquals("Losers should have total for knocking down 4 pins with every shot (80).",
				80, match.getTotalScore(losers[0]));
		
		assertTrue("Winners should all have equal scores.",
				match.getTotalScore(winners[0]) == match.getTotalScore(winners[1])
				&& match.getTotalScore(winners[1]) == match.getTotalScore(winners[2]));
		assertTrue("Losers should all have equal scores.",
				match.getTotalScore(losers[0]) == match.getTotalScore(losers[1])
				&& match.getTotalScore(losers[1]) == match.getTotalScore(losers[2]));
		
		assertTrue("Match should return that it is finished",
				match.isFinished());
		
		assertEquals("Winners should contain three players.",
				3, match.getLeaders().size());
		
		for (String player : winners)
			assertTrue("Winners should contain the three players added.",
				match.getLeaders().contains(player));
	}
}
