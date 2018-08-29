package authoring.frontend;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * Class to create/manage the GridPane for path authoring and display.
 * @author Erik Riis
 *
 */
public class CreatePathGrid {

    public static final int INITIAL_PATH_SIZE = 50; 
    public static final int DEFAULT_SIZE = 2;
    public static final String DEFAULT_BACKGROUND_IMAGE = "file:images/generalbackground.jpg"; 
    public static final String START = "start";
    public static final String PATH = "path";
    public static final String END = "end";
    private int pathSize = INITIAL_PATH_SIZE;
    private GridPane grid = new GridPane();
    private GridPane checkGrid;
    private ArrayList<Point> pathCoords = new ArrayList<Point>();
    private ArrayList<Point> startPoints = new ArrayList<Point>();
    private AuthoringView myView;
    private int myGridWidth;
    private int myGridHeight;
    private GridHelper gridCommand = new GridHelper();

    /**
     * Constructor for creating the path grid
     * @param view
     * @param width
     * @param height
     */
    public CreatePathGrid(AuthoringView view, int width, int height) {
	myView = view;
	myGridWidth = width;
	myGridHeight = height;
    }

    /**
     * Constructs the GridPane and sets its background and row/column constraints, initializes a shift selection functionality
     * to select a path block and then copy and paste it to any selected grid cells
     * @return grid
     */
    protected GridPane makePathGrid() {
	grid = new GridPane();
	grid.setMaxSize(myGridWidth, myGridHeight); 

	checkGrid = new GridPane();
	checkGrid.setMaxSize(myGridWidth, myGridHeight);

	SelectionModel model = new SelectionModel(grid, checkGrid, pathSize);
	new ShiftSelection(grid, model);

	grid.setStyle("-fx-background-image: url(" + DEFAULT_BACKGROUND_IMAGE + ")"); 
	setGridConstraints(grid, INITIAL_PATH_SIZE);
	
	@SuppressWarnings("unchecked")
	Map<String, List<Point>> loadedPathMap = (Map<String, List<Point>>) myView.getObjectAttribute("Path", "", "myPathMap");
	if (loadedPathMap.size() > DEFAULT_SIZE) {
	    setGridConstraints(grid, (int) myView.getObjectAttribute("Path", "", "myPathSize"));
	    grid.setStyle("-fx-background-image: url("+myView.getObjectAttribute("Path", "", "myBackgroundImage")+")");
	    addImagesToGrid(loadedPathMap, (int) myView.getObjectAttribute("Path", "", "myPathSize"));
	}
	return grid;
    }

    /**
     * Populates the grid cells with pane that except drag drop events in order to drag and drop the path blocks
     * @param grid
     */
    private void populateGridCells(GridPane grid) { 
	for (int x = 0 ; x < grid.getColumnCount(); x++) {
	    for (int y = 0 ; y < grid.getRowCount(); y++) {
		new GridDropCell(grid, checkGrid, x, y);
	    }
	}
    }
    
    /**
     * Sets the constraints for the rows and columns of the GridPane according to the desired path size and the grid height/width 
     * @param grid
     * @param size
     */
    protected void setGridConstraints(GridPane grid, int size) {
	grid.getColumnConstraints().clear();
	grid.getRowConstraints().clear();
	pathSize = size;
	for (int i = 0; i < myGridWidth/pathSize; i++) {
	    ColumnConstraints colConst = new ColumnConstraints();
	    colConst.setPercentWidth(myGridWidth/pathSize);
	    grid.getColumnConstraints().add(colConst);
	}
	for (int i = 0; i < myGridHeight/pathSize; i++) {
	    RowConstraints rowConst = new RowConstraints();
	    rowConst.setPercentHeight(myGridHeight/pathSize);
	    grid.getRowConstraints().add(rowConst);         
	}
	populateGridCells(grid);
    }

    /**
     * Populates the GridPane with draggable images when loading a previously constructed path for a level
     * @param map
     * @param pathSize
     */
    private void addImagesToGrid(Map<String, List<Point>> map, int pathSize) {
	checkGrid.getChildren().clear();
	for (String key: map.keySet()) {
	    String imageKey = key.substring(1);
	    List<Point> pointList = map.get(key);
	    for (int i = 0; i < pointList.size(); i++) {
		Point point = pointList.get(i);
		DraggableImage path = new DraggableImage(new Image(imageKey));
		path.setDraggable(checkGrid, (int)point.getY(), (int)point.getX());
		path.getPathImage().setFitWidth(pathSize);
		path.getPathImage().setFitHeight(pathSize);
		grid.add(path.getPathImage(), (int)point.getX(), (int)point.getY());
		if (key.equals("s"+myView.getObjectAttribute("Path", "", "myStartImage"))) {
		    checkGrid.add(new Label(START), (int)point.getX(), (int)point.getY());
		    path.getPathImage().setId(START);
		} else if (key.equals("p"+myView.getObjectAttribute("Path", "", "myPathImage"))) {
		    checkGrid.add(new Label(PATH), (int)point.getX(), (int)point.getY());
		    path.getPathImage().setId(PATH);
		} else if (key.equals("e"+myView.getObjectAttribute("Path", "", "myEndImage"))) {
		    checkGrid.add(new Label(END), (int)point.getX(), (int)point.getY());
		    path.getPathImage().setId(END);
		}
		GridPane.setFillWidth(path.getPathImage(), true);
		GridPane.setFillHeight(path.getPathImage(), true);
	    }
	}
    }

    /**
     * Recursively iterates the GridPane following the path blocks to check if the start blocks and end blocks are connected
     * and adds coordinates at each step that enemies will follow in the game engine
     * @param grid
     * @param row
     * @param col
     * @return boolean that indicates if the path is complete
     */
    protected boolean checkPathConnected(GridPane grid, int row, int col) {
	Label checkLabel = (Label) gridCommand.getNode(grid, col, row);
	if (gridCommand.getNode(grid, col, row) != null) {
	    if (checkLabel.getText() == END) {
		return true;
	    } 
	} else {
	    return false;
	}
	gridCommand.removeNode(grid, row, col);
	addCoordinates(row, col);

	if ((checkPathConnected(grid, row + 1 , col)) == true) {
	    grid.add(new Label(PATH), col, row);
	    return true;
	}
	if ((checkPathConnected(grid, row, col + 1)) == true) {
	    grid.add(new Label(PATH), col, row);
	    return true;
	}
	if ((checkPathConnected(grid, row, col - 1)) == true) {
	    grid.add(new Label(PATH), col, row);
	    return true;
	}
	if ((checkPathConnected(grid, row - 1, col)) == true) {
	    grid.add(new Label(PATH), col, row);
	    return true;
	}
	return false;
    }

    /**
     * Adds a coordinate point that represents the coordinate absolute location (not row/col index) in the pane to a list 
     * for enemies to follow in game engine
     * @param row
     * @param col
     */
    protected void addCoordinates(int row, int col) {
	Bounds nodeBounds = gridCommand.getNode(grid, col, row).getBoundsInParent();
	double x = nodeBounds.getMinX();
	double y = nodeBounds.getMinY();
	Point point = new Point((int) Math.round(x), (int) Math.round(y));
	pathCoords.add(point);
    }

    /**
     * Iterates through the grid to find all of the starting positions in the GridPane as row and column indexes
     * @param grid
     * @return startPoints
     */
    protected List<Point> getStartingPosition(GridPane grid) { 
	startPoints = new ArrayList<Point>();
	for (int x = 0; x < grid.getColumnCount(); x++) {
	    for (int y = 0; y < grid.getRowCount(); y++) {
		if (gridCommand.getNode(grid, x, y) != null && ((Label) gridCommand.getNode(grid, x, y)).getText().equals(START)) {
		    startPoints.add(new Point(x, y));
		}
	    }
	}
	return startPoints;
    }

    /**
     * Creates a map that correlates a path image file to all of its locations within the GridPane so that the GamePlayer frontend 
     * can reconstruct the path
     * @param grid
     * @param startImage
     * @param pathImage
     * @param endImage
     * @return gridImageCoordinates
     */
    protected HashMap<String, List<Point>> getGridImageCoordinates(GridPane grid, String startImage, String pathImage, String endImage) {
	List<Point> pathPoints = new ArrayList<Point>();
	List<Point> endPoints = new ArrayList<Point>();
	HashMap<String, List<Point>> gridImageCoordinates = new HashMap<String, List<Point>>();
	for (int x = 0; x < grid.getColumnCount(); x++) {
	    for (int y = 0; y < grid.getRowCount(); y++) {
		if (gridCommand.getNode(grid, x, y) != null && ((Label) gridCommand.getNode(grid, x, y)).getText().equals(PATH)) {
		    pathPoints.add(new Point(x, y));
		} else if (gridCommand.getNode(grid, x, y) != null && ((Label) gridCommand.getNode(grid, x, y)).getText().equals(END)) {
		    endPoints.add(new Point(x, y));
		}
	    }
	}
	gridImageCoordinates.put("s"+startImage, startPoints);
	gridImageCoordinates.put("e"+endImage, endPoints);
	gridImageCoordinates.put("p"+pathImage, pathPoints);
	return gridImageCoordinates;
    }
    
    /**
     * Copies the gridPane that is used for checking if the grid is completed in chechPathConnected
     * @param grid
     * @return copiedGrid
     */
    public GridPane copyGrid(GridPane grid) {
	GridPane copiedGrid = new GridPane();
	for (int x = 0; x < grid.getColumnCount(); x++) {
	    for (int y = 0; y < grid.getRowCount(); y++) {
		if (gridCommand.getNode(grid, x, y) != null && ((Label) gridCommand.getNode(grid, x, y)).getText() == START) {
		    copiedGrid.add(new Label(START), x, y);
		} else if (gridCommand.getNode(grid, x, y) != null && ((Label) gridCommand.getNode(grid, x, y)).getText() == PATH) {
		    copiedGrid.add(new Label(PATH), x, y);
		} else if (gridCommand.getNode(grid, x, y) != null && ((Label) gridCommand.getNode(grid, x, y)).getText() == END) {
		    copiedGrid.add(new Label(END), x, y);
		}
	    }
	}
	return copiedGrid;
    }

    /**
     * Gets the width of the gridPane
     * @return myGridWidth
     */
    public int getGridWidth() {
	return myGridWidth;
    }

    /**
     * Gets the height of the gridPane
     * @return myGridHeight
     */
    public int getGridHeight() {
	return myGridHeight;
    }

    /**
     * Returns the main gridPane
     * @return grid
     */
    protected GridPane getGrid() {
	return grid;
    }

    /**
     * returns the gridPane that is used for checking path completion
     * @return checkGrid
     */
    protected GridPane getCheckGrid() {
	return checkGrid;
    }

    /**
     * Sets the background of the gridPane
     * @param backGroundFileName
     */
    protected void setBackgroundImage(String backGroundFileName) {
	grid.setStyle("-fx-background-image: url(" + backGroundFileName + ")");
    }

    /**
     * Gets the size of each path block
     * @return pathSize
     */
    protected int getPathSize() {
	return pathSize;
    }

    /**
     * Gets the absolute coordinates of each block in the gridPane (not row/col index)
     * @return pathCoords
     */
    protected List<Point> getAbsoluteCoordinates() {
	return pathCoords;
    }
}