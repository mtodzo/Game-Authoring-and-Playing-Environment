/**
 * @author susiechoi
 * @author Katherine Van Dyk
 * Abstract class for developing the fields for customizing (new or existing) enemy object
 */

package authoring.frontend;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

class AdjustEnemyScreen extends AdjustNewOrExistingScreen {

	public static final String DEFAULT_NOSELECTION_ERRORKEY = "NoSelection";
	public static final String OBJECT_TYPE = "Enemy";
	public static final String DEFAULT_SHOOTING_SCREENFLOW_KEY = "Shooting";
	public static final String DEFAULT_APPLYBUTTON_SCREENFLOW = "Apply";
	public static final String ENEMY_IMAGE_PREFIX = "images/ThemeSpecificImages/EnemyImages/";
	public static final String ENEMY_IMAGE_SUFFIX = "EnemyImageNames.properties";
	public static final String ENEMY_FIELDS = "default_objects/EnemyFields.properties";

	private String myObjectName; 
	private TextField myNameField; 
	private Slider mySpeedSlider;
	private Slider myInitialHealthSlider; 
	private Slider myHealthImpactSlider; 
	private Slider myValueSlider; 

	private Double mySpeed;
	private Double myInitialHealth;
	private Double myHealthImpact;
	private Double myKillReward;

	protected AdjustEnemyScreen(AuthoringView view, String selectedObjectName) {
		super(view, selectedObjectName, ENEMY_FIELDS, OBJECT_TYPE);
		myObjectName = selectedObjectName; 
	}

	@Override
	protected Parent populateScreenWithFields() {
		VBox vb = new VBox(); 	
		vb.getChildren().add(getUIFactory().makeScreenTitleText(getErrorCheckedPrompt("CustomizeEnemy")));
		HBox enemyImageSelect = makeImageSelector(OBJECT_TYPE, "", ENEMY_IMAGE_PREFIX + getView().getTheme() + ENEMY_IMAGE_SUFFIX);
		vb.getChildren().add(enemyImageSelect);

		Slider enemySpeedSlider = getUIFactory().setupSlider(getMyMaxSpeed()); 
		mySpeedSlider = enemySpeedSlider; 
		HBox enemySpeed = getUIFactory().setupSliderWithValue(enemySpeedSlider, getErrorCheckedPrompt("EnemySpeed"));
		vb.getChildren().add(enemySpeed);
		mySpeedSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
			mySpeed = (Double) newValue;
			//	getView().setObjectAttribute(OBJECT_TYPE, myObjectName, "mySpeed", newValue);
		});

		Slider enemyInitialHealthSlider = getUIFactory().setupSlider(getMyMaxHealthImpact()); 
		myInitialHealthSlider = enemyInitialHealthSlider; 
		HBox initialHealth = getUIFactory().setupSliderWithValue(enemyInitialHealthSlider, getErrorCheckedPrompt("EnemyInitialHealth")); 
		vb.getChildren().add(initialHealth);
		myInitialHealthSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
			myInitialHealth = (Double) newValue;
			//getView().setObjectAttribute(OBJECT_TYPE, myObjectName, "myInitialHealth", newValue);
		});

		Slider enemyHealthImpactSlider = getUIFactory().setupSlider(getMyMaxHealthImpact()); 
		myHealthImpactSlider = enemyHealthImpactSlider; 
		HBox enemyImpact = getUIFactory().setupSliderWithValue(enemyHealthImpactSlider, getErrorCheckedPrompt("EnemyHealthImpact")); 
		vb.getChildren().add(enemyImpact);
		myHealthImpactSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
			myHealthImpact = (Double) newValue;
			//getView().setObjectAttribute(OBJECT_TYPE, myObjectName, "myHealthImpact", newValue);
		});

		Slider enemyValueSlider = getUIFactory().setupSlider(getMyMaxPrice());
		myValueSlider = enemyValueSlider;
		HBox enemyValue = getUIFactory().setupSliderWithValue(enemyValueSlider, getErrorCheckedPrompt("EnemyValue"));
		vb.getChildren().add(enemyValue);
		myValueSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
			myKillReward = (Double) newValue;
			//getView().setObjectAttribute(OBJECT_TYPE, myObjectName, "myKillReward", newValue);
		});
		
		vb.getChildren().add(makePropertySelector(OBJECT_TYPE));

		Button goToProjectileLauncherButton = getUIFactory().makeTextButton(getErrorCheckedPrompt("CustomizeProjectileLauncher"));
		vb.getChildren().add(goToProjectileLauncherButton);
		goToProjectileLauncherButton.setOnAction(e -> {
			try{
				setProperty(OBJECT_TYPE, myObjectName, "ValueProperty", myKillReward);
				setProperty(OBJECT_TYPE, myObjectName, "DamageProperty", 0.0, 0.0, myHealthImpact);
				setProperty(OBJECT_TYPE, myObjectName, "HealthProperty", 0.0, 0.0, myInitialHealth);
				setProperty(OBJECT_TYPE, myObjectName, "SpeedProperty", 0.0, 0.0, mySpeed);
				setSaved();
				getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_SHOOTING_SCREENFLOW_KEY, myObjectName);
			}
			catch(NullPointerException e1) {
				getView().loadErrorAlert(DEFAULT_NOSELECTION_ERRORKEY); 
			}
		});

		Button backButton = setupBackButton();

		Button applyButton = getUIFactory().setupApplyButton();
		applyButton.setOnAction(e -> {
			try {
				setProperty(OBJECT_TYPE, myObjectName, "ValueProperty", myKillReward);
				setProperty(OBJECT_TYPE, myObjectName, "DamageProperty", 0.0, 0.0, myHealthImpact);
				setProperty(OBJECT_TYPE, myObjectName, "HealthProperty", 0.0, 0.0, myInitialHealth);
				setProperty(OBJECT_TYPE, myObjectName, "SpeedProperty", 0.0, 0.0, mySpeed);
				setSaved();
				getView().goForwardFrom(this.getClass().getSimpleName()+DEFAULT_APPLYBUTTON_SCREENFLOW);			
			}
			catch(NullPointerException e1) {
				getView().loadErrorAlert(DEFAULT_NOSELECTION_ERRORKEY);
			}

		});

		HBox backAndApplyButton = getUIFactory().setupBackAndApplyButton(backButton, applyButton);
		vb.getChildren().add(backAndApplyButton);

		return vb;
	}

	protected TextField getNameField() {
		return myNameField; 
	}
}
