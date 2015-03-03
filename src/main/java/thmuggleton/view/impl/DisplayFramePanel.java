package thmuggleton.view.impl;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thmuggleton.Constants;
import thmuggleton.model.Frame;
import thmuggleton.view.View;

/**
 * Defines a panel on which to display scores.
 * 
 * @author Thomas Muggleton
 */
public class DisplayFramePanel extends JPanel implements ChangeListener {

	/**
	 * Auto-generated serial version ID. 
	 */
	private static final long serialVersionUID = -2340594802764839409L;
	
	// Fields
	private Frame model;
	private JLabel[] shots;
	private JLabel total;
	
	/**
	 * Constructor: lays out components to display scores.
	 * 
	 * @param numShots defines how many shots will be
	 * displayed on this panel.
	 * @param frame is the model object associated with this
	 * score panel.
	 */
	public DisplayFramePanel(Frame frame, int numShots) {
		
		this.model = frame;
		
		this.setLayout(new GridLayout(2,1));
		
		// Create top panel for individual shots
		JPanel shotsPanel = new JPanel(new GridLayout(1, numShots));
		
		// Add labels for individual shots
		shots = new JLabel[numShots];
		
		for (int i = 0; i < numShots; i++) {
			shots[i] = new JLabel("   ");
			shots[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			shotsPanel.add(shots[i]);
		}
		
		// Add label for total for this frame
		total = new JLabel();
		
		// Add top panel and total label to this panel  
		this.add(shotsPanel);
		this.add(total);
	}
	
	/**
	 * Displays all scores stored in the given frame if these have been set.
	 * 
	 * @param frame
	 */
	private void refreshScores() {
		
		// Set labels for individual scores 
		int[] scores = model.getScores();
		
		// Iterate over all scores for frame and display if set
		for (int i = 0; i < scores.length; i++) {
			int score = scores[i];
			
			// Set spare: second shot, total is 10 and first shot
			// not a strike; the latter is necessary for last frame
			if (i == 1 && (score + scores[0]) == Constants.TOTAL_PINS
					&& scores[0] != Constants.TOTAL_PINS)
				shots[i].setText(View.SPARE_CHARACTER);
			
			// Set strike
			else if (score == Constants.TOTAL_PINS)
				shots[i].setText(View.STRIKE_CHARACTER);
			
			// Set normal score
			else if (score != Frame.SCORE_UNSET)
				shots[i].setText("" + score);
		}
		
		// Set label for total for frame, if set
		int totalScore = model.getTotal();
		
		if (totalScore != Frame.SCORE_UNSET)
			total.setText("" + totalScore);
	}

	/**
	 * Reacts to change events in the model.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		this.refreshScores();		
	}
}
