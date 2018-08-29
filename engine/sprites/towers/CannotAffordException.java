package engine.sprites.towers;

public class CannotAffordException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -963578103655786288L;

    public CannotAffordException() {
	super();
    }

    public CannotAffordException(String s) {
	super(s);
    }

    public CannotAffordException(Throwable t) {
	super(t);
    }

    public CannotAffordException(String s, Throwable t) {
	super(s, t);
    }

}
