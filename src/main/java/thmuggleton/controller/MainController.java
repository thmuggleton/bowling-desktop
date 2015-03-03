package thmuggleton.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thmuggleton.Command;
import thmuggleton.Constants;
import thmuggleton.model.Match;
import thmuggleton.model.impl.MatchImpl;
import thmuggleton.view.View;
import thmuggleton.view.impl.MainWindow;

/**
 * Defines the main controller for the application.
 * 
 * @author Thomas Muggleton
 */
public class MainController implements ActionListener, ChangeListener {

	// Fields
	private Match model;
	private View view;
	private MatchController matchController;
	private IOController ioController;
	private boolean winnerDeclared;
	
	/**
	 * Constructor to create model and view.
	 */
	public MainController() {
		
		// Create model and view
		model = new MatchImpl();
		model.addChangeListener(this);
		view = new MainWindow(this, model);
		
		// Create sub-controllers
		matchController = new MatchController(model, view);
		ioController = new IOController(view);
	}
	
	/**
	 * Main handler method for action events.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		
		switch (command) {
		
		case (Command.NEW_MATCH) :
			winnerDeclared = false;
			matchController.createNewMatch();
			break;
		
		case (Command.EXPORT_IMAGE) :
			ioController.exportScoreboardImage();
			break;
		
		case (Command.EXIT) :
			this.exit();
			break;
			
		case (Command.ADD_PLAYER) :
			matchController.addPlayer();
			break;
			
		case(Command.ADD_SCORE) :
			matchController.addScore();
			break;
			
		case(Command.ABOUT) :
			JOptionPane.showMessageDialog(view.getWindow(),
					Constants.ABOUT_DIALOG_MESSAGE, "About",
					JOptionPane.INFORMATION_MESSAGE);
			break;
		}
	}

	/**
	 * Reacts to change events in the model.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		
		// Retrieve leaders
		Set<String> leaders = model.getLeaders();
		
		// Iterate over leader(s) to highlight in view
		view.highlightLeaders(leaders);
		
		// Display message if match is over and winner has not been declared
		if (model.isFinished() && !winnerDeclared) {
			view.setMatchFinished();
			this.displayMessageForEndOfMatch(leaders);
			this.winnerDeclared = true;
		}
	}

	/**
	 * Displays a message at the end of the match.
	 * 
	 * @param winners Set containing the winner(s) of this match.
	 */
	private void displayMessageForEndOfMatch(Set<String> winners) {
		
		// Build message
		StringBuilder winnersMessage = new StringBuilder();
		winnersMessage.append("Congratulations, ");
		
		// Create iterator over leaders
		Iterator<String> it = winners.iterator();
		
		// Branch for one winner
		if (winners.size() == 1)
			winnersMessage.append(it.next());
		
		// Branch for tied first place
		else {
			while(it.hasNext())
				winnersMessage.append(it.next() + ", ");
			
			// Remove last comma and space, second-last comma, and insert "and"
			winnersMessage.delete(winnersMessage.lastIndexOf(","), winnersMessage.length());
			winnersMessage.deleteCharAt(winnersMessage.lastIndexOf(","));
			winnersMessage.insert(winnersMessage.lastIndexOf(" "), " and");
		}
		
		winnersMessage.append(", you won!");
		
		JOptionPane.showMessageDialog(view.getWindow(),
				winnersMessage.toString(), "Congratulations!",
				JOptionPane.INFORMATION_MESSAGE, 
				this.createIcon(Constants.PATH_TO_TROPHY_IMAGE));
	}

	/**
	 * Creates an icon from the image to which the path points.
	 * 
	 * @param path
	 * @return
	 */
	private Icon createIcon(String path) {
		
		Icon result = null;
		
		// Retrieve image file resource and create icon
		URL imageURL = this.getClass().getClassLoader().getResource(path);
		if (imageURL != null)
			result = new ImageIcon(imageURL);
		else
			System.err.println("Could not locate image at " + path);
		
		return result;
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
