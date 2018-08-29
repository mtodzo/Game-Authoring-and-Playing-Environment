package xml.serialization;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageWrapper implements ImageViewSerializer {

    private String myImagePath;
    private double height;
    private double width;
    private double x;
    private double y;
    private boolean visible;
    
    public ImageWrapper(String filepath) {
	myImagePath = filepath;
    }
    
    @Override
    public void fromImageView(ImageView image) {
	height = image.getFitHeight();
	width = image.getFitWidth();
	x = image.getX();
	y = image.getY();
	visible = image.isVisible();
    }

    @Override
    public ImageView toImageView() {
	ImageView image = new ImageView(new Image("file:"+myImagePath,50,50,true,true));
	image.setFitHeight(height);
	image.setFitWidth(width);
	//System.out.println("ImageWrapper - IMAGEVIEW FILEPATH: " + myImagePath);
	image.setX(x);
	image.setY(y);
	image.setVisible(visible);
	return image;
    }
    
    /**
     * Updates string representing this object's image filepath based on the parameter
     * @param image	Filepath to change to
     */
    public void updateImageString(String image) {
	myImagePath = image;
    }

}
