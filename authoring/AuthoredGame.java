package authoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import authoring.frontend.exceptions.ObjectNotFoundException;
import data.GameData;
import engine.Settings;
import engine.level.Level;
import engine.path.Path;
import engine.sprites.enemies.Enemy;
import engine.sprites.towers.Tower;

/**
 * 
 * @author Ben Hodgson 4/19/18
 *
 * Class that contains all the information necessary to run an authored game. 
 */
public class AuthoredGame implements GameData {

    private Settings mySettings; 
    private Map<Integer, Level> myLevels;
    private String myGameName;
    protected AuthoredGame() {
	myLevels = new HashMap<>();
    }
    
    protected void setSettings(Settings newSettings) {
	mySettings = newSettings;
	myGameName = mySettings.getGameName();
    }
    
    protected void addLevel(int levelNum, Level newLevel) {
	myLevels.put(levelNum, newLevel);
    }

    protected void setGameName(String name) {
	mySettings.setGameName(name);
	myGameName = name;
    }
    
    /**
     * @return String name of this game
     */
    public String getGameName() {
	return myGameName; 
    }
    
    /**
     * @return double: the starting health the player starts with
     */
    public double startingHealth() {
	return mySettings.startingHealth();
    }
    
    /**
     * @return double: the starting money the player starts with
     */
    public double startingMoney() {
	return mySettings.startingMoney();
    }
    
    
    /**
     * @return Settings object of this game (contains info about 
     * instructions, game name, and starting currency)
     */
    public Settings getSettings() {
	return mySettings;
    }
    
    /**
     * Returns Level object corresponding to an integer level number
     * @param level is number of level desired
     * @return Level object of that number
     * @throws ObjectNotFoundException
     */
    public Level levelCheck(int level) throws ObjectNotFoundException {
	Level currentLevel = myLevels.get(level);
	if (currentLevel == null) {
	    throw new ObjectNotFoundException(Integer.toString(level));
	}
	return currentLevel;
    }
    
    /**
     * Returns a list of Level objects corresponding to all Levels that have been previously made
     * @return a list of all Levels currently made in this game
     */
    public List<Level> unmodifiableLevels() {
	List<Level> ret = new ArrayList<>();
	for(Level level : myLevels.values()) {
	    ret.add(level);
	}
	return Collections.unmodifiableList(ret);
    }
    
    /**
     * Returns a list of level numbers as strings currently in the authored game
     * Useful in displaying the levels available for edit by the user
     * @return List<String>: A list of unmodifiable level numbers as strings
     */
    public List<String> unmodifiableLevelNums() {
	List<String> listToReturn = new ArrayList<>(); 
	for (Integer level : myLevels.keySet()) {
	    listToReturn.add(Integer.toString(level));
	}
	return Collections.unmodifiableList(listToReturn); 
    }
    
    /**
     * Returns a path based on the name (integer value) of that path
     * @param name is int value "name" of this path
     * @param levelNum is level containing path
     * @return Path object corresponding to that name
     * @throws ObjectNotFoundException
     */
    public Path getPathFromName(int name, int levelNum) throws ObjectNotFoundException {
	return levelCheck(levelNum).getPaths().get(name-1);
    }
    
    /**
     * Only called by AuthoringModelReader; used to reconstruct/reinitialize all ImageViews that were serialized
     */
    public void reconstruct() {
	for (Integer i:myLevels.keySet()) {
	    Level thisLevel = myLevels.get(i);
	    for (String enemyName:thisLevel.getAllEnemies()) {
		Enemy enemy = thisLevel.getEnemy(enemyName);
		enemy.loadImage();
	    }
	    for (String towerName:thisLevel.getAllTowers()) {
		Tower tower = thisLevel.getTower(towerName);
		tower.loadImage();
	    }
	}
    }
}
