package authoring.frontend;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Allows for users to select one path block at a time and multiple gridPane cells in order to copy and paste path blocks if they click
 * the corresponding items while holding the Shift key
 * @author erikriis
 *
 */
public class ShiftSelection {

    private SelectionModel selectionModel;
    private Pane myGroup;

    /**
     * Initializes the shift selection functionality and adds the corresponding items to a selection model that holds the selected items
     * @param group
     * @param model
     */
    public ShiftSelection(Pane group, SelectionModel model) {
	this.selectionModel = model;
	this.myGroup = group;
	
	myGroup.setOnMouseClicked(new EventHandler <MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		if (event.isShiftDown() && event.getTarget() instanceof StackPane) {
		    selectionModel.addCell((Pane) event.getTarget());
		    new RightClickDropDown((Pane) event.getTarget(), selectionModel);
		} else if (!event.isShiftDown() && event.getButton() != MouseButton.SECONDARY && event.getTarget() instanceof StackPane) {
		    selectionModel.removeCell((Pane) event.getTarget());
		} else if (event.isShiftDown() && event.getTarget() instanceof ImageView) {
		    ImageView selectedImage = (ImageView) event.getTarget();
		    selectedImage.setId(((ImageView) event.getTarget()).getId());
		    selectionModel.addImage(selectedImage);
		    new RightClickDropDown((Node) event.getTarget(), selectionModel);
		} else if (!event.isShiftDown() && event.getButton() != MouseButton.SECONDARY && event.getTarget() instanceof ImageView) {
		    selectionModel.removeNode((Node) event.getTarget());
		}
	    }
	});
    }
 }
