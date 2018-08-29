/**
 * Class to create the original screen users see when entering the Game Authoring environment. 
 * Dependent on the AuthoringView to create it correctly and on the UIFactory to make UI
 * elements correctly. Also dependent on View to correctly lead Screenflow and load the next
 * screen.
 * @author Sarahbland
 * @author susiechoi
 *
 */

package authoring.frontend;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.tools.packager.Log;

import authoring.frontend.exceptions.MissingPropertiesException;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StartScreen extends AuthoringScreen {
	
	public static final String DEFAULT_NEW_SCREENFLOW_IDETIFIER = "New";
	public static final String DEFAULT_NOFILE_ERROR_KEY = "NoFile";
	public static final String DEFAULT_XML_FOLDER = "/SavedModels";
	public static final String DEFAULT_THEMES = "images/ThemeSpecificImages/Themes.properties";
	public static final String DEFAULT_STYLINGS  = "src/styling/CurrentCSS.properties";
	
	private AuthoringView myView; 
	private final List<String> myCSSFiles; 
	private int currCSSIndex; 

	protected StartScreen(AuthoringView view) {
		super(view);
		setSaved();
		myView = view; 
		List<String> css = null;
		try {
			css = myView.getPropertiesReader().findVals(DEFAULT_STYLINGS);
		} catch (MissingPropertiesException e) {
		    Log.debug(e);
			myView.loadErrorScreen("NoCSS");
		} 
		myCSSFiles = css; 
		currCSSIndex = 0; 
	}

	/**
	 * Creates UI elements necessary to display to user (i.e. selection of which game to edit or to 
	 * create a new game).
	 * @see frontend.Screen#makeScreenWithoutStyling()
	 */
	@Override

	public Parent makeScreenWithoutStyling() {
		Text startHeading = new Text();
		VBox vbox = new VBox();
		startHeading = getUIFactory().makeScreenTitleText(getErrorCheckedPrompt("StartScreenHeader"));
		vbox.getChildren().add(startHeading);

		setupNewGameComponents(vbox);
		setupEditGameComponents(vbox);

		Button changeCSS = getUIFactory().makeTextButton(getErrorCheckedPrompt("ChangeStyling"));
		changeCSS.setOnAction(e -> {
			currCSSIndex++; 
			if (currCSSIndex > myCSSFiles.size()-1) {
				currCSSIndex = 0; 
			}
			myView.setCurrentCSS(myCSSFiles.get(currCSSIndex));
		});
		
		vbox.getChildren().add(changeCSS);
		
		Button backButton = setupBackButton(); 
		vbox.getChildren().add(backButton);
		
		return vbox;
	}

	private void setupNewGameComponents(VBox vbox) {
		ArrayList<String> existingThemes = new ArrayList<>(); 
		existingThemes.add(getErrorCheckedPrompt("ThemeSelector"));
		try {
			existingThemes.addAll(getPropertiesReader().findVals(DEFAULT_THEMES));
		} catch (MissingPropertiesException e1) {
		    Log.debug(e1);
			getView().loadErrorScreen(DEFAULT_NOFILE_ERROR_KEY);
		}

		String newGameButtonPrompt = getErrorCheckedPrompt("NewGameButtonLabel"); 
		Button newGameButton = getUIFactory().makeTextButton("", newGameButtonPrompt);
		ComboBox<String> themeChooser = getUIFactory().makeTextDropdownSelectAction("", existingThemes, e -> {
			newGameButton.setDisable(false);}, e -> {newGameButton.setDisable(true);}, newGameButtonPrompt);
		themeChooser.addEventHandler(ActionEvent.ACTION, e -> {
			getView().setTheme(themeChooser.getSelectionModel().getSelectedItem()); 
		});
		newGameButton.setDisable(true);
		newGameButton.setOnAction(e -> {
			getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_NEW_SCREENFLOW_IDETIFIER, getErrorCheckedPrompt("NewGame"));
		});
		
		HBox newGame = new HBox(); 
		newGame.getChildren().add(themeChooser);
		newGame.getChildren().add(newGameButton);
		vbox.getChildren().add(newGame);
	}
	
	private void setupEditGameComponents(VBox vbox) {
		List<String> existingGames = new ArrayList<>();
		String gameNamePrompt = getErrorCheckedPrompt("GameEditSelector");
		existingGames.add(gameNamePrompt);
		existingGames.addAll(getUIFactory().getFileNames(DEFAULT_XML_FOLDER));
		Button editButton = getUIFactory().makeTextButton("editbutton", getErrorCheckedPrompt("EditButtonLabel"));
		ComboBox<String> gameChooser = getUIFactory().makeTextDropdownSelectAction(existingGames, e -> {
			editButton.setDisable(false);}, e -> {editButton.setDisable(true);}, gameNamePrompt);
		editButton.setDisable(true);
		editButton.setOnAction(e -> {
			try {
				getView().readFromFile(gameChooser.getValue());
			} catch (MissingPropertiesException e1) {
			    Log.debug(e1);
				getView().loadErrorScreen("NoObject");
			}
		});
		HBox editExisting = new HBox();
		editExisting.getChildren().add(gameChooser);
		editExisting.getChildren().add(editButton);
		vbox.getChildren().add(editExisting);
	}


}
