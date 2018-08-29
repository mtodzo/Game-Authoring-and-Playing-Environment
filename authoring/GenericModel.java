package authoring;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import authoring.frontend.exceptions.MissingPropertiesException;
import engine.Settings;
import engine.builders.EnemyBuilder;
import engine.builders.LauncherBuilder;
import engine.builders.PathBuilder;
import engine.builders.ProjectileBuilder;
import engine.builders.SettingsBuilder;
import engine.builders.TowerBuilder;
import engine.level.Level;
import engine.path.Path;
import engine.sprites.enemies.Enemy;
import engine.sprites.towers.Tower;
import engine.sprites.towers.launcher.Launcher;
import engine.sprites.towers.projectiles.Projectile;
import frontend.PropertiesReader;

/**
 * 
 * @author Ben Hodgson 4/8/18
 * @author benauriemma
 *
 * Class to generate generic objects to be used in the AuthoringModel
 */
public class GenericModel {

	public static final String DEFAULT_SETTINGS_FILE = "default_objects/Settings.properties";


	private String DEFAULT_SHOOTING_SOUND;
	private String DEFAULT_TOWER_FILEPATH;
	private String DEFAULT_ENEMY_FILEPATH;
	private String DEFAULT_PATH_START;
	private String DEFAULT_PATH_MIDDLE;
	private String DEFAULT_PATH_END;
	private String DEFAULT_BACKGROUND_IMAGE;
	private String DEFAULT_CONSTANT_FILEPATH;
	
	
	private PropertiesReader myPropertiesReader;
	private String myDefaultName;

	public GenericModel() throws MissingPropertiesException {
		populateInstanceVariables();
	}


	/**
	 * Creates a generic path that the game begins with in the case that
	 * a user does not make a path
	 * @return Path to be added to generic level
	 * @throws NumberFormatException
	 * @throws FileNotFoundException
	 */
	public Path generateGenericPath() throws NumberFormatException, FileNotFoundException {
		List<Point> dummyPathPoints = new ArrayList<>();
		dummyPathPoints.add(new Point(2, 2));
		dummyPathPoints.add(new Point(2, 3));
		List<List<Point>> dummyCoordinates = new ArrayList<>();
		dummyCoordinates.add(dummyPathPoints);
		HashMap<String, List<Point>> pathImages = new HashMap<>();
		List<Point> dummyPathStartPoints = new ArrayList<>();
		dummyPathStartPoints.add(new Point(2, 2));
		List<Point> dummyPathEndPoints = new ArrayList<>();
		dummyPathEndPoints.add(new Point(2, 3));
		pathImages.put(DEFAULT_PATH_START, dummyPathStartPoints);
		pathImages.put(DEFAULT_PATH_END, dummyPathEndPoints);
		Path newPath = new PathBuilder().construct(dummyCoordinates, pathImages, DEFAULT_BACKGROUND_IMAGE, DEFAULT_PATH_MIDDLE, DEFAULT_PATH_START, DEFAULT_PATH_END, 60, 1020/60, 650/60, false);
		return newPath;
	}
	
	/**
	 * Creates a generic Level object so that all parameters have defaults
	 * if a user does not choose to change them
	 * @param levelNumber is number of the generic level
	 * @param tower is generic tower populating level
	 * @param enemy is generic enemy populating level
	 * @param path is generic path populating level
	 * @return generic Level object
	 * @throws MissingPropertiesException
	 */
	public Level generateGenericLevel(int levelNumber, Tower tower, Enemy enemy, Path path) throws MissingPropertiesException {
		Level level = new Level(levelNumber);
		level.addTower(myDefaultName, new Tower(tower));
		Enemy testEnemy = new Enemy(enemy);
		level.addEnemy(myDefaultName, testEnemy);
		level.addPath(path);
		return level; 
	}
	
	/**
	 * Creates default Settings object
	 * @return default Settings object
	 * @throws MissingPropertiesException
	 */
	public Settings generateGenericSettings() throws MissingPropertiesException {
		return generateGenericSettings(myDefaultName);
	}

	/**
	 * Creates a generic Settings object to give users a default example
	 * @param name is default generic settings name
	 * @return generic Settings game
	 * @throws MissingPropertiesException
	 */
	public Settings generateGenericSettings(String name) throws MissingPropertiesException {
		String defaultGameName = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "NewGame");
		int startingHealth = Integer.parseInt(myPropertiesReader.findVal(DEFAULT_CONSTANT_FILEPATH, "StartingHealth"));
		int startingMoney = Integer.parseInt(myPropertiesReader.findVal(DEFAULT_CONSTANT_FILEPATH, "StartingMoney"));
		String startingCSS = myPropertiesReader.findVal(DEFAULT_CONSTANT_FILEPATH, "StartingCSS");
		String startingTheme = myPropertiesReader.findVal(DEFAULT_CONSTANT_FILEPATH, "StartingTheme");
		String startingInstructions = myPropertiesReader.findVal(DEFAULT_CONSTANT_FILEPATH, "StartingInstructions");
		String defaultBackgroundMusic = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "BackgroundMusic");
		String defaultLevelWinSound = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "LevelWinSound");
		String defaultLevelLossSound = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "LevelLossSound");
		return new SettingsBuilder().construct(defaultGameName, 
			startingHealth, startingMoney, startingCSS, startingTheme, startingInstructions, defaultBackgroundMusic, defaultLevelWinSound, defaultLevelLossSound);

	}

	/**
	 * Reads information from GenericEnemy.properties file to create a default
	 * Enemy object to be used to populate user input fields.
	 * 
	 * @return Enemy: a generic enemy with attribute read in from .properties file
	 * @throws FileNotFoundException 
	 * @throws NumberFormatException 
	 * @throws MissingPropertiesException 
	 */
	public Enemy generateGenericEnemy() throws NumberFormatException, FileNotFoundException, MissingPropertiesException {
		return generateGenericEnemy(myDefaultName);
	}

	/**
	 * Reads information from GenericEnemy.properties file to create a default
	 * Enemy object to be used to populate user input fields.
	 * 
	 * @param name: A string name to give to the Enemy object
	 * @return Enemy: a generic enemy with attribute read in from .properties file
	 * @throws FileNotFoundException 
	 * @throws NumberFormatException 
	 * @throws MissingPropertiesException 
	 */
	public Enemy generateGenericEnemy(String name) throws NumberFormatException, FileNotFoundException, MissingPropertiesException {
		Projectile enemyProjectile = generateGenericProjectile(name);
		Launcher enemyLauncher = generateGenericLauncher(enemyProjectile);
		Enemy newEnemy = new EnemyBuilder().construct(
				name, 
				myPropertiesReader.findVal(DEFAULT_ENEMY_FILEPATH, "enemyImage"), 
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_ENEMY_FILEPATH,"enemySpeed")), 
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_ENEMY_FILEPATH,"enemyHealth")), 
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_ENEMY_FILEPATH,"enemyHealthImpact")), 
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_ENEMY_FILEPATH,"enemyKillReward")), 
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_ENEMY_FILEPATH,"enemyKillUpgradeCost")), 
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_ENEMY_FILEPATH,"enemyKillUpgradeValue")),
				enemyLauncher);
		return newEnemy;
	}

	/**
	 * Reads information from GenericTower.properties file to create a default
	 * Tower object to be used to populate user input fields.
	 * 
	 * @return Tower: a generic tower with attribute read in from .properties file
	 * @throws MissingPropertiesException 
	 */
	public Tower generateGenericTower() throws NumberFormatException, FileNotFoundException, MissingPropertiesException {

	    return generateGenericTower(myDefaultName);
	}
    
	/**
	 * Reads information from GenericTower.properties file to create a default
	 * Tower object to be used to populate user input fields.
	 * 
	 * @param name: A string name to give to the Tower object
	 * @return Tower: a generic tower with attribute read in from .properties file
	 * @throws MissingPropertiesException 
	 */
	public Tower generateGenericTower(String name) throws NumberFormatException, FileNotFoundException, MissingPropertiesException {
		double towerSize = Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "towerSize"));
		Projectile towerProjectile = generateGenericProjectile(name);
		Launcher towerLauncher = generateGenericLauncher(towerProjectile);
		Tower newTower = new TowerBuilder().construct(
				name, 
				myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "towerImage"), 
				towerSize, 
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "towerHealth")), 
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "towerHealthUpgradeValue")), 
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "towerHealthUpgradeCost")), 
				towerLauncher,
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "towerValue")),
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "towerUpgradeCost")),
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "towerUpgradeValue"))); 
		return newTower;
	}

	/**
	 * Reads information from GenericTower.properties file to create a default
	 * Launcher object to be used to manage a projectile in a Tower object.
	 * 
	 * @return Launcher: a generic projectile launcher with attribute read in from .properties file
	 * @throws MissingPropertiesException 
	 */
	private Launcher generateGenericLauncher(Projectile towerProjectile) throws NumberFormatException, FileNotFoundException, MissingPropertiesException {
		//	double projectileSize = Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "projectileSize"));
		return new LauncherBuilder().construct(
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "launcherSpeed")),  
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "launcherRange")), 
				towerProjectile);  
	}

	/**
	 * Reads information from GenericTower.properties file to create a default
	 * Projectile object to be used to populate input fields for a Tower object.
	 * 
	 * @param name: A string name to give to the Projectile object
	 * @return Projectile: a generic projectile with attribute read in from .properties file
	 * @throws MissingPropertiesException 
	 */
	private Projectile generateGenericProjectile(String name) throws NumberFormatException, FileNotFoundException, MissingPropertiesException {
		//	double projectileSize = Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "projectileSize"));
		return new ProjectileBuilder().construct(
				name,  
				myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "projectileImage"), 
				// TODO add projectile speed !!!!
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "projectileDamage")), 
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "projectileSize")),
				Double.parseDouble(myPropertiesReader.findVal(DEFAULT_TOWER_FILEPATH, "projectileSpeed")),
				DEFAULT_SHOOTING_SOUND); // TODO FOR BMA: this is a hardcoded default sound for a projectile
	}

	private void populateInstanceVariables() throws MissingPropertiesException {
		myPropertiesReader = new PropertiesReader();
		myPropertiesReader = new PropertiesReader();

		DEFAULT_SHOOTING_SOUND = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "ShootingSound");
		DEFAULT_TOWER_FILEPATH = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "TowerFiles");
		DEFAULT_ENEMY_FILEPATH = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "EnemyFiles");
		DEFAULT_PATH_START = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "PathStart");
		DEFAULT_PATH_MIDDLE = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "PathMiddle");
		DEFAULT_PATH_END = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "PathEnd");
		DEFAULT_BACKGROUND_IMAGE = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "Background");
		DEFAULT_CONSTANT_FILEPATH = myPropertiesReader.findVal(DEFAULT_SETTINGS_FILE, "ConstantFiles");
		myDefaultName = myPropertiesReader.findVal(DEFAULT_CONSTANT_FILEPATH, "DefaultObjectName");
	}

}
