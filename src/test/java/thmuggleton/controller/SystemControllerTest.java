package thmuggleton.controller;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;

import thmuggleton.Command;
import thmuggleton.model.Match;
import thmuggleton.view.View;

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
public class SystemControllerTest {

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();
	
	// Test class and fields
	@Tested private SystemController controller;
	@Injectable private Match model;
	@Injectable private View view;
	
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
}
