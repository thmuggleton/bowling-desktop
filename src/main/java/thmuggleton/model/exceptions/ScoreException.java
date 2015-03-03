package thmuggleton.model.exceptions;

/**
 * Defines an exception that is thrown when an action violates the scoring rules
 * of ten-pin bowling.
 * 
 * @author Thomas Muggleton
 */
public class ScoreException extends BowlingException {

	/**
	 * Auto-generated serial version ID.
	 */
	private static final long serialVersionUID = -5859061501084031160L;

	public ScoreException(String errorMessage) {
		super(errorMessage);
	}
}
