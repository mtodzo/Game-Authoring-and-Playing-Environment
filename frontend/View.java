package frontend;

import authoring.AuthoringModel;
import authoring.frontend.exceptions.MissingPropertiesException;
import controller.MVController;
import controller.PlayController;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Abstract class for View classes in both frontends (authoring and gameplay). Used for common
 * functionality of loading error screens.
 * @author Sarahbland
 *
 */

public class View {
    StageManager myManager;
    PromptReader myPromptReader;
    protected ErrorReader myErrorReader;
    PropertiesReader myPropertiesReader;
    String myLanguage;
    MVController myController;
    
    public View(StageManager manager, String languageIn, MVController controller) {
	myPromptReader = new PromptReader(languageIn, this);
	myErrorReader = new ErrorReader(languageIn, this);
	myPropertiesReader = new PropertiesReader();
	myManager = manager;
	myLanguage = languageIn;
	myController = controller;
    }

    
    
    
	/**
	 * Loads an error screen when a user has done something so problematic that the program
	 * cannot recover (such as choosing a language with no prompts and not having English
	 * prompts to default to).
	 * @param error is key to the Error the user has committed
	 */
    public void loadErrorScreen(String errorMessage) {
	errorMessage = myErrorReader.resourceDisplayText(errorMessage);
	VBox vb = new VBox();
	Text errorScreenMessage = new Text(errorMessage);
	vb.setAlignment(Pos.CENTER);
	errorScreenMessage.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
	vb.getChildren().add(errorScreenMessage);
	myManager.switchScreen(vb);
    }

	/**
	 * Loads an error alert when the user needs to be notified, but the program can
	 * recover.
	 * @param error is error key for error User has committed
	 */
    public void loadErrorAlert(String errorMessage) {
	errorMessage = myErrorReader.resourceDisplayText(errorMessage);
	Alert alert = new Alert(AlertType.ERROR);
	alert.setContentText(errorMessage);
	alert.showAndWait();
    }

    /**
     * @param prompt is key used in properties file to retrieve desired prompt
     * @return prompt after error checking
     */
    public String getErrorCheckedPrompt(String prompt) {
	return myPromptReader.resourceDisplayText(prompt);
    }
    
    /**
     * @return PropertiesReader used to read from properties files
     */
    public PropertiesReader getPropertiesReader() {
	return myPropertiesReader; 
    }
    public void playControllerDemo() {
	try {
 	myController.playControllerDemo(myManager, myLanguage);
	}
	catch(MissingPropertiesException e) {
	    loadErrorScreen("NoFile");
	}
     }
    public void playControllerInstructions() {
	try {
	    new PlayController(myManager, myLanguage, new AuthoringModel()).loadInstructionScreen();
	}
 	catch(MissingPropertiesException e) {
 	    loadErrorScreen("NoFile");
 	}
     }

}
