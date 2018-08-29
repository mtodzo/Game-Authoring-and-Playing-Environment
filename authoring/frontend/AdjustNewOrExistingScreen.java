/**
 * @author susiechoi
 * @author Katherine Van Dyk
 * @author sarahbland
 * 
 * Abstract class of screens that have both "new" and "existing" object edit options 
 * (e.g. AdjustTowerScreen extends AdjustNewOrExistingScreen because a designer can edit 
 * a new or existing Tower) 
 * 
 */

package authoring.frontend;

import java.util.ArrayList;
import java.util.List;
import com.sun.javafx.tools.packager.Log;
import authoring.frontend.exceptions.MissingPropertiesException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

abstract class AdjustNewOrExistingScreen extends AdjustScreen {

	public static final String DEFAULT_PROPERTIES_FILES_PREFIX = "default_objects/Properties/";
	public static final String DEFAULT_OBJATTRIBUTEDNE_KEY = "ObjectAttributeDNE"; 
	public static final String DEFAULT_NOIMAGEFILE_KEY = "NoImageFile";
	public static final String DEFAULT_CONSTANTS = "src/frontend/Constants.properties";
	public static final String DEFAULT_COLLISION_KEYWORD = "Collision";
	public static final String EMPTY_STRING = "";
	public static final String DEFAULT_POPUP_WIDTH_KEYWORD = "PopupWidth";
	public static final String DEFAULT_POPUP_HEIGHT_KEYWORD = "PopupHeight";
	public static final String DEFAULT_NOCONSTANTS_ERROR_KEY = "NoConstants";

	private String mySelectedObjectName; 
	private String myDefaultObjectName; 
	private int myMaxHealthImpact;
	private int myMaxSpeed;
	private int myMaxRange;
	private int myMaxPrice; 
	private int myMaxUpgradeIncrement; 

	private boolean myIsNewObject; 	

	protected AdjustNewOrExistingScreen(AuthoringView view, String selectedObjectName, String fieldsPropertiesPath, String objectDescription) {
		super(view);
		setConstants();
		mySelectedObjectName = selectedObjectName; 
		myIsNewObject = selectedObjectName.equals(myDefaultObjectName);
	}

	protected AdjustNewOrExistingScreen(AuthoringView view) {
		super(view);
		setConstants();
	}

	private void setConstants() {
		try {
			myDefaultObjectName = getPropertiesReader().findVal(DEFAULT_CONSTANTS, "DefaultObjectName");
			myMaxHealthImpact = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS, "MaxHealthImpact"));
			myMaxSpeed = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS, "MaxSpeed"));
			myMaxRange = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS, "MaxRange"));
			myMaxPrice = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS, "MaxPrice"));
			myMaxUpgradeIncrement = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS, "MaxUpgradeIncrement"));
		} catch (NumberFormatException e) {
			Log.debug(e);
			getView().loadErrorScreen("BadConstants");
		} catch (MissingPropertiesException e) {
			Log.debug(e);
			getView().loadErrorScreen(DEFAULT_NOCONSTANTS_ERROR_KEY);
		}

	}

	/**
	 * For all screens in which users can edit either new or existing objects, the makeScreenWithoutStyling method should 
	 * ensure that the screen is populated with fields and that, if deemed necessary by the subclass, 
	 * the fields are populated with data (in the case that an existing object is being edited) 
	 */
	@Override
	public Parent makeScreenWithoutStyling() {
		Parent constructedScreen = populateScreenWithFields();
		populateFieldsWithData(); 
		return constructedScreen;
	}

	protected abstract Parent populateScreenWithFields();

	protected Parent makePropertySelector(String subfolderPath, Button applyButton) {
		Parent propertySelector = makePropertySelector(subfolderPath);
		for (Node n : propertySelector.getChildrenUnmodifiable()) {
			if (n.getClass() == ComboBox.class) {
				((ComboBox<String>)n).addEventFilter(ActionEvent.ACTION, e-> {
					applyButton.setDisable(false);
				});
				applyButton.setDisable(true);
			}
		}
		return propertySelector; 
	}
	
	protected Parent makePropertySelector(String subfolderPath) {
		HBox hb = new HBox(); 

		String propertySelectionPrompt = getView().getErrorCheckedPrompt("PropertySelection");

		ArrayList<String> propertyOptions = new ArrayList<>();
		propertyOptions.add(propertySelectionPrompt);
		propertyOptions.addAll(getUIFactory().getFileNames(DEFAULT_PROPERTIES_FILES_PREFIX+subfolderPath));

		Button addPropertyButton = getUIFactory().makeTextButton(propertySelectionPrompt);

		ComboBox<String> availableProperties = getUIFactory().makeTextDropdownSelectAction(propertyOptions, 
				e-> { addPropertyButton.setDisable(false); }, 
				e-> { addPropertyButton.setDisable(true);  }, 
				propertySelectionPrompt);
		addPropertyButton.setDisable(true);
		addPropertyButton.setOnAction(e -> {
			Stage propertyPopup = new Stage();
			Parent propScreenRoot = new PropertyScreen(getView(), availableProperties.getSelectionModel().getSelectedItem(), subfolderPath, mySelectedObjectName, propertyPopup).getScreen(); 
			propertyPopup.setScene(new Scene(propScreenRoot));
			propertyPopup.show();
			try {
				propertyPopup.setHeight(Double.parseDouble(getPropertiesReader().findVal(DEFAULT_CONSTANTS, DEFAULT_POPUP_HEIGHT_KEYWORD)));
				propertyPopup.setWidth(Double.parseDouble(getPropertiesReader().findVal(DEFAULT_CONSTANTS, DEFAULT_POPUP_WIDTH_KEYWORD)));
			} catch (NumberFormatException | MissingPropertiesException e1) {
				Log.debug(e1);
				getView().loadErrorScreen(DEFAULT_NOCONSTANTS_ERROR_KEY);
			}
		});
		hb.getChildren().addAll(availableProperties, addPropertyButton);
		return hb; 
	}

	protected void populateFieldsWithData() {
		//		AttributeFinder attributeFinder = new AttributeFinder(); 
		//
		//		Map<String, String> fieldsToAttributes = new HashMap<String, String>(); 
		//
		//		try {
		//			fieldsToAttributes = getView().getPropertiesReader().read(myFieldsPropertiesPath);
		//		} catch (MissingPropertiesException e) {
		//		    Log.debug(e);
		//			getView().loadErrorScreen(DEFAULT_OBJATTRIBUTEDNE_KEY);
		//		}
		//
		//		for (String key : fieldsToAttributes.keySet()) {
		//			Object myField = null; 
		//			try {
		//				myField = attributeFinder.retrieveFieldValue(key, this);
		//				getUIFactory().setSliderToValue((Slider) myField, getView().getObjectAttribute(myObjectDescription, getMySelectedObjectName(), fieldsToAttributes.get(key)).toString());
		//			} catch (IllegalArgumentException | ObjectNotFoundException | IllegalAccessException e) {
		//			    Log.debug(e);	
		//			    getView().loadErrorScreen(DEFAULT_OBJATTRIBUTEDNE_KEY);
		//			}
		//		}
	}

	/**
	 * Used when the changes on the Screen are applied and the Screen must convey whether the object that has been created is new or existing 
	 * @return
	 */
	protected boolean getIsNewObject() {
		return myIsNewObject; 
	}

	protected void setEditableOrNot(TextField name, boolean isNewObject) {
		if (!isNewObject) name.setEditable(false);
	}

	protected String getMyDefaultName() {
		return myDefaultObjectName; 
	}

	/** 
	 * The following methods are getters for range-specifying constants so that subclasses may know what range to depict on their sliders
	 * @return int max of the sliders 
	 */
	protected int getMyMaxHealthImpact() {
		return myMaxHealthImpact;
	}

	protected int getMyMaxSpeed() {
		return myMaxSpeed;
	}

	protected int getMyMaxRange() {
		return myMaxRange; 
	}

	protected int getMyMaxPrice() {
		return myMaxPrice;
	}

	protected int getMyMaxUpgradeIncrement() {
		return myMaxUpgradeIncrement; 
	}

	protected String getMySelectedObjectName() {
		return mySelectedObjectName; 
	}

	protected HBox makeImageSelector(String objectType, String imageName, String propertiesFilepath){
		HBox imageSelect = new HBox();
		ComboBox<String> imageDropdown = new ComboBox<>();
		ImageView imageDisplay = new ImageView(); 
		try {
			imageDropdown = getUIFactory().makeTextDropdown(getPropertiesReader().allKeys(propertiesFilepath));
		} catch (MissingPropertiesException e) {
			Log.debug(e);
			getView().loadErrorScreen(DEFAULT_NOIMAGEFILE_KEY);
		} 
		ComboBox<String> imageDropdownCopy = imageDropdown;
		imageDropdown.addEventHandler(ActionEvent.ACTION,e -> {
			try {
			    	//System.out.println("IMAGE NAME: " + imageName + " OBJECT NAME: " + mySelectedObjectName + " OBJECT TYPE: " + objectType);
				getView().setObjectAttribute(objectType, mySelectedObjectName, "my" + imageName + "Image", getPropertiesReader().findVal(propertiesFilepath, imageDropdownCopy.getSelectionModel().getSelectedItem())); 
	
			}
			catch(MissingPropertiesException e2) {
				Log.debug(e2);
				getView().loadErrorScreen(DEFAULT_NOIMAGEFILE_KEY);
			}
		});

		try {
			imageSelect = getUIFactory().setupImageSelector(getPropertiesReader(), "", propertiesFilepath, 50, getErrorCheckedPrompt("NewImage"), getErrorCheckedPrompt("LoadImage"),
					getErrorCheckedPrompt("NewImageName"),imageDropdown, imageDisplay);
			String key = getPropertiesReader().findKey(propertiesFilepath, (String)getView().getObjectAttribute(objectType, mySelectedObjectName, "myImage"));
			ActionEvent fakeSelection = new ActionEvent();
			if(key.equals(EMPTY_STRING)) {
				imageDropdown.getSelectionModel().select(0);
				imageDropdown.fireEvent(fakeSelection);
			}
			else {
				imageDropdown.getSelectionModel().select(key);
				imageDropdown.fireEvent(fakeSelection);
			}
		} catch (MissingPropertiesException e) {
			Log.debug(e);
			getView().loadErrorScreen(DEFAULT_NOIMAGEFILE_KEY);
		}

		return imageSelect;
	}

	protected void setProperty(String objectType, String objectName, String propertyName, Double ...args) {
		List<Double> attributes = makeList(args);
		getView().createProperty(objectType, objectName, propertyName, attributes);
	}

	protected List<Double> makeList(Double ...attributes) {
		List<Double> list = new ArrayList<>();
		for(Double attribute : attributes) {
			list.add(attribute);
		}
		return list;
	}


}