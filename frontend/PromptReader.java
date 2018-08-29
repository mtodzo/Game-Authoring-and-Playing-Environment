package frontend;

/**
 * Class for reading in Prompts given a key from an Errors properties file.
 * @author Andrew Arnold
 *
 */
public class PromptReader extends PropertiesBuffer{
    private final String DEFAULT_LANGUAGE_FILEPATH = "languages/";
    private final String DEFAULT_PROMPT_FILENAME = "/Prompts.properties";
    public PromptReader(String languageIn, View view) {
	super(languageIn,view);
    }
    @Override
    protected String makeFilepath(String language) {
	return DEFAULT_LANGUAGE_FILEPATH + language + DEFAULT_PROMPT_FILENAME;
    }
    
    

}
