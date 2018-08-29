package authoring.frontend;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;


/**
 * Class to create the righthand panel for the path authoring screen.
 * @author Erik Riis
 *
 */
public class CreatePathPanel extends PathPanel {

    public static final int PANEL_PATH_SIZE = 90;
    public static final int PANEL_WIDTH = 330;
    public static final int PANEL_HEIGHT = 900;
    public static final int TRASH_IMAGE_SIZE = 120;
    public static final String TRASH_IMAGE = "file:images/trash.png";
    public static final String DEFAULT_PATH_IMAGE = "file:images/cobblestone.png";
    public static final String DEFAULT_START_IMAGE = "file:images/brick.png";
    public static final String DEFAULT_END_IMAGE = "file:images/darkstone.png";
    private VBox pathPanel;
    private DraggableImage pathImage;
    private DraggableImage startImage;
    private DraggableImage endImage;
    private Button applyButton;
    private Button backButton;
    private ImageView trashImage;
    private boolean isTransparent;

    public CreatePathPanel(AuthoringView view) {
	super(view);
	makeScreenWithoutStyling();
	makePanel();
    }

    @Override
    protected Button getApplyButton() {
	return applyButton;
    }


    @Override
    public Parent makeScreenWithoutStyling() {
	pathPanel = new VBox();
	pathPanel.setMaxSize(PANEL_WIDTH, PANEL_HEIGHT);
	pathPanel.getStylesheets();
	return pathPanel;
    }

    @Override
    protected void makePanel() {

	Label panelTitle = new Label("Drag and Drop Paths");
	Label startLabel = new Label("Start:");
	Label pathLabel = new Label("Path:");
	Label endLabel = new Label("End:");

	pathImage = new DraggableImage(new Image(DEFAULT_PATH_IMAGE));
	pathImage.setCopyDraggable();
	pathImage.getPathImage().setId(CreatePathGrid.PATH);

	startImage = new DraggableImage(new Image(DEFAULT_START_IMAGE));
	startImage.setCopyDraggable();
	startImage.getPathImage().setId(CreatePathGrid.START);

	endImage = new DraggableImage(new Image(DEFAULT_END_IMAGE));
	endImage.setCopyDraggable();
	endImage.getPathImage().setId(CreatePathGrid.END);

	applyButton = getUIFactory().makeTextButton("", "Apply");
	backButton = setupBackButton();

	trashImage = new ImageView(new Image(TRASH_IMAGE, TRASH_IMAGE_SIZE, TRASH_IMAGE_SIZE, true, false));

	ToggleGroup radioGroup = new ToggleGroup();
	ToggleButton transparentToggle = new ToggleButton("Make Path Transparent");
	transparentToggle.setToggleGroup(radioGroup);
	radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
	    public void changed(ObservableValue<? extends Toggle> ov,
		    Toggle toggle, Toggle new_toggle) {
		if (new_toggle == null) {
		    isTransparent = false;
		} else {
		    isTransparent = true;
		}
	    }
	});
	pathPanel.getChildren().addAll(panelTitle, startLabel, startImage.getPathImage(), pathLabel, pathImage.getPathImage(), endLabel, endImage.getPathImage(), trashImage, transparentToggle, applyButton, backButton);
    }

    @Override
    protected void setApplyButtonAction(EventHandler<ActionEvent> e) {
	applyButton.setOnAction(
		event -> {
		    setSaved();
		    e.handle(event);
		});
    }

    @Override
    protected Node getPanel() {
	return pathPanel;
    }

    /**
     * Creates the trash image in the panel that allows for dragging and dropping path blocks to delete them from the gridPane
     * @return trashImage
     */
    protected ImageView makeTrashImage() {
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
	return trashImage;
    }

    /**
     * gets a boolean that discerns if the user has toggled for the path blocks to be transparent in the Game Player
     * @return isTransparent
     */
    protected boolean getTransparent() {
	return isTransparent;
    }

    /**
     * Gets the DraggableImage that represents a path block
     * @return pathImage
     */
    protected DraggableImage getPanelPathImage() {
	return pathImage;
    }
    
    /**
     * Gets the DraggableImage that represents a start block
     * @return startImage
     */
    protected DraggableImage getPanelStartImage() {
	return startImage;
    }

    /**
     * Gets the DraggableImage that represents an end block
     * @return endImage
     */
    protected DraggableImage getPanelEndImage() {
	return endImage;
    }
}
