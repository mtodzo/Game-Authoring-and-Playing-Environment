/**
 * Class for generating common front end UI elmenets, e.g. comboboxes, sliders
 * To ensure consistency of front end design
 * @author susiechoi 
 * @author sarahbland
 */

package frontend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.javafx.tools.packager.Log;

import authoring.frontend.exceptions.MissingPropertiesError;
import authoring.frontend.exceptions.MissingPropertiesException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UIFactory {

	public static final String DEFAULT_BACK_IMAGE = "images/back.gif"; 
	public static final char[] NOT_ALLOWED_FILEPATH = {'*', '.', '\\', '/','\"', '[', ']', ':', ';', '|', '=', ',', ' '};

	/**
	 * Makes Text object for displaying titles of screens to the user
	 * @param titleText is String text displayed as title
	 * @return Text object with this String
	 */
	public Text makeScreenTitleText(String titleText) {
		Text screenTitle = new Text(titleText);
		screenTitle.setId("screenTitle");
		return screenTitle;
	}

	/**
	 * Makes a new button with an image prompt
	 * @param id is CSS id of button
	 * @param buttonImage is Image to be displayed on button
	 * @return Button desired
	 */
	public Button makeImageButton(String id, Image buttonImage) {
		Button newButton = new Button();
		ImageView buttonImageView = new ImageView(buttonImage);
		newButton.setGraphic(buttonImageView);
		newButton.setId(id);
		return newButton; 
	}

	/**
	 * Makes a new button with an image prompt given an ImageView
	 * @param id is CSS id of button
	 * @param buttonImage is ImageView to be displayed on button
	 * @return Button desired
	 */
	public Button makeImageViewButton(String id, ImageView buttonImage) {
		Button newButton = new Button();
		newButton.setGraphic(buttonImage);
		newButton.setId(id);
		return newButton; 
	}

	public TextField makeTextField() {
		return makeTextField("");
	}
	
	/**
	 * Makes new Text field
	 * @param description is CSS description of TextField
	 * @return TextField desired
	 */
	public TextField makeTextField(String description) {
		TextField tf = new TextField();
		tf.setId(description);
		return tf;
	}

	public Button makeTextButton(String buttonText) {
		return makeTextButton("", buttonText);
	}
	
	/**
	 * Makes a new button with an text prompt 
	 * @param id is CSS id of button
	 * @param buttonImage is text to be displayed on button
	 * @return Button desired
	 */
	public Button makeTextButton(String id, String buttonText) {
		Button newButton = new Button(buttonText);
		newButton.setId(id);
		return newButton; 
	}
	
	public ComboBox<String> makeTextDropdownSelectAction(List<String> dropdownOptions, EventHandler<ActionEvent> chooseAction,
			EventHandler<ActionEvent> noChoiceAction, String prompt) {
		return makeTextDropdownSelectAction("", dropdownOptions, chooseAction, noChoiceAction, prompt);
	}

	/**
	 * Makes a ComboBox that completes certain actions when a choice other than the prompt is 
	 * selected and other options when the prompt is selected
	 * @param id is CSS id of ComboBox
	 * @param dropdownOptions is list of options to be displayed on ComboBox
	 * @param chooseAction is lambda of action to be taken when a choice is made
	 * @param noChoiceAction is lambda of action to be taken when prompt is chosen (no choice)
	 * @param prompt is Prompt option
	 * @return ComboBox with eventhandlers set
	 */
	public ComboBox<String> makeTextDropdownSelectAction(String id, List<String> dropdownOptions, EventHandler<ActionEvent> chooseAction,
			EventHandler<ActionEvent> noChoiceAction, String prompt){
		ComboBox<String> dropdown = makeTextDropdown(id, dropdownOptions);
		dropdown.setOnAction( e ->{
			if(!dropdown.getValue().equals(prompt)){
				chooseAction.handle(e);
			}
			else {
				noChoiceAction.handle(e);
			}
		});
		return dropdown;
	}

	public ComboBox<String> makeTextDropdown(List<String> dropdownOptions) {
		return makeTextDropdown("", dropdownOptions);
	}
	
	/**
	 * Sets up a new ComboBox dropdown with text
	 * @param id is CSS id of dropdown
	 * @param dropdownOptions is list of options to be displayed to user
	 * @return ComboBox dropdown desired
	 */
	public ComboBox<String> makeTextDropdown(String id, List<String> dropdownOptions) {
		ObservableList<String> options = FXCollections.observableArrayList(dropdownOptions);
		ComboBox<String> newDropdown = new ComboBox<String>(options);
		newDropdown.getSelectionModel().selectFirst(); 
		newDropdown.setId(id);
		return newDropdown; 
	}

	public Slider setupSlider(int sliderMax) {
		return setupSlider("", sliderMax);
	}
	
	/**
	 * Sets up a new Slider given a max value
	 * @param id is CSS id of slider
	 * @param sliderMax is max value allowed on Slider
	 * @return Slider desired
	 */
	public Slider setupSlider(String id, int sliderMax) {
		Slider slider = new Slider();
		slider.setMax(sliderMax);
		Text sliderValue = new Text(String.format("%03d", (int)slider.getValue()));
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
				sliderValue.setText(String.format("%03d", (int)(double)new_val));
			}
		});
		slider.setId(id);
		return slider; 

	}

	/**
	 * Makes a Text Scroll pane with certain options
	 * @param id is CSS id of ScrollPane
	 * @param options is List of options to be displayed
	 * @return ScrollPane
	 */
	public ScrollPane makeTextScrollPane(String id, List<String> options) {
		ScrollPane newPane = new ScrollPane();
		String allOptions = "";
		for (String s : options) {
			allOptions += s+"\n";
		}
		newPane.setContent(new Text(allOptions));
		newPane.setId(id);
		return newPane; 
	}

	/**
	 * Makes an Image Scroll pane with certain options
	 * @param id is CSS id of ScrollPane
	 * @param options is List of files containing Image options to be displayed
	 * @return ScrollPane
	 */
	public ScrollPane makeImageScrollPane(String id, File[] files) {
		ScrollPane newPane = new ScrollPane();
		VBox imageConsolidator = new VBox();
		for (File f : files) {
			ImageView oneImageView = new ImageView();
			oneImageView.setImage(new Image(f.toURI().toString()));
			imageConsolidator.getChildren().add(oneImageView);
		}
		newPane.setContent(imageConsolidator);
		newPane.setId(id);
		return newPane; 
	}

	/**
	 * Sets up a FileChooser UI element to allow a user to select a new file
	 * @param id is CSS id of FileChooser
	 * @param newFilePrompt is text prompting to select a new file
	 * @param newFileNamePrompt is text prompting to enter a new filename
	 * @param propertiesFilepath is filepath to properties where filename should be writted
	 * @param action is action to be taken when file is chosen
	 * @param keysAndVals is map of current keys and values in the properties file
	 * @param extension is extension (ex. ".png") of file to be chosen
	 * @return VBox containing filechooser and prompts
	 * @throws MissingPropertiesException
	 */
	public VBox setupFileChooser(String id, String newFilePrompt, String newFileNamePrompt, String propertiesFilepath,
			EventHandler<ActionEvent> action, Map<String, String> keysAndVals, String extension) throws MissingPropertiesException{
		VBox vbox = new VBox();
		TextField imageNameEntry = new TextField();
		HBox imageNamer = addPromptAndSetupHBox("selector",imageNameEntry, newFileNamePrompt);
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extensionFilter = 
				new FileChooser.ExtensionFilter("file extension","*" + extension);
		fileChooser.getExtensionFilters().add(extensionFilter);
		Button filePrompt = makeTextButton(id, newFilePrompt);
		filePrompt.setDisable(true);
		imageNameEntry.setOnAction(e -> {filePrompt.setDisable(false);});
		try {
			filePrompt.setOnAction(e -> {
				String imageName = imageNameEntry.getText();
				if(!imageName.equals(null)) {
					for(int k = 0; k< NOT_ALLOWED_FILEPATH.length; k+=1) {
						imageName.replace(NOT_ALLOWED_FILEPATH[k], 'a');
					}

					File file = fileChooser.showOpenDialog(new Stage());
					file.getAbsolutePath();
					File fileCopy = new File("images/" + imageName + extension);
					try{
						Files.copy(file.toPath(), fileCopy.toPath(), StandardCopyOption.REPLACE_EXISTING);
					}
					catch(IOException e2) {
						throw new MissingPropertiesError("");
					}
					keysAndVals.put(imageName, fileCopy.getPath());
					PropertiesWriter writer = new PropertiesWriter(propertiesFilepath, keysAndVals);
					writer.write();
					action.handle(e);
				}
			});
		}
		catch(MissingPropertiesError e) {
			throw new MissingPropertiesException(e.getLocalizedMessage());
		}
		vbox.setId(id);
		vbox.getChildren().add(imageNamer);
		vbox.getChildren().add(filePrompt);
		return vbox;
	}

	/**
	 * Sets up a Selector to select a file or from given options or a new file
	 * @param propertiesReader is a PropertiesReader
	 * @param description is CSS description
	 * @param propertiesFilepath is filepath to PropertiesFile used to select
	 * @param newFilePrompt is prompt for user to choose a new file
	 * @param newFileNamePrompt is prompt for user to choose a new filename
	 * @param extension is extension of file to choose (ex. ".png")
	 * @param dropdown is ComboBox to populate
	 * @return VBox containing fileChooser/selector options
	 * @throws MissingPropertiesException
	 */
	public VBox setupSelector(PropertiesReader propertiesReader, String description, String propertiesFilepath,
			String newFilePrompt, String newFileNamePrompt, String extension, ComboBox<String> dropdown) throws MissingPropertiesException{
		VBox vb = new VBox();
		Map<String, String> options = propertiesReader.read(propertiesReader.loadProperties(propertiesFilepath));
		ArrayList<String> optionsList = new ArrayList<String>(options.keySet());
		VBox fc = new VBox();
		try {
			fc = setupFileChooser("", newFilePrompt, newFileNamePrompt, propertiesFilepath, e -> {
				optionsList.clear();
				Map<String, String> options2 = new HashMap<>();
				try {
					options2 = propertiesReader.read(propertiesReader.loadProperties(propertiesFilepath));
				}
				catch(MissingPropertiesException e2) {
				    	Log.debug(e2);
					e2.printStackTrace();
					throw new MissingPropertiesError("");
				}
				ArrayList<String> optionsList2 = new ArrayList<String >(options2.keySet());
				optionsList.addAll(optionsList2);
				dropdown.getItems().clear();
				dropdown.getItems().addAll(optionsList);
			}, options, extension);
		}
		catch(MissingPropertiesError e) {
			throw new MissingPropertiesException(e.getLocalizedMessage());
		}
		vb.getChildren().add(dropdown);
		vb.getChildren().add(fc);
		vb.setId("selector");
		return vb;
	}

	/**
	 * Sets up a new Image Selector from which users can choose an existing image option or
	 * upload their own image.
	 * @param propertiesReader is PropertiesReader to read from properties file
	 * @param description is CSS id
	 * @param propertiesFilepath is filepath to Properties file housing names/filepaths
	 * @param imageSize is image size desired
	 * @param loadImagePrompt is prompt to user to load an existing image
	 * @param newImagePrompt is prompt to user to create a new image
	 * @param newImageNamePrompt is prompt to user to load a new image name
	 * @param dropdown is ComboBox containing existing choices
	 * @param imageDisplay is ImageView where the chosen image will be displayed
	 * @return HBox containing entire image selector module (image view, file chooser, dropdown)
	 * @throws MissingPropertiesException
	 */
	public HBox setupImageSelector(PropertiesReader propertiesReader, String description, String propertiesFilepath, double imageSize,
			String loadImagePrompt, String newImagePrompt, String newImageNamePrompt, ComboBox<String> dropdown, ImageView imageDisplay) throws MissingPropertiesException {
		Map<String, Image> enemyImageOptions = propertiesReader.keyToImageMap(propertiesFilepath, imageSize, imageSize);
		ArrayList<Image> images = new ArrayList<Image>(enemyImageOptions.values()); 
		imageDisplay.setImage(images.get(0));
		VBox selector = setupSelector(propertiesReader, description, propertiesFilepath, newImagePrompt, newImageNamePrompt,".png", dropdown);
		try {
			dropdown.setOnAction(e ->
			{
				try{
					imageDisplay.setImage(images.get(dropdown.getSelectionModel().getSelectedIndex()));
				}
				catch(Exception e2) {
					try {
						imageDisplay.setImage(images.get(0));
					}
					catch(Exception e3) {
						throw new MissingPropertiesError("");
					}

				};
			});
		}
		catch(MissingPropertiesError e) {
			throw new MissingPropertiesException(e.getLocalizedMessage());
		}
		HBox hb = new HBox();
		hb.getChildren().add(selector);
		hb.getChildren().add(imageDisplay);
		return hb; 
	}
	
	public HBox setupSliderWithValue(Slider slider, String prompt) {
		return setupSliderWithValue("", slider, prompt);
	}
	
	/**
	 * Sets up a new slider displaying its value next to it
	 * @param id is CSS id
	 * @param slider is Slider already created
	 * @param prompt is prompt to user to change slider
	 * @return HBox containing Slider/prompt/current value displaying
	 */
	public HBox setupSliderWithValue(String id, Slider slider, String prompt) {
		Text sliderValue = new Text(String.format("%03d", (int)slider.getValue()));
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
				sliderValue.setText(String.format("%03d", (int)(double)new_val));
			}
		});
		HBox hb = new HBox();
		hb.getChildren().add(slider);
		hb.getChildren().add(sliderValue);
		return addPromptAndSetupHBox(id, hb, prompt);
	}

	public HBox addPromptAndSetupHBox(Node node, String prompt) {
		return addPromptAndSetupHBox("", node, prompt);
	}
	
	/**
	 * Adds Prompt and sets up an HBox given a UI element
	 * @param id is CSS ID
	 * @param node is UI element needing to be boxed
	 * @param prompt is prompt to be displayed alongside UI element
	 * @return HBox containing prompt, UI element
	 */
	public HBox addPromptAndSetupHBox(String id, Node node, String prompt) {
		HBox hbox = new HBox();
		hbox.setId(id);
		Text promptText = new Text(prompt);
		hbox.getChildren().add(promptText);
		hbox.getChildren().add(node);
		return hbox;	
	}

	/**
	 * Makes TextField cursor blink
	 * @param screen is Scene containing TextField
	 * @param textField is TextField that needs to blink
	 */
	public void applyTextFieldFocusAction(Scene screen, TextField textField) {
		screen.setOnMousePressed(event -> {
			if (!textField.equals(event.getSource())) {
				textField.getParent().requestFocus();
			}
		});
	}


	/**
	 * Sets up a generic Back Button
	 * @param action is action to be taken on BackButton press
	 * @param backText is text to be displayed on back button
	 * @return Button desired
	 */
	public Button setupBackButton(EventHandler<ActionEvent> action, String backText) {
		Button backButton = makeTextButton("backButton",backText);
		backButton.setOnAction(e -> {action.handle(e);});
		return backButton; 
	}

	/**
	 * @return Button with text Apply
	 */
	public Button setupApplyButton() {
		Button applyButton = makeTextButton("applyButton", "Apply"); //TODO: set up prompts properties file	
		return applyButton;
	}

	/**
	 * Creates an HBox to display back and apply buttons
	 * @param back is Back button
	 * @param apply is Apply button
	 * @return HBox displaying the two buttons
	 */
	public HBox setupBackAndApplyButton(Button back, Button apply) {
		HBox backAndApplyButton = new HBox(); 
		backAndApplyButton.getChildren().add(back);
		backAndApplyButton.getChildren().add(apply);
		return backAndApplyButton; 
	}

	/**
	 * Method used in appropriately-setting the ComboBox when populating data fields with the existing object values
	 * @param combobox - combobox to be set to a value
	 * @param selectionValue - the value that the combobox should be set to 
	 */
	public void setComboBoxToValue(ComboBox<String> combobox, String selectionValue) {
		int dropdownIdx = combobox.getItems().indexOf(selectionValue); 
		combobox.getSelectionModel().select(dropdownIdx);
	}

	/**
	 * Method used in appropriately-setting the slider when populating data fields with the existing object values
	 * @param slider - slider to be set to a value
	 * @param valueAsString - the value that the slider should be set to 
	 */
	public void setSliderToValue(Slider slider, String valueAsString) {
		Double value = Double.parseDouble(valueAsString);
		slider.setValue(value);
	}

	public List<String> getFileNames(String folderName) {
	    	//System.out.println("folder name: ");
		String currentDir = System.getProperty("user.dir");
		File file = new File(currentDir + File.separator + folderName);
		//System.out.println(currentDir+ File.separator + folderName);
		File[] fileArray = file.listFiles();
		List<String> fileNames = new ArrayList<>();
		for (File aFile : fileArray) {
			String colorName = aFile.getName();
			String[] nameSplit = colorName.split("\\.");
			String fileName = nameSplit[0];
			fileNames.add(fileName);
		}
		return Collections.unmodifiableList(fileNames);
	}

}