package authoring.frontend.exceptions;

/**
 * Error thrown when a resources file is missing (used inside lambdas and
 * always wrapped by MissingPropertiesException)
 * @author SarahBland
 *
 */
public class MissingPropertiesError extends Error {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MissingPropertiesError(String missingPropertiesName) {
	super(missingPropertiesName);
}

}
