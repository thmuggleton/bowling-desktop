package thmuggleton.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

import thmuggleton.Command;
import thmuggleton.model.Match;
import thmuggleton.model.exceptions.BowlingException;
import thmuggleton.model.exceptions.ScoreException;
import thmuggleton.view.View;

/**
 * Tests for the MatchController controller class.
 * <p>
 * Adopts BDD naming conventions suggested by
 * <a href="http://dannorth.net/introducing-bdd/">Dan North</a>.
 * <p>
 * Takes a 'mockist' TDD approach to focus on behavioural expectations 
 * since this is well-suited to testing a controller. (See
 * <a href="http://martinfowler.com/articles/mocksArentStubs.html">Mocks Aren't Stubs</a>).
 * <p>
 * However, to avoid coupling the test too closely to the implementation,
 * looser expectations have been set where these do not affect the central
 * purpose of the test.
 * 
 * @author Thomas Muggleton
 */
@RunWith(JMockit.class) 
public class MatchControllerTest {

	@Tested private MatchController controller;
	@Injectable private Match model;
	@Injectable private View view;
	
	/**
	 * Tests that the match controller correctly adds a valid new player.
	 */
	@Test
	public void shouldCorrectlyAddValidPlayer(
			@Mocked final ActionEvent mockEvent) {
		
		/* **************
		 *  Record phase
		 * **************/
		String testName = "Test name";
		
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.ADD_PLAYER;
			
			view.getNewPlayerName();
			result = testName;
			
			model.addPlayer(testName);
			view.addPlayer(testName);
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the controller correctly adds a valid score.
	 */
	@Test
	public void shouldCorrectlyAddScoreForValidScoreAndValidPlayer(
			@Mocked final ActionEvent mockEvent) {
		
		/* **************
		 *  Record phase
		 * **************/
		final int testScore = 10;
		
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.ADD_SCORE;
			
			// Get data from view
			view.getScoreEntered();
			result = testScore;
			
			// Update model
			model.addScore(testScore);
			
			// Disable addition of further players
			view.disableFurtherPlayers();
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the controller displays an error dialog if model throws an exception
	 * when adding a new player.
	 * 
	 * @param optionPane
	 *            ensures that showMessageDialog is called on a mock object to
	 *            make test more efficient and reduce the need for additional
	 *            test stubs.
	 */
	@Test
	public void shouldDisplayErrorDialogForInvalidAddPlayer(
			@Mocked final ActionEvent mockEvent,
			@Mocked JOptionPane optionPane) {
		
		/* **************
		 *  Record phase
		 * **************/
		final String testName = "Test Name";
		final String exceptionMessage = "Test exception";
		
		// Set expectations for method calls
		new StrictExpectations () {{
			
			// Set expectations for mock add player event
			mockEvent.getActionCommand();
			result = Command.ADD_PLAYER;
			
			// Get data from view
			view.getNewPlayerName();
			result = testName;
			
			// Update model which throws exception
			model.addPlayer(testName);
			result = new BowlingException(exceptionMessage);
			
			// Display error in response to exception
			view.getWindow();
			JOptionPane.showMessageDialog((Component) any, exceptionMessage, anyString, anyInt);
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	
	/**
	 * Tests that the controller displays an error dialog if an invalid score is entered.
	 * 
	 * @param optionPane
	 *            ensures that showMessageDialog is called on a mock object to
	 *            make test more efficient and reduce the need for additional
	 *            test stubs.
	 */
	@Test
	public void shouldDisplayErrorDialogForInvalidScore(
			@Mocked final ActionEvent mockEvent,
			@Mocked JOptionPane optionPane) {
		
		/* **************
		 *  Record phase
		 * **************/
		final String exceptionMessage = "Test exception";
		final int testScore = 10;
		
		// Set expectations for method calls
		new StrictExpectations() {{
			
			// Set expectations for mock add score event
			mockEvent.getActionCommand();
			result = Command.ADD_SCORE;
		
			// Get data from view
			view.getScoreEntered();
			result = testScore;
			
			// Update model which throws exception
			model.addScore(testScore);
			result = new ScoreException(exceptionMessage);
			
			// Display error in response to exception
			JOptionPane.showMessageDialog(view.getWindow(), exceptionMessage, anyString, anyInt);
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the controller displays an error dialog if a score is added which
	 * violates the rules of a bowling match.
	 * 
	 * @param optionPane
	 *            ensures that showMessageDialog is called on a mock object to
	 *            make test more efficient and reduce the need for additional
	 *            test stubs.
	 */
	@Test
	public void shouldDisplayErrorDialogWhenScoreIsAddedToCompletedGame(
			@Mocked final ActionEvent mockEvent,
			@Mocked final JOptionPane optionPane) {
		
		/* **************
		 *  Record phase
		 * **************/
		final String exceptionMessage = "Test exception";
		final int testScore = 10;
		
		// Set expectations for method calls
		new StrictExpectations () {{
			
			// Set expectations for mock add score event
			mockEvent.getActionCommand();
			result = Command.ADD_SCORE;			
			
			// Get data from view
			view.getScoreEntered();
			result = testScore;
			
			// Update model which throws exception
			model.addScore(testScore);
			result = new BowlingException(exceptionMessage);
			
			// Display error in response to exception
			view.getWindow();
			JOptionPane.showMessageDialog((Component) any, exceptionMessage, anyString, anyInt);
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the controller clears the model and view if no match is in
	 * progress and the user tries to create a new match.
	 */
	@Test
	public void shouldClearModelAndViewForNewMatch(@Mocked final ActionEvent mockEvent) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.NEW_MATCH;
			
			model.isFinished();
			result = true;
			
			view.clear();
			model.clear();
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the controller prompts the user before clearing the model and
	 * view if a match is in progress and the user tries to create a new match.
	 */
	@Test
	public void shouldPromptUserBeforeClearingMatchInProgress(
			@Mocked final ActionEvent mockEvent,
			@Mocked final JOptionPane mockedOptionPane) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.NEW_MATCH;
			
			model.isFinished();
			result = false;
			
			JOptionPane.showConfirmDialog(view.getWindow(), anyString,
					anyString, JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			result = JOptionPane.YES_OPTION;
			
			view.clear();
			model.clear();
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the controller does not clear the model and view if a match is
	 * in progress and the user selects not to create a new match when prompted.
	 */
	@Test
	public void shouldNotClearMatchInProgressIfUserChoosesNoOption(
			@Mocked final ActionEvent mockEvent,
			@Mocked final JOptionPane mockedOptionPane) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.NEW_MATCH;
			model.isFinished();
			result = false;
			
			JOptionPane.showConfirmDialog(view.getWindow(), anyString,
					anyString, JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			result = JOptionPane.NO_OPTION;
			
			view.clear();
			times = 0;
			
			model.clear();
			times = 0;
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the main controller retrieves the current leaders from the
	 * model and highlights these in the view when it receives a state change
	 * event from the model.
	 */
	@Test
	public void shouldRetrieveAndHighlightWinnersWhenModelStateChanges(
			@Mocked final ChangeEvent mockEvent) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			view.highlightLeaders(model.getLeaders());
			
			model.isFinished();
			result = false;
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.stateChanged(mockEvent);
	}
	
	/**
	 * Tests that the main controller displays a dialog to the user indicating
	 * the match winners when it receives a state change event from the model
	 * and the match is finished.
	 */
	@Test
	public void shouldDisplayWinnerDialogWhenModelStateChangesAndMatchIsOver(
			@Mocked final ChangeEvent mockEvent,
			@Mocked final JOptionPane mockedOptionPane) {
		
		/* **************
		 *  Record phase
		 * **************/
		final Set<String> testWinners = new HashSet<String>();
		testWinners.add("Alice");
		testWinners.add("Bob");
		testWinners.add("Charlie");
		
		new StrictExpectations() {{
			model.getLeaders();
			result = testWinners;
			
			view.highlightLeaders(testWinners);
			
			model.isFinished();
			result = true;
			
			view.setMatchFinished();
			
			JOptionPane.showMessageDialog(view.getWindow(), anyString,
					anyString, JOptionPane.INFORMATION_MESSAGE, (Icon) any);
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.stateChanged(mockEvent);
	}
}
