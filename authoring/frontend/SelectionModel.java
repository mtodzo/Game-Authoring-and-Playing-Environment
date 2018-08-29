package authoring.frontend;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Creates the selection model that items get added to on shift selection and the functionalities of copy and paste
 * @author erikriis
 */
public class SelectionModel {
    public static final String CELL_SELECT_STYLE = "-fx-background-color: rgba(255,255,255, 0.3); -fx-background-radius: 10;";
    public static final String CELL_DESELECT_STYLE = "-fx-background-color: rgba(0,0,0,0);";
    private Set<Pane> selectedCells = new HashSet<>();
    private ImageView selectedNode;
    private ImageView copyImage;
    private GridPane myGrid;
    private GridPane myCheckGrid;
    private int mySize;

    /**
     * Constructor for the Selection Model
     * @param grid
     * @param checkGrid
     * @param size
     */


    public SelectionModel(GridPane grid, GridPane checkGrid, int size) {
	
	myGrid = grid;
	myCheckGrid = checkGrid;
	mySize = size;
    }

    /**
     * Adds a gridPane cell to selection model
     * @param pane
     */
    public void addCell(Pane pane) {
	pane.setStyle(CELL_SELECT_STYLE);
	selectedCells.add(pane);
    }

    /**
     * Removes a gridPane cell from selection model
     * @param pane
     */
    public void removeCell(Pane pane) {
	pane.setStyle(CELL_DESELECT_STYLE);
	selectedCells.remove(pane);
    }

    /**
     * Sets a path block node as "selected"
     * @param node
     */
    public void addImage(ImageView node) {
	if (selectedNode != null) {
	    selectedNode.setEffect(null);
	}
	ColorAdjust colorAdjust = new ColorAdjust();
	colorAdjust.setBrightness(0.5);
	node.setEffect(colorAdjust);
	selectedNode = node;
    }

    /**
     * Sets a path block node as "unselected"
     * @param node
     */
    public void removeNode(Node node) {
	node.setEffect(null);
	selectedNode = null;
    }

    /**
     * Returns a set that contains all of the selected gridPane cells
     * @return
     */
    public Set<Pane> getSelectedCells() {
	return selectedCells;
    }

    /**
     * Sets the selected path block node as "copied"
     */
    public void copy() {
	selectedNode.setEffect(null);
	copyImage = selectedNode;
    }

    /**
     * Pastes the selected path block node into every selected gridPane cell
     */
    public void paste() { 
	
	if (copyImage != null) {
	    for (Pane cell: selectedCells) {
		cell.setStyle(CELL_DESELECT_STYLE);
		int col = GridPane.getColumnIndex(cell);
		int row = GridPane.getRowIndex(cell);
		DraggableImage path = new DraggableImage(copyImage.getImage());
		path.setDraggable(myCheckGrid, row, col);
		path.getPathImage().setFitWidth(mySize);
		path.getPathImage().setFitHeight(mySize);
		if (copyImage.getId() == CreatePathGrid.START) {
		    myCheckGrid.add(new Label(CreatePathGrid.START), col, row);
		    path.getPathImage().setId(CreatePathGrid.START);
		} else if (copyImage.getId() == CreatePathGrid.PATH) {
		    myCheckGrid.add(new Label(CreatePathGrid.PATH), col, row);
		    path.getPathImage().setId(CreatePathGrid.PATH);
		} else if (copyImage.getId() == CreatePathGrid.END) {
		    myCheckGrid.add(new Label(CreatePathGrid.END), col, row);
		    path.getPathImage().setId(CreatePathGrid.END);
		}
		myGrid.add(path.getPathImage(), col, row);
		GridPane.setFillWidth(path.getPathImage(), true);
		GridPane.setFillHeight(path.getPathImage(), true);
	    }
	}
    }
}

