package thmuggleton.view.impl;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import thmuggleton.Command;
import thmuggleton.Constants;

/**
 * Defines a panel to allow user input
 * of scores.
 * 
 * @author Thomas Muggleton
 */
public class AddScoresPanel extends JPanel {

	/**
	 * Auto-generated serial version ID.
	 */
	private static final long serialVersionUID = 2267913999206416138L;
	
	// Fields
	private JComboBox<Integer> scoresComboBox;
	private JButton inputScoresButton;
	
	/**
	 * Constructor; creates the panel used to add scores.
	 * 
	 * @param controller handler for command to add a score to the match.
	 * @return
	 */
	protected AddScoresPanel (ActionListener controller) {
		
		// Add combo boxes to result
		this.add(this.createScoresComboBox());
		
		// Add button to input scores; disable until players are added
		inputScoresButton = new JButton(Command.ADD_SCORE);
		inputScoresButton.setActionCommand(Command.ADD_SCORE);
		inputScoresButton.addActionListener(controller);
		inputScoresButton.setEnabled(false);
		this.add(inputScoresButton);
	}
	
	/**
	 * Returns the entered score.
	 * 
	 * @return
	 */
	protected int getScore() {
		return (Integer) scoresComboBox.getSelectedItem();
	}
	
	/**
	 * Enables ({@code true}) or disables ({@code false}) the input on this
	 * panel.
	 * 
	 * @param enable
	 *            boolean indicating whether input should be enabled
	 *            ({@code true}) or disabled ({@code false}).
	 */
	protected void enableInput(boolean enable) {
		inputScoresButton.setEnabled(enable);
	}
	
	/**
	 * Creates and returns a combo box to input scores.
	 * 
	 * @return
	 */
	private JComboBox<Integer> createScoresComboBox() {
		
		// Create array of valid scores
		Integer[] scores = new Integer[Constants.TOTAL_PINS + 1];
		for (int i = 0; i < scores.length; i++) 
			scores[i] = i;
		
		scoresComboBox = new JComboBox<Integer>(scores);
		
		return scoresComboBox;
	}
}
