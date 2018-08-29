package authoring.frontend.exceptions;

/**
 * Exception thrown when a user attempts to delete the default objects provided to them.
 * @author Sarahbland
 *
 */
public class DeleteDefaultException extends Exception{

    public DeleteDefaultException(String message) {
	super(message);
    }

}
