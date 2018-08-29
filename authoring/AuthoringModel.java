/**
 * 
 * @author susiechoi 
 * @author Ben Hodgson 4/8/18
 * @author Katie Van Dyk 4/24/18
 * @author benauriemma
 * @author Sarah Bland
 * 
 * Represents the Model component of the Authoring environment's MVC. 
 * Receives input from Controller to determine what object to create/adjust
 */

package authoring;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import authoring.factory.AttributeFactory;
import authoring.factory.PropertyFactory;
import authoring.factory.SpriteFactory;
import authoring.frontend.exceptions.DeleteDefaultException;
import authoring.frontend.exceptions.MissingPropertiesException;
import authoring.frontend.exceptions.NoDuplicateNamesException;
import authoring.frontend.exceptions.ObjectNotFoundException;
import engine.builders.PathBuilder;
import engine.Settings;
import engine.builders.SettingsBuilder;
import engine.level.Level;
import engine.path.Path;
import engine.sprites.enemies.Enemy;
import engine.sprites.enemies.wave.Wave;
import engine.sprites.towers.Tower;
import frontend.PropertiesReader;

public class AuthoringModel {

    public static final String DEFAULT_SETTINGS_FILE = "default_objects/Settings.properties";
    public static final String DEFAULT_PROMPTS_FILE_KEY = "PromptsFile";
    public static final String DEFAULT_CONSTANTS_FILE_KEY = "ConstantFiles"; 
    public static final String DEFAULT_OBJECT_NAME_KEY = "DefaultObjectName"; 
    public static final String DEFAULT_GAME_NAME_KEY = "NewGame";
    public static final String DEFAULT_STARTINGHEALTH_KEY = "StartingHealth"; 
    public static final String DEFAULT_STARTINGMONEY_KEY = "StartingMoney";
    public static final String DEFAULT_STARTINGCSS_KEY = "StartingCSS";
    public static final String DEFAULT_STARTINGTHEME_KEY = "StartingTheme";
    public static final String DEFAULT_WAVENAME_SEPARATOR = " ";
    public static final int DEFAULT_FIRSTLEVEL_NUMBER = 1;

    private final GenericModel myGeneric;
    private final AuthoredGame myGame;
    private String DEFAULT_CONSTANT_FILEPATH;
    private PropertiesReader myPropertiesReader;
    private String myDefaultName; 
    private Tower myDefaultTower;
    private Enemy myDefaultEnemy;
    private Path myDefaultPath;
    private SpriteFactory spriteFactory;
    protected Map<String, List<Point>> myImageMap = new HashMap<String, List<Point>>();
    protected String myBackgroundImage = new String();
    protected List<Point> myPathCoordinates = new ArrayList<Point>();
    private PropertyFactory propertyFactory = new PropertyFactory();
    private AttributeFactory attributeFactory = new AttributeFactory();

    public AuthoringModel() throws MissingPropertiesException {
	this(new AuthoredGame());
	populateInstanceVariables();
    }

    public AuthoringModel(AuthoredGame game) throws MissingPropertiesException {
	myGeneric = new GenericModel(); 
    	myGame = game;
	spriteFactory = new SpriteFactory(myGeneric);
	try {
	populateDefaultObjects();
	}
	catch(FileNotFoundException e) {
	    throw new MissingPropertiesException(e.getLocalizedMessage());
	}
    }

    private void populateInstanceVariables() throws MissingPropertiesException {
	
	try {
	    populateDefaultObjects();
	    myGame.setSettings(myGeneric.generateGenericSettings());
	    myGame.addLevel(DEFAULT_FIRSTLEVEL_NUMBER, myGeneric.generateGenericLevel(DEFAULT_FIRSTLEVEL_NUMBER, myDefaultTower, myDefaultEnemy, myDefaultPath));
	} catch (NumberFormatException | FileNotFoundException e) {
	    throw new MissingPropertiesException(myDefaultName);
	}
    }
    
    private void populateDefaultObjects() throws MissingPropertiesException, FileNotFoundException{
	myPropertiesReader = new PropertiesReader();
	DEFAULT_CONSTANT_FILEPATH = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, DEFAULT_CONSTANTS_FILE_KEY);
	myDefaultName = myPropertiesReader.findVal(DEFAULT_CONSTANT_FILEPATH, DEFAULT_OBJECT_NAME_KEY); 
	myDefaultTower = myGeneric.generateGenericTower();
	myDefaultEnemy = myGeneric.generateGenericEnemy();
	myDefaultPath = myGeneric.generateGenericPath();
    }

    /**
     * Class to make a wave to be used in a specified level
     * @throws ObjectNotFoundException 
     */
    public void makeWave(int level, Wave wave) throws ObjectNotFoundException {
	Level currentLevel = myGame.levelCheck(level);
	currentLevel.addWave(wave);
    }

    /**
     * Makes a new path (if the starting coordinate does not already exist 
     * in a path) or updates the coordinates of an existing path for every path
     * currently on screen
     * @param level is level to add path
     * @param coordinates are lists of path coordinates (each list corresponds to one path)
     * @param imageCoordinates is map of image filepaths to the points that have those images as path bricks
     * @param backgroundImage is filepath to path background image
     * @param pathImage is filepath to middle-brick image
     * @param startImage is filepath to start-brick image
     * @param endImage is filepath to end-brick image
     * @param pathSize is width of a single path brick
     * @param width is width of entire path pane
     * @param height is height of entire path pane
     * @param transparent determines if path should be made transparent or not
     * @throws ObjectNotFoundException
     */
    public void makePath(int level, List<List<Point>> coordinates, Map<String, List<Point>> imageCoordinates, 
	    String backgroundImage, String pathImage, String startImage, String endImage, int pathSize, int width, int height, boolean transparent) throws ObjectNotFoundException {
	myImageMap = imageCoordinates; //map (row/column), coordinates is absoluteCoordinates
	myBackgroundImage = backgroundImage;
	Level currentLevel = myGame.levelCheck(level);
	List<Path> newPathList = new ArrayList<>();
	for(List<Point> list : coordinates) {
	    boolean added = false;
	    for(Path path : currentLevel.getPaths()) {
		if(list.get(0).equals(path.initialPoint())){
		    path.updatePathPoints(list, imageCoordinates);
		    newPathList.add(path);
		    added = true;
		}
	    }
	    if(!added) {
//		System.out.println("adding a new path!!!");
		List<List<Point>> listOfLists = new ArrayList<List<Point>>();
		listOfLists.add(list);
		Path newPath = new PathBuilder().construct(listOfLists, imageCoordinates, backgroundImage, pathImage, startImage, endImage, pathSize, width, height, transparent);
		newPathList.add(newPath);
	    }
	}
	currentLevel.replacePaths(newPathList);

    }

    /**
     * Makes & saves a tower in the authored game with values equal to that of the default tower
     * @param level
     * @param name - name of the tower
     * @throws NoDuplicateNamesException
     * @throws MissingPropertiesException
     * @throws NumberFormatException
     * @throws FileNotFoundException
     * @throws ObjectNotFoundException
     */
    public void makeTower(int level, String name) throws NoDuplicateNamesException, MissingPropertiesException, NumberFormatException, FileNotFoundException, ObjectNotFoundException {
	Level currentLevel = myGame.levelCheck(level);
	if (currentLevel.containsTower(name)) {
	    throw new NoDuplicateNamesException(name);
	}
	Tower newTower = myGeneric.generateGenericTower(name);
	currentLevel.addTower(name, newTower);
    }

    /**
     * Checks if level exists and, if so, returns Level obj
     * @param level - number of level 
     * @return
     * @throws ObjectNotFoundException
     */
    public Level levelCheck(int level) throws ObjectNotFoundException {
	return myGame.levelCheck(level);
    }

    /**
     * Method through which SpecifyScreens can get information about existing objects that designers may have the option of editing
     * @param level - level that the user wants to edit
     * @param objectType - type of object that the user wants to edit
     * @return List of String names of objects 
     * @throws ObjectNotFoundException 
     */
    public List<String> getCurrentObjectOptions(int level, String objectType) throws ObjectNotFoundException {
	List<String> listToReturn = new ArrayList<>(); 
	Level currentLevel = myGame.levelCheck(level);
	if (objectType.equals("Enemy")) {
	    listToReturn = currentLevel.getAllEnemies();  
	    if (listToReturn.size() == 0) {
		//System.out.println("IS DEFAULT ENEMY NULL");
		//System.out.println(myDefaultEnemy);
		//listToReturn.add(myDefaultEnemy.getName());
	    }
	} else if (objectType.equals("Tower")) {
	    listToReturn = currentLevel.getAllTowers();
	    if (listToReturn.size() == 0) {
		listToReturn.add(myDefaultTower.getName());
	    }
	}
	if(objectType.equals("Wave")) {
	    int size = currentLevel.getHighestWaveNumber();
	    for(Integer k = 1; k<=(size+1); k+=1) {
		listToReturn.add("Wave " + k.toString());
	    }
	}
	listToReturn.remove(myDefaultName);
	return listToReturn; 
    }

    /**
     * Method through which information can be sent to instantiate or edit a path object
     * Wraps constructor in case of new object creation
     */
    public void makeResources(String gameName, double startingHealth, double starting$, String css, String theme, String instructions, String backgroundMusic, String levelWinSound, String levelLossSound) {
	Settings newSettings = new SettingsBuilder().construct(gameName, startingHealth, starting$, css, theme, instructions, backgroundMusic, levelWinSound, levelLossSound);
	myGame.setSettings(newSettings);
    }



    /**
     * Creates property for object
     * @param level - level of object whose Property is being created
     * @param objectType - type of object whose Property will be created (e.g. Tower) 
     * @param objectName - name of object whose Property will be created
     * @param propertyName - name of Property to be created
     * @param attributes - double attributes that compose the new Property 
     * @throws ObjectNotFoundException
     * @throws MissingPropertiesException
     */
    public void createProperty(int level, String objectType, String objectName, String propertyName, List<Double> attributes) throws ObjectNotFoundException, MissingPropertiesException{
	Level currentLevel = myGame.levelCheck(level);
	propertyFactory.setProperty(currentLevel, objectType, objectName, propertyName, attributes);
    }

    /**
     * Returns highest existing wave number
     * @param level is level looking at waves for
     * @return current highest wave number
     * @throws ObjectNotFoundException
     */
    public Integer getHighestWaveNumber(int level) throws ObjectNotFoundException {
	Level currentLevel = myGame.levelCheck(level);
	return currentLevel.getHighestWaveNumber();
    }

    /**
     * Returns a Map of String image names to Lists of points where those images should
     * be found in the path
     * @return Map of image names to Point lists
     */
    public Map<String, List<Point>> getImageMap() {
	return myImageMap;
    }

    /**
     * 
     * @return AuthoredGame: return the AuthoredGame object created with this model
     */
    public AuthoredGame getGame() {
	return myGame;
    }

    /**
     * 
     * @return String: the name field in the AuthoredGame object
     */
    public String getGameName() {
	return myGame.getGameName();
    }

    public void setGameName(String name) {
	myGame.setGameName(name);
    }

    /**
     * Returns a Path object from the AuthoredGame object from the specified level with 
     * the specified name.
     * 
     * @param name: path name
     * @param levelNum: level number
     * @return Path: the path with the specified name in the specified level
     * @throws ObjectNotFoundException
     */
    public Path getPathFromName(int name, int levelNum) throws ObjectNotFoundException {
	return myGame.levelCheck(levelNum).getPaths().get(name-1);
    }

    /**
     * Returns a list of level numbers as strings currently stored in the 
     * AuthoredGame object
     * 
     * Useful in displaying the levels available for edit by the user
     * @return List<String>: A list of unmodifiable level numbers as strings
     */
    public List<String> getLevels() {
	return myGame.unmodifiableLevelNums();
    }

    /**
     * Returns Level object corresponding to an integer level number from the 
     * AuthoredGame object
     * 
     * @param level is number of level desired
     * @return Level object of that number
     * @throws ObjectNotFoundException
     */
    public Level getLevel(int level) throws ObjectNotFoundException {
	return myGame.levelCheck(level);
    }


    /**
     * @return List<Level>: an unmodifiable list of Level objects in the AuthoredGame object
     */
    public List<Level> allLevels() {
	return myGame.unmodifiableLevels();
    }

    /**
     * Adds a new level to the authored game, based on the previous level that the user has created
     * (or the default level if the user has not customized any level) 
     */
    public int addNewLevel() throws ObjectNotFoundException{
	List<Level> levels = myGame.unmodifiableLevels();
	int newLevelNumber = levels.size()+1;
	Level newLevel = new Level(newLevelNumber);
	newLevel.addPath(levels.get(levels.size()-1).getDefaultPath());
	myGame.addLevel(newLevelNumber, newLevel);
	return newLevelNumber; 
	//		int newLevelNumber = myLevels.size()+1; 
	//		myLevels.put(newLevelNumber, new Level(newLevelNumber));
    }

    /**
     * Gets a path with a certain starting point (used to match waves)
     * @param level is level containing desired path
     * @param point is Point object of desired starting point
     * @return Path with that starting point
     * @throws ObjectNotFoundException
     */
    public Path getPathWithStartingPoint(int level, Point point) throws ObjectNotFoundException, MissingPropertiesException {
	Level currentLevel = myGame.levelCheck(level);
	List<Path> paths = currentLevel.getPaths();
	
	int pathSize = Integer.parseInt(myPropertiesReader.findVal(DEFAULT_CONSTANT_FILEPATH, "PathSize"));
	for(Path path: paths) {
	    if(Math.abs(path.initialPoint().getX()-point.getX())<pathSize && Math.abs(path.initialPoint().getY()-point.getY())<pathSize) { // HELLOOO PLEASE CHANGE
		return path;
	    }
	}
	throw new ObjectNotFoundException("");
    }

    /**
     * Autogenerates a new level based on the previous Level's settings (enemies, towers, etc.)
     * @return int corresponding to level number of level generated
     * @throws MissingPropertiesException 
     */
    public int autogenerateLevel() throws MissingPropertiesException {
	
	List<Level> levels = myGame.unmodifiableLevels();
	int newLevelNumber = levels.size()+1;
	Level copiedLevel = levels.get(levels.size()-1);
	Level newLevel = new Level(copiedLevel);
	newLevel.incrementNumber();
	myGame.addLevel(newLevelNumber, newLevel);
	return newLevelNumber; 
    }

    /**
     * Makes a Sprite (Enemy, Tower, etc.) and adds it to the AuthoringModel
     * @param objectType is type of Sprite to be made
     * @param level is level to add it to
     * @param name is user-given name of sprite
     * @throws ObjectNotFoundException
     * @throws NumberFormatException
     * @throws FileNotFoundException
     * @throws NoDuplicateNamesException
     * @throws MissingPropertiesException
     */
    public void makeSprite(String objectType, int level, String name) throws ObjectNotFoundException, NumberFormatException, FileNotFoundException, NoDuplicateNamesException, MissingPropertiesException {
	Level currentLevel = myGame.levelCheck(level);
	spriteFactory.makeSprite(objectType, currentLevel, name);
    }

    /**
     * Deletes a Sprite from the AuthoringModel
     * @param level is level containing Sprite to be deleted
     * @param objectType is type of Sprite
     * @param name is user-given name of Sprite
     * @throws ObjectNotFoundException
     * @throws DeleteDefaultException
     */
    public void deleteObject(int level, String objectType, String name) throws ObjectNotFoundException, DeleteDefaultException {
	Level currentLevel = myGame.levelCheck(level);
	spriteFactory.deleteSprite(objectType, currentLevel, name);
    } 

    /**
     * Used in the case that the user wants to edit an existing object:
     * Populates fields with current attributes of object 
     * @param objectType - type of object being manipulated
     * @param name - name of object being manipulated
     * @param attribute - attribute/field of object being manipulated
     * @return requested attribute in String form: used in populating textfield, finding correct dropdown option, etc.
     */
    public Object getObjectAttribute(int level, String objectType, String name, String attribute) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ObjectNotFoundException {
	return attributeFactory.getObjectAttribute(level, objectType, name, attribute, myGame);
    }

    /**
     * Used in case a user wants to edit an existing object (for fields
     * not included in Properties)
     * @param level is level of Sprite
     * @param objectType is object type of Sprite (i.e. Tower)
     * @param name is user-given name of Sprite
     * @param attribute is attribute setting in Sprite
     * @param attributeValue is value giving that attribute
     * @throws ObjectNotFoundException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void setObjectAttribute(int level, String objectType, String name, String attribute, Object attributeValue) throws ObjectNotFoundException, IllegalArgumentException, IllegalAccessException {
	attributeFactory.setObjectAttribute(level, objectType, name, attribute, attributeValue, myGame);
    }
    
}

