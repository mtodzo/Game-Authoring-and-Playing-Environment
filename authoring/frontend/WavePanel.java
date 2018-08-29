package authoring.frontend;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import com.sun.javafx.tools.packager.Log;

import engine.path.Path;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


/**
 * Class to create the panel seen on the righthand side of the WaveScreen that allows
 * users to specify Enemy information/numbers for a specific wave for a specific path.
 * Dependent on AuthoringModel to make/retrieve waves correctly and on UIFactory to correctly
 * create UI elements.
 * @author Sarahbland
 *
 */

public class WavePanel extends PathPanel{


	private Path myPath;
	private Button myApplyButton;
	private VBox myRoot;
	private ComboBox<String> myEnemyDropdown;
	private TextField myNumberTextField;
	private Integer myEnemyNumber;
	private Integer myWaveNumber;
	private WavePanel mySelf;
	
	public WavePanel(AuthoringView view, Point point, String waveNumber) {
		super(view);
		myPath = getView().getPathWithStartingPoint(getView().getLevel(), point);
		
		if(waveNumber.equals("Default")) {
		    myWaveNumber = getView().getHighestWaveNumber(getView().getLevel());
		}
		else {
		    String[] splitUpWave = waveNumber.split(" ");
		    if(splitUpWave.length>1) {
			myWaveNumber = Integer.parseInt(splitUpWave[1]) - 1; //Coming from combobox
		    }
		    else {
			myWaveNumber = Integer.parseInt(splitUpWave[0]);  //coming from wavepanel
		    }
		}
		//System.out.println("highest wave number panel: " + getView().getHighestWaveNumber(getView().getLevel()));
		//myPathNumber = 1; //TODO!!
		mySelf = this;
		makePanel();
	}
	
	protected void makePanel() {
		Map<String, Integer> enemyMap = getView().getEnemyNameToNumberMap(getView().getLevel(), myPath, myWaveNumber);
		myRoot = new VBox();
		myRoot.setMaxSize(280, 900);
		VBox pseudoRoot = new VBox();
		Label waveText = new Label(getErrorCheckedPrompt("WavescreenHeader") + (myWaveNumber+1));
		List<String> enemyOptions = getView().getCurrentObjectOptions("Enemy");
		myEnemyDropdown = getUIFactory().makeTextDropdown("", enemyOptions);
		myEnemyDropdown.setOnAction(e -> {
			myNumberTextField.setText(enemyMap.get(myEnemyDropdown.getValue()).toString());
		});
		for(String enemyName: enemyOptions) {//TODO: refactor - this is getting pretty messy/repeated!!
			if(!enemyMap.containsKey(enemyName)) {
				enemyMap.put(enemyName, 0);
			}
		}
		if(enemyOptions.size()>0) {
			myEnemyDropdown.setValue(enemyOptions.get(0));
		}
		Text enemyDropdownText = new Text(getView().getErrorCheckedPrompt("ChooseEnemy"));
		myNumberTextField = new TextField();
		if(myEnemyDropdown.getValue()!=null) {
		    myNumberTextField.setText(enemyMap.get(myEnemyDropdown.getValue()).toString());
		}
		//		HBox sizingButtons = makeSizingButtons();
		Text textFieldPrompt = new Text(getView().getErrorCheckedPrompt("ChooseEnemyNumber"));
		//HBox textFieldPrompted = getUIFactory().addPromptAndSetupHBox("", myNumberTextField, "ChooseEnemyNumber");
		Button backButton = setupBackButton();
		Button myApplyButton = getUIFactory().makeTextButton("", getView().getErrorCheckedPrompt("Apply"));
		myApplyButton.setOnAction(e -> {
		    setSaved();
			if(errorcheckResponses()) {
				getView().addWaveEnemy(getView().getLevel(),myPath, myWaveNumber, 
						myEnemyDropdown.getValue(), myEnemyNumber);
				getView().goForwardFrom(mySelf.getClass().getSimpleName()+"Apply", myWaveNumber.toString());
				//System.out.println("highest wave number" + getView().getHighestWaveNumber(getView().getLevel()));
			}

		});
		pseudoRoot.getChildren().addAll(waveText, enemyDropdownText, myEnemyDropdown, textFieldPrompt, myNumberTextField, backButton, myApplyButton);
		myRoot.getChildren().add(pseudoRoot);
		myRoot.getStyleClass().add("rootPanel");
	}


	private boolean errorcheckResponses() {
		if(myEnemyDropdown.getValue() == null ) {
			getView().loadErrorAlert("BadValue");
			return false;
		}
		try {
			myEnemyNumber = Integer.parseInt(myNumberTextField.getText());
		}
		catch(NumberFormatException e) {
		    Log.debug(e);
		    getView().loadErrorAlert("BadValue");
			return false;
		}
		return true;
	}

	@Override
	protected Button getApplyButton() {
		return myApplyButton;
	}
	@Override
	protected Node getPanel() {
		return myRoot;
	}
	@Override
	protected void setApplyButtonAction(EventHandler<ActionEvent> e) {
		myApplyButton.setOnAction(event -> {e.handle(event);});

	}

	/**
	 * Returns myRoot, which is Parent which will be styled by CSS in Screen
	 * @see frontend.Screen#makeScreenWithoutStyling()
	 */
	@Override
	public Parent makeScreenWithoutStyling() {
		return myRoot;
	}
}
