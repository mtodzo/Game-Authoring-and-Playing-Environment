package engine;


import gameplayer.ScreenManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;
import voogasalad.util.soundfactory.ITRTSoundFactory;
import voogasalad.util.soundfactory.SoundFactory;

import java.util.List;
import java.util.Map;

import authoring.AuthoringModel;
import authoring.frontend.exceptions.MissingPropertiesException;
import controller.MVController;
import controller.PlayController;
import engine.sprites.FrontEndSprite;
import engine.sprites.Sprite;
import engine.sprites.towers.CannotAffordException;
import engine.sprites.towers.FrontEndTower;
import engine.sprites.towers.Tower;
import frontend.StageManager;

import java.awt.Point;
import java.io.IOException;

import xml.BadGameDataException;
import xml.PlayLoader;
import xml.PlaySaverWriter;
import xml.XMLFactory;

/**
 * This class serves as a bridge between the front end, back end, and file I/O of our game player
 * Each of these areas holds an instance of its corresponding class
 *
 * Mediator has a method for every event that can occur in the game. These methods delegate tasks to other classes that are more specialized for handling
 * those behaviors.
 *
 * @author benauriemma 4/5
 * @author andrewarnold
 * @author Brendan Cheng
 * @author Alexi Kontos
 * @author Ryan Pond
 *
 */
public class Mediator implements MVController{

    private static final String PROPERTIES_FILE_PATH = "src/sound/resources/soundFiles.properties";

    private ScreenManager myScreenManager;
    private GameEngine myGameEngine;
    private PlayController myPlayController;
    private SoundFactory mySoundFactory;

    //    private ObservableList<Tower> placedTowers = FXCollections.observableArrayList();
    //    private ObservableList<Tower> availableTowers = FXCollections.observableArrayList();
    //    private ObservableValue<Integer> gameSpeed;
    //    private ObservableValue<Integer> level;
    //    private ObservableValue<Integer> difficulty;
    //    private ObservableValue<Tower> placeTower;
    //    private ObservableValue<Boolean> saveFileAvailable;
    //    private ObservableValue<Boolean> loadGameFromFile;

    /**
     * Constructs Mediator object and sets all fields to null.
     * Before class is used, setGameEngine and setScreenManager methods should be called to set appropriate instance variables
     */
    public Mediator(PlayController p) {
	myPlayController = p;
	mySoundFactory = new ITRTSoundFactory(PROPERTIES_FILE_PATH);
	//	loadGameFromFile = new ReadOnlyObjectWrapper<>(false);
	//	saveFileAvailable = new ReadOnlyObjectWrapper<>(false);
    }


    /************************************************ SETUP ********************************************/

    /**
     * Sets ScreenManager
     * @param sm	ScreenManager to be set
     */
    public void setScreenManager(ScreenManager sm) {
	myScreenManager = sm;

    }

    /**
     * Sets GameEngine
     * @param ge	GameEngine to be set
     */
    public void setGameEngine(GameEngine ge) {
	myGameEngine = ge;
    }

    /**
     * Sets PlayController
     * @param pc	The PlayController to be set
     */
    public void setPlayController(PlayController pc) {
	myPlayController = pc;
    }

    /************************************************ FILE I/O ********************************************/

    /**
     * Starts a new play given a path to an AuthoringModel. To be called when a user chooses a file on the front end to start a new Play
     * @param filename	name of file
     * @throws MissingPropertiesException 
     */
    public void startPlay(String filename) throws MissingPropertiesException {
	myPlayController.newPlay(filename);
	//myPlayController.setAuthoring();
    }

    /**
     * Saves current state of game in xml file
     * @param filename	String representing name of file. Path and ".xml" are handled by method in XML package.
     * @throws IOException 
     * @throws BadGameDataException 
     */
    public void savePlay(String filename) throws BadGameDataException, IOException {
	PlaySaverWriter p = (PlaySaverWriter) XMLFactory.generateWriter("PlaySaverWriter");
	p.write(myGameEngine.getPlayState(), filename);
    }

    /**
     * Loads a saved Play from an xml file
     * @param filename	name of file where Play is saved
     * @return PlayState object containing data from xml file
     */
    public PlayState loadPlay(String filename) {
	PlayLoader p = (PlayLoader) XMLFactory.generateReader("PlayLoader");
	return p.createModel(filename);
    }

    /************************************************ GAMEPLAY ********************************************/
    /**
     * to be called by the frontend when a user drops a tower on the gamescreen.
     * @param location, where the tower should be placed
     * @param towerType, type of tower to be placed
     * @return frontEndTower that can be used to refer to the tower in the future
     * @throws CannotAffordException 
     * @throws MissingPropertiesException 
     */
    public FrontEndTower placeTower(Point location, String towerType) throws CannotAffordException, MissingPropertiesException {
	//TODO add in money (decrement when purchased)
	return myGameEngine.getPlayState().placeTower(location, towerType);
    }
    
    /**
     * Method called when a click-to-shoot tower is clicked
     * @param tower is Tower shooting
     * @param clickedX is X coordinate where user clicked to shoot
     * @param clickedY is Y coordinate where user clicked to shoot
     * @throws MissingPropertiesException
     */
    public void handleTowerClickToShoot(FrontEndTower tower, double clickedX, double clickedY) throws MissingPropertiesException{
	Sprite shotProjectile = myGameEngine.getPlayState().handleClick(tower, clickedX, clickedY);
	if (shotProjectile != null) {
	    this.addSpriteToScreen(shotProjectile);
	}
    }

    /**
     * To be called by the backend any time a projectile or enemy should be added to the screen
     * @param sprite is the projectile or enemy to be added, cast as a FrontEndSprite
     */
    public void addSpriteToScreen(FrontEndSprite sprite) {
	myScreenManager.display(sprite);
    }

    /**
     * to be called by the backend any time a projectile or enemy should be removed to the screen
     * @param sprite is the projectile or enemy to be removed, cast as a FrontEndSprite
     */
    public void removeSpriteFromScreen(FrontEndSprite sprite) {
	myScreenManager.remove(sprite);
    }

    /**
     * Sets available towers on the screen based on authored towers
     * @param availableTowers
     */
    public void setAvailableTowers(List<FrontEndTower> availableTowers) {  
	myScreenManager.setAvailableTowers(availableTowers);
    }

    /**
     * to be called when a user sells the tower. Frontend should not call
     * frontendtower.sell(), let mediator handle.
     * @param tower
     */
    public void sellTower(FrontEndTower tower) {
	//TODO increase money when sold in sell method
	myGameEngine.getPlayState().sellTower(tower);
    }

    //WILL BE ADDED BACK IN WHEN UPGRADES ARE ADDED
    /**
     * to be called by the frontend and pass upgradeName into the method and allow mediator to handle the call of upgrade.
     * @param tower
     * @param upgradeName
     */
    public void upgradeTower(FrontEndTower tower, String upgradeName) {
	myGameEngine.getPlayState().upgradeTower(tower, upgradeName);
    }

    /**
     * Called by the frontend when the restart button is pressed.
     * @throws MissingPropertiesException 
     */
    public void restartLevel() throws MissingPropertiesException {
	myGameEngine.getPlayState().restartLevel();
    }

    /**
     * to be called by the backend to play the simulation
     */
    public void play() {
	myGameEngine.start();
    }

    /**
     * to be called by the backend to pause the simulation
     */
    public void pause() {
	myGameEngine.pause();
    }

    /**
     * called by a slider, values between 1-10 (subject to change but between 1 and 10 will make it easier
     * to speed up and slow down
     * @param sliderValue
     */
    public void fastForward(Integer sliderValue) {
	myGameEngine.setSpeed(sliderValue);
    }

    
    /**
     * to be called by the backend to tell the frontend the new level number
     * @param newLevel
     */
    public void updateLevel(Integer newLevel) {
	myScreenManager.updateLevelCount(newLevel);
    }

    /**
     * PlayState passing integer properties to Game Screen to attach listeners for currency, score and 
     * lives. 
     * @param myResources integer property for currency
     * @param myScore	integer property for score
     * @param IntegerProperty	 integer property for health
     */
    public void addIntegerProperties(IntegerProperty myCurrency, IntegerProperty myScore, SimpleIntegerProperty myLives) {
	myScreenManager.attachListeners(myCurrency, myScore, myLives);
    }

    /**
     * Takes a list of sprites that are to be removed from PlayState, removes them
     * @param list
     */
    public void removeListOfSpritesFromScreen(List<Sprite> list) {
	for(Sprite sprite : list) {
	    this.removeSpriteFromScreen( (FrontEndSprite) sprite); 
	}
    }

    /**
     * Sets up the path based on information from the authoring side
     * @param imageMap is map of image filepaths to their 
     * @param backgroundImageFilePath
     * @param pathSize
     * @param width
     * @param height
     * @param transparent
     * @return
     */
    public boolean setPath(Map<String, List<Point>> imageMap, String backgroundImageFilePath, int pathSize, int width, int height, boolean transparent) {
	return myScreenManager.setPath(imageMap, backgroundImageFilePath, pathSize, width, height, transparent);
    }


    /**
     * PlayState passing integer properties to Game Screen to attach listeners for currency, score and 
     * lives. 
     * @param myResources integer property for currency
     * @param myScore	integer property for score
     * @param simpleIntegerProperty	 integer property for health
     */
    public void addIntegerProperties(IntegerProperty myCurrency, IntegerProperty myScore, IntegerProperty myLives) {
	myScreenManager.attachListeners(myCurrency, myScore, myLives);
    }

    /**
     * Ends game loop in case that user wants to return to authoring/editing the game
     * @author susiechoi
     */
    public void endLoop() {
	myGameEngine.endLoop();
    }

    public String getStyling() {
	String styling = null; 
	if (myGameEngine.getPlayState() != null) {
	    try {
		styling = myGameEngine.getPlayState().getStyling();
	    } catch (MissingPropertiesException e) {
		myScreenManager.loadErrorAlert("NoFile");
	    }
	}
	return styling; 
    }

    public void gameWon() {
	myScreenManager.getGameScreen().gameWon();
    }

    public void nextLevel(List<FrontEndTower> availableTowers) {
	setAvailableTowers(availableTowers);
	myScreenManager.getGameScreen().nextLevel();
    }

    @Override
    public void playControllerDemo(StageManager manager, String instructions) throws MissingPropertiesException{
	myPlayController.demoPlay(new AuthoringModel().getGame());
    }

    public void gameLost() {
	myScreenManager.getGameScreen().gameLost();
    }

    public SoundFactory getSoundFactory() {
	return mySoundFactory;
    }

    public void moveTowers(FrontEndTower tower, KeyCode c) {
	myGameEngine.getPlayState().moveTowers(tower, c);
    }

    public String getInstructions() {
        return myGameEngine.getPlayState().getInstructions();
    }
}
