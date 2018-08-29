package engine.path;

import java.util.List;
import java.util.Map;
import java.awt.Point;

/**
 * Class for constructing path and determining next coordinates
 * 
 * @author Ryan Pond
 * @author Katherine Van Dyk 4/8/18
 */
public class Path {

    private final double THRESHOLD = 5;
    private List<Point> myCoordinates;
    private List<List<Point>> newCoordinates;
    private double myAngle;
    private int pathIndex;
    private int myPathSize;
    private int myWidth;
    private int myHeight;
    private Map<String, List<Point>> myPathMap;
    private String myBackgroundImage;
    private String myPathImage;
    private String myStartImage;
    private String myEndImage;
    private boolean isTransparent;

    public Path(List<List<Point>> coordinates, Map<String, List<Point>> imageCoordinates, String backgroundImage, String pathImage, String startImage, String endImage, int pathSize, int width, int height, boolean transparent) {
	myCoordinates = coordinates.get(0);
	myBackgroundImage = backgroundImage;
	myPathImage = pathImage;
	myStartImage = startImage;
	myEndImage = endImage;
	myPathSize = pathSize;
	myWidth = width;
	myHeight = height;
	pathIndex = 0;
	//		myAngle = getAngle(myCoordinates.get(pathIndex), myCoordinates.get(pathIndex+1));
	myPathMap = imageCoordinates;
	isTransparent = transparent;
    }


    /**
     * Returns the next position along the Path
     * @param pathIndex : current index of the path it is on (block index)
     * @return : returns the next point along the path index
     */
    public Point nextPosition(int pathIndex) {
	return myCoordinates.get(pathIndex+1);
    }

    /**
     * Checks if current position is within the bounds of the next path turn
     * 
     * @return boolean: True if within range of next path coordinate, false otherwise
     */
    private boolean checkBounds(Point currentPos, int pathIndex) {
	double xDistance = Math.pow(myCoordinates.get(pathIndex+1).getX()  - currentPos.getX(), 2);
	double yDistance = Math.pow(myCoordinates.get(pathIndex+1).getY() - currentPos.getY(), 2); 
	return Math.sqrt(xDistance + yDistance) < THRESHOLD;
    }

    /**
     * Returns a new angle for the image
     * 
     * @param point1: initial point
     * @param point2: next point
     * @return double representing angle
     */
    private double getAngle(Point point1, Point point2) {
	double deltaY = point2.getY() - point1.getY();
	double deltaX = point2.getX() - point1.getX();
	double angle = Math.atan2(deltaX,deltaY);
	return angle;
    }

    /**
     * Returns the next position of the object according to its speed
     * 
     * @param mySpeed
     */
    public Point nextPosition(Point currentPos, int pathIndex, double pathAngle) {
	//	if(checkBounds(currentPos, pathIndex)) {
	//	    currentPos = myCoordinates.get(pathIndex+1);
	//	    return currentPos;
	//	}
	//	else {
	//	    System.out.println("here");
	//	    // 	System.out.println("CURRENT XPOS: " + currentPos.getX());
	//	    // 	System.out.println("CURRENT YPOS: " + currentPos.getY());
	//	    double newX = currentPos.getX() + OFFSET - Math.cos(pathAngle) * 3;
	//	    double newY = currentPos.getY() + OFFSET + Math.sin(pathAngle) * 3;
	//	    currentPos.setLocation(newX, newY);
	//	    return currentPos; 
	//	}
	return myCoordinates.get(pathIndex+1);
    }

    public double pathAngle(int currIndex) {
	return getAngle(myCoordinates.get(currIndex),myCoordinates.get(currIndex++));
    }

    public Map<String, List<Point>> getPathMap() {
	return myPathMap;
    }

    public Point initialPoint() {
	return myCoordinates.get(0);
    }

    public int getIndex(Point currentPos, int pathIndex) {
	if(checkBounds(currentPos, pathIndex)) {
	    return pathIndex + 1;
	}
	return pathIndex;
    }

    public boolean checkKill(Point currentPos) {
	double xDistance = Math.pow(myCoordinates.get(myCoordinates.size()-1).getX() - currentPos.getX(), 2);
	double yDistance = Math.pow(myCoordinates.get(myCoordinates.size()-1).getY() - currentPos.getY(), 2); 
	return Math.sqrt(xDistance + yDistance) < 1+THRESHOLD;
    }

    public int getPathSize() {
	return myPathSize;
    }

    public String getBackgroundImage() {
	return myBackgroundImage;
    }

    public int getGridWidth() {
	return myWidth;
    }

    public int getGridHeight() {
	return myHeight;
    }

    public String getPathImage( ) {
	return myPathImage;
    }

    public String getStartImage( ) {
	return myStartImage;
    }

    public String getEndImage( ) {
	return myEndImage;
    }

    public boolean getTransparent() {
	return isTransparent;
    }

    public void updatePathPoints(List<Point> newCoords, Map<String, List<Point>> newImageMap) {
	myCoordinates = newCoords;
	myPathMap = newImageMap;
    }


}
