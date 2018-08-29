/**
 * Menu screen where user can choose which graphs they would like to view for the 
 * specified game.
 * @author Susie Choi
 *
 */

package authoring.frontend;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GraphMenuScreen extends AuthoringScreen {

	public static final String DEFAULT_GRAPHS_FOLDER = "graphing/";
	public static final String DEFAULT_CSS = "styling/GameAuthoringStartScreen.css";
	
	private String myGraphType; 
	
	protected GraphMenuScreen(AuthoringView view, String graphType) {
		super(view);
		myGraphType = graphType; 
		setSaved();
	}
	
	private String makeFullFilepath(String dropdownSelection) {
		return DEFAULT_GRAPHS_FOLDER+myGraphType+"/"+dropdownSelection;
	}

	/**
	 * Creates UI elements (specifically button choices) that allow the user
	 * to select which graph he or she would like to view.
	 * @see frontend.Screen#makeScreenWithoutStyling()
	 */
	@Override
	public Parent makeScreenWithoutStyling() {
		VBox vb = new VBox();

		Text screenTitle = getUIFactory().makeScreenTitleText(getErrorCheckedPrompt("GraphMenu"));
		vb.getChildren().add(screenTitle);
		
		List<String> availableGraphs = getUIFactory().getFileNames(DEFAULT_GRAPHS_FOLDER+myGraphType);
		List<String> relevantGraphs = new ArrayList<>(); 

		for (String graphName : availableGraphs) {
			String currGameName = getView().getGameName();
			if (graphName.indexOf(currGameName) > -1) {
				relevantGraphs.add(graphName);
			}
		}
		
		Button singleButton = getUIFactory().makeTextButton(getErrorCheckedPrompt("SingleGame"));
		String choosePrompt = getView().getErrorCheckedPrompt("ChooseGame");
		List<String> singleDropdownOptions = new ArrayList<String>();
		singleDropdownOptions.add(choosePrompt);
		singleDropdownOptions.addAll(relevantGraphs);
		ComboBox<String> singleChooser = getUIFactory().makeTextDropdownSelectAction("", singleDropdownOptions, 
				e -> {singleButton.setDisable(false);}, 
				e -> {singleButton.setDisable(true);}, choosePrompt);
		singleButton.setDisable(true);
		singleButton.setOnAction(e -> {
			String fullFilepath  = makeFullFilepath(singleChooser.getSelectionModel().getSelectedItem()); 
			getView().getStageManager().switchScreen(new SingleGraphScreen(getView(), fullFilepath, myGraphType).getScreen());
		});
		
		vb.getChildren().addAll(singleChooser, singleButton);
		vb.getChildren().add(new Text(getErrorCheckedPrompt("or")));
		
		Button compareButton = getUIFactory().makeTextButton(getErrorCheckedPrompt("CompareGames"));
		List<String> dropdownOptions = new ArrayList<String>();
		dropdownOptions.add(choosePrompt);
		dropdownOptions.addAll(relevantGraphs);
		ComboBox<String> game1Chooser = getUIFactory().makeTextDropdownSelectAction("", dropdownOptions, 
				e -> {;}, 
				e -> {compareButton.setDisable(true);}, choosePrompt);
		ComboBox<String> game2Chooser = getUIFactory().makeTextDropdownSelectAction("", dropdownOptions, 
			e -> { compareButton.setDisable(false);}, 
			e -> {compareButton.setDisable(true);}, choosePrompt);
		compareButton.setDisable(true);
		compareButton.setOnAction(e -> {
			String game1Path = makeFullFilepath(game1Chooser.getSelectionModel().getSelectedItem()); 
			String game2Path = makeFullFilepath(game2Chooser.getSelectionModel().getSelectedItem()); 
			getView().getStageManager().switchScreen(new DoubleGraphScreen(getView(), game1Path, game2Path, myGraphType).getScreen());
		});

		vb.getChildren().addAll(game1Chooser, game2Chooser, compareButton);
		vb.getChildren().add(setupBackButton());

		return vb; 
	}


}