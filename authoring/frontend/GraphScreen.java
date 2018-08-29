/**
 * Abstract class used to display graphs of game play behavior to authoring users.
 * Used to graph metrics such as currency and score over time to give authors more
 * information about the efficacy of their games.
 * @author Susie Choi
 *
 */

package authoring.frontend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.sun.javafx.tools.packager.Log;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

abstract class GraphScreen extends AuthoringScreen {
	
	protected String myGraphType; 
	
	protected GraphScreen(AuthoringView view, String graphType) {
		super(view);
		myGraphType = graphType; 
		setSaved(); 
	}
	
	protected String parseTitle(String title) {
		String gameName = getView().getGameName(); 
		return title.substring(title.indexOf(gameName)+gameName.length()+1);
	}

	protected LineChart<Number, Number> makeGraph(String title) {
		NumberAxis x = new NumberAxis();
		x.setLabel(getErrorCheckedPrompt("Time"));
		x.setTickLabelsVisible(false);
		x.setForceZeroInRange(false);
		NumberAxis y = new NumberAxis(); 
		y.setLabel(myGraphType);
		LineChart<Number, Number> graph = new LineChart<Number, Number>(x, y);
		graph.setLegendVisible(false);
		graph.setTitle(title);
		return graph; 
	}

	protected void addPointsToGraph(String filepath, LineChart<Number, Number> graph) {
		NumberAxis x = (NumberAxis) graph.getXAxis();
		
		if (graph != null) {
			Series<Number, Number> series = new XYChart.Series<>(); 
			graph.getData().add(series); 

			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(filepath));
			} catch (FileNotFoundException e) {
			    Log.debug(e);	
			    getView().loadErrorScreen("NoGraph");
			}
			String point = null;

			boolean min = true; 
			try {
				while ((point = br.readLine()) != null) {
					String[] xyCoors = point.split("\\s+");
					if (xyCoors.length == 2) {
						series.getData().add(new XYChart.Data<Number, Number>(Integer.parseInt(xyCoors[0]), Integer.parseInt(xyCoors[1])));
						if (min) {
							if (x.getLowerBound() == 0 ||  (x.getLowerBound() > Integer.parseInt(xyCoors[0]))) {
								//System.out.println("MY MIN IS "+Integer.parseInt(xyCoors[0]));
								x.setLowerBound(Integer.parseInt(xyCoors[0]));
							}
							min = false; 
						}
						if (x.getUpperBound() < Integer.parseInt(xyCoors[0])) {
						//	System.out.println("mY MAX IS "+Integer.parseInt(xyCoors[0]));
							x.setUpperBound(Integer.parseInt(xyCoors[0]));
						}
					}
				}
			} catch (NumberFormatException | IOException e) {
			    Log.debug(e);	
			    getView().loadErrorAlert("InvalidValues");
			}
		}
	}

}