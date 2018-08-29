package authoring.frontend;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridHelper {
    
    /**
     * Finds a node within a gridPane given a row and column index
     * @param gridPane
     * @param col
     * @param row
     * @return node
     */
    protected Node getNode(GridPane gridPane, int col, int row) {
	Node result = null;
	for (Node node : gridPane.getChildren()) {
	    if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null) {
		result = null;
	    }
	    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
		result = node;
		break;
	    }
	}
	return result;
    }

    /**
     * Removes a node from a gridPane at a given row and column index
     * @param grid
     * @param row
     * @param col
     */
    protected void removeNode(GridPane grid, int row, int col) {
	for(Node node : grid.getChildren()) {
	    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
		grid.getChildren().remove(node);
		break;
	    }
	} 
    }
}

