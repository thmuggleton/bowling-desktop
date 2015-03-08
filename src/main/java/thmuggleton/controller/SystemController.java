package thmuggleton.controller;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import thmuggleton.Command;
import thmuggleton.Constants;
import thmuggleton.model.Match;
import thmuggleton.view.View;

/**
 * Defines the controller that handles system-related events, such as exit calls.
 * 
 * @author Thomas Muggleton
 */
public class SystemController implements Controller {

	// Fields
	private Match model;
	private View view;
	
	/**
	 * Constructor
	 */
	public SystemController(Match model) {
		this.model = model;
	}
	
	/**
	 * Sets the given view for this controller.
	 */
	@Override
	public void setView(View view) {
		this.view = view;
	}
	
	/**
	 * Main handler method for action events.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		
		switch (command) {
		
		case(Command.ABOUT) :
			JOptionPane.showMessageDialog(view.getWindow(),
					Constants.ABOUT_DIALOG_MESSAGE, "About",
					JOptionPane.INFORMATION_MESSAGE);
			break;
		
		case (Command.EXIT) :
			this.exit();
			break;
		}
	}
	
	/**
	 * Defines behaviour when the user exits the application;
	 * if a match is in progress, they are prompted before the
	 * application terminates.
	 */
	private void exit() {
		
		// Prompt user if match is in progress
		if(!model.isFinished()) {
			int option = JOptionPane.showConfirmDialog(view.getWindow(),
					String.format("The match is still in progress;%nare you sure you want to quit?"),
					"Match in progress", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			
			// If user presses 'No', return and do not exit
			if (option != JOptionPane.YES_OPTION)
				return;
		}
		
		// Quit the application with normal status code
		System.exit(0);
	}
}
