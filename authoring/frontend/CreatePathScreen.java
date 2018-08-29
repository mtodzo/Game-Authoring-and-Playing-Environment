package authoring.frontend;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import authoring.frontend.exceptions.MissingPropertiesException;
import authoring.frontend.exceptions.ObjectNotFoundException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Sets up the entire create path screen
 * @author erikriis
 *
 */
public class CreatePathScreen extends PathScreen {
    public static final int POPUP_SIZE = 500;
    private CreatePathPanel myPathPanel;
    private CreatePathToolBar myPathToolBar;
    private GridHelper gridCommand = new GridHelper();
    private String myBackgroundImage = CreatePathGrid.DEFAULT_BACKGROUND_IMAGE;
    private List<List<Point>> myCoords = new ArrayList<List<Point>>();
    private boolean gridCheck = false;
    private CreatePathScreen me;
    private AuthoringView myView;
    private Button myPlusButton;
    private Button myMinusButton;
    private Button mySizeApplyButton;
    private Stage myDialogStage;

    /**
     * Constructor for the create path screen
     * @param view
     */
    public CreatePathScreen(AuthoringView view) {
	super(view);
	myView = view;

    }
    @Override
    protected void makePanels() {
	myPathPanel = new CreatePathPanel(getView());
	myPathToolBar = new CreatePathToolBar(getView());
	me = this;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void initializeGridSettings(CreatePathGrid gridIn) {
	setPathPanel(myPathPanel, myPathToolBar);
	setGridApplied(gridIn);
	if (((Map<String, List<Point>>) getView().getObjectAttribute("Path", "", "myPathMap")).size() == CreatePathGrid.DEFAULT_SIZE) {
	    setPathInstructionPopup();
	    setGridUIComponents();
	}
    }
    
    /**
     * Sets the functionality of the apply button in the create path screen which checks for path completion and send the path info to
     * game engine and game player or throws error alerts if the path is not complete
     * @param grid
     */
    private void setGridApplied(CreatePathGrid grid) {
	myPathPanel.setApplyButtonAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent e) {
		setSaved();
		List<Point> startCoords = grid.getStartingPosition(grid.getCheckGrid());
		if (startCoords.size() == 0) {
		    getView().loadErrorAlert("PathNoStart");
		}
		for (Point point: startCoords) {
		    gridCheck = false;
		    grid.getAbsoluteCoordinates().clear();
		    if (grid.checkPathConnected(grid.copyGrid(grid.getCheckGrid()), (int) point.getY(), (int) point.getX()) == true) {
			gridCheck = true;
			List<Point> coords = new ArrayList<Point>(grid.getAbsoluteCoordinates());
			myCoords.add(coords);

		    } else {
			gridCheck = false;
		    }
		}

		if (gridCheck == true) {
		    try {
			String startImage = myPathPanel.getPanelStartImage().getPathImage().getImage().getUrl();
			String pathImage = myPathPanel.getPanelPathImage().getPathImage().getImage().getUrl();
			String endImage = myPathPanel.getPanelEndImage().getPathImage().getImage().getUrl();
			grid.getStartingPosition(grid.getCheckGrid());
			
			getView().makePath(grid.getGrid(), myCoords, grid.getGridImageCoordinates(grid.getCheckGrid(), startImage, pathImage, endImage), myBackgroundImage, 
				pathImage, startImage, endImage, grid.getPathSize(), grid.getGridWidth(), grid.getGridHeight(), myPathPanel.getTransparent());
			getView().goForwardFrom(me.getClass().getSimpleName()+"Apply");
		    } catch (ObjectNotFoundException e1) {
			getView().loadErrorScreen("NoObject");
		    }
		} else {
		    getView().loadErrorAlert("PathCustomization");
		}
	    }
	});
    }
    
    /**
     * Sets an HBox containing the path sizing (plus and minus) buttons
     * @return sizingButtons
     */
    protected HBox makeSizingButtons() {
	HBox sizingButtons = new HBox();
	try {
	    int buttonWidth = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS_FILEPATH, "SizeButtonWidth"));
	    int buttonHeight = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS_FILEPATH, "SizeButtonHeight"));
	    Image plusImg = new Image(DEFAULT_PLUS_IMAGE, buttonWidth, buttonHeight, true, false);
	    myPlusButton = getUIFactory().makeImageButton("", plusImg);
	    myPlusButton.setStyle("-fx-min-width: 50;");

	    Image minusImg = new Image(DEFAULT_MINUS_IMAGE, buttonWidth, buttonHeight, true, false);
	    myMinusButton = getUIFactory().makeImageButton("", minusImg);
	    myMinusButton.setStyle("-fx-min-width: 50;");

	    mySizeApplyButton = getUIFactory().makeTextButton("", "Apply");
	    sizingButtons.getChildren().addAll(myPlusButton, myMinusButton, mySizeApplyButton);

	    return sizingButtons;
	}
	catch(MissingPropertiesException e) {
	    getView().loadErrorScreen("NoConstants");
	    return null;
	}
    }

    /**
     * Sets functionality for the sizing buttons and size apply button
     */
    protected void setGridUIComponents() {
	myPlusButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		try {
		    int gridResize = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS_FILEPATH, "GridResize"));
		    if (grid.getPathSize() < Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS_FILEPATH, "MaxGridSize"))) {
			grid.setGridConstraints(pathGrid, grid.getPathSize() + gridResize);
		    }
		}
		catch(MissingPropertiesException e) {
		    getView().loadErrorScreen("NoFile");
		}
	    }
	});

	myMinusButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		try {
		    int gridResize = Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS_FILEPATH, "GridResize"));
		    if (grid.getPathSize() > Integer.parseInt(getPropertiesReader().findVal(DEFAULT_CONSTANTS_FILEPATH, "MinGridSize"))) {
			grid.setGridConstraints(pathGrid, grid.getPathSize() - gridResize);
		    }
		}
		catch(MissingPropertiesException e) {
		    getView().loadErrorScreen("NoFile");
		}
	    }
	});

	mySizeApplyButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		myPathPanel.getPanel().setDisable(false);
		myPathToolBar.getPanel().setDisable(false);
		grid.getGrid().setGridLinesVisible(false);
		myDialogStage.close();
	    }
	});
    }

    /**
     * Sets the popup that contains the path builder instructions and the path sizing buttons which must be set before setting path blocks
     */
    public void setPathInstructionPopup() {
	myDialogStage = new Stage();
	VBox dialogVbox = new VBox();
	dialogVbox.setMaxSize(POPUP_SIZE, POPUP_SIZE);
	Label popupTitle = new Label(getView().getErrorCheckedPrompt("PathPopupTitle"));
	Label popupInstructions = new Label(getView().getErrorCheckedPrompt("PathInstructions"));
	popupInstructions.setWrapText(true);
	dialogVbox.getChildren().addAll(popupTitle, popupInstructions, makeSizingButtons());
	
	Scene dialogScene = new Scene(dialogVbox);
	dialogScene.getStylesheets().add(myView.getCurrentCSS());
	myDialogStage.setScene(dialogScene);
	myDialogStage.setAlwaysOnTop(true);
	
	myPathPanel.getPanel().setDisable(true);
	myPathToolBar.getPanel().setDisable(true);
	grid.getGrid().setGridLinesVisible(true);
	
	myDialogStage.setOnCloseRequest(event -> {
	    myPathPanel.getPanel().setDisable(false);
	    myPathToolBar.getPanel().setDisable(false);
	    grid.getGrid().setGridLinesVisible(false);
	});
	myDialogStage.show();
    }


    @Override
    protected void setSpecificUIComponents() {
	ImageView trashImage = myPathPanel.makeTrashImage();
	trashImage.setOnDragOver(new EventHandler <DragEvent>() {
	    @Override
	    public void handle(DragEvent event) {
		if (event.getDragboard().hasImage()) {
		    event.acceptTransferModes(TransferMode.ANY);
		}
	    }
	});

	trashImage.setOnDragDropped(new EventHandler <DragEvent>() {
	    @Override
	    public void handle(DragEvent event) {
		event.acceptTransferModes(TransferMode.ANY);
		Dragboard db = event.getDragboard();
		boolean success = false;
		if (db.hasImage()) {
		    success = true;
		}
		event.setDropCompleted(success);
		event.consume();
	    }
	});

	ComboBox<String> backgroundComboBox =  getComboBox(myPathToolBar.getBackgroundHBox());
	backgroundComboBox.addEventHandler(ActionEvent.ACTION, e -> {
	    myBackgroundImage = "file:images/"+backgroundComboBox.getValue()+".png";
	    getGrid().setBackgroundImage(myBackgroundImage);
	});

	ComboBox<String> pathComboBox = getComboBox(myPathToolBar.getPathHBox());
	setPathImages(pathComboBox, CreatePathGrid.PATH);

	ComboBox<String> startComboBox = getComboBox(myPathToolBar.getStartHBox());
	setPathImages(startComboBox, CreatePathGrid.START);

	ComboBox<String> endComboBox = getComboBox(myPathToolBar.getEndHBox());
	setPathImages(endComboBox, CreatePathGrid.END);
    }
    
    
    @SuppressWarnings("unchecked")
    private ComboBox<String> getComboBox(HBox hb)  {
	return (ComboBox<String>) ((VBox) hb.getChildren().get(0)).getChildren().get(0);
    }
    
    /**
     * Changes the path block images in the panel 
     * @param box
     * @param pathType
     */
    private void setPathImages(ComboBox<String> box, String pathType) {
	box.addEventHandler(ActionEvent.ACTION, e -> {
	    String pathImageFilePath = "file:images/"+box.getValue()+".png";
	    if (pathType == CreatePathGrid.PATH) {
		myPathPanel.getPanelPathImage().setNewImage(new Image(pathImageFilePath));
	    } else if (pathType == CreatePathGrid.START) {
		myPathPanel.getPanelStartImage().setNewImage(new Image(pathImageFilePath));
	    } else if (pathType == CreatePathGrid.END) {
		myPathPanel.getPanelEndImage().setNewImage(new Image(pathImageFilePath));
	    }
	    changeGridImages(pathImageFilePath, pathType);
	});
    }

    /**
     * Changes the corresponding path block images in the gridPane
     * @param imageFilePath
     * @param pathType
     */
    private void changeGridImages(String imageFilePath, String pathType) {
	for (int i = 0; i < getGrid().getGrid().getChildren().size(); i++) {
	    Node node = getGrid().getGrid().getChildren().get(i);
	    if (node instanceof ImageView && ((Label) gridCommand.getNode(getGrid().getCheckGrid(), GridPane.getColumnIndex(node), GridPane.getRowIndex(node))).getText() == pathType) {
		((ImageView) node).setImage(new Image(imageFilePath));
	    }
	}
    }
}
