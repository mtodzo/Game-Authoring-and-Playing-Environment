package authoring.frontend;

import java.util.ArrayList;
import java.util.List;
import authoring.frontend.AuthoringScreen;
import authoring.frontend.AuthoringView;
import authoring.frontend.exceptions.MissingPropertiesException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
//import jdk.internal.jimage.ImageReader.Node;
import javafx.stage.Stage;

/**
 * Class that creates popup allowing user to specify a Property for a specific
 * Sprite. Dependent upon the Screen that creates an option to create it.
 * @author SusieChoi
 * @author SarahBland
 *
 */
public class PropertyScreen extends AuthoringScreen {

	public static final String DEFAULT_NOFILEERROR_KEY = "NoFile";
	public static final String DEFAULT_PROPERTIES_FILES_PREFIX = "default_objects/Properties/";
	public static final String DEFAULT_PROPERTIES_FILES_SUFFIX = ".properties";
	public static final String DEFAULT_FILEPATH_SEPARATOR = "/";
	public static final String DEFAULT_CONSTANTS_FILEPATH = "src/frontend/Constants.properties";
	public static final String DEFAULT_PROPERTIES_SLIDER_MAX_KEY = "PropertiesSliderMax";
	public static final String DEFAULT_SLIDER_KEYWORD = "Slider";
	public static final String DEFAULT_TOGGLE_KEYWORD = "ToggleButton";
	public static final String DEFAULT_APPLY_PROMPT_KEY = "Apply";
	public static final String DEFAULT_NOOBJECT_ERROR_KEY = "NoObject";

	private String myPropertiesFilepath; 
	private String myPropertyName;
	private String myObjectType;
	private String myObjectName;
	private Stage myStage;

	public PropertyScreen(AuthoringView view, String propertyName, String objectType, String objectName, Stage stage) {
		super(view);
		myPropertiesFilepath = DEFAULT_PROPERTIES_FILES_PREFIX+objectType+DEFAULT_FILEPATH_SEPARATOR+propertyName+DEFAULT_PROPERTIES_FILES_SUFFIX; 
		myPropertyName = propertyName;
		myObjectType = (objectType.contains(DEFAULT_FILEPATH_SEPARATOR)) ? objectType.split(DEFAULT_FILEPATH_SEPARATOR)[0] : objectType; 
		myObjectName = objectName;
		myStage = stage;
	}

	/* (non-Javadoc)
	 * Creates specific UI components for the popup. The necessary sliders and
	 * fields are read from a properties file specifying the attributes necessary
	 * to create a Property
	 * @see frontend.Screen#makeScreenWithoutStyling()
	 */
	@Override
	public Parent makeScreenWithoutStyling() {
		VBox vb = new VBox();
		vb.getChildren().add(getUIFactory().makeScreenTitleText(myPropertyName));
		List<String> keys = null;
		try {
			keys = getView().getPropertiesReader().allKeys(myPropertiesFilepath);
			//for (String s : keys) System.out.println(s);
		} catch (MissingPropertiesException e1) {
			getView().loadErrorScreen(DEFAULT_NOFILEERROR_KEY);
		}
		List<Node> allSliders = new ArrayList<>(); 
		for (String key : keys) {
			Slider aSlider = new Slider();
			try {
				aSlider = getUIFactory().setupSlider(Integer.parseInt(getView().getPropertiesReader().findVal(DEFAULT_CONSTANTS_FILEPATH, DEFAULT_PROPERTIES_SLIDER_MAX_KEY)));
				allSliders.add(aSlider);
			} catch (NumberFormatException | MissingPropertiesException e1) {
				getView().loadErrorScreen(DEFAULT_NOFILEERROR_KEY);
			}
			try {
				getUIFactory().setSliderToValue(aSlider, getView().getPropertiesReader().findVal(myPropertiesFilepath, key));
			} catch (MissingPropertiesException e1) {
				getView().loadErrorScreen(DEFAULT_NOOBJECT_ERROR_KEY);
			}
			HBox sliderWithPrompt = getUIFactory().setupSliderWithValue(aSlider, getView().getErrorCheckedPrompt(key));
			vb.getChildren().add(sliderWithPrompt);
		}
		Button applyButton = getUIFactory().makeTextButton(getView().getErrorCheckedPrompt(DEFAULT_APPLY_PROMPT_KEY));
		applyButton.setOnAction(e -> {
			applyPropertyScreen(myPropertyName, myObjectType, myObjectName, allSliders);
			myStage.close();
		});
		vb.getChildren().add(applyButton);
		return vb;
	}

	private void applyPropertyScreen(String propertyName, String objectType, String objectName, List<Node> children) {
		List<Double> attributeValues = new ArrayList<>();
		for(Node node : children) {
			if (node.getClass() == Slider.class) {
				attributeValues.add(((Slider)node).getValue());
			}
		}
		getView().createProperty( objectType, objectName, propertyName, attributeValues);
	}

}