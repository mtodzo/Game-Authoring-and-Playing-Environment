/**
 * @author susiechoi
 * @author sarahbland
 * Creates screen in which user can customize the starting resources of the player
 */

package authoring.frontend;

import com.sun.javafx.tools.packager.Log;

import authoring.frontend.exceptions.MissingPropertiesException;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdjustResourcesScreen extends AdjustNewOrExistingScreen {

    public static final String DEFAULT_GAME_NAME_KEY = "myGameName"; 
    public static final String DEFAULT_INSTRUCTIONS_KEY = "myInstructions";
    public static final String DEFAULT_HEALTH_KEY = "myStartingHealth";
    public static final String DEFAULT_MONEY_KEY = "myStartingMoney";
    public static final String BACKGROUND_MUSIC_KEY = "myBackgroundMusic";
    public static final String LEVEL_WIN_SOUND_KEY = "myLevelWinSound";
    public static final String LEVEL_LOSS_SOUND_KEY = "myLevelLossSound";
    public static final String DEFAULT_CSS_STYLES = "src/styling/CurrentCSS.properties";
    public static final String OBJECT_TYPE = "Settings";
    public static final String DEFAULT_SOUND_FILEPATH = "src/sound/resources/soundFiles.properties";

    private TextField myGameNameEntry;
    private Slider myStartingHealthSlider;
    private Slider myStartingCurrencySlider;
    private TextArea myGameInstructionsEntry;
    private String myBackgroundMusic;
    private String myLevelWinSound;
    private String myLevelLossSound;
    //	private ComboBox<String> myCSSFilenameChooser;


    protected AdjustResourcesScreen(AuthoringView view) {
	super(view);
    }

    /**
     * Creates features (specifically, sliders) that users can manipulate to change starting resources of player
     */
    @Override
    public Parent populateScreenWithFields(){
	VBox vb = new VBox(); 

	vb.getChildren().add(getUIFactory().makeScreenTitleText(getErrorCheckedPrompt("SpecifyStartingResources")));

	HBox soundSelectors = new HBox();

	//VBox backgroundMusicSelector = getUIFactory().makeSoundSelector(getPropertiesReader(), soundPropertiesFilePath,  e = (String s) -> {this.setBackgroundMusic(s)});

	ComboBox<String> backgroundMusicDropdown;
	try {
	    backgroundMusicDropdown = this.getUIFactory().makeTextDropdown(this.getPropertiesReader().allKeys(DEFAULT_SOUND_FILEPATH)); // TODO: this has no prompt, and the action for no choice is null

	    backgroundMusicDropdown.addEventHandler(ActionEvent.ACTION,e -> setBackgroundMusic(backgroundMusicDropdown.getSelectionModel().getSelectedItem()));

	    VBox backgroundMusicSelect = this.getUIFactory().setupSelector(this.getPropertiesReader(), "", DEFAULT_SOUND_FILEPATH, "Load New Sound", "Background Music:", ".wav", backgroundMusicDropdown);
	    backgroundMusicDropdown.addEventHandler(ActionEvent.ACTION,e -> {
		getView().setObjectAttribute(OBJECT_TYPE, BACKGROUND_MUSIC_KEY, myBackgroundMusic);
	    });

	    soundSelectors.getChildren().add(backgroundMusicSelect);
	} catch (MissingPropertiesException e) {
	    e.printStackTrace(); //TODO
	    getView().loadErrorAlert("NoFile");
	}

	ComboBox<String> levelWinSoundDropdown;
	try {
	    levelWinSoundDropdown = this.getUIFactory().makeTextDropdown(this.getPropertiesReader().allKeys(DEFAULT_SOUND_FILEPATH)); // TODO: this has no prompt, and the action for no choice is null

	    levelWinSoundDropdown.addEventHandler(ActionEvent.ACTION,e -> setLevelWinSound(levelWinSoundDropdown.getSelectionModel().getSelectedItem()));

	    VBox levelWinSoundSelect = this.getUIFactory().setupSelector(this.getPropertiesReader(), "", DEFAULT_SOUND_FILEPATH, "Load New Sound", "Level Win Sound:", ".wav", levelWinSoundDropdown);
	    soundSelectors.getChildren().add(levelWinSoundSelect);
	    levelWinSoundDropdown.addEventHandler(ActionEvent.ACTION,e -> {
		getView().setObjectAttribute(OBJECT_TYPE, LEVEL_WIN_SOUND_KEY, myLevelWinSound);
	    });
	} catch (MissingPropertiesException e) {
	    e.printStackTrace(); //TODO
	    getView().loadErrorAlert("NoFile");
	}


	ComboBox<String> levelLossSoundDropdown;
	try {
	    levelLossSoundDropdown = this.getUIFactory().makeTextDropdown(this.getPropertiesReader().allKeys(DEFAULT_SOUND_FILEPATH)); // TODO: this has no prompt, and the action for no choice is null

	    levelLossSoundDropdown.addEventHandler(ActionEvent.ACTION,e -> setLevelLossSound(levelLossSoundDropdown.getSelectionModel().getSelectedItem()));

	    VBox levelLossSoundSelect = this.getUIFactory().setupSelector(this.getPropertiesReader(), "", DEFAULT_SOUND_FILEPATH, "Load New Sound", "Level Loss Sound:", ".wav", levelLossSoundDropdown);
	    levelLossSoundDropdown.addEventHandler(ActionEvent.ACTION,e -> {
		getView().setObjectAttribute(OBJECT_TYPE, LEVEL_LOSS_SOUND_KEY, myLevelLossSound);
	    });
	    soundSelectors.getChildren().add(levelLossSoundSelect);
	} catch (MissingPropertiesException e) {
	    e.printStackTrace(); //TODO
	    getView().loadErrorAlert("NoFile");
	}



	//myBackgroundMusic = "stillDre";
	//myLevelWinSound = "applause";
	//myLevelLossSound  = "boo";

	Text settingsHeading = getUIFactory().makeScreenTitleText(getErrorCheckedPrompt("SettingsHeading"));
	myGameNameEntry = getUIFactory().makeTextField();
	myGameInstructionsEntry = new TextArea();
	HBox gameInstructions = getUIFactory().addPromptAndSetupHBox(myGameInstructionsEntry, getErrorCheckedPrompt("Instructions"));
	vb.getChildren().add(settingsHeading);
	int maxStartingHealth = 0;
	int maxStartingCurrency = 0;
	try {
	    maxStartingHealth = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS_FILEPATH, "StartingHealth"));
	    maxStartingCurrency = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS_FILEPATH, "StartingMoney"));
	}
	catch(MissingPropertiesException e) {
	    Log.debug(e);
	    getView().loadErrorScreen("NoConstants");
	}
	HBox promptGameName = getUIFactory().addPromptAndSetupHBox(myGameNameEntry, getErrorCheckedPrompt("GameName"));
	vb.getChildren().add(promptGameName);
	vb.getChildren().add(gameInstructions);
	myStartingHealthSlider = getUIFactory().setupSlider(maxStartingHealth);
	myStartingHealthSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
	    getView().setObjectAttribute(OBJECT_TYPE, DEFAULT_HEALTH_KEY, newValue);
	});
	HBox startingHealth = getUIFactory().setupSliderWithValue(myStartingHealthSlider, getErrorCheckedPrompt("StartingHealth"));
	vb.getChildren().add(startingHealth);
	myStartingCurrencySlider = getUIFactory().setupSlider(maxStartingCurrency);

	myStartingCurrencySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
	    getView().setObjectAttribute(OBJECT_TYPE, DEFAULT_MONEY_KEY, newValue);
	});
	HBox startingCurrency = getUIFactory().setupSliderWithValue(myStartingCurrencySlider, getErrorCheckedPrompt("StartingCurrency"));
	vb.getChildren().add(startingCurrency);

	//		List<String> cssOptions = new ArrayList<>(); 
	//		try {
	//			cssOptions = getPropertiesReader().allKeys(DEFAULT_CSS_STYLES);
	//		} catch (MissingPropertiesException e1) {
	//		    	Log.debug(e1);
	//			getView().loadErrorAlert("NoFile");
	//		}
	//		myCSSFilenameChooser = getUIFactory().makeTextDropdown(cssOptions);
	//		vb.getChildren().add(getUIFactory().addPromptAndSetupHBox(myCSSFilenameChooser, getErrorCheckedPrompt("CSS")));
	//		myCSSFilenameChooser.addEventHandler(ActionEvent.ACTION, e -> {
	//			getView().setObjectAttribute(OBJECT_TYPE, "myCSSTheme", myCSSFilenameChooser.getSelectionModel().getSelectedItem()); 
	//		});

	vb.getChildren().add(soundSelectors);

	Button backButton = setupBackButton();
	Button applyButton = getUIFactory().setupApplyButton();
	applyButton.setOnAction(e -> {
	    setSaved();
	    getView().setGameName(myGameNameEntry.getText());
	    getView().setObjectAttribute(OBJECT_TYPE, DEFAULT_GAME_NAME_KEY, myGameNameEntry.getText());
	    getView().setObjectAttribute(OBJECT_TYPE, DEFAULT_INSTRUCTIONS_KEY, myGameInstructionsEntry.getText());
	    //getView().setObjectAttribute(OBJECT_TYPE, BACKGROUND_MUSIC_KEY, myBackgroundMusic);
	    //getView().setObjectAttribute(OBJECT_TYPE, LEVEL_WIN_SOUND_KEY, myLevelWinSound);
	    //getView().setObjectAttribute(OBJECT_TYPE, LEVEL_LOSS_SOUND_KEY, myLevelLossSound);

	    getView().goForwardFrom(this.getClass().getSimpleName()+"Apply");
	});
	HBox backAndApplyButton = getUIFactory().setupBackAndApplyButton(backButton, applyButton);

	vb.getChildren().add(backAndApplyButton);

	return vb;
    }

    protected void setBackgroundMusic(String backgroundMusic) {
	myBackgroundMusic = backgroundMusic;
    }

    protected void setLevelWinSound(String levelWinSound) {
	myLevelWinSound = levelWinSound;
    }

    protected void setLevelLossSound(String levelLossSound) {
	myLevelLossSound = levelLossSound;
    }

    @Override
    protected void populateFieldsWithData() {
	myGameNameEntry.setText(getView().getObjectAttribute(OBJECT_TYPE, DEFAULT_GAME_NAME_KEY).toString());
	myGameInstructionsEntry.setText(getView().getObjectAttribute(OBJECT_TYPE, DEFAULT_INSTRUCTIONS_KEY).toString());
	getUIFactory().setSliderToValue(myStartingHealthSlider, getView().getObjectAttribute(OBJECT_TYPE, DEFAULT_HEALTH_KEY).toString());
	getUIFactory().setSliderToValue(myStartingCurrencySlider, getView().getObjectAttribute(OBJECT_TYPE, DEFAULT_MONEY_KEY).toString());	
    }
}
