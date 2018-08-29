/**
 * @author susiechoi
 * @author Katherine Van Dyk
 * @author benauriemma
 * Abstract class for developing the fields for customizing 
 * (new or existing, depending on whether corresponding tower is new or existing) launcher/projectile object
 */

package authoring.frontend;
import authoring.frontend.exceptions.MissingPropertiesException;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

class AdjustLauncherProjectileScreen extends AdjustNewOrExistingScreen {	

    public static final String PROJECTILE_OBJECT_TYPE = "Projectile";
    public static final String DEFAULT_SUBFOLDERPATH_SEPARATOR = "/";
    public static final String DEFAULT_MOVEMENTPROPERTY_SUBFOLDERNAME = "Movement";
    public static final String DEFAULT_COLLISIONPROPERTY_SUBFOLDERNAME = "Collision";
    public static final String LAUNCHER_OBJECT_TYPE = "Launcher";
    public static final String PROJECTILE_IMAGE_PREFIX = "images/ThemeSpecificImages/ProjectileImages/";
    public static final String PROJECTILE_IMAGE_SUFFIX = "ProjectileImageNames.properties";
    public static final String PROJECTILE_FIELDS = "default_objects/ProjectileFields.properties";
    public static final String DEFAULT_APPLYBUTTON_SCREENFLOW = "Apply";

    private String myObjectName; 
    //    private Slider myProjectileDamageSlider;
    //    private Slider myProjectileSpeedSlider; 
    //    private Slider myLauncherRateSlider;
    //    private Slider myLauncherRangeSlider;
    //    private Slider myProjectileSizeSlider;
    // TODO: add something for the sound -bma
    private Double myProjectileSpeed;
    private Double myLauncherRate;
    private Double myLauncherRateValue;
    private Double myLauncherRateCost;
    private Double myLauncherRange;


    protected AdjustLauncherProjectileScreen(AuthoringView view, String selectedObjectName) {
	super(view, selectedObjectName, PROJECTILE_FIELDS, PROJECTILE_OBJECT_TYPE);
	myObjectName = selectedObjectName; 
    }

    @Override
    protected Parent populateScreenWithFields() {
	VBox vb = new VBox(); 
	vb.getChildren().add(getUIFactory().makeScreenTitleText(getErrorCheckedPrompt("CustomizeProjectileLauncher")));

	makeProjectileComponents(vb);
	makeLauncherComponents(vb);

	Button applyButton = getUIFactory().setupApplyButton();
	applyButton.setOnAction(e -> {
	    try {
		setProperty(PROJECTILE_OBJECT_TYPE, myObjectName, "ConstantSpeedProperty", myProjectileSpeed);
		setProperty(LAUNCHER_OBJECT_TYPE, myObjectName, "RangeProperty", myLauncherRange);
		setProperty(LAUNCHER_OBJECT_TYPE, myObjectName, "FireRateProperty", myLauncherRateCost, myLauncherRateValue, myLauncherRate);
		setSaved();
		getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_APPLYBUTTON_SCREENFLOW);
	    }
	    catch(NullPointerException e1) {
		getView().loadErrorAlert("NoSelection");
	    }
	});
	
	Parent movementPropertySelector = makePropertySelector(PROJECTILE_OBJECT_TYPE+DEFAULT_SUBFOLDERPATH_SEPARATOR+DEFAULT_MOVEMENTPROPERTY_SUBFOLDERNAME); 
	Parent collisionPropertySelector = makePropertySelector(PROJECTILE_OBJECT_TYPE+DEFAULT_SUBFOLDERPATH_SEPARATOR+DEFAULT_COLLISIONPROPERTY_SUBFOLDERNAME, applyButton); 

	vb.getChildren().add(movementPropertySelector);
	vb.getChildren().add(collisionPropertySelector);
		
	vb.getChildren().add(applyButton);
	
	return vb;
    }

    private void makeProjectileComponents(VBox vb) {

	HBox selectors = new HBox();

	// 86-101 ben auriemma
	String soundPropertiesFilePath = "src/sound/resources/soundFiles.properties";
	ComboBox<String> soundDropdown;
	try {
	    soundDropdown = this.getUIFactory().makeTextDropdown(this.getPropertiesReader().allKeys(soundPropertiesFilePath)); // TODO: this has no prompt, and the action for no choice is null

	    soundDropdown.addEventHandler(ActionEvent.ACTION,e -> {
		    getView().setObjectAttribute(PROJECTILE_OBJECT_TYPE, myObjectName, "mySound", soundDropdown.getSelectionModel().getSelectedItem()); 
	    });
	    
	    VBox projectileSoundSelect = this.getUIFactory().setupSelector(this.getPropertiesReader(), "", soundPropertiesFilePath, "Load New Sound", "New Sound Name:", ".wav", soundDropdown);
	    selectors.getChildren().add(projectileSoundSelect);
	} catch (MissingPropertiesException e) {
	    getView().loadErrorAlert("NoFile");
	    e.printStackTrace(); 
	}

	HBox projectileImageSelect = makeImageSelector(PROJECTILE_OBJECT_TYPE, PROJECTILE_OBJECT_TYPE, PROJECTILE_IMAGE_PREFIX+getView().getTheme()+PROJECTILE_IMAGE_SUFFIX);
	selectors.getChildren().add(projectileImageSelect);
	
	vb.getChildren().add(selectors);

	Slider myProjectileSpeedSlider = getUIFactory().setupSlider(getMyMaxUpgradeIncrement());
	HBox projectileSpeed = getUIFactory().setupSliderWithValue(myProjectileSpeedSlider, getErrorCheckedPrompt("ProjectileSpeed"));
	vb.getChildren().add(projectileSpeed);
	myProjectileSpeedSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
	    myProjectileSpeed = (Double) newValue;
	});
    }

    private void makeLauncherComponents(VBox vb) {

	Slider myLauncherRateSlider = getUIFactory().setupSlider(getMyMaxSpeed());
	HBox launcherRate = getUIFactory().setupSliderWithValue(myLauncherRateSlider, getErrorCheckedPrompt("LauncherRate"));
	vb.getChildren().add(launcherRate);
	myLauncherRateSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
	    myLauncherRate = (Double) newValue;
	});
	
	Slider myLauncherRateValueSlider = getUIFactory().setupSlider(getMyMaxRange());
	HBox launcherRateValue = getUIFactory().setupSliderWithValue(myLauncherRateValueSlider, getErrorCheckedPrompt("LauncherRateUpgradeValue"));
	vb.getChildren().add(launcherRateValue);
	myLauncherRateValueSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
	    myLauncherRateValue = (Double) newValue;
	});
	
	Slider myLauncherRateCostSlider = getUIFactory().setupSlider(getMyMaxPrice());
	HBox launcherRateCost = getUIFactory().setupSliderWithValue(myLauncherRateCostSlider, getErrorCheckedPrompt("LauncherRateUpgradeCost"));
	vb.getChildren().add(launcherRateCost);
	myLauncherRateCostSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
	    myLauncherRateCost = (Double) newValue;
	});
	

	Slider myLauncherRangeSlider = getUIFactory().setupSlider(getMyMaxRange());
	HBox launcherRange = getUIFactory().setupSliderWithValue(myLauncherRangeSlider, getErrorCheckedPrompt("LauncherRange"));
	vb.getChildren().add(launcherRange);
	myLauncherRangeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
	    myLauncherRange = (Double) newValue;
	});
    }
}
