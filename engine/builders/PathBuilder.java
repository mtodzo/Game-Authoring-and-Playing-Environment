/*
 * Author: Erik Riis
 */

package engine.builders;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import engine.path.Path;

public class PathBuilder {
	public Path construct(List<List<Point>> coordinates, Map<String, List<Point>> imageCoordinates, String backgroundImage, String pathImage, String startImage, String endImage, int pathSize, int width, int height, boolean transparent) {
		Path newPath = new Path(coordinates, imageCoordinates, backgroundImage, pathImage, startImage, endImage, pathSize, width, height, transparent);
		return newPath;
	}
}
