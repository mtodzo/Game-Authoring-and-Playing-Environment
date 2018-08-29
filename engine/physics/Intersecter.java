package engine.physics;

import javafx.scene.Node;

/**
 * This interface is to be implemented by an Intersecter which checks if two Nodes are overlapping
 * Any game object which should have behavior associated with intersecting another object will 
 * 		hold an IIntersecter interface
 * Any time a game object needs to check intersection with something else, it will delegate to
 * 		its IIntersecter
 * This design is modeled after the design concept of Composition
 * The implementation of intersection is encapsulated, so methods with different speed and precision 
 * 		can be interchanged without affecting use in the game object
 * 
 * @author benauriemma
 *
 */
public interface Intersecter {
	
	/**
	 * This method is to be used by a game object to check if it is intersecting with another game object
	 * @param thatNode is the Node that it wants to check intersection with
	 * @return true if the objects are overlapping, and false if they are not
	 */
	boolean overlaps(Node thatNode);
	
}
