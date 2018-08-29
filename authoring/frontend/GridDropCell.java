package authoring.frontend;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Creates the cells that populate the gridPane and allow for the pahtBlocks to be dropped onto them
 * @author erikriis
 */
public class GridDropCell {
    private GridPane myGrid;
    private GridPane myCheckGrid;

    /**
     * Constructs an instance of the drop cell
     * @param grid
     * @param checkGrid
     * @param col
     * @param row
     */
    GridDropCell(GridPane grid, GridPane checkGrid, int col, int row) {
	StackPane cell = new StackPane();
	cell.setMinSize(0, 0);
	myGrid = grid;
	myCheckGrid = checkGrid;
	setCellDragProperties(cell, col, row);
    }

    /**
     * Sets the setOnDragOver and setOnDragDropped functionalities to the cell
     * @param cell
     * @param col
     * @param row
     * @return cell
     */
    private Pane setCellDragProperties(Pane cell, int col, int row) {
	cell.setOnDragOver(new EventHandler <DragEvent>() {
	    public void handle(DragEvent event) {
		if (event.getDragboard().hasImage()) {
		    event.acceptTransferModes(TransferMode.ANY);
		}
		event.consume();
	    }
	});

	cell.setOnDragDropped(new EventHandler <DragEvent>() {
	    public void handle(DragEvent event) {
		event.acceptTransferModes(TransferMode.ANY);
		Dragboard db = event.getDragboard();
		boolean success = false;
		if (db.hasImage()) {
		    DraggableImage path = new DraggableImage(db.getImage());
		    path.setDraggable(myCheckGrid, row, col);
		    path.getPathImage().fitWidthProperty().bind(cell.widthProperty()); 
		    path.getPathImage().fitHeightProperty().bind(cell.heightProperty());
		    myGrid.add(path.getPathImage(), col, row);
		    if (((ImageView) event.getGestureSource()).getId() == CreatePathGrid.START) {	
			path.getPathImage().setId(CreatePathGrid.START);
			myCheckGrid.add(new Label(CreatePathGrid.START), col, row);
		    } else if (((ImageView) event.getGestureSource()).getId() == CreatePathGrid.END) {
			path.getPathImage().setId(CreatePathGrid.END);
			myCheckGrid.add(new Label(CreatePathGrid.END), col, row);
		    } else if (((ImageView) event.getGestureSource()).getId() == CreatePathGrid.PATH) {
			path.getPathImage().setId(CreatePathGrid.PATH);
			myCheckGrid.add(new Label(CreatePathGrid.PATH), col, row);
		    }
		    success = true;
		}
		event.setDropCompleted(success);
		event.consume();
	    }
	});
	myGrid.add(cell, col, row);
	return cell;
    }
}
