package thmuggleton.model.exceptions;

/**
 * Defines an exception that is thrown if an action violates the rules of
 * ten-pin bowling.
 * 
 * @author Thomas Muggleton
 */
public class BowlingException extends RuntimeException {

	/**
	 * Auto-generated serial version ID.
	 */
	private static final long serialVersionUID = -8457323519090154244L;

	public BowlingException(String errorMessage) {
		super(errorMessage);
	}
}
