package xml;


/**
 * Exception that is thrown if XMLFactory fails to generate an XMLReader/XMLWriter by reflection
 * 
 * @author Brendan Cheng 4/29/2018
 *
 */
public class ReflectionErrorException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    public ReflectionErrorException() {
	super();
    }
    public ReflectionErrorException(String s) {
	super(s);
    }
    public ReflectionErrorException(String s, Throwable throwable) {
	super(s, throwable);
    }
    public ReflectionErrorException(Throwable throwable) {
	super(throwable);
    }
}
