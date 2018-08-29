package authoring.frontend.exceptions;

/**
 * Exception thrown when the user attempts to acces an object in the AuthoringModel
 * that does not exist. 
 * @author SusieChoi
 *
 */
public class ObjectNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObjectNotFoundException(String object) {
		super(object+" could not be located"); 
	}
	
}
