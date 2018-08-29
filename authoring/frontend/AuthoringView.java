/**
 * @author Sarah Bland
 * @author susiechoi
 * @author benauriemma
 * 
 * Represents View of authoring environment's MVC. 
 * Allows for screen transitions and the communication of object altering/creation to Controller. 
 */

package authoring.frontend;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.javafx.tools.packager.Log;

import authoring.AuthoringController;
import authoring.AuthoringModel;
import authoring.frontend.exceptions.MissingPropertiesException;
import authoring.frontend.exceptions.NoDuplicateNamesException;
import authoring.frontend.exceptions.ObjectNotFoundException;
import engine.path.Path;
import frontend.PropertiesReader;
import frontend.Screen;
import frontend.StageManager;
import frontend.View;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.GridPane;
import xml.BadGameDataException;
public class AuthoringView extends View {

	public static final String DEFAULT_SCREENFLOW_FILEPATH = "src/frontend/ScreenFlow.properties";
	public static final String DEFAULT_ERROR_FILEPATH_BEGINNING = "languages/";
	public static final String DEFAULT_ERROR_FILEPATH_END = "/Errors.properties";
	public static final String DEFAULT_AUTHORING_CSS = "styling/GameAuthoringSpecific.css";
	public static final String DEFAULT_LANGUAGE = "English";
	public static final String DEFAULT_THEME_IDENTIFIER = "myGameTheme";
	public static final String DEFAULT_SETTINGS_OBJ_NAME = "Settings"; 
	public static final String DEFAULT_BACK_SCREENFLOW_KEY = "Back"; 
	public static final String DEFAULT_NOOBJECTERROR_KEY = "NoObject"; 
	public static final String DEFAULT_DUPLICATE_ERROR_KEY = "NoDuplicateNames"; 
	public static final String DEFAULT_NOIMAGEERROR_KEY = "NoImageFile"; 

	private StageManager myStageManager; 
	private PropertiesReader myPropertiesReader;
	private AuthoringController myController; 
	private String myCurrentCSS;
	private int myLevel; 
	private GridPane myGrid;
	private BooleanProperty myCSSChanged;
	private String myTheme; 

	public AuthoringView(StageManager stageManager, String languageIn, AuthoringController controller) {
		super(stageManager, languageIn, controller);
		myGrid = new GridPane(); 
		myPropertiesReader = new PropertiesReader();
		myStageManager = stageManager; 
		myController = controller; 
		myCurrentCSS = DEFAULT_AUTHORING_CSS;
		myCSSChanged = new SimpleBooleanProperty(false);
	}


	/**
	 * Sets the AuthoringModel used to retrieve backend information.
	 * @param model is new Model used
	 */
	public void setModel(AuthoringModel model) {
		myController.setModel(model);
	}

	/**
	 * Loads the first authoring screen shown to user (currently StartScreen) from which ScreenFlow
	 *  can direct further screens.
	 */
	public void loadInitialScreen() {
		myStageManager.switchScreen((new StartScreen(this)).getScreen());
	}

	protected void loadScreen(Screen screen) {
		myStageManager.switchScreen(screen.getScreen());
	}
	/**
	 * @return current CSS filepath
	 */
	protected String getCurrentCSS() {
		return myCurrentCSS;
	}

	protected void setCurrentCSS(String css) {
		myCurrentCSS = css; 
		myCSSChanged.set(!myCSSChanged.get());
	}
	protected void setWaveTime(int waveNumber, int time) {
		try {
			myController.setWaveTime(getLevel(), waveNumber, time);
		}
		catch(ObjectNotFoundException e) {
			Log.debug(e);
			loadErrorScreen("NoObject");
		}
	}
	protected void addWaveEnemy(int level, Path path, int waveNumber, String enemyKey, int amount) {
		try {

			myController.addWaveEnemy(level, path, waveNumber, enemyKey, amount);
		}
		catch(ObjectNotFoundException e) {
			Log.debug(e);
			loadErrorScreen(DEFAULT_NOOBJECTERROR_KEY);
		}
	}

	protected void goBackFrom(String id) {
		goForwardFrom(id+DEFAULT_BACK_SCREENFLOW_KEY);
	}


	protected void goForwardFrom(String id) {
		goForwardFrom(id, "");
	}
	
	
	/**
	 * Controls Screenflow from a certain screen based on Reflection and
	 * the Screenflow properties file
	 * @param id is name of current class (key to properties file)
	 * @param name is List of Object Names needed to construct next screen
	 * (i.e. an AdjustTowerScreen would need the name of the Tower)
	 */
	protected void goForwardFrom(String id, List<String> name) {
		try {
			String nextScreenClass = myPropertiesReader.findVal(DEFAULT_SCREENFLOW_FILEPATH, id);
			Class<?> clazz = Class.forName(nextScreenClass);
			Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
			if(constructor.getParameterTypes().length == 2) {
				if(constructor.getParameterTypes()[1].equals(ArrayList.class)) {
					AuthoringScreen nextScreen = (AuthoringScreen) constructor.newInstance(this, name);
					myStageManager.switchScreen(nextScreen.getScreen());
				}
				else if(constructor.getParameterTypes()[1].equals(String.class)) {
					AuthoringScreen nextScreen = (AuthoringScreen) constructor.newInstance(this, name.get(0));
					myStageManager.switchScreen(nextScreen.getScreen());
				}
				else if(constructor.getParameterTypes()[0].equals(StageManager.class)){
					Screen nextScreen = (Screen) constructor.newInstance(myStageManager, this);
					myStageManager.switchScreen(nextScreen.getScreen());
				}
			}
			else if(constructor.getParameterTypes()[0].equals(AuthoringView.class)) {
//				System.out.println(clazz.getSimpleName());
				AuthoringScreen nextScreen = (AuthoringScreen) constructor.newInstance(this);
				myStageManager.switchScreen(nextScreen.getScreen());
			}
			else {
				throw new MissingPropertiesException("");
			}

		}
		catch(MissingPropertiesException | ClassNotFoundException | InvocationTargetException
				| IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		    	Log.debug(e);	
			loadErrorScreen("NoScreenFlow");
		}
	}
	
	/**
	 * @see goForwardFrom above
	 * @param id is ID of current screen
	 * @param name is name necessary to construct next screen
	 */
	public void goForwardFrom(String id, String name) {
		ArrayList<String> parameterList= new ArrayList<>();
		parameterList.add(name);
		goForwardFrom(id,  parameterList);
	}

	protected void makePath(GridPane grid, List<List<Point>> coordinates, HashMap<String, List<Point>> imageCoordinates, String backgroundImage, String pathImage, String startImage, String endImage, int pathSize, int width, int height, boolean transparent) throws ObjectNotFoundException {
		myController.makePath(myLevel, grid, coordinates, imageCoordinates, backgroundImage, pathImage, startImage, endImage, pathSize, width, height, transparent);
	}

	/**
	 * Method through which information can be sent to instantiate or edit the Resources object in Authoring Model;
	 */

	protected void makeResources(String gameName, double startingHealth, double starting$, String instructions, String css, String backgroundMusic, String levelWinSound, String levelLossSound) {
		myController.makeResources(gameName, startingHealth, starting$, css, getTheme(), instructions, backgroundMusic, levelWinSound, levelLossSound);
	}

	/**
	 * Method through which information can be retrieved from AuthoringModel re: the current objects of a given type are available for editing
	 */
	protected List<String> getCurrentObjectOptions(String objectType) {
		List<String> availableObjectOptions = new ArrayList<>(); 
		try {
			availableObjectOptions = myController.getCurrentObjectOptions(myLevel, objectType);
		} catch (ObjectNotFoundException e) {
			Log.debug(e);	
			loadErrorScreen(DEFAULT_NOOBJECTERROR_KEY);
		}
		return availableObjectOptions; 
	}

	protected Object getObjectAttribute(String objectType, String attribute) {
		return getObjectAttribute(objectType, "", attribute);
	}

	/**
	 * Method through which information about object fields can be requested
	 * Invoked when populating authoring frontend screens used to edit existing objects
	 */
	protected Object getObjectAttribute(String objectType, String objectName, String attribute) {
		Object returnedObjectAttribute = ""; 
		try {
			returnedObjectAttribute = myController.getObjectAttribute(myLevel, objectType, objectName, attribute);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ObjectNotFoundException e) {
			Log.debug(e);	
			loadErrorScreen(DEFAULT_NOOBJECTERROR_KEY);
		} 
		return returnedObjectAttribute; 
	}

	/**
	 * Enumerates the current level that the user is editing 
	 * Useful when creating new objects so that Model may organize objects by the level in which they become accessible to the user 
	 * @param level - the level that the user has selected to edit
	 */
	protected void setLevel(int level) {
		myLevel = level; 
	}

	/**
	 * Returns the StageManager object used by the game to switch the Screens
	 * displayed on the Stage.
	 * 
	 * @return StageManager: the StageManager object in the game
	 */
	public StageManager getStageManager() {
		return myStageManager;
	}

	protected void addNewLevel() {
	    	try {
		int newLevel = myController.addNewLevel(); 
		setLevel(newLevel);
	    	}
	    	catch(ObjectNotFoundException e) {
	    	    loadErrorScreen("NoObject");
	    	}
	}

	protected List<String> getLevels() {
		return myController.getLevels(); 
	}

	protected void autogenerateLevel() throws MissingPropertiesException {
		int newLevel = myController.autogenerateLevel(); 
		setLevel(newLevel); 
	}

	protected int getLevel() {
		return myLevel; 
	}

	/**
	 * Changes game name, useful in displaying game name to user on main screen
	 * @param gameName
	 */
	public void setGameName(String gameName) {
		myController.setGameName(gameName);
	}


	protected Map<String, Integer> getEnemyNameToNumberMap(int level, Path path, int waveNumber) { 
		try {
			return myController.getEnemyNameToNumberMap(level, path, waveNumber);
		}
		catch(ObjectNotFoundException e) {
			Log.debug(e);	
			loadErrorAlert(DEFAULT_NOOBJECTERROR_KEY);
		}
		return new HashMap<>();

	}

	protected Integer getHighestWaveNumber(int level) {
		try {
			return myController.getHighestWaveNumber(level);
		}
		catch(ObjectNotFoundException e) {
			Log.debug(e);
			loadErrorScreen(DEFAULT_NOOBJECTERROR_KEY);
		}
		return 1;
	}

	protected void writeToFile() throws BadGameDataException, IOException {
		try {
			myController.writeToFile();
		} catch (ObjectNotFoundException e) {
			Log.debug(e);
			loadErrorScreen(DEFAULT_NOOBJECTERROR_KEY);
		} 
	}

	protected void readFromFile(String name) throws MissingPropertiesException {
		myController.setModel(name);
	}


	protected BooleanProperty cssChangedProperty() {
		return myCSSChanged; 
	}

	protected String getGameName() {
		return myController.getGameName();
	}

	protected void deleteObject(String objectType, String objectName) {
		myController.deleteObject(myLevel, objectType, objectName);
	}

	//
	//	public void makeTower(String name) throws NumberFormatException, FileNotFoundException, ObjectNotFoundException {
	//		try {
	//			myController.makeTower(myLevel, name);
	//		} catch (MissingPropertiesException e) {
	//		    Log.debug(e);	
	//		    loadErrorAlert("NoImageFile");
	//		} catch (NoDuplicateNamesException e) {
	//		    Log.debug(e);	
	//		    loadErrorAlert("NoDuplicateNames");
	//		} 
	//	}
	//	
	//	public void makeEnemy(String name) throws NumberFormatException, FileNotFoundException, ObjectNotFoundException {
	//		try {
	//			myController.makeEnemy(myLevel, name);
	//		} catch (MissingPropertiesException e) {
	//		    Log.debug(e);	
	//		    loadErrorAlert("NoImageFile");
	//		} catch (NoDuplicateNamesException e) {
	//		    Log.debug(e);	
	//		    loadErrorAlert("NoDuplicateNames");
	//		} 
	//	}

	protected void makeSprite(String objectType, String name) throws NumberFormatException, FileNotFoundException, ObjectNotFoundException {
		try {
			myController.makeSprite(objectType, myLevel, name);
		} catch (MissingPropertiesException e) {
			Log.debug(e);	
			loadErrorAlert(DEFAULT_NOIMAGEERROR_KEY);
		} catch (NoDuplicateNamesException e) {
			Log.debug(e);	
			loadErrorAlert(DEFAULT_DUPLICATE_ERROR_KEY);
		} 
	}

	protected void setObjectAttribute(String objectType, String attribute, Object attributeValue) {
		setObjectAttribute(objectType, "", attribute, attributeValue);
	}
	
	protected void setObjectAttribute(String objectType, String name, String attribute, Object attributeValue) {
		try {
			myController.setObjectAttribute(myLevel, objectType, name, attribute, attributeValue);
		} catch (IllegalArgumentException | IllegalAccessException | ObjectNotFoundException e) {
			Log.debug(e);	
			loadErrorScreen(DEFAULT_NOOBJECTERROR_KEY);
		}
	}

	protected void createProperty(String objectType, String name, String propertyName, List<Double> attributes) {
		try {
			try {
				myController.createProperty(myLevel, objectType, name, propertyName, attributes);
			} catch (MissingPropertiesException e) {
				loadErrorAlert("NoObject");
			}
		} catch (IllegalArgumentException | IllegalAccessException | ObjectNotFoundException e) {
			Log.debug(e);	
			loadErrorScreen(DEFAULT_NOOBJECTERROR_KEY);
		}
	}

	/**
	 * Sets game theme, useful in determining which image options to display to user for sprites
	 * @param selectedTheme
	 */
	public void setTheme(String selectedTheme) {
		myTheme = selectedTheme; 
		setObjectAttribute(DEFAULT_SETTINGS_OBJ_NAME, DEFAULT_THEME_IDENTIFIER, myTheme);
	}

	protected String getTheme() {
		if (myTheme == null) {
			try {
				myTheme = (String) myController.getObjectAttribute(1, DEFAULT_SETTINGS_OBJ_NAME, "", DEFAULT_THEME_IDENTIFIER);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ObjectNotFoundException e) {
				Log.debug(e);	
				loadErrorAlert("NoFile");
			}
		}
		return myTheme; 
	}

	protected Path getPathWithStartingPoint(int level, Point point) {
		try {
			return myController.getPathWithStartingPoint(level, point);
		}
		catch(ObjectNotFoundException | MissingPropertiesException e) {
			Log.debug(e);
			loadErrorScreen(DEFAULT_NOOBJECTERROR_KEY);
		}
		return null;
	}

}
