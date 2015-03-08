package thmuggleton.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thmuggleton.Constants;
import thmuggleton.model.Frame;
import thmuggleton.model.Match;
import thmuggleton.view.View;

/**
 * Defines a panel to display a player name and their scores.
 * 
 * @author Thomas Muggleton
 */
public class PlayerScorePanel extends JPanel implements ChangeListener {

	/**
	 * Auto-generated serial version ID. 
	 */
	private static final long serialVersionUID = -9028381822431874536L;
	
	// Fields
	private Match model;
	private JTextField playerNameField;
	private DisplayFramePanel[] frameScorePanels;
	private JLabel totalScoreLabel;
	
	/**
	 * Constructor
	 * 
	 * @param model object containing match data. 
	 * @param playerName String containing the name of the player.
	 */
	protected PlayerScorePanel(Match model, String playerName) {
		
		// Initialise fields
		this.model = model;
		
		// Create panel
		this.layoutComponents(model, playerName);
	}
	
	/**
	 * Highlights the player name with the given colour.
	 * 
	 * @param colour Color object containing the colour with which to
	 * highlight the given player.
	 */
	protected void highlightPlayer(Color colour) {
		playerNameField.setBackground(colour);		
	}
	
	/*
	 * **********************
	 *     HELPER METHODS
	 * **********************
	 */
	
	/**
	 * Creates and returns a panel on which to display a player name and their
	 * scores.
	 * 
	 * @param model object containing match data. 
	 * @param playerName String containing the name of the player.
	 */
	private void layoutComponents(Match model, String playerName) {
	
		// Layout panel so that name and scores take up full width
		this.setLayout(new BorderLayout());
		
		// Create non-editable text field for player name
		playerNameField = new JTextField(playerName,
				View.PLAYER_NAME_MAX_SIZE);
		playerNameField.setBackground(View.DEFAULT_PLAYER_COLOUR);
		playerNameField.setEditable(false);

		// Create panel for scores associated with this player
		JPanel scoresPanel = this.createScoresPanel(model, playerName);
		
		// Add name text field and scores panel to player panel
		// and add player panel to scoreboard panel
		this.add(playerNameField, BorderLayout.WEST);
		this.add(scoresPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Creates and returns a panel on which to display scores
	 * for the given player.
	 * 
	 * @param model object containing match data. 
	 * @param playerName String containing the name of the player.
	 */
	private JPanel createScoresPanel(Match model, String playerName) {
		
		// Declare total labels, including label for total
		int totalScorePanels = Constants.NUMBER_OF_FRAMES + 1;
		
		// Create panel for scores associated with a player
		JPanel allScoresPanel = new JPanel(new GridLayout(1, totalScorePanels));
		
		// Retrieve frames from model and create panels to display scores for each frame
		Frame[] frames = model.getFrames(playerName);
		frameScorePanels = new DisplayFramePanel[Constants.NUMBER_OF_FRAMES];
		
		// Create all frame score panels
		for (int i = 0; i < frames.length; i++) {
			
			// Retrieve current frame
			Frame currentFrame = frames[i];
			
			// Branch for last frame
			if (i == (Constants.NUMBER_OF_FRAMES - 1))
				frameScorePanels[i] = new DisplayFramePanel(currentFrame, Frame.LAST_FRAME);
				
			// Branch for regular frames
			else 
				frameScorePanels[i] = new DisplayFramePanel(currentFrame, Frame.REGULAR_FRAME);
			
			// Set properties for frame score panel
			frameScorePanels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			allScoresPanel.add(frameScorePanels[i]);
			
			// Set frame panel and this panel to listen for state change events
			currentFrame.addChangeListener(frameScorePanels[i]);
			currentFrame.addChangeListener(this);
		}
		
		// Add total score label to scoreboard map and panel
		totalScoreLabel = new JLabel(" ");
		totalScoreLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		allScoresPanel.add(totalScoreLabel);
		
		return allScoresPanel;
	}

	/**
	 * Reacts to change events in the model.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		// Update total score label
		totalScoreLabel.setText("" + model.getTotalScore(playerNameField.getText()));		
	}	
}
