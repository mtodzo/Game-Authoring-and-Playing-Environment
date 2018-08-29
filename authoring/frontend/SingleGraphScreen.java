/**
 * Class that creates Screen to allow users to view a single screen depicting
 * a graph of a certain game attribute (currency, etc.). Dependent on gameplayer
 * to record information correctly.
 * @author susiechoi
 *
 */

package authoring.frontend;

import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SingleGraphScreen extends GraphScreen {
	
	private String myGameFilepath;

	protected SingleGraphScreen(AuthoringView view, String filepath, String graphType) {
		super(view, graphType);
		myGameFilepath = filepath; 
		setSaved(); 
	}

	/**
	 * Creates UI components (specifically graph) that user sees on screen.
	 */
	@Override
	public Parent makeScreenWithoutStyling() {	
		VBox vb = new VBox();
		
		Text graphTitle = getUIFactory().makeScreenTitleText(getErrorCheckedPrompt("GraphTitle"));
		LineChart<Number, Number> graph = makeGraph(parseTitle(myGameFilepath)); 
		addPointsToGraph(myGameFilepath, graph);
		vb.getChildren().addAll(graphTitle, graph, setupBackButton());
		
		return vb;
	}
}
