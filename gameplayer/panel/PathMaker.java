package gameplayer.panel;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import com.sun.javafx.tools.packager.Log;

import frontend.View;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


/**
 * @Author Andrew Arnold & Erik Riis
 */

public class PathMaker {

    private GridPane grid;
    private int myPathSize;

    private final Map<String,String> GAMEPLAYER_PROPERTIES;
    private final View myView;

    public PathMaker(Map<String,String> gamePlayerProperties, View view) {
	GAMEPLAYER_PROPERTIES = gamePlayerProperties;
	myView = view;
    }

    public GridPane initGrid(Map<String, List<Point>> map, String backgroundImage, int pathSize, int width, int height, boolean transparent) {
	grid = new GridPane();

	grid.setMaxSize(width, height);
	grid.setMinSize(width, height);

	myPathSize = pathSize;
	setGridConstraints(grid, width, height);
	if(map!= null && transparent == false) {
	    addImagesToGrid(map);
	}
	return grid;
    }

    private void addImagesToGrid(Map<String, List<Point>> map) {
	for (String key: map.keySet()) {
	    String imageKey = key.substring(1);
	    String imageType = key.substring(0, 1);
	    List<Point> pointList = map.get(key);
	    for (int i = 0; i < pointList.size(); i++) {
		Point point = pointList.get(i);
		ImageView image = new ImageView();
		try{
		    image = new ImageView(new Image(imageKey));
		}
		catch(IllegalArgumentException e){
		    try {
			image = new ImageView(new Image("file:" + GAMEPLAYER_PROPERTIES.get("defaultPathImageFilePath" + imageType)));
		    }
		    catch(IllegalArgumentException e1){
			Log.debug(e1);
			myView.loadErrorScreen("missingPathImages");
		    }
		}
		image.setFitWidth(myPathSize);
		image.setFitHeight(myPathSize);
		GridPane.setFillWidth(image, true);
		GridPane.setFillHeight(image, true);
		grid.add(image, (int)point.getX(), (int)point.getY());
	    }
	}
    }


    private void setGridConstraints(GridPane grid, int width, int height) {
	for (int i = 0; i < width/myPathSize; i++) {
	    ColumnConstraints colConst = new ColumnConstraints();
	    colConst.setPrefWidth(myPathSize);
	    grid.getColumnConstraints().add(colConst);
	}

	for (int i = 0; i < height/myPathSize; i++) {
	    RowConstraints rowConst = new RowConstraints();
	    rowConst.setPrefHeight(myPathSize);
	    grid.getRowConstraints().add(rowConst);         
	}
    }


}
