package engine.sprites.properties;

import engine.sprites.enemies.Enemy;

/**
 * Superclass for the properties that handle collisions
 * @author ryanpond
 *
 */
public abstract class CollisionProperty extends Property{
    
    
    public CollisionProperty(double property) {
	super(property);
	// TODO Auto-generated constructor stub
    }

    public abstract void collidesWith(Enemy target);

}
