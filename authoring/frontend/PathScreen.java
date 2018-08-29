package authoring.frontend;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

/**
 * Abstract class of Screens that include a view of the Path (currently PathScreens and WaveScreens,
 * but open to extension by other types of screens). Dependent on concrete classed to make the
 * specific panel and toolbar, but creates the grid and allows specific setup options.
 * @author erik riis
 * @author Sarahbland
 *
 */
public abstract class PathScreen extends AuthoringScreen {

    public static final String DEFAULT_OWN_STYLESHEET = "styling/CreatePath.css";
    public static final String DEFAULT_PLUS_IMAGE = "file:images/plus.png";
    public static final String DEFAULT_MINUS_IMAGE = "file:images/minus.png";
    public static final String DEFAULT_NOFILE_ERROR_KEY = "NoFile";
    private StackPane pathRoot;
    protected GridPane pathGrid;
    protected CreatePathGrid grid;
    
    protected PathScreen(AuthoringView view) {	
	super(view);
	setStyleSheet(view.getCurrentCSS());

	Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	int gridWidth = (int) primaryScreenBounds.getWidth() - CreatePathPanel.PANEL_WIDTH;
	int gridHeight = (int) primaryScreenBounds.getHeight() - PathToolBar.TOOLBAR_HEIGHT;

	grid = new CreatePathGrid(view, gridWidth, gridHeight);
    }

    protected void setPathPanel(PathPanel panel, PathToolBar toolbar) {
	pathRoot.getChildren().clear();
	pathRoot.getChildren().add(panel.getPanel());
	pathRoot.getChildren().add(toolbar.getPanel());

	pathRoot.getChildren().add(grid.getGrid());

	StackPane.setAlignment(grid.getGrid(), Pos.TOP_LEFT);
	StackPane.setAlignment(panel.getPanel(), Pos.CENTER_RIGHT);
	StackPane.setAlignment(toolbar.getPanel(), Pos.BOTTOM_LEFT);
    }

    /** (non-Javadoc)
     * Creates basic screen components (
     * @see frontend.Screen#makeScreenWithoutStyling()
     */
    @Override
    public Parent makeScreenWithoutStyling() {

	pathGrid = grid.makePathGrid();
	pathRoot = new StackPane();
	makePanels();
	initializeGridSettings(grid);
	setSpecificUIComponents();
	return pathRoot; 	
    }

    protected abstract void makePanels();

    protected abstract void initializeGridSettings(CreatePathGrid grid);

    protected abstract void setSpecificUIComponents();

    protected CreatePathGrid getGrid() {
	return grid;
    }
}
