/**
 * Class to create Screen where users choose which elements they would like to customize (a level,
 * settings, etc.) or save/demo a game. Dependent on the View to house/relay information from the Model
 * and on View screenflow methods to send the user to the correct next screen.
 * @author Sarahbland
 * @author susiechoi
 */

package authoring.frontend;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.tools.packager.Log;

import authoring.frontend.exceptions.MissingPropertiesException;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import xml.BadGameDataException;

public class CustomizationChoicesScreen extends AuthoringScreen {
	
	public static final String TEST_PROPERTIES = "images/TestProperties.properties";
	public static final String DEFAULT_EDITEXISTINGLEVEL_KEY = "EditExistingLevel"; 
	public static final String DEFAULT_GRAPHS_KEY = "Graphs"; 
	
	protected CustomizationChoicesScreen(AuthoringView view) {
	    	super(view);
	}
	
	/**
	 * Creates UI elements (buttons, selectors) necessary for User to choose next customization
	 * or demo/save.
	 * @see frontend.Screen#makeScreenWithoutStyling()
	 */
	@Override
	public Parent makeScreenWithoutStyling(){
		VBox vbox = new VBox();
		HBox hbox = new HBox();
		HBox newLevelHBox = new HBox();

		Text heading = getUIFactory().makeScreenTitleText(getView().getGameName());
		vbox.getChildren().add(heading);

		Button resourcesButton = getUIFactory().makeTextButton(getErrorCheckedPrompt("CustomizeResources"));
		resourcesButton.setOnAction(e -> {getView().goForwardFrom(this.getClass().getSimpleName()+"ResourcesButton");});
		vbox.getChildren().add(resourcesButton);

		Button newLevelButton = getUIFactory().makeTextButton(getErrorCheckedPrompt("CreateLevelLabel"));
		newLevelButton.setOnAction(e -> {
			getView().addNewLevel();
			getView().goForwardFrom(this.getClass().getSimpleName()+"EditNewLevel");
		});
		Button demoButton = getUIFactory().makeTextButton(getErrorCheckedPrompt("DemoLabel"));
		demoButton.setOnAction(e -> {
		    	try {
			    getView().writeToFile();
			} catch (BadGameDataException e1) {
			    getView().loadErrorScreen("Authored model is badly formatted");
			} catch (IOException e1) {
			    getView().loadErrorScreen("File couldn't be written");
			}
		    	getView().playControllerDemo();
		});
		Button saveButton = getUIFactory().makeTextButton(getErrorCheckedPrompt("SaveLabel"));
		saveButton.setDisable(false);
		saveButton.setOnAction(e -> {
		    	setSaved();
		    	saveButton.setDisable(true);
			try {
			    getView().writeToFile();
			} catch (BadGameDataException e1) {
			    getView().loadErrorScreen("Authored model is badly formatted");
			} catch (IOException e1) {
			    getView().loadErrorScreen("File couldn't be written");
			}
		});
		Button mainButton = setupBackButton();
		String levelPrompt = getErrorCheckedPrompt("EditDropdownLabel");

		List<String> currentLevels = new ArrayList<String>();
		currentLevels.add(levelPrompt);
		currentLevels.addAll(getView().getLevels()); 
		if (currentLevels.size() > 0) {
			Button editButton = getUIFactory().makeTextButton(levelPrompt);
			ComboBox<String> levelChooser = getUIFactory().makeTextDropdownSelectAction(currentLevels, e -> {
				editButton.setDisable(false);}, e -> {editButton.setDisable(true);}, levelPrompt);
			editButton.setDisable(true);
			editButton.setOnAction(e -> {
				getView().setLevel(Integer.parseInt(levelChooser.getValue()));
				getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_EDITEXISTINGLEVEL_KEY);
			});
			Button autogenerateButton = getUIFactory().makeTextButton(getErrorCheckedPrompt("AutogenerateLevel"));
			autogenerateButton.setOnAction(e -> {
				try {
				    getView().autogenerateLevel();
				} catch (MissingPropertiesException e1) {
				    // TODO Auto-generated catch block
				    Log.debug(e1);
				    e1.printStackTrace();
				    getView().loadErrorScreen("NoFile");
				} 
				getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_EDITEXISTINGLEVEL_KEY);
			});
			hbox.getChildren().add(levelChooser);
			hbox.getChildren().add(editButton);
			newLevelHBox.getChildren().add(newLevelButton);
			newLevelHBox.getChildren().add(autogenerateButton);
		}
		

		Button visualizations = getUIFactory().makeTextButton(getErrorCheckedPrompt(DEFAULT_GRAPHS_KEY));

		visualizations.setOnAction(click-> {
			getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_GRAPHS_KEY);
		});
		
		vbox.getChildren().addAll(newLevelHBox, hbox, demoButton, saveButton, visualizations, mainButton);
		//vbox.getChildren().add(songPrompted); TODO: change to mp3 selector and readd
		return vbox;

	}

}