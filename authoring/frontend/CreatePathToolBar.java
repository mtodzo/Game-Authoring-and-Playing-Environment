package authoring.frontend;

import com.sun.javafx.tools.packager.Log;
import authoring.frontend.exceptions.MissingPropertiesException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class to create the tool bar component of the path authoring screen.
 * @author Erik Riis
 *
 */

public class CreatePathToolBar extends PathToolBar {

    public static final String BACKGROUND_IMAGE_PREFIX = "images/ThemeSpecificImages/BackGroundImages/";
    public static final String BACKGROUND_IMAGE_SUFFIX = "BackGroundImageNames.properties";
    public static final String PATH_IMAGE_PREFIX = "images/ThemeSpecificImages/PathImages/";
    public static final String PATH_IMAGE_SUFFIX = "PathImageNames.properties";
    public static final String START_IMAGE_PREFIX = "images/ThemeSpecificImages/StartImages/";
    public static final String START_IMAGE_SUFFIX = "StartImageNames.properties";
    public static final String END_IMAGE_PREFIX = "images/ThemeSpecificImages/EndImages/";
    public static final String END_IMAGE_SUFFIX = "EndImageNames.properties";
    public static final String BACKGROUND_BUTTON_TEXT = "Choose Background Image";
    public static final String START_BUTTON_TEXT = "Choose Start Image";
    public static final String PATH_BUTTON_TEXT = "Choose Path Image";
    public static final String END_BUTTON_TEXT = "Choose End Image";
    private HBox startImageSelect;
    private HBox endImageSelect;
    private HBox pathImageSelect;
    private HBox backgroundImageSelect;
    private AuthoringView myView;

    /**
     * Constructor for the path toolbar
     * @param view
     */
    public CreatePathToolBar(AuthoringView view) {
	super(view);
	myView = view;
    }

    @Override
    protected void makePanel() {

	backgroundImageSelect = makeImageSelector("BackGround", "", BACKGROUND_IMAGE_PREFIX + getView().getTheme() + BACKGROUND_IMAGE_SUFFIX);
	pathImageSelect = makeImageSelector("BackGround", "", PATH_IMAGE_PREFIX + getView().getTheme() + PATH_IMAGE_SUFFIX);
	startImageSelect = makeImageSelector("BackGround", "", START_IMAGE_PREFIX + getView().getTheme() + START_IMAGE_SUFFIX);
	endImageSelect = makeImageSelector("BackGround", "", END_IMAGE_PREFIX + getView().getTheme() + END_IMAGE_SUFFIX);

	Button backgroundImageButton = getUIFactory().makeTextButton("", BACKGROUND_BUTTON_TEXT);
	setImageButtonEvent(backgroundImageButton, backgroundImageSelect);

	Button pathImageButton = getUIFactory().makeTextButton("", PATH_BUTTON_TEXT);
	setImageButtonEvent(pathImageButton, pathImageSelect);

	Button startImageButton = getUIFactory().makeTextButton("", START_BUTTON_TEXT);
	setImageButtonEvent(startImageButton, startImageSelect);

	Button endImageButton = getUIFactory().makeTextButton("", END_BUTTON_TEXT);
	setImageButtonEvent(endImageButton, endImageSelect);

	getToolBar().getChildren().addAll(backgroundImageButton, startImageButton, pathImageButton, endImageButton);
    }
    
    /**
     * All screen elements are made in the various other methods, so getToolBar()
     * triggers them
     * @see frontend.Screen#makeScreenWithoutStyling()
     */
    @Override
    public Parent makeScreenWithoutStyling() {
	return getToolBar();
    }

    /**
     * Initializes functionality of a given image button which when pressed creates a popup containing an image selector for changing
     * the background and path block images
     * @param button
     * @param imageSelector
     */
    private void setImageButtonEvent(Button button, HBox imageSelector) {
	button.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		Stage dialog = new Stage();
		VBox dialogVbox = new VBox();
		dialogVbox.getChildren().add(imageSelector);
		Scene dialogScene = new Scene(dialogVbox);
		dialogScene.getStylesheets().add(myView.getCurrentCSS());
		dialog.setScene(dialogScene);
		dialog.show();
	    }
	});
    }

    /**
     * Creates the image selectors for changing the background and path block images
     * @param objectType
     * @param imageName
     * @param propertiesFilepath
     * @return
     */
    protected HBox makeImageSelector(String objectType, String imageName, String propertiesFilepath){
	HBox imageSelect = new HBox();
	ComboBox<String> imageDropdown = new ComboBox<String>();
	ImageView imageDisplay = new ImageView(); 
	try {
	    imageDropdown = getUIFactory().makeTextDropdown("", getPropertiesReader().allKeys(propertiesFilepath));
	} catch (MissingPropertiesException e) {
	    Log.debug(e);
	    getView().loadErrorScreen("NoImageFile");
	} 
	
	imageDropdown.addEventHandler(ActionEvent.ACTION,e -> {
	});

	try {
	    imageSelect = getUIFactory().setupImageSelector(getPropertiesReader(), "", propertiesFilepath, 50, getErrorCheckedPrompt("NewImage"), getErrorCheckedPrompt("LoadImage"),
		    getErrorCheckedPrompt("NewImageName"),imageDropdown, imageDisplay);
	} catch (MissingPropertiesException e) {
	    Log.debug(e);
	    getView().loadErrorScreen("NoImageFile");
	}
	return imageSelect;
    }
    
    /**
     * Gets the HBox containing the path image selector
     * @return pathImageSelect
     */
    public HBox getPathHBox() {
	return pathImageSelect;
    }

    /**
     * Gets the HBox containing the start image selector
     * @return startImageSelect
     */
    public HBox getStartHBox() {
	return startImageSelect;
    }

    /**
     * Gets the HBox containing the end image selector
     * @return endImageSelect
     */
    public HBox getEndHBox() {
	return endImageSelect;
    }
    
    /**
     * Gets the HBox containing the background image selector
     * @return backgroundImageSelect
     */
    public HBox getBackgroundHBox() {
	return backgroundImageSelect;
    }
}

