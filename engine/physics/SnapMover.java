package engine.physics;

import javafx.scene.Node;

/**
 * Handles moving objects to a specific set of coordinates
 * 
 * @author Katherine Van Dyk
 * @date 4/5/18
 *
 */
public class SnapMover implements Mover {

    @Override
    public void move(Node thisNode, double targetX, double targetY) {
	thisNode.setLayoutX(targetX);
	thisNode.setLayoutY(targetY);
    }
    
    @Override
    public void rotate(Node thisNode, Integer targetAngle) {
	thisNode.setRotate(targetAngle);
    }

}
