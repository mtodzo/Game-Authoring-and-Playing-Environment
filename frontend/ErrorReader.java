package frontend;

/**
 * Class for reading in Errors given a key from an Errors properties file.
 * @author Sarahbland
 *
 */
public class ErrorReader extends PropertiesBuffer{
    private final String DEFAULT_LANGUAGE_FILEPATH = "languages/";
    private final String DEFAULT_PROMPT_FILENAME = "/Errors.properties";
    public ErrorReader(String languageIn, View view) {
	super(languageIn,view);
    }
    @Override
    protected String makeFilepath(String language) {
	return DEFAULT_LANGUAGE_FILEPATH + language + DEFAULT_PROMPT_FILENAME;
    }

}
