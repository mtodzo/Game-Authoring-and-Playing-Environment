package authoring.frontend;

import javafx.scene.layout.HBox;

/**
 * Abstract class representing the bottom toolbar portion of screens displaying a Path.
 * Used to add basic sizing functionality to those toolbars and extended to provide other 
 * functionality.
 * @author Sarahbland
 *
 */
public abstract class PathToolBar extends AdjustScreen {

    public static final int TOOLBAR_HEIGHT = 120;
    public static final int TOOLBAR_WIDTH = 600;
    private HBox myPathToolBar;

    public PathToolBar(AuthoringView view) {
	super(view);
	setUpSizing();
	makePanel();
    }

    protected void setUpSizing() {
	myPathToolBar = new HBox();
	myPathToolBar.setMaxSize(TOOLBAR_WIDTH, TOOLBAR_HEIGHT);
	myPathToolBar.getStylesheets();
    }

    protected HBox getToolBar() {
	return myPathToolBar;
    }

    protected HBox getPanel() {
	return myPathToolBar;
    }

    protected abstract void makePanel();
}
