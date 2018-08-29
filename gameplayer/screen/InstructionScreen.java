
package gameplayer.screen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import frontend.PromptReader;
import frontend.Screen;
import frontend.UIFactory;
import frontend.View;
import gameplayer.ScreenManager;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Map;
import java.util.logging.Logger;

import com.sun.javafx.tools.packager.Log;

import authoring.frontend.exceptions.MissingPropertiesException;

/**
 * @Author Alexi Kontos & Andrew Arnold
 */


public class InstructionScreen extends Screen {
	private String DEFAULT_SHARED_STYLESHEET;
	private final ScreenManager SCREEN_MANAGER;
	private Map<String,String> GAMEPLAYER_PROPERTIES;
	private final PromptReader PROMPTS;
	private final UIFactory UIFACTORY;
	private Button continueButt;

	public InstructionScreen(ScreenManager screenManager, PromptReader promptReader) {
		SCREEN_MANAGER = screenManager;
		GAMEPLAYER_PROPERTIES = SCREEN_MANAGER.getGameplayerProperties();
		UIFACTORY = new UIFactory();
		PROMPTS = promptReader;
		DEFAULT_SHARED_STYLESHEET = GAMEPLAYER_PROPERTIES.get("sharedStylingSheet");
		setStyleSheet(DEFAULT_SHARED_STYLESHEET);
	}

	@Override
	public Parent makeScreenWithoutStyling() {
		VBox rootBox = new VBox();
		String titleText = GAMEPLAYER_PROPERTIES.get("newGameText"); 
		Text title = getUIFactory().makeScreenTitleText(titleText);
		//	Button newGameButt = UIFACTORY.makeTextButton(".button", PROMPTS.resourceDisplayText("NewGameButton"));
		//	newGameButt.setOnMouseClicked((arg0) -> SCREEN_MANAGER.loadGameScreenNew());

		ArrayList<String> gameOptions = new ArrayList<>(); 
		gameOptions.add(titleText);
		gameOptions.addAll(gameOptions());
		ComboBox<String> allGames = UIFACTORY.makeTextDropdown("", gameOptions);
		allGames.setOnAction(click ->{ 
			continueButt.setDisable(false);
		});

		continueButt = UIFACTORY.makeTextButton(GAMEPLAYER_PROPERTIES.get("buttonID"), PROMPTS.resourceDisplayText("ContinueButton"));
		continueButt.setDisable(true);
		continueButt.setOnMouseClicked(arg0 -> {
		    try {
			SCREEN_MANAGER.loadGameScreenNew(allGames.getValue());
		    } catch (MissingPropertiesException e1) {
			// TODO Auto-generated catch block
			Log.debug(e1);
			e1.printStackTrace();
			getView().loadErrorScreen("NoFile");
		    }
		});
		//	continueButt.setOnMouseClicked((arg0) -> SCREEN_MANAGER.loadGameScreenContinuation());
		Button backButton = UIFACTORY.setupBackButton(e->{
			SCREEN_MANAGER.toMain();
		}, PROMPTS.resourceDisplayText("Cancel"));

		VBox center = new VBox(title, allGames, continueButt, backButton);
		center.setAlignment(Pos.CENTER);
		center.setMaxWidth(Double.MAX_VALUE);
		VBox.setVgrow(center, Priority.ALWAYS);

		rootBox.getChildren().add(center);
		return rootBox;
	}

	/**
	 * Returns all models in savedModels directory for display on the instruction screen
	 * 
	 * @return List<String>: containing all game options
	 */
	private List<String> gameOptions(){
		File[] files = new File(GAMEPLAYER_PROPERTIES.get("savedModelsPathname")).listFiles();
		List<String> ret = new ArrayList<>();
		for(File file : files){
			ret.add(file.getName().substring(0, file.getName().indexOf(".")));
		}
		return ret;
	}


	@Override
	protected View getView() {
		return SCREEN_MANAGER;
	}
}

