package authoring.frontend.exceptions;

/**
 * Exception thrown when a resources file is missing
 * @author SusieChoi
 *
 */
public class MissingPropertiesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MissingPropertiesException(String missingPropertiesName) {
		super(missingPropertiesName+" was not found.");
	}

}
