package authoring.frontend;

import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Class used to graph two game graphs at once (i.e. graph the score for 
 * two different plays of the same game).
 * @author Susie Choi
 *
 */
public class DoubleGraphScreen extends GraphScreen {

	private String myGame1FilePath; 
	private String myGame2FilePath; 

	protected DoubleGraphScreen(AuthoringView view, String game1Path, String game2Path, String graphType) {
		super(view, graphType);
		myGame1FilePath = game1Path; 
		myGame2FilePath = game2Path; 
	}

	/**
	 * Makes the UI elements necessary to render the screen.
	 * @see frontend.Screen#makeScreenWithoutStyling()
	 */
	@Override
	public Parent makeScreenWithoutStyling() {
		VBox vb = new VBox();

		Text graphTitle = getUIFactory().makeScreenTitleText(getErrorCheckedPrompt("GraphTitle"));
		LineChart<Number, Number> graph = makeGraph(parseTitle(myGame1FilePath)); 
		addPointsToGraph(myGame1FilePath, graph);
		LineChart<Number, Number> graph2 = makeGraph(parseTitle(myGame2FilePath)); 
		addPointsToGraph(myGame2FilePath, graph2);
		
		vb.getChildren().addAll(graphTitle, graph, graph2, setupBackButton());

		return vb;
	}

}
