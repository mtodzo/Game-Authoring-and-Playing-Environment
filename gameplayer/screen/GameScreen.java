package gameplayer.screen;

import authoring.frontend.exceptions.MissingPropertiesException;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import com.sun.javafx.tools.packager.Log;
import authoring.AuthoringController;
import engine.Mediator;
import engine.sprites.FrontEndSprite;
import engine.sprites.towers.CannotAffordException;
import engine.sprites.towers.FrontEndTower;
import frontend.PromptReader;
import frontend.Screen;
import frontend.View;
import gameplayer.BrowserPopup;
import gameplayer.GameplayerAlert;
import gameplayer.ScreenManager;
import gameplayer.panel.*;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import voogasalad.util.soundfactory.*;

/**
 * @Author Alexi Kontos & Andrew Arnold
 */
public class GameScreen extends Screen {
    public static final String DEFAULT_POPUP_STYLESHEET = "styling/GameAuthoringStartScreen.css";
    public static final String PROPERTIES_FILE_PATH = "src/sound/resources/soundFiles.properties";
    //public static final String PROPERTIES_FILE_PATH = "src/sound/resources/soundFiles.properties";

    private final PromptReader PROMPTS;
    private TowerPanel TOWER_PANEL;
    private GamePanel GAME_PANEL;
    private ScorePanel SCORE_PANEL;
    private ControlsPanel CONTROLS_PANEL;
    private UpgradePanel UPGRADE_PANEL;
    private ScreenManager SCREEN_MANAGER;
    private BorderPane displayPane;
    private BorderPane gamePane;
    private final Mediator MEDIATOR;
    private BorderPane rootPane;
    private SoundFactory SOUND_FACTORY;
    private Map<String,String> GAMEPLAYER_PROPERTIES;
    private GameplayerAlert ALERT;

    public GameScreen(ScreenManager ScreenController, PromptReader promptReader, Mediator mediator) {
	SCREEN_MANAGER = ScreenController;
	GAMEPLAYER_PROPERTIES = SCREEN_MANAGER.getGameplayerProperties();
	setStyleSheet(DEFAULT_POPUP_STYLESHEET);
	PROMPTS = promptReader;
	MEDIATOR = mediator;
	SOUND_FACTORY = MEDIATOR.getSoundFactory();
	TOWER_PANEL = new TowerPanel(this, SCREEN_MANAGER);
	CONTROLS_PANEL = new ControlsPanel(this, PROMPTS, SCREEN_MANAGER);
	SCORE_PANEL = new ScorePanel(this);
	GAME_PANEL = new GamePanel(this);
    }


    @Override
    public Parent makeScreenWithoutStyling() {
	MEDIATOR.pause();
	rootPane = new BorderPane();
	rootPane.setId(GAMEPLAYER_PROPERTIES.get("GameScreenRootID"));

	displayPane = new BorderPane();
	displayPane.setCenter(TOWER_PANEL.getPanel());
	displayPane.setBottom(CONTROLS_PANEL.getPanel());

	SplashPanel SPLASH_PANEL = new SplashPanel(this, GAMEPLAYER_PROPERTIES.get("gameStart"));
	SPLASH_PANEL.getPanel().setOnMouseClicked(arg0 -> {
	    gameStart();
	});

	gamePane = new BorderPane();
	gamePane.setTop(SCORE_PANEL.getPanel());
	gamePane.setCenter(SPLASH_PANEL.getPanel());
	MEDIATOR.pause();
	rootPane.setCenter(gamePane);
	setVertPanelsLeft();
	return rootPane;

    }

    public void towerSelectedForPlacement(FrontEndTower tower) {
	GAME_PANEL.towerSelected(tower);
	TowerInfoPanel TOWER_INFO_PANEL = new TowerInfoPanel(this,PROMPTS,tower);
	displayPane.setBottom(TOWER_INFO_PANEL.getPanel());
    }


    @Override
    protected View getView() {
	return SCREEN_MANAGER;
    }

    public void displaySprite(FrontEndSprite sprite) {
	GAME_PANEL.addSprite(sprite);
    }

    public void remove(FrontEndSprite sprite) {
	GAME_PANEL.removeSprite(sprite);
    }

    public void setAvailbleTowers(List<FrontEndTower> availableTowers) {
	TOWER_PANEL.setAvailableTowers(availableTowers);
    }

    public void loadErrorScreen(String message) {
	SCREEN_MANAGER.loadErrorScreen(message);
    }

    //TODO implement reflection//rest of controls
    public void controlTriggered(String control) throws MissingPropertiesException {
	if(control.equals(GAMEPLAYER_PROPERTIES.get("play"))) {
	    MEDIATOR.play();
	}
	else if(control.equals(GAMEPLAYER_PROPERTIES.get("pause"))) {
	    MEDIATOR.pause();
	}
	else if(control.equals(GAMEPLAYER_PROPERTIES.get("speedup"))) {
	    MEDIATOR.fastForward(Integer.parseInt(GAMEPLAYER_PROPERTIES.get("fastForwardSize")));
	}
	else if(control.equals(GAMEPLAYER_PROPERTIES.get("quit"))) { //WHY DO I HAVE TO MAKE A NEW PLAY-CONTROLLER OH MY GOD
	    getView().playControllerInstructions();
	}
	else if (control.equals(GAMEPLAYER_PROPERTIES.get("edit"))) { // Susie added this
	    MEDIATOR.endLoop();
	    AuthoringController authoringController = new AuthoringController(SCREEN_MANAGER.getStageManager(), SCREEN_MANAGER.getLanguage());
	    authoringController.setModel(SCREEN_MANAGER.getGameFilePath());
	}
	else if (control.equals(GAMEPLAYER_PROPERTIES.get("settings"))) {
	    settingsClickedOn();
	}
	else if (control.equals(GAMEPLAYER_PROPERTIES.get("restart"))) {
	    MEDIATOR.restartLevel();
	}
    }

    public void settingsTriggered(String setting) {
	if (setting.equals(GAMEPLAYER_PROPERTIES.get("volumeToggle"))) {
	    SOUND_FACTORY.mute();
	} else if (setting.equals(GAMEPLAYER_PROPERTIES.get("playMusic"))) {
	    SOUND_FACTORY.playBackgroundMusic();


	} else if (setting.equals(GAMEPLAYER_PROPERTIES.get("pauseMusic"))) {
	    SOUND_FACTORY.pauseBackgroundMusic();
	} else if (setting.equals(GAMEPLAYER_PROPERTIES.get("instructions"))) {
	    Stage pop = new Stage();
	    VBox vb = new VBox();
	    Text text = new Text(MEDIATOR.getInstructions());
	    text.setWrappingWidth(Integer.parseInt(GAMEPLAYER_PROPERTIES.get("InstructionsHeight")));
	    vb.getChildren().add(text);
	    Scene unstyled = new Scene(vb);
	    vb.getStylesheets().add(DEFAULT_POPUP_STYLESHEET);

	    pop.setScene(unstyled);
	    pop.setHeight(Integer.parseInt(GAMEPLAYER_PROPERTIES.get("InstructionsHeight")));
	    pop.show();
	} else if (setting.equals(GAMEPLAYER_PROPERTIES.get("help"))) {
	    BrowserPopup pop = new BrowserPopup(GAMEPLAYER_PROPERTIES.get("helpURL"), GAMEPLAYER_PROPERTIES);
	    pop.makePopupBrowser();
	}
    }


    /**
     * Attaches listener which trigger automatic GamePlayer updates to the Engine's currency, score and health
     * Additionally synchronizes the initial display value of each to the passed values
     * @param myCurrency	Engine's currency object
     * @param myScore	Engine's score object
     * @param myLives	Engine's lives object
     */
    public void attachListeners(IntegerProperty myCurrency, IntegerProperty myScore, IntegerProperty myLives) {
	myCurrency.addListener(TOWER_PANEL.createCurrencyListener(myCurrency.get()));
	myScore.addListener(SCORE_PANEL.createScoreListener(myScore.get()));
	myLives.addListener(SCORE_PANEL.createHealthListener(myLives.get()));
    }

    public void updateLevel(Integer newLevel) {
	SCORE_PANEL.updateLevel(newLevel);
    }


    public FrontEndTower placeTower(FrontEndTower tower, Point position) throws CannotAffordException, MissingPropertiesException {
	return MEDIATOR.placeTower(position, tower.getName());
    }


    public void towerClickedOn(FrontEndTower tower) {
	SCREEN_MANAGER.moveTower(tower);
	TowerInfoPanel TOWER_INFO_PANEL = new TowerInfoPanel(this,PROMPTS,tower);
	UPGRADE_PANEL = new UpgradePanel(this, tower);
	displayPane.setBottom(TOWER_INFO_PANEL.getPanel());
	gamePane.setBottom(UPGRADE_PANEL.getPanel());
    }

    public void upgradeClickedOn(FrontEndTower tower, String upgradeName) {
	BuyPanel BUY_PANEL = new BuyPanel(this,PROMPTS, tower,upgradeName);
	displayPane.setBottom(BUY_PANEL.getPanel());
	gamePane.setBottom(UPGRADE_PANEL.getPanel());
    }


    private void settingsClickedOn() {
	SettingsPanel SETTINGS_PANEL = new SettingsPanel(this);
	displayPane.setBottom(SETTINGS_PANEL.getPanel());
    }

    public void blankGamePanelClick() {
	gamePane.setBottom(null);
	displayPane.setBottom(CONTROLS_PANEL.getPanel());
    }

    public void sellTower(FrontEndTower tower) {
	GAME_PANEL.removeTower(tower);
	MEDIATOR.sellTower(tower);
	blankGamePanelClick();
    }

    public boolean setPath(Map<String, List<Point>> imageMap, String backgroundImageFilePath, int pathSize, int width, int height, boolean transparent) {
	return GAME_PANEL.setPath(imageMap, backgroundImageFilePath, pathSize, width, height, transparent);
    }


    private void setVertPanelsLeft() {
	rootPane.getChildren().remove(displayPane);
	rootPane.setRight(null);
	rootPane.setLeft(displayPane);

    }
    private void setVertPanelsRight() {
	rootPane.getChildren().remove(displayPane);
	rootPane.setLeft(null);
	rootPane.setRight(displayPane);
    }

    public void swapVertPanel() {
	if(rootPane.getRight() == null) {
	    setVertPanelsRight();
	}
	else {
	    setVertPanelsLeft();
	}
    }

    public String getGameName() {
	return SCREEN_MANAGER.getGameFilePath();
    }

    public ScreenManager getScreenManager() {
	return SCREEN_MANAGER;
    }

    public void upgradeBought(FrontEndTower tower, String upgradeName) {
	//System.out.println("upgrade has bought");
	MEDIATOR.upgradeTower(tower, upgradeName);
    }


    public SoundFactory getSoundFactory() {
	return SOUND_FACTORY;
    }

    public Map<String,String> getGameplayerProperties() {
	return GAMEPLAYER_PROPERTIES;
    }

    public void gameWon() {
	MEDIATOR.pause();
	SplashPanel SPLASH_PANEL = new SplashPanel(this, GAMEPLAYER_PROPERTIES.get("gameWon"));
	gamePane.setCenter(SPLASH_PANEL.getPanel());
    }

    public void gameLost() {
	MEDIATOR.pause();
	SplashPanel SPLASH_PANEL = new SplashPanel(this,GAMEPLAYER_PROPERTIES.get("gameLost"));
	gamePane.setCenter(SPLASH_PANEL.getPanel());
    }

    public void nextLevel() {
	SplashPanel SPLASH_PANEL = new SplashPanel(this, GAMEPLAYER_PROPERTIES.get("nextLevel"));
	gamePane.setCenter(SPLASH_PANEL.getPanel());
	MEDIATOR.pause();
	SPLASH_PANEL.getPanel().setOnMouseClicked(arg0 -> newLevel());

    }

    private void newLevel() {
	gamePane.setCenter(GAME_PANEL.getPanel());
	MEDIATOR.play();
    }

    private void gameStart() {
	gamePane.setCenter(GAME_PANEL.getPanel());
	MEDIATOR.play();
    }

    public String getInstructions() {
	return MEDIATOR.getInstructions();
    }


    public void clickToShoot(FrontEndTower clickedTower, double x, double y) {
	//System.out.println("going to mediator");
	try {
	    MEDIATOR.handleTowerClickToShoot(clickedTower, x, y);
	}catch(MissingPropertiesException e) {
	    SCREEN_MANAGER.loadErrorScreen("NoFile");
	}

    }

}
