package engine.physics;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import engine.sprites.Sprite;
import javafx.scene.Node;

/**
 * Handles determining whether or not two images have intersected
 * 
 * @author Katherine Van Dyk
 * @date 4/5/18
 *
 */
public class ImageIntersecter implements Intersecter {

    @XStreamOmitField
    private transient Node currentNode;
    
    public ImageIntersecter(Sprite input) {
	currentNode = input.getImageView();
    }
    /**
     * Determines if two nodes on the screen overlap
     * 
     * @return True if two nodes intersect, false otherwise
     */
    @Override
    public boolean overlaps(Node thatNode) {
	return currentNode.getBoundsInLocal().intersects(thatNode.getBoundsInLocal());
    }

}
