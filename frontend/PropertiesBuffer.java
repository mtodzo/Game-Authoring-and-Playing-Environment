package frontend;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import authoring.frontend.exceptions.MissingPropertiesException;

/**
 * Class to complete error-checking when retrieving a Prompt or Error from a properties file.
 * Dependent on PropertiesReader to read in the files correctly.
 * @author Andrew Arnold
 * @author Sarahbland
 *
 */
/**
 * @author Sarahbland
 *
 */
public abstract class PropertiesBuffer {
    private final String DEFAULT_PROMPT_KEY = "PromptDNE";
    public static final String DEFAULT_LANGUAGE = "English";
    public static final String DEFAULT_NO_FILE_MESSAGE = "No language files detected!";
    private String language = "English";
    private final PropertiesReader PROP_READER;
    private final View SCREEN_MANAGER;
    private Properties myLanguageProperty;
    private Map<String, String> myLanguageMap;


    public PropertiesBuffer(String languageIn, View screenManager) {
	PROP_READER = new PropertiesReader();
	SCREEN_MANAGER = screenManager;
	myLanguageMap = new TreeMap<String,String>();
	myLanguageProperty = new Properties();
	updateLanguage(languageIn);
    }

    /**
     * Displays text for a given key if it exists, or a default prompt if it does not exist,
     * or nothing if the default prompt does not exist.
     * @param key is key of text desired
     * @return text in correct language
     */
    public String resourceDisplayText(String key) {
	if(myLanguageMap.containsKey(key)) {
	    return myLanguageMap.get(key);
	}
	else if(myLanguageMap.containsKey(DEFAULT_PROMPT_KEY)) {
	    return myLanguageMap.get(DEFAULT_PROMPT_KEY);
	}
	return ""; //TODO make this reasonable
    }

    /**
     * Updates the current language being displayed in Errors and Prompts
     * @param languageIn is new language
     */
    public void updateLanguage(String languageIn) {
	language = languageIn;
	try {
	    myLanguageProperty = PROP_READER.loadProperties(makeFilepath(language));
	} catch (MissingPropertiesException e) {
	    try {
		myLanguageProperty = PROP_READER.loadProperties(makeFilepath(DEFAULT_LANGUAGE));
	    } catch (MissingPropertiesException e1) {
		SCREEN_MANAGER.loadErrorScreen(DEFAULT_NO_FILE_MESSAGE);
	    }
	}
	myLanguageMap = PROP_READER.read(myLanguageProperty);
    }
    protected abstract String makeFilepath(String language);

}
