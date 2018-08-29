package gameplayer.panel;

import gameplayer.GameplayerAlert;
import gameplayer.screen.GameScreen;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.javafx.tools.packager.Log;

import javafx.scene.control.ScrollPane;
import frontend.PropertiesReader;
import frontend.UIFactory;
import authoring.frontend.exceptions.MissingPropertiesException;
import engine.sprites.towers.FrontEndTower;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


/**
 * @Author Alexi Kontos & Andrew Arnold
 */


public class UpgradePanel extends SpecificPanel {



    private final GameScreen GAME_SCREEN;
    private final UIFactory UI_FACTORY;
    private PropertiesReader PROP_READ;
	private Map<String,String> GAMEPLAYER_PROPERTIES;
	private GameplayerAlert ALERT;

    public UpgradePanel (GameScreen gameScreen, FrontEndTower tower) {
	super(tower);
	GAME_SCREEN = gameScreen;
	GAMEPLAYER_PROPERTIES = GAME_SCREEN.getGameplayerProperties();
	UI_FACTORY = new UIFactory();
	PROP_READ = new PropertiesReader();
    }


    @Override
    public void makePanel() {
	HBox towerUpgrades = new HBox();
	fillUpgrades(towerUpgrades);
	towerUpgrades.setFillHeight(true);
	towerUpgrades.setAlignment(Pos.CENTER);
	towerUpgrades.setMaxWidth(Double.MAX_VALUE);
	ScrollPane upgradeDisplay = new ScrollPane(towerUpgrades);
	upgradeDisplay.setFitToWidth(true);

	VBox panelRoot = new VBox(upgradeDisplay);
	VBox.setVgrow(upgradeDisplay, Priority.ALWAYS);
	panelRoot.setAlignment(Pos.CENTER);
	panelRoot.setId(GAMEPLAYER_PROPERTIES.get("upgradePanelID"));
	PANEL = panelRoot;
    }

    private void fillUpgrades(HBox availUpgrades) {
	HBox upgrades = availUpgrades;
	Integer UPGRADE_IMAGE_SIZE = Integer.parseInt(GAMEPLAYER_PROPERTIES.get("upgradeImageSize"));
	String UPGRADE_NAMES_FILE_PATH = GAMEPLAYER_PROPERTIES.get("upgradeNamesFilePath");
	try {
	    Map<String, Image> upgradeMap = PROP_READ.keyToImageMap(UPGRADE_NAMES_FILE_PATH, UPGRADE_IMAGE_SIZE, UPGRADE_IMAGE_SIZE);
	    for (Entry<String, Image> entry: upgradeMap.entrySet()) {
			Button upgradeButton = UI_FACTORY.makeImageButton(GAMEPLAYER_PROPERTIES.get("buttonID"), upgradeMap.get(entry.getKey()));
			upgradeButton.setOnMouseClicked(arg0 -> GAME_SCREEN.upgradeClickedOn(TOWER, entry.getKey()));
			upgrades.getChildren().add(upgradeButton);
			HBox.setHgrow(upgradeButton, Priority.ALWAYS);
			upgradeButton.setMaxWidth(Double.MAX_VALUE);
			upgradeButton.setMaxHeight(Double.MAX_VALUE);
	    }
	}
	catch (MissingPropertiesException e) {
	    Log.debug(e);
		ALERT = new GameplayerAlert(e.getMessage());
	   // System.out.println("upgrade image load fail");

	}
    }
}
