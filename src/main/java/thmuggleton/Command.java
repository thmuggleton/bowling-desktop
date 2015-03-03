package thmuggleton;

/**
 * Interface defining constants for application commands.
 * 
 * @author Thomas Muggleton
 */
public interface Command {

	// File menu commands
	public static final String NEW_MATCH = "New match";
	public static final String EXPORT_IMAGE = "Export scoreboard as image";
	public static final String EXIT = "Exit";
	
	// Editing commands
	public static final String ADD_PLAYER = "Add player";
	public static final String ADD_SCORE = "Add score";
	
	// Help command
	public static final String ABOUT = "About";
}
