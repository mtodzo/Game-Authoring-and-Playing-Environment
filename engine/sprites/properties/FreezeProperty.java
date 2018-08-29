package engine.sprites.properties;

import engine.sprites.enemies.Enemy;

/**
 * This property is a collision property that will freeze the enemies movement for a specified number of seconds
 * @author ryanpond
 *
 */
public class FreezeProperty extends CollisionProperty{

    private double freezeTime;
    
    public FreezeProperty(double property) {
	super(property);
	freezeTime = property;
	// TODO Auto-generated constructor stub
    }
    
    public FreezeProperty(Property p) {
	super(p.getProperty());
	freezeTime = p.getProperty();
    }

    @Override
    public void collidesWith(Enemy target) {
	target.freeze(freezeTime);
    }
    
    
    
    

}
