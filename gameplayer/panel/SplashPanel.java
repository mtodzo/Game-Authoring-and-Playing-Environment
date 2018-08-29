package gameplayer.panel;

import authoring.frontend.exceptions.MissingPropertiesException;
import gameplayer.screen.GameScreen;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.sun.javafx.tools.packager.Log;

import java.util.Map;

/**
 * @Author Alexi Kontos
 */


public class SplashPanel extends Panel {

    private GameScreen GAME_SCREEN;
    private String GAME_STATE;
    private Map<String,String> GAMEPLAYER_PROPERTIES;
    private String GAME_INSTRUCTIONS;

    public SplashPanel(GameScreen gameScreen, String gameState) {
        GAME_SCREEN = gameScreen;
        GAMEPLAYER_PROPERTIES = GAME_SCREEN.getGameplayerProperties();
        GAME_STATE = gameState;
        //GAME_INSTRUCTIONS = GAME_SCREEN.getInstructions();
    }

    @Override
    public void makePanel() {
        Label titleLabel = new Label();
        Label infoLabel = new Label();

        if (GAME_STATE.equals(GAMEPLAYER_PROPERTIES.get("gameStart"))) {
            titleLabel.setText(GAMEPLAYER_PROPERTIES.get("gameStartTitle"));
            infoLabel.setText(GAMEPLAYER_PROPERTIES.get(GAME_INSTRUCTIONS));
        }
        else if (GAME_STATE.equals(GAMEPLAYER_PROPERTIES.get("nextLevel"))) {
            titleLabel.setText(GAMEPLAYER_PROPERTIES.get("nextLevelTitle"));
            infoLabel.setText(GAMEPLAYER_PROPERTIES.get("nextLevelInfo"));
        }
        else if (GAME_STATE.equals(GAMEPLAYER_PROPERTIES.get("gameWon"))) {
            titleLabel.setText(GAMEPLAYER_PROPERTIES.get("gameWinTitle"));
            infoLabel.setText(GAMEPLAYER_PROPERTIES.get("gameWinInfo"));
        }
        else if (GAME_STATE.equals(GAMEPLAYER_PROPERTIES.get("gameLost"))) {
            titleLabel.setText(GAMEPLAYER_PROPERTIES.get("gameLoseTitle"));
            infoLabel.setText(GAMEPLAYER_PROPERTIES.get("gameLoseInfo"));
        }
        VBox panelRoot = new VBox(titleLabel, infoLabel);
        PANEL = panelRoot;

    }
}
