package xml;


/**
 * Provides exception that is thrown if an incorrect data container type (subclass of GameData interface) is given when writing an XML document of a certain type
 * 
 * @author Brendan Cheng 3/31/2018
 *
 */
public class BadGameDataException extends RuntimeException{

    private static final long serialVersionUID = -7370199345461366056L;
    public BadGameDataException() {
	super();
    }
    public BadGameDataException(String s) {
	super(s);
    }
    public BadGameDataException(String s, Throwable throwable) {
	super(s, throwable);
    }
    public BadGameDataException(Throwable throwable) {
	super(throwable);
    }
}
