package thmuggleton;

import javax.swing.UIManager;

import thmuggleton.controller.InitialisationController;

/**
 * Initialises the bowling application.
 * 
 * @author Thomas Muggleton
 */
public class App {

	public static void main(String[] args) {
		
		setLookAndFeel();
		
		// Initialise application
		new InitialisationController();	
	}

	/**
	 * Tries to set the look and feel of the application
	 * to match the native system.  Falls back to the
	 * default Java look and feel if an exception is
	 * thrown.
	 */
	private static void setLookAndFeel() {
		
		// Try to set native system look and feel
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}

		// EXCEPTION HANDLING

		catch (Exception nativeException) {
			// Set to default look and feel
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			}

			catch (Exception defaultException) {
				defaultException.printStackTrace();
			}
		}	
	}
}
