package authoring.frontend;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;


/**
 * Class of Images that can be drag/dropped for use in the Path.
 * @author Erik Riis
 *
 */
public class DraggableImage extends Parent {
    private ImageView pathImage;

    /**
     * Constructor of draggable image object that represents the path blocks in path builder
     * @param image
     */
    public DraggableImage(Image image) {
	setPathImage(image);
    }

    /**
     * Sets the ImageView of the draggable image object
     * @param image
     */
    private void setPathImage(Image image) {
	pathImage = new ImageView();
	pathImage.setImage(image);
	pathImage.setFitHeight(CreatePathPanel.PANEL_PATH_SIZE);
	pathImage.setFitWidth(CreatePathPanel.PANEL_PATH_SIZE);
    }

    /**
     * Sets the draggable image so that the image can be dragged but its location (in the panel) stays the same
     * @return pathImage
     */
    public ImageView setCopyDraggable() {
	pathImage.setOnMouseEntered(new EventHandler <MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event){
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0.3);
		pathImage.setEffect(colorAdjust);
		event.consume();    
	    }
	});
	pathImage.setOnMouseExited(new EventHandler <MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event){
		pathImage.setEffect(null);
		event.consume();    
	    }
	});
	pathImage.setOnDragDetected(new EventHandler <MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event){
		Dragboard db = pathImage.startDragAndDrop(TransferMode.COPY);
		ClipboardContent content = new ClipboardContent();
		content.putImage(pathImage.getImage());
		db.setContent(content);
		event.consume();    
	    }
	});

	pathImage.setOnDragDone(new EventHandler <DragEvent>() {
	    @Override
	    public void handle(DragEvent event){
		if (event.getTransferMode() == TransferMode.MOVE){
		    pathImage.setImage(null);
		}
		event.consume();
	    }
	});
	return pathImage;
    }

    /**
     * Sets the draggable image so that it can be dragged and its previous location deleted for dragging in the gridPane
     * @param grid
     * @param row
     * @param col
     */
    public void setDraggable(GridPane grid, int row, int col) {
	pathImage.setOnDragDetected(new EventHandler <MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event){
		removeNode(grid, row, col);
		Dragboard db = pathImage.startDragAndDrop(TransferMode.MOVE);
		ClipboardContent content = new ClipboardContent();
		content.putImage(pathImage.getImage());
		db.setContent(content);
		event.consume();
	    }
	});

	pathImage.setOnDragDone(new EventHandler<DragEvent>() {
	    @Override
	    public void handle(DragEvent e){
		if (e.getTransferMode() == TransferMode.MOVE){
		    ((ImageView) e.getSource()).setImage(null);
		}
		e.consume();
	    }
	});
    }

    /**
     * Removes node from the gridPane given a row and column index
     * @param grid
     * @param row
     * @param col
     */
    public void removeNode(GridPane grid, int row, int col) {
	for(Node node : grid.getChildren()) {
	    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
		grid.getChildren().remove(node);
		break;
	    }
	} 
    }

    /**
     * Sets a new image to the draggable image object
     * @param image
     */
    protected void setNewImage(Image image) {
	pathImage.setImage(image);
    }

    /**
     * Gets the imageView of the draggable image object
     * @return pathImage
     */
    protected ImageView getPathImage() {
	return pathImage;
    }

}