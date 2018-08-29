package authoring.frontend;

import java.io.FileNotFoundException;

import com.sun.javafx.tools.packager.Log;
import authoring.frontend.exceptions.MissingPropertiesException;
import authoring.frontend.exceptions.ObjectNotFoundException;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Abstract class for screens that ask the user to provide a name for a new
 * object (such as a new Tower or Enemy).
 * @author SusieChoi
 *
 */
abstract class SpecifyNameScreen extends AuthoringScreen {

	public static final String DEFAULT_TOWER_TYPE = "Tower";
	public static final String DEFAULT_ENEMY_TYPE = "Enemy";
	public static final String DEFAULT_NO_OBJECT_ERROR_KEY = "NoObject";
    public static final String DEFAULT_CONSTANTS = "src/frontend/Constants.properties";
    public static final String DEFAULT_APPLY_SCREENFLOW = "Apply"; 

    private String myDefaultObjectName; 
    private String myObjectDescription; 
    private TextField myNameField; 

    protected SpecifyNameScreen(AuthoringView view, String objectDescription) {
	super(view);
	myObjectDescription = objectDescription; 
	setConstants(); 
    }

    private void setConstants() {
	try {
	    myDefaultObjectName = getPropertiesReader().findVal(DEFAULT_CONSTANTS, "DefaultObjectName");
	} catch (NumberFormatException e) {
	    Log.debug(e);
	    getView().loadErrorScreen("BadConstants");
	} catch (MissingPropertiesException e) {
	    Log.debug(e);
	    getView().loadErrorScreen("NoConstants");
	}
    }

    /**
     * Makes the screen with the option of creating a new object OR editing an existing one 
     * @return Parent/root to attach to Scene that will be set on the stage
     */
    @Override
    public Parent makeScreenWithoutStyling() {
	VBox vb = new VBox(); 

	vb.getChildren().add(getUIFactory().makeScreenTitleText(getErrorCheckedPrompt("SpecifyObjectName")+myObjectDescription));
	TextField nameField = getUIFactory().makeTextField();
	vb.getChildren().add(nameField);
	myNameField = nameField; 

	Button backButton = setupBackButton(); 
	Button applyButton = getUIFactory().setupApplyButton();
	applyButton.setOnAction(e -> {
	    if (validNameField(myNameField)) {
		if (this.getClass().getSimpleName().equals("SpecifyTowerNameScreen")) {
		    try {
			getView().makeSprite(DEFAULT_TOWER_TYPE, myNameField.getText());
		    } catch (NumberFormatException | FileNotFoundException | ObjectNotFoundException e1) {
			Log.debug(e1);
			getView().loadErrorScreen(DEFAULT_NO_OBJECT_ERROR_KEY);
		    }
		}
		else if (this.getClass().getSimpleName().equals("SpecifyEnemyNameScreen")) {
		    try {
			getView().makeSprite(DEFAULT_ENEMY_TYPE, myNameField.getText());
		    } catch (NumberFormatException | FileNotFoundException
			    | ObjectNotFoundException e1) {
			 Log.debug(e1);
			getView().loadErrorScreen(DEFAULT_NO_OBJECT_ERROR_KEY);
		    }
		}
		getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_APPLY_SCREENFLOW,myNameField.getText());
	    }
	});
	HBox backAndApplyButton = getUIFactory().setupBackAndApplyButton(backButton, applyButton);
	vb.getChildren().add(backAndApplyButton);

	return vb;
    }

    protected boolean validNameField(TextField nameField) {
	boolean valid = true; 
	if (nameField.getText().length() == 0) {
	    getView().loadErrorAlert("PopulateName");
	    valid = false; 
	}
	else if (nameField.getText().equals(myDefaultObjectName)) {
	    getView().loadErrorAlert("NoDefaultName");
	    valid = false; 
	}
	return valid; 
    }

}
