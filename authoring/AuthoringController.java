/**
 * 
 * @author susiechoi 
 * @author Ben Hodgson 4/9/18
 * @author benauriemma
 * @author Katherine Van Dyk
 *
 * Class that handles mediating creation of authoring environment objects (towers, enemies, path). 
 * Represents Controller in MVC of the authoring environment. 
 * 
 */

package authoring;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import authoring.frontend.AuthoringView;
import authoring.frontend.exceptions.DeleteDefaultException;
import authoring.frontend.exceptions.MissingPropertiesException;
import authoring.frontend.exceptions.NoDuplicateNamesException;
import authoring.frontend.exceptions.ObjectNotFoundException;
import controller.MVController;
import controller.PlayController;
import engine.level.Level;
import engine.path.Path;
import engine.sprites.enemies.Enemy;
import engine.sprites.enemies.wave.Wave;
import frontend.StageManager;
import javafx.scene.layout.GridPane;
import xml.AuthoringModelReader;
import xml.AuthoringModelWriter;
import xml.BadGameDataException;


public class AuthoringController implements MVController{

	public static final String NO_DEFAULT_OBJ_ALERT_KEY = "NoDefaultObject"; 
	public static final String DEFAULT_EDIT_BUTTON_CTRLFLOW = "Edit";
	public static final String DEFAULT_OBJNOTFOUNDEXCEPTION_ALERT = "NoObject";
	public static final String DEFAULT_NODEFAULTOBJECT_ALERT = "NoDeleteDefault"; 
	
    private AuthoringView myView; 
    private Map<String, List<Point>> myImageMap;
    private AuthoringModel myModel; 

    /**
     * Creates a new AuthoringController
     * @param stageManager contains Stage game is being authored on
     * @param languageIn is chosen language for prompts
     */
    public AuthoringController(StageManager stageManager, String languageIn) {
	myView = new AuthoringView(stageManager, languageIn, this);
	try {
	    myModel = new AuthoringModel();
	} catch (MissingPropertiesException e) {
	    myView.loadErrorScreen(NO_DEFAULT_OBJ_ALERT_KEY);
	}
	myView.setModel(myModel);
	myView.loadInitialScreen();
    }

    /**
     * Method through which information about object fields can be requested
     * Invoked when populating authoring frontend screens used to edit existing objects
     * @throws IllegalArgumentException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws NoSuchFieldException 
     * @throws ObjectNotFoundException 
     */
    public Object getObjectAttribute(int level, String objectType, String name, String attribute) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ObjectNotFoundException {
	return myModel.getObjectAttribute(level, objectType, name, attribute);
    }

    /**
     * Method through which information can be sent to instantiate or edit the Resources object in Authoring Model;
     */
    public void makeResources(String gameName, double startingHealth, double starting$, String css, String theme, String instructions, String backgroundMusic, String levelWinSound, String levelLossSound) {
	myModel.makeResources(gameName, startingHealth, starting$, css, theme, instructions, backgroundMusic, levelWinSound, levelLossSound);
    }

    // TODO
    /**
     * Method through which information can be sent to instantiate or edit a Path in Authoring Model
     * @throws ObjectNotFoundException 
     */
    public void makePath(int level, GridPane grid, List<List<Point>> coordinates, Map<String, List<Point>> imageCoordinates, String backgroundImage, String pathImage, String startImage, String endImage, int pathSize, int width, int height, boolean transparent) throws ObjectNotFoundException { 
	myModel.makePath(level, coordinates, imageCoordinates, backgroundImage, pathImage, startImage, endImage, pathSize, width, height, transparent); 
	myImageMap = imageCoordinates;
    }

    /**
     * Method that wraps Model method to return a Path object given its level and 
     * int identifier
     * @param name is int identifier of this path
     * @param level is current level
     * @return Path object corresponding to this level/id
     * @throws ObjectNotFoundException
     */
    public Path getPathFromName(int name, int level) throws ObjectNotFoundException {
	return myModel.getPathFromName(name, level);
    }

    /**
     * Method through which information can be retrieved from AuthoringMOdel re: the current objects of a given type are available for editing
     * @throws ObjectNotFoundException 
     */
    public List<String> getCurrentObjectOptions(int level, String objectType) throws ObjectNotFoundException {
	return myModel.getCurrentObjectOptions(level, objectType);
    }

    /**
     * Invokes a Model method that adds a new level to the authored game, 
     * based on the previous level that the user has created
     * (or the default level if the user has not customized any level) 
     * @return integer of new level created
     */
    public int addNewLevel() throws ObjectNotFoundException{
	return myModel.addNewLevel(); 
    }

    /**
     * Invokes a Model method that returns a list of level numbers 
     * as strings currently in the authored game
     * Useful in displaying the levels available for edit by the user
     * @return List<String>: A list of level numbers as strings
     */
    public List<String> getLevels() {
	return myModel.getLevels(); 
    }

    /**
     * Wraps Model method to autogenerate a level
     * @return int level number of new level
     * @throws MissingPropertiesException 
     */
    public int autogenerateLevel() throws MissingPropertiesException {
	return myModel.autogenerateLevel(); 
    }

    /**
     * Edits/adds an enemy composition in a specified wave
     * 
     * @param level: the level the wave pertains to
     * @param path: the path that specifies the wave
     * @param waveNumber: the wave number
     * @param enemyKey: the unique String name that identifies the enemy
     * @param newAmount: the new amount of the specified enemy to put in the wave
     * @throws ObjectNotFoundException: thrown if the level isn't found
     */
    public void addWaveEnemy(int level, Path path, int waveNumber, String enemyKey, int newAmount) throws ObjectNotFoundException {
	Level thisLevel = myModel.getLevel(level);
	Enemy thisEnemy = thisLevel.getEnemy(enemyKey);
	Wave thisWave;
	if(!thisLevel.containsWaveNumber(waveNumber)) {
	    thisWave = new Wave();
	    thisLevel.addWave(thisWave);
	}
	else{
	    thisWave = thisLevel.getWave(waveNumber);

	}
	thisWave.addEnemy(thisEnemy, path, newAmount);
    }

    /**
     * Returns the number of waves in a specified level that belong to a specified
     * path object.
     * 
     * @param level: the current level
     * @param path: the path that the waves belong to
     * @return int: the number of waves that belong to the path in the level
     * @throws ObjectNotFoundException: thrown if the level isn't found
     */
    public int wavesNumber(int level, Path path) throws ObjectNotFoundException {
	Level thisLevel = myModel.getLevel(level);
	List<Wave> levelWaves = thisLevel.getWaves();
	return levelWaves.size();
    }

    /**
     * Gets a Map of the current Enemy names to the number of that enemy for a given wave/path/
     * level combination
     * @param level of wave desired
     * @param path of wave desired
     * @param waveNumber of wave desired
     * @return Map of enemy names to the number of those enemies going down this path in this wave
     * @throws ObjectNotFoundException
     */
    public Map<String, Integer> getEnemyNameToNumberMap(int level, Path path, int waveNumber) throws ObjectNotFoundException {
	Level currentLevel = myModel.getLevel(level);
	//TODO: issue here - if there is no wave yet then need to make it first!
	if(!currentLevel.containsWaveNumber(waveNumber) || currentLevel.getWaves().get(waveNumber).getUnmodifiableEnemies(path)==null) {
	    return new HashMap<String, Integer>();
	}
	Map<Enemy, Integer> enemyMap = currentLevel.getWaves().get(waveNumber).getUnmodifiableEnemies(path);
	Map<String,Integer> enemyNameMap = new HashMap<>();
	for(Enemy enemy : enemyMap.keySet()) {
	    enemyNameMap.put(enemy.getName(), enemyMap.get(enemy));
	}
	return enemyNameMap;   
    }

    /**
     * Returns a List of the enemies contained in the level 
     * 
     * @param level: the current level
     * @return List<String>: a list of the unique String names for each enemy in the
     * level.
     * @throws ObjectNotFoundException: thrown if the level isn't found
     */
    public List<String> levelEnemies(int level) throws ObjectNotFoundException {
	Level thisLevel = myModel.getLevel(level);
	return thisLevel.getAllEnemies();
    }

    /**
     * Sets name of the game per user entry
     * @param gameName  is new game name
     */
    public void setGameName(String gameName) {
	myModel.setGameName(gameName);
    }	

    /**
     * Gets current name of the game
     * @return current name of the game
     */
    public String getGameName() {
	return myModel.getGameName(); 
    }
    /**
     * Sets the current AuthoringModel being used based on an XML file
     * @param gameName is name of game/XML file being loaded in
     * @throws MissingPropertiesException 
     */
    public void setModel(String gameName) throws MissingPropertiesException {
	myView.setGameName(gameName);
	AuthoringModelReader reader = new AuthoringModelReader();
	myModel = new AuthoringModel(reader.createModel(gameName));
	myView.setModel(myModel);
	myView.goForwardFrom(this.getClass().getSimpleName()+DEFAULT_EDIT_BUTTON_CTRLFLOW, getGameName());
	
    }

    /**
     * Sets the AuthoringModel currently being used by this AuthoringController
     * (based on which game chosen from StartScreen)
     * @param model is new AuthoringModel to be used going forward
     */
    public void setModel(AuthoringModel model) {
	myModel = model;
    }

    /**
     * Returns a map of String image names to a list of Point coordinates where those 
     * images should be found on the path
     * @return image name to coordinate map
     */
    public Map<String, List<Point>> getGrid() {
	return myImageMap;
    }
    /**
     * Returns a path with a certain starting point (used for matching with Waves)
     * @param level is level of wave
     * @param point is Point object of desired starting location
     * @return Path object with that particular starting point
     * @throws ObjectNotFoundException
     */
    public Path getPathWithStartingPoint(int level, Point point) throws ObjectNotFoundException, MissingPropertiesException {
	return myModel.getPathWithStartingPoint(level, point);
    }
    /**
     * Method to retrieve the highest wave number found in a level (including all paths)
     * @param level is level desired
     * @return highest wave number found in that level
     * @throws ObjectNotFoundException
     */
    public Integer getHighestWaveNumber(int level) throws ObjectNotFoundException{
	return myModel.getHighestWaveNumber(level);
    }

    /**
     * Makes a new Sprite (Enemy, Tower, or Projectile)
     * @param objectType is type of object (Enemy, Tower, or Projectile)
     * @param level is level to be added to
     * @param name is user-specified name of sprite
     * @throws NoDuplicateNamesException
     * @throws MissingPropertiesException
     * @throws NumberFormatException
     * @throws FileNotFoundException
     * @throws ObjectNotFoundException
     */
    public void makeSprite(String objectType, int level, String name) throws NoDuplicateNamesException, MissingPropertiesException, NumberFormatException, FileNotFoundException, ObjectNotFoundException {
	myModel.makeSprite(objectType, level, name);
    }

    /**
     * Sets attribute of object through reflection
     * @param level - level number of object whose value is to be set
     * @param objectType - type of object whose value is to be set, e.g. Toewr
     * @param objectName - name of object whose value is to be set, e.g. MyNewTower
     * @param attribute - name of object attribute/field whose value is to be set, e.g. myTowerImage
     * @param attributeValue - value of attribute to be set, 
     * @throws ObjectNotFoundException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void setObjectAttribute(int level, String objectType, String name, String attribute, Object attributeValue) throws ObjectNotFoundException, IllegalArgumentException, IllegalAccessException {
	myModel.setObjectAttribute(level, objectType, name, attribute, attributeValue);
    }

    /**
     * Gives a new property to a Sprite (examples include a ClickToShoot property, etc.)
     * @param level is level Sprite is in
     * @param objectType is type of Sprite (Projectile, Enemy, or Tower)
     * @param name is user-given name of sprite
     * @param propertyName is name of Property
     * @param attributes are the Double attributes necessary to create this property
     * @throws ObjectNotFoundException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws MissingPropertiesException
     */
    public void createProperty(int level, String objectType, String name, String propertyName, List<Double> attributes) throws ObjectNotFoundException, IllegalArgumentException, IllegalAccessException, MissingPropertiesException {
	myModel.createProperty(level, objectType, name, propertyName, attributes);
    }

    /**
     * Sets time between waves based on user input
     * @param level is level containing wave
     * @param waveNumber is number of that Wave
     * @param time is time until next wave
     * @throws ObjectNotFoundException
     */
    public void setWaveTime(int level, int waveNumber, int time) throws ObjectNotFoundException{
	Level currentLevel = myModel.getLevel(level);
	if(!currentLevel.containsWaveNumber(waveNumber)) {
	    currentLevel.addWave(waveNumber);
	}
	Wave desiredWave = currentLevel.getWaves().get(waveNumber);
	desiredWave.setWaveTime(time);
    }

    /**
     * Saves an XML file with current AuthoredGame to be used for reauthoring or gameplay
     * @throws ObjectNotFoundException
     * @throws BadGameDataException
     * @throws IOException
     */
    public void writeToFile() throws ObjectNotFoundException, BadGameDataException, IOException {
	AuthoringModelWriter writer = new AuthoringModelWriter();
	writer.write(myModel.getGame(), myModel.getGameName());
    }
    
    /**
     * Method used to demo a game
     * @param StageManager contains current stage
     * @param language is current language
     * @see controller.MVController#playControllerDemo(frontend.StageManager, java.lang.String)
     */
    @Override
    public void playControllerDemo(StageManager manager, String language) throws MissingPropertiesException {
	new PlayController(manager, language,
		myModel).demoPlay(myModel.getGame());
    }
    
    /**
     * Deletes object 
     * @param level - level of object to be deleted
     * @param objectType - type of object to be deleted, e.g. Toewr
     * @param objectName - name of object to be deleted, e.g. MyNewTower
     */
    public void deleteObject(int level, String objectType, String objectName) {
	try {
	    myModel.deleteObject(level, objectType, objectName);
	}
	catch(ObjectNotFoundException e) {
	    myView.loadErrorScreen("NoObject");
	}
	catch(DeleteDefaultException e2) {
	    myView.loadErrorAlert("NoDeleteDefault");
	}
    }
}