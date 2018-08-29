package authoring.frontend;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Class that aids in creating dropdowns in the pathscreen when a user
 * right clicks on a specific block. Can be used to create other dropdowns
 * in other contexts as well.
 * @author erik riis
 *
 */
public class RightClickDropDown {
    private Node myNode;
    private SelectionModel myModel;

    public RightClickDropDown(Node node, SelectionModel model) {
	myNode = node;
	myModel = model;
	setupDropDown();
	setRightClickDropDown();
    }

    /**
     * Sets a dropdown on right click for the node given when the class was
     * constructed.
     */
    private void setRightClickDropDown() {
	ContextMenu dropDown = setupDropDown();
	myNode.setOnMousePressed(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY || event.isControlDown()) {
		    dropDown.show(myNode, event.getScreenX(), event.getScreenY());
		}
	    }
	});
    }

    private ContextMenu setupDropDown() {
	ContextMenu contextMenu = new ContextMenu();
	
	MenuItem item1 = new MenuItem("Copy");
	item1.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		myModel.copy();
	    }
	});
	
	MenuItem item2 = new MenuItem("Paste");
	item2.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		myModel.paste();
	    }
	});

	contextMenu.getItems().addAll(item1, item2);
	return contextMenu;
    }
}
