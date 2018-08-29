package authoring.frontend;


import com.sun.javafx.tools.packager.Log;
import authoring.frontend.exceptions.MissingPropertiesException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Class that creates a Panel to show users instructions on how to select
 * a path to create a wave for and to allow users to specify time until the next
 * wave. Dependent on the grid to create correctly.
 * @author Sarahbland
 *
 */
public class WaveDirectionsPanel extends PathPanel{
    private int myWaveNumber;
    private Slider myTimeSlider;
    private VBox myRoot;
    private Button myApplyButton;
    public WaveDirectionsPanel(AuthoringView view, String waveNumber) {
	super(view);
	if(waveNumber.equals("Default")) { //coming from new wave
	    myWaveNumber = getView().getHighestWaveNumber(getView().getLevel())+1;
	    try {
		getView().setWaveTime(myWaveNumber,Integer.parseInt(getPropertiesReader().findVal(AdjustNewOrExistingScreen.DEFAULT_CONSTANTS, "DefaultWaveTime")));
	    }
	    catch(MissingPropertiesException e) {
		 Log.debug(e);
		getView().loadErrorScreen("NoFile");
	    }
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


	setUpPanel();
    }
    private void setUpPanel() {
	myRoot = new VBox();
	myRoot.setMaxSize(280, 900);
	Button backButton = getUIFactory().makeTextButton("", getErrorCheckedPrompt("Cancel"));
	backButton.setOnAction(e -> {getView().goForwardFrom(this.getClass().getSimpleName()+"Back", "Wave");});
	Label waveText = new Label(getErrorCheckedPrompt("WavescreenHeader") + (myWaveNumber+1));
	VBox pseudoRoot = new VBox();
	Text directions = new Text(getErrorCheckedPrompt("WaveDirections")); //TODO
	HBox waveTimeSliderPrompted = new HBox();
	try {
	    myTimeSlider = getUIFactory().setupSlider("", Integer.parseInt(getPropertiesReader().findVal(AdjustNewOrExistingScreen.DEFAULT_CONSTANTS, "MaxWaveTime")));
	    waveTimeSliderPrompted = getUIFactory().setupSliderWithValue("", myTimeSlider, getErrorCheckedPrompt("WaveTime"));
	    String time = getView().getObjectAttribute("Wave", ((Integer) myWaveNumber).toString(), "myTime").toString();
	    myTimeSlider.setValue(Double.parseDouble(time));
	    myTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> ov,
			Number old_val, Number new_val) {
		    myApplyButton.setDisable(false);
		}
	    });
	}
	catch(MissingPropertiesException e) {
	    Log.debug(e);
	    getView().loadErrorScreen("NoFile");
	}
	myApplyButton = getUIFactory().makeTextButton("", getErrorCheckedPrompt("Apply"));
	myApplyButton.setOnAction(e -> {
	    myApplyButton.setDisable(true);
	    setSaved();
	    getView().setWaveTime(myWaveNumber,(int) Math.round(myTimeSlider.getValue()));
	});
	pseudoRoot.getChildren().add(waveText);
	pseudoRoot.getChildren().add(directions);
	pseudoRoot.getChildren().add(waveTimeSliderPrompted);
	pseudoRoot.getChildren().add(myApplyButton);
	pseudoRoot.getChildren().add(backButton);
	myRoot.getChildren().add(pseudoRoot);
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
     * myRoot is Parent that will need styling added to it, so it is returned by
     * this method.
     * @see frontend.Screen#makeScreenWithoutStyling()
     */
    @Override
    public Parent makeScreenWithoutStyling() {
	return myRoot;
    }
    @Override
    protected void makePanel() {
	myRoot = new VBox();
    }


}
