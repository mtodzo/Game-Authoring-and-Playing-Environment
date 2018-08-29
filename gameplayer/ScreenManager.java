package gameplayer;

import java.awt.Point;
import java.util.List;
import java.util.Map;


import com.sun.javafx.tools.packager.Log;
import authoring.frontend.exceptions.MissingPropertiesException;
import engine.Mediator;
import engine.sprites.FrontEndSprite;
import engine.sprites.towers.FrontEndTower;
import frontend.MainScreen;
import frontend.PromptReader;
import frontend.PropertiesReader;
import frontend.StageManager;
import frontend.View;
import gameplayer.screen.GameScreen;
import gameplayer.screen.InstructionScreen;
import javafx.beans.property.IntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

/**
 * 
 * @author Ben Hodgson 4/11/18, Alexi Kontos and Andrew Arnold
 * 
 * Class to manage updating Screen elements that remain across the entire game
 * (score, level, health, currency, etc.)
 */
public class ScreenManager extends View {


	public static final String FILE_ERROR_KEY = "FileErrorPrompt";
	public static final String SCREEN_ERROR_KEY = "ScreenErrorPrompt";
	public static final String DEFAULT_OWN_CSS = "styling/EngineFrontend.css";

	/**
	 * not sure where we're getting these values to display on the panels and stuff
	 * TALK TO ANDREW ABOUT
	 */

	private String myLanguage;
	private String myGameFilePath;
	private Mediator MEDIATOR;
	private final StageManager STAGE_MANAGER;
	private GameScreen GAME_SCREEN;
	private final PromptReader PROMPTS;
	private PropertiesReader PROP_READ = new PropertiesReader();
	private Map<String, String> GAMEPLAYER_PROPERTIES;
	private GameplayerAlert ALERT;

	//private final FileIO FILE_READER;

	public ScreenManager(StageManager stageManager, String language, Mediator mediator) {
		super(stageManager, language, mediator);
		STAGE_MANAGER = stageManager;
		try {
			GAMEPLAYER_PROPERTIES = PROP_READ.read("src/gameplayer/gameplayer.properties");
		}
		catch (MissingPropertiesException e) {
			Log.debug(e);
			ALERT = new GameplayerAlert(e.getMessage());
		}
		PROMPTS = new PromptReader(language, this);
		myLanguage = language;
		MEDIATOR = mediator;
	}



	public void loadInstructionScreen() {
		InstructionScreen instructScreen = new InstructionScreen(this, PROMPTS);
		Parent instructRoot = instructScreen.getScreen();
		STAGE_MANAGER.switchScreen(instructRoot);
	}

	public void loadGameScreenNew(String filepath) throws MissingPropertiesException {
		setGameFilePath(filepath);
		GAME_SCREEN = new GameScreen(this, PROMPTS, MEDIATOR);
		Parent gameScreenRoot = GAME_SCREEN.getScreen();
		STAGE_MANAGER.switchScreen(gameScreenRoot);
		//System.out.println("FILEPATH");
		//System.out.println(filepath);
		MEDIATOR.startPlay(filepath);
	}

	public void loadGameScreenNew() {
		GAME_SCREEN = new GameScreen(this, PROMPTS, MEDIATOR);
		Parent gameScreenRoot = GAME_SCREEN.getScreen();
		STAGE_MANAGER.switchScreen(gameScreenRoot);
//		System.out.println("StageLoaded");
	}

	public void loadMainScreen() {
		MainScreen mainScreen = new MainScreen(STAGE_MANAGER, this);
		Parent mainScreenRoot = mainScreen.getScreen();
		STAGE_MANAGER.switchScreen(mainScreenRoot);
	}

	public void updateLevelCount(Integer newLevelCount) {
		checkGameScreenInitialization();
		GAME_SCREEN.updateLevel(newLevelCount);
	}

	public void display(FrontEndSprite sprite) {
		checkGameScreenInitialization();
		GAME_SCREEN.displaySprite(sprite);
	}


	public void toMain() {
		STAGE_MANAGER.switchScreen(new MainScreen(STAGE_MANAGER, this).getScreen());
	}

	public StageManager getStageManager() {
		return STAGE_MANAGER;
	}

	/**
	 * Returns current language
	 * Useful in instantiating new AuthoringController with correct language 
	 * 	when user decides to continue authoring the game
	 * @return String representing the language
	 */
	public String getLanguage() {
		return myLanguage;
	}

	public void setGameFilePath(String filepath) {
		myGameFilePath = filepath; 
	}

	/**
	 * Returns filepath of XML of current game
	 * Useful in instantiating new AuthoringController with correct language 
	 * 	when user decides to continue authoring the game
	 * @return String XML filepath
	 */
	public String getGameFilePath() {
		return myGameFilePath; 
	}

	public void remove(FrontEndSprite sprite) {
		checkGameScreenInitialization();
		GAME_SCREEN.remove(sprite);
	}

	public void setAvailableTowers(List<FrontEndTower> availableTowers) {
		checkGameScreenInitialization();
		GAME_SCREEN.setAvailbleTowers(availableTowers);
	}


	public boolean setPath(Map<String, List<Point>> imageMap, String backgroundImageFilePath, int pathSize, int width, int height, boolean transparent) {
		checkGameScreenInitialization();
		return GAME_SCREEN.setPath(imageMap, backgroundImageFilePath, pathSize, width, height, transparent);
	}

	public void attachListeners(IntegerProperty myCurrency, IntegerProperty myScore,
			IntegerProperty myLives) {
		checkGameScreenInitialization();
		GAME_SCREEN.attachListeners(myCurrency, myScore, myLives);	    
	}


	public Map<String,String> getGameplayerProperties() {
		return GAMEPLAYER_PROPERTIES;
	}

	/**
	 * Called before GameScreen is used to eliminate OrderOfCall dependencies
	 */
	private void checkGameScreenInitialization() {
		if(GAME_SCREEN == null) {
			GAME_SCREEN = new GameScreen(this, PROMPTS, MEDIATOR);
		}
	}

	public GameScreen getGameScreen() {
		return GAME_SCREEN;
	}
	
	public void moveTower(FrontEndTower tower) {
	    System.out.println("LOOKING FOR KEY EVENT ");
	        STAGE_MANAGER.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
	            @Override
	            public void handle(KeyEvent event) {
	               MEDIATOR.moveTowers(tower, event.getCode());
	            }
	        });
	}
}
