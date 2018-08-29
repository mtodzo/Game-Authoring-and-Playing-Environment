package engine;

import java.util.ArrayList;
import java.util.List;
import com.sun.javafx.tools.packager.Log;
import authoring.frontend.exceptions.MissingPropertiesException;
import data.GameData;
import java.awt.Point;
import java.io.FileNotFoundException;
import engine.level.Level;
import engine.managers.EnemyManager;
import engine.managers.TowerManager;
import engine.path.Path;
import engine.sprites.enemies.Enemy;
import engine.sprites.enemies.wave.Wave;
import engine.sprites.properties.ClickProperty;
import engine.sprites.properties.Property;
import engine.sprites.ShootingSprites;
import engine.sprites.towers.CannotAffordException;
import engine.sprites.Sprite;
import engine.sprites.towers.FrontEndTower;
import engine.sprites.towers.Tower;
import engine.sprites.towers.projectiles.Projectile;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;


/**
 * Handles the current state of the game, including current score, money, and lists
 * of active towers and enemies
 * @author Miles Todzo
 * @author Katherine Van Dyk
 * @author benauriemma 4/8
 * @author Ben Hodgson 
 * @author Ryan Pond
 */
public class PlayState implements GameData {

    private final int FRAMES_PER_SECOND = 60;
    private int count;
    private IntegerProperty myScore;
    private IntegerProperty myResources;
    private IntegerProperty myHealth;
    private Settings mySettings;
    private TowerManager myTowerManager;
    private EnemyManager myEnemyManager;
    private Mediator myMediator;
    private List<Level> myLevels;
    private Level currentLevel;
    private Level currentLevelCopy;
    private boolean backgroundSet;

    /**
     * Constructor for play state object that sets up initial levels.
     * 
     * @param mediator
     * @param levels
     * @param score
     * @param resources
     * @param universalTime
     * @throws MissingPropertiesException 
     */
    public PlayState(Mediator mediator, List<Level> levels, int score, Settings settings, double universalTime) throws MissingPropertiesException {
	myMediator = mediator;
	myLevels = levels;
	currentLevel = myLevels.get(0);
	currentLevelCopy = new Level(currentLevel);
	myTowerManager = new TowerManager(currentLevel.getTowers());
	myEnemyManager = new EnemyManager();
	myScore = new SimpleIntegerProperty(score);
	myResources = new SimpleIntegerProperty((int) settings.startingMoney());
	myHealth = new SimpleIntegerProperty((int) settings.startingHealth());
	myMediator.addIntegerProperties(myResources, myScore, myHealth);
	mySettings=settings;
	List<FrontEndTower> availTowers = new ArrayList<>();
	availTowers.addAll(currentLevel.getTowers().values());
	myMediator.setAvailableTowers(availTowers);
	myTowerManager.setAvailableTowers(currentLevel.getTowers().values());
	count = 0;
	backgroundSet = false;
	try {
	    myMediator.getSoundFactory().setBackgroundMusic(mySettings.getBackgroundMusic());
	} catch (FileNotFoundException e) {
	    e.printStackTrace(); // TODO Auto-generated catch block
	}
    }

    public void update(double elapsedTime) throws MissingPropertiesException, FileNotFoundException {
	//Background has to be passed after a layout pass has been done on the Scene in order to adapt to
	//differences in computers screen size 
	if(!backgroundSet) {
	    backgroundSet = myMediator.setPath(myLevels.get(0).getLevelPathMap(), myLevels.get(0).getBackGroundImage(), myLevels.get(0).getPathSize(), myLevels.get(0).getGridWidth(), myLevels.get(0).getGridHeight(), myLevels.get(0).getTransparent());
	}
	count++;
	checkLoss();
	if (count % FRAMES_PER_SECOND == 0) {
	    spawnEnemies();
	}
	checkPathEnd(elapsedTime);
	handleCollisions(elapsedTime);
    }

    private void handleCollisions(double elapsedTime) throws MissingPropertiesException, FileNotFoundException {
	List<Sprite> toBeRemoved = new ArrayList<>();
	toBeRemoved.addAll(myTowerManager.checkForCollisions(myEnemyManager.getListOfActive()));
	toBeRemoved.addAll(myEnemyManager.checkForCollisions(myTowerManager.getListOfActive()));
	List<ShootingSprites> activeEnemies = myEnemyManager.getListOfActive();
	List<ShootingSprites> activeTowers = myTowerManager.getListOfActive();
	activeEnemies.removeAll(toBeRemoved);
	activeTowers.removeAll(toBeRemoved);
	myEnemyManager.removeFromMap(toBeRemoved);
	myEnemyManager.setActiveList(activeEnemies);
	toBeRemoved.addAll(myTowerManager.moveProjectiles(elapsedTime));
	toBeRemoved.addAll(myEnemyManager.moveProjectiles(elapsedTime));

	for (Projectile projectile: myTowerManager.shoot(myEnemyManager.getListOfActive(), elapsedTime)) {
	    myMediator.addSpriteToScreen(projectile);
	    try {
		myMediator.getSoundFactory().playSoundEffect(projectile.getShootingSound()); // THIS SHOULD BE CUSTOMIZED: should be something like playSoundEffect(projectile.getSound())
	    } catch (FileNotFoundException e) {
		throw new FileNotFoundException();
	    }
	}
	//System.out.println("ENEMIES ABOUT TO SHOOT **************************************************************************");
	for(Projectile projectile: myEnemyManager.shoot(myTowerManager.getListOfActive(), elapsedTime)) {
	    myMediator.addSpriteToScreen(projectile);
	    try {
		myMediator.getSoundFactory().playSoundEffect(projectile.getShootingSound()); // THIS SHOULD BE CUSTOMIZED: should be something like playSoundEffect(projectile.getSound())
	    } catch (FileNotFoundException e) {
		e.printStackTrace(); // YIKES THAT'S AN EASY FAIL
	    }
	}
	//System.out.println("ENEMIES DONE SHOOTING **************************************************************************");

	updateScore(toBeRemoved);
	myMediator.removeListOfSpritesFromScreen(toBeRemoved);
    }

    /**
     * Checks if enemies have reached the end of the path. Removes the enemies from the
     * screen and the enemy manager object if they reach the end of the path.
     */
    private void checkPathEnd(double elapsedTime) {
	List<Sprite> endPathEnemies = myEnemyManager.moveEnemies(elapsedTime);
	updateHealth(endPathEnemies);
	List<ShootingSprites> activeEnemies = myEnemyManager.getListOfActive();
	activeEnemies.removeAll(endPathEnemies);
	myEnemyManager.removeFromMap(endPathEnemies);
	myEnemyManager.setActiveList(activeEnemies);
	myMediator.removeListOfSpritesFromScreen(endPathEnemies);
    }


    private void spawnEnemies() throws MissingPropertiesException {
	try {
	    Wave currentWave = currentLevel.getWave(0);
	    if (currentWave.isFinished()) {
		double time = currentWave.getWaveTime();
		currentLevel.removeWave();  
		currentWave = currentLevel.getWave(0);
		// TODO remove magic number! put FPS in constant file
		currentWave.setWaveTime(time*FRAMES_PER_SECOND + count);		
	    }
	    for (Path currentPath : currentLevel.getPaths()) {
		try {
		    spawnEnemy(currentWave, currentPath);
		}
		catch (Exception e) {
		    // do nothing, path contains no enemies or is not ready to spawn
		}
	    }
	}
	catch (Exception e) {
	    // Level is over
	    checkWin();
	}
    }
    
    private void checkWin() throws MissingPropertiesException {
	// Level is over
	if (currentLevel.isFinished() && currentLevel.myNumber() < myLevels.size()
		&& deadEnemies()) {
	    advanceLevel();
	}
	else {
	    // TODO: end game, player won. 
	    if (deadEnemies()) {
		myMediator.gameWon();
	    }
	}
    }
    
    private boolean deadEnemies() {
	for (ShootingSprites thisEnemy : myEnemyManager.getListOfActive()) {
	    if (thisEnemy.isAlive()) {
		return false;
	    }
	}
	return true;
    }
    
    private void advanceLevel() throws MissingPropertiesException {
	List<Level> newLevels = new ArrayList<Level>();
	for (Level thisLevel : myLevels) {
	    if (thisLevel.equals(currentLevel)) {
		newLevels.add(currentLevelCopy);
	    }
	    else {
		newLevels.add(thisLevel);
	    }
	}
	myLevels = newLevels;
	currentLevel = myLevels.get(currentLevel.myNumber());
	currentLevelCopy = new Level(currentLevel);
	myMediator.updateLevel(currentLevel.myNumber());
	setLevel(currentLevel.myNumber());
	List<FrontEndTower> availableTowers = new ArrayList<>();
	
	availableTowers.addAll(currentLevel.getTowers().values());
	try {
	    myMediator.getSoundFactory().playSoundEffect(mySettings.getLevelWinSound());
	} catch (FileNotFoundException e1) {
	    Log.debug(e1);
	    e1.printStackTrace(); 
	}
	myTowerManager = new TowerManager(currentLevel.getTowers());
	myTowerManager.setAvailableTowers(currentLevel.getTowers().values());
	myMediator.nextLevel(availableTowers);

    }

    
    private void spawnEnemy(Wave wave, Path path) throws MissingPropertiesException {
	Enemy newEnemy = wave.getEnemySpecificPath(path, count);
	newEnemy.setInitialPoint(path.initialPoint());
	myEnemyManager.addEnemy(path, newEnemy);
	myEnemyManager.addToActiveList(newEnemy);
	myMediator.addSpriteToScreen(newEnemy);
    }

    
    private void checkLoss() {
	if (myHealth.getValue() <= 0) {
	    myMediator.pause();
	    myMediator.endLoop();
	    myMediator.gameLost();
	    try {
		myMediator.getSoundFactory().playSoundEffect(mySettings.getLevelLossSound());
	    } catch (FileNotFoundException e) {
		Log.debug(e);
	    }
	}
    }

    private void updateScore(List<Sprite> toBeRemoved) {
	for(Sprite sprite : toBeRemoved) {
	    myScore.set(myScore.get() + sprite.getPointValue());
	    if(sprite.getClass().getName().equals("engine.sprites.enemies.Enemy")) {
		Enemy enemy = (Enemy) sprite;
		myResources.set(myResources.get() + enemy.getPointValue());
	    }
	}
    }
    
    private void updateHealth(List<Sprite> toBeRemoved) {
	for(Sprite sprite : toBeRemoved) {
	    if(sprite.getClass().getName().equals("engine.sprites.enemies.Enemy")) {
		Enemy enemy = (Enemy) sprite;
		myHealth.set(myHealth.get() 
			- Double.valueOf(Math.round(enemy.getDamage())).intValue());
	    }
	}
    }

    public void setLevel(int levelNumber) throws MissingPropertiesException {
	clearLevel();
	currentLevel = myLevels.get(levelNumber - 1);
	currentLevelCopy = new Level(currentLevel);
	myTowerManager.setAvailableTowers(currentLevel.getTowers().values());
	myMediator.updateLevel(currentLevel.myNumber());
	myMediator.setPath(currentLevel.getLevelPathMap(), currentLevel.getBackGroundImage(), 
		currentLevel.getPathSize(), currentLevel.getGridWidth(), currentLevel.getGridHeight(), currentLevel.getTransparent());
    }

    /**
     * Restarts the level that you were currently on.
     * @throws MissingPropertiesException 
     */
    public void restartLevel() throws MissingPropertiesException {
	clearLevel();
	currentLevel = currentLevelCopy;
	currentLevelCopy = new Level(currentLevel);
	myTowerManager.setAvailableTowers(currentLevel.getTowers().values());	
    }

    private void clearLevel() {
	List<Sprite> toBeRemoved = new ArrayList<>();
	toBeRemoved.addAll(myTowerManager.getListOfActive());
	toBeRemoved.addAll(myTowerManager.removeAllProjectiles());
	toBeRemoved.addAll(myEnemyManager.getListOfActive());
	myMediator.removeListOfSpritesFromScreen(toBeRemoved);
	myTowerManager.getListOfActive().clear();
	myEnemyManager.getListOfActive().clear();
	myEnemyManager.clearEnemiesMap();
    }

    /**
     * Places a tower, adds to the necessary backend data structures
     * @param location : The point on the map where the tower is added
     * @param towerType : Type of tower
     * @return : the front end tower
     * @throws CannotAffordException : thrown if the user does not have enough money
     * @throws MissingPropertiesException 
     */
    public FrontEndTower placeTower(Point location, String towerType) throws CannotAffordException, MissingPropertiesException {
	FrontEndTower placedTower = myTowerManager.place(location, towerType);
	try {
	    myResources.set(placedTower.purchase(myResources.get()));
	    return placedTower;
	}
	catch(CannotAffordException e) {
	    myTowerManager.sell(placedTower);
	    throw new CannotAffordException(e.getMessage());
	}
    }
    /**
     * Sells the tower, increments users currency, and removes it from collection and screen
     * @param tower
     */
    public void sellTower(FrontEndTower tower) {
	myTowerManager.upgrade(tower,"rando",myResources.get());
	myResources.set(myResources.get()+myTowerManager.sell(tower));
	myMediator.removeSpriteFromScreen(tower);
	try {
	    myMediator.getSoundFactory().playSoundEffect("cash");
	} catch (FileNotFoundException e) {
	    Log.debug(e);
	} 
    }

    /**
     * Called when a certain tower is to be upgraded. The type of upgrade is specified
     * with the string upgradeName
     * @param tower
     * @param upgradeName
     */
    public void upgradeTower(FrontEndTower tower, String upgradeName) {
	myResources.set((int) myTowerManager.upgrade(tower,upgradeName,myResources.get())); 
    }
    
    public String getStyling() throws MissingPropertiesException {
    	return mySettings.getCSSTheme();
    }
    
    public Sprite handleClick(FrontEndTower activeTower, double clickedX, double clickedY) throws MissingPropertiesException{
	Tower tower = (Tower) activeTower;
//	for (Property p: tower.getProperties()) {
//	    System.out.println(p.getName());
//	}
//	System.out.println("handling click " + tower.getProperty("ClickToShootProperty"));
	if (tower.getProperty("ClickToShootProperty") != null) {
	    ClickProperty myClickProp = (ClickProperty) tower.getProperty("ClickProperty");
	    Sprite sprite = (Sprite) tower.getNewProjectile(clickedX, clickedY);
	  //  System.out.println(sprite);
	    return sprite;
	}
	return null;
    }

    public void moveTowers(FrontEndTower tower, KeyCode c) {
	myTowerManager.moveTowers(tower, c);
    }

    public String getInstructions() {
    	return mySettings.getInstructions();
	}
}
