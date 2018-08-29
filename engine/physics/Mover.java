package engine.physics;

import javafx.scene.Node;

/**
 * This interface is to be implemented by an Mover which can move or rotate an object
 * Any game object which should have the behaviors of moving and rotating should hold an 
 * 		instance of this interface
 * Any time a game object needs to move or rotate, it will delegate to its IMover
 * This design is modeled after the design concept of Composition
 * The implementation of movement and rotation are encapsulated, so methods that use different types of 
 * 		animation can be substituted without affecting code in the game object
 * 
 * @author benauriemma
 *
 */
public interface Mover {
	
	/**
	 * This method is to be called by an object which desires to move to a new location in the scene
	 * @param thisNode is a Node of the object which wants to move
	 * @param targetX is the x coordinate of the pixel this object desires to move to
	 * @param targetY is the y coordinate of the pixel this object desires to move to
	 */
	void move(Node thisNode, double targetX, double targetY);
	
	/**
	 * This method is to be called by an object which desires to rotate to a new orientation
	 * @param thisNode is a Node of the object which wants to rotate
	 * @param targetAngle is the desired orientation in degrees counterclockwise from the positive x axis
	 */
	void rotate(Node thisNode, Integer targetAngle);
	
}
