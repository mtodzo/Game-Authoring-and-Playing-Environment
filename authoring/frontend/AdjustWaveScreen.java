package authoring.frontend;

import java.awt.Point;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Class to create the Screen where a Wave is specified. Dependent on the Path classes (CreatePathGrid, 
 * etc.) because an interactive view of the Path is displayed to allow designers to choose a specific
 * path for a wave to go down. 
 * @author Sarahbland
 *
 */
public class AdjustWaveScreen extends PathScreen {
    private String myWaveNumber;
    private WaveDirectionsPanel myWaveDirectionsPanel;
    private WaveToolBar myWaveToolBar;
    private CreatePathGrid myGrid;
    private Point myCurrentClicked;


    protected AdjustWaveScreen(AuthoringView view, String waveNumber) {
	super(view);
	myWaveNumber = waveNumber;

    }

    protected void makePanels() {
	myWaveDirectionsPanel = new WaveDirectionsPanel(getView(), myWaveNumber);
	myWaveToolBar = new WaveToolBar(getView());
    }
    /**
     * Sets up grid to show user a new WavePanel (containing choices specific to a given Path) whenever
     * a new Path start block is clicked.
     * @see authoring.frontend.PathScreen#initializeGridSettings(authoring.frontend.CreatePathGrid)
     */
    @Override
    public void initializeGridSettings(CreatePathGrid grid) {
	myGrid = grid;
	setPathPanel(myWaveDirectionsPanel, myWaveToolBar);
	setUpForWaves(e -> {setPathPanel(new WavePanel(getView(), getMostRecentlyClicked(), myWaveNumber), myWaveToolBar);});//TODO: action here!!!);
    }

    protected void setUpForWaves(EventHandler<MouseEvent> action) {
	makeUnDraggable(action);
    }

    private void makeUnDraggable(EventHandler<MouseEvent> action) {	
	for (int i = 0; i < myGrid.getGrid().getChildren().size(); i++) {
	    if (myGrid.getGrid().getChildren().get(i) instanceof ImageView) {
		ImageView node = (ImageView) myGrid.getGrid().getChildren().get(i);
		node.setOnDragDetected(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent e){
			//Do nothing
		    }
		});
	    }
	}

	myGrid.getGrid().setOnMouseClicked(new EventHandler <MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		Node node = (Node) event.getTarget();
		if (node instanceof ImageView && ((ImageView) node).getId() == "start") {
		    Bounds nodeBounds = node.getBoundsInParent();
		    double x = nodeBounds.getMinX();
		    double y = nodeBounds.getMinY();
		    Point point = new Point((int) x, (int) y);
		    myCurrentClicked = point;
		    action.handle(event);
		    ColorAdjust colorAdjust = new ColorAdjust();
		    colorAdjust.setBrightness(0.5);
		    ((ImageView) node).setEffect(colorAdjust);
		}
	    }
	});
    }

    protected Point getMostRecentlyClicked() {
	return myCurrentClicked;
    }


    /**
     * There are no specific UI components in the UI of the WaveScreen, so this method is empty.
     * @see authoring.frontend.PathScreen#setSpecificUIComponents()
     */
    @Override
    public void setSpecificUIComponents() {
	//	    setGridUIComponents(myWaveDirectionsPanel, myWaveToolBar);
    }
}

