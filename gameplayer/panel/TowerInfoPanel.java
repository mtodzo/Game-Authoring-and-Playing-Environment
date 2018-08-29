package gameplayer.panel;

import java.util.Map;

import engine.sprites.towers.FrontEndTower;
import frontend.PromptReader;
import frontend.UIFactory;
import gameplayer.screen.GameScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @Author Alexi Kontos & Andrew Arnold
 */


public class TowerInfoPanel extends SpecificPanel {

	private final GameScreen GAME_SCREEN;
	private final UIFactory UI_FACTORY;
	private PromptReader PROMPTS;
	private Map<String,String> GAMEPLAYER_PROPERTIES;

	public TowerInfoPanel(GameScreen gameScreen, PromptReader promptReader, FrontEndTower tower) {
		super(tower);
		GAME_SCREEN = gameScreen;
		GAMEPLAYER_PROPERTIES = GAME_SCREEN.getGameplayerProperties();
		PROMPTS = promptReader;	
		UI_FACTORY = new UIFactory();
	}

	@Override
	public void makePanel() {
		Map<String,Integer> towerStats = TOWER.getTowerStats();
		Label TowerInfo = new Label(prepareStats(towerStats));
		Button sellTower = UI_FACTORY.makeTextButton(GAMEPLAYER_PROPERTIES.get("buttonID"), PROMPTS.resourceDisplayText("SellTowerButton"));
		sellTower.setOnMouseClicked(arg0 -> GAME_SCREEN.sellTower(TOWER));

		VBox panelRoot = new VBox(TowerInfo, sellTower);
		VBox.setVgrow(sellTower, Priority.ALWAYS);
		panelRoot.setAlignment(Pos.CENTER);
		panelRoot.setId(GAMEPLAYER_PROPERTIES.get("sellTowerPanelID"));
		PANEL = panelRoot;
	}

	private String prepareStats(Map<String,Integer> towerStats) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String,Integer> key: towerStats.entrySet()) {
			sb.append(key.getKey() + ":" + towerStats.get(key.getKey()) + "\n");
		}
		return sb.toString();
	}


}
