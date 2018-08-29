package frontend;

import com.sun.javafx.tools.packager.Log;

import authoring.AuthoringController;
import authoring.AuthoringModel;
import authoring.frontend.GraphMenuScreen;
import authoring.frontend.exceptions.MissingPropertiesException;
import controller.PlayController;
import frontend.Screen;
import frontend.UIFactory;
import frontend.View;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Screen that users initially see where they can choose between authoring a game and playing it.
 * @author Ben Hodgson
 *
 */
public class MainScreen extends Screen {
	//TODO re-factor style sheets to abstract
	public static final String DEFAULT_CSS = "styling/GameAuthoringStartScreen.css";
	// private  final String DEFAULT_SHARED_STYLESHEET = "styling/SharedStyling.css";
	//    private  final String DEFAULT_ENGINE_STYLESHEET = "styling/EngineFrontEnd.css";
	public static final String DEFAULT_LANGUAGE = "English";

	private final UIFactory myUIFactory;
	private final StageManager myStageManager;
	private View myView;

	public MainScreen(StageManager stageManager, View view) {
	    	super();
		myUIFactory = new UIFactory();
		myStageManager = stageManager;
		myView = view;
		setStyleSheet(DEFAULT_CSS);
	}

	/**
	 * Creates all UI elements (buttons) for the screen to allow the user to choose an option.
	 * @see frontend.Screen#makeScreenWithoutStyling()
	 */
	@Override
	public Parent makeScreenWithoutStyling() {
//	    	System.out.println("makin a mainscreen");
		VBox rootBox = new VBox();
		Text title = getUIFactory().makeScreenTitleText(myView.getErrorCheckedPrompt("Welcome"));
		Button newAuthorButt = getUIFactory().makeTextButton("editbutton", myView.getErrorCheckedPrompt("Author"));
		newAuthorButt.setOnAction(click->{
			new AuthoringController(myStageManager,DEFAULT_LANGUAGE);
		});
		newAuthorButt.setOnMouseClicked((argo0) -> new AuthoringController(myStageManager, DEFAULT_LANGUAGE));

		Button newGameButt = getUIFactory().makeTextButton("editbutton", myView.getErrorCheckedPrompt("Load"));
		newGameButt.setOnAction(click->{
			try {
				new PlayController(myStageManager, DEFAULT_LANGUAGE, new AuthoringModel())
				.loadInstructionScreen();
			} catch (MissingPropertiesException e) {
				// TODO Auto-generated catch block
			    	Log.debug(e);
				myView.loadErrorScreen("NoFile");
			}
		});

		//	HBox leftCenter = new HBox(newAuthorButt);
		//	leftCenter.setAlignment(Pos.CENTER);
		//	leftCenter.setMaxWidth(Double.MAX_VALUE);
		//	HBox rightCenter = new HBox(newGameButt);
		//	rightCenter.setAlignment(Pos.CENTER);
		//	rightCenter.setMaxWidth(Double.MAX_VALUE);
		//
		//	HBox buttonBox = new HBox(leftCenter,rightCenter);
		//	buttonBox.setAlignment(Pos.CENTER);
		//	HBox.setHgrow(leftCenter, Priority.ALWAYS);
		//	HBox.setHgrow(rightCenter, Priority.ALWAYS);

		rootBox.getChildren().addAll(title, newAuthorButt, newGameButt);

		rootBox.getStylesheets().add(DEFAULT_SHARED_STYLESHEET);
		//	rootBox.getStylesheets().add(DEFAULT_ENGINE_STYLESHEET);
		return rootBox;
	}

	@Override
	protected View getView() {
		// TODO Auto-generated method stub
		return null;
	}
}
