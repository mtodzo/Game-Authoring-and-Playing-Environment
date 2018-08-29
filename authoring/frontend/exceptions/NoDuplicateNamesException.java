package authoring.frontend.exceptions;


/**
 * Exception thrown when a user attempts to create an object with a duplicate name
 * @author SusieChoi
 *
 */
public class NoDuplicateNamesException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoDuplicateNamesException(String objectName) {
		super(objectName+" already in use. Choose another name.");
	}

}
