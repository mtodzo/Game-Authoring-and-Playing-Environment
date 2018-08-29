package frontend;
import java.util.logging.Logger;
import javafx.scene.Parent;

/**
 * @author transition to abstract class & error checking - Sarah Bland
 * @author all other populated methods - Susie Choi 
 * Abstract Class used for generating Screen objects, which represent the entirety of the view 
 * displayed to the user. 
 */

public abstract class Screen {

	public static final String DEFAULT_SHARED_STYLESHEET = "styling/SharedStyling.css";
	public static final String DEFAULT_FILE_ERRORMESSAGE = "Missing specified language property files.";
	public static final String DEFAULT_CONSTANTS_FILEPATH = "src/frontend/Constants.properties";
	public static final String DEFAULT_PROMPT = "";
	//private AuthoringView myView; 
	private String myStylesheet; 
	private Parent myRoot;
	private UIFactory myUIFactory;
	private PropertiesReader myPropertiesReader;
	private Logger myLogger;
	protected Screen() {
		myUIFactory = new UIFactory();
		myPropertiesReader = new PropertiesReader();
		//myLogger = Logger.getLogger(this.getClass().getSimpleName());
	}
	protected Logger getLogger() {
	    return myLogger;
	}

	protected UIFactory getUIFactory() {
		return myUIFactory;
	}

	protected void setStyleSheet(String stylesheetString) {
		myStylesheet = stylesheetString;
		replaceStyle(myStylesheet);
	}

	private void replaceStyle(String stylesheet) {
		if (myRoot != null) {
			myRoot.getStylesheets().remove(myRoot.getStylesheets().size()-1);
			myRoot.getStylesheets().add(stylesheet);
		}
	}

	/**
	 * Creates & returns the styled Screen
	 */
	protected Parent makeScreen() {
		myRoot = makeScreenWithoutStyling();
		applyDefaultStyling();
		applyStyle(myStylesheet);
		return myRoot; 
	}
	
	/**
	 * Creates all given UI elements of a given screen (without styling with CSS)
	 * @return Parent holding as children all UI elements on Screen
	 */
	public abstract Parent makeScreenWithoutStyling();

	private void applyDefaultStyling() {
		if (myRoot != null) {
			myRoot.getStylesheets().add(DEFAULT_SHARED_STYLESHEET);
			myRoot.getStylesheets().add("https://fonts.googleapis.com/css?family=Quicksand");
			myRoot.getStylesheets().add("https://fonts.googleapis.com/css?family=Open+Sans");
		}
	}

	public void applyStyle(String stylesheet) {
		if (myRoot != null && stylesheet != null) {
			myRoot.getStylesheets().add(stylesheet);
		}
	}

	/**
	 * Returns the Scene object to be loaded on the screen
	 */
	public Parent getScreen() {
		if (myRoot == null) {
			myRoot = makeScreen(); 
		}
		return myRoot;
	}
	protected Parent getRoot() {
		return myRoot;
	}
	protected abstract View getView();
	protected PropertiesReader getPropertiesReader() {
		return myPropertiesReader;
	}
}


