package thmuggleton.controller;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;

import thmuggleton.Command;
import thmuggleton.model.impl.MatchImpl;
import thmuggleton.view.impl.MainWindow;

/**
 * Tests for the MainController controller class.
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
public class MainControllerTest {

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();
	
	// Test class and fields;
	// @Mocked rather than @Injectable because
	// they are instantiated by the controller
	@Tested private MainController controller;
	@Mocked private MatchImpl model;
	@Mocked private MainWindow view;
	@Mocked private MatchController matchController;
	@Mocked private IOController ioController;
	
	/**
	 * Tests that the main controller correctly delegates creation of a
	 * new match when the corresponding action event is received. 
	 * 
	 * @param mockEvent
	 */
	@Test
	public void shouldDelegateCreationOfNewMatch(@Mocked final ActionEvent mockEvent) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.NEW_MATCH;
			
			matchController.createNewMatch();
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the main controller correctly delegates exporting an image of
	 * the scoreboard when the corresponding action event is received.
	 * 
	 * @param mockEvent
	 */
	@Test
	public void shouldDelegateExportScoreboardImage(@Mocked final ActionEvent mockEvent) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.EXPORT_IMAGE;
			
			ioController.exportScoreboardImage();
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the main controller terminates the application if the match is
	 * finished and the corresponding action event is received.
	 * 
	 * @param mockEvent
	 */
	@Test
	public void shouldExitWithoutPromptIfMatchIsFinishedWhenExitIsCalled(
			@Mocked final ActionEvent mockEvent) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.EXIT;
			
			model.isFinished();
			result = true;
			
			exit.expectSystemExitWithStatus(0);
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the main controller prompts the user before terminating the
	 * application if the match is not finished and the corresponding action
	 * event is received.
	 * 
	 * @param mockEvent
	 */
	@Test
	public void shouldPromptBeforeExitIfMatchIsInProgressWhenExitIsCalled(
			@Mocked final ActionEvent mockEvent,
			@Mocked final JOptionPane mockedOptionPane) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.EXIT;
			
			model.isFinished();
			result = false;
			
			JOptionPane.showConfirmDialog(view.getWindow(), anyString,
					anyString, JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			result = JOptionPane.YES_OPTION;
			
			exit.expectSystemExitWithStatus(0);
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}

	/**
	 * Tests that the main controller does not terminate the application if the
	 * user chooses not to exit after prompting when the match is not finished
	 * and the corresponding action event is received.
	 * 
	 * @param mockEvent
	 */
	@Test
	public void shouldNotExitIfMatchIsInProgressAndUserCancelsActionWhenExitIsCalled(
			@Mocked final ActionEvent mockEvent,
			@Mocked final JOptionPane mockedOptionPane) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.EXIT;
			
			model.isFinished();
			result = false;
			
			JOptionPane.showConfirmDialog(view.getWindow(), anyString,
					anyString, JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			result = JOptionPane.NO_OPTION;
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the main controller correctly delegates the addition of a new
	 * player when the corresponding action event is received.
	 * 
	 * @param mockEvent
	 */
	@Test
	public void shouldDelegateAdditionOfNewPlayer(@Mocked final ActionEvent mockEvent) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.ADD_PLAYER;
			
			matchController.addPlayer();
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.actionPerformed(mockEvent);
	}
	
	/**
	 * Tests that the main controller correctly delegates the addition of a new
	 * score when the corresponding action event is received.
	 * 
	 * @param mockEvent
	 */
	@Test
	public void shouldDelegateAdditionOfNewScore(@Mocked final ActionEvent mockEvent) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			mockEvent.getActionCommand();
			result = Command.ADD_SCORE;
			
			matchController.addScore();
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
