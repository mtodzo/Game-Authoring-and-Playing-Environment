/**
 * @author susiechoi
 * Creates screen in which user can customize what aspect of the level to customize (tower, path, etc)
 */
package authoring.frontend;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class CustomizeLevelScreen extends AuthoringScreen {
	
	public static final String DEFAULT_CUSTOMIZETOWER_KEY = "CustomizeTower";
	public static final String DEFAULT_CUSTOMIZEENEMY_KEY = "CustomizeEnemy";
	public static final String DEFAULT_CUSTOMIZEPATH_KEY = "CustomizePath";
	public static final String DEFAULT_CUSTOMIZEWAVE_KEY = "CustomizeWave";
	public static final String DEFAULT_TITLE_SEPARATOR = " ";
	
	protected CustomizeLevelScreen(AuthoringView view) {
		super(view);
//		System.out.println("trying to make a customizelevelscreen");
		setSaved();
	}

	/**
	 * Makes all UI elements (specifically buttons) that physically create
	 * the screen.
	 * @see frontend.Screen#makeScreenWithoutStyling()
	 */
	@Override
	public Parent makeScreenWithoutStyling() {
		VBox vb = new VBox(); 

		vb.getChildren().add(getUIFactory().makeScreenTitleText(getErrorCheckedPrompt("CustomizeLevel")+DEFAULT_TITLE_SEPARATOR+getView().getLevel()));

		Button towersButton = getUIFactory().makeTextButton(getErrorCheckedPrompt(DEFAULT_CUSTOMIZETOWER_KEY));
		towersButton.setOnAction(e -> {getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_CUSTOMIZETOWER_KEY);});
		vb.getChildren().add(towersButton);

		Button enemiesButton = getUIFactory().makeTextButton(getErrorCheckedPrompt(DEFAULT_CUSTOMIZEENEMY_KEY));
		enemiesButton.setOnAction(e -> {getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_CUSTOMIZEENEMY_KEY);});
		vb.getChildren().add(enemiesButton);
		
		Button pathButton = getUIFactory().makeTextButton(getErrorCheckedPrompt(DEFAULT_CUSTOMIZEPATH_KEY));
		pathButton.setOnAction(e -> {getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_CUSTOMIZEPATH_KEY);});
		vb.getChildren().add(pathButton);

		Button waveButton = getUIFactory().makeTextButton(getErrorCheckedPrompt(DEFAULT_CUSTOMIZEWAVE_KEY));
		waveButton.setOnAction(e -> {getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_CUSTOMIZEWAVE_KEY);});
		vb.getChildren().add(waveButton);
		Button backButton = setupBackButton();
		vb.getChildren().add(backButton);

		return vb;
	}


}
