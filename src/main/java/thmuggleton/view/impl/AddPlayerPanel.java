package thmuggleton.view.impl;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import thmuggleton.Command;
import thmuggleton.view.View;

/**
 * Defines a panel to allow users to input new players.
 * 
 * @author Thomas Muggleton
 */
public class AddPlayerPanel extends JPanel {

	/**
	 * Auto-generated serial version ID 
	 */
	private static final long serialVersionUID = 8393531429571521208L;
	
	// Fields
	private JButton addPlayerButton;
	private JTextField addPlayerTextField;

	/**
	 * Constructor; creates a panel to input new players for the bowling match.
	 * 
	 * @param controller handler for the command to add a new player.
	 * @return
	 */
	protected AddPlayerPanel(ActionListener controller) {
		
		// Create input text field
		addPlayerTextField = new JTextField(View.PLAYER_NAME_MAX_SIZE);
		addPlayerTextField.setBackground(Color.WHITE);
		addPlayerTextField.setActionCommand(Command.ADD_PLAYER);
		addPlayerTextField.addActionListener(controller);
		
		// Create input button
		addPlayerButton = new JButton(Command.ADD_PLAYER);
		addPlayerButton.setActionCommand(Command.ADD_PLAYER);
		addPlayerButton.addActionListener(controller);
		
		// Add components to panel
		this.add(addPlayerTextField);
		this.add(addPlayerButton);
	}
	
	/**
	 * Returns the String currently stored in the player name input
	 * field.  Leading and trailing whitespace is removed before 
	 * returning the input. The input field is then cleared.
	 * 
	 * @return
	 */
	protected String getEnteredName() {
		
		// Retrieve input and clear text field
		String enteredName = addPlayerTextField.getText().trim();
		addPlayerTextField.setText("");		
		
		return enteredName;
	}

	/**
	 * Enables ({@code true}) or disables ({@code false}) the input
	 * on this panel.
	 * 
	 * @param enable
	 *            boolean indicating whether input should be enabled
	 *            ({@code true}) or disabled ({@code false}).
	 */
	protected void enableInput(boolean enable) {
		addPlayerTextField.setEditable(enable);
		addPlayerButton.setEnabled(enable);
	}
}
