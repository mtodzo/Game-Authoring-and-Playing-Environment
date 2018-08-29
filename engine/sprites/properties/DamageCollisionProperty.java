package engine.sprites.properties;

import engine.sprites.enemies.Enemy;

/**
 * This should be the default CollisionProperty, as it just does damage to the enemy
 * @author ryanpond
 *
 */
public class DamageCollisionProperty extends CollisionProperty{

    private double myDamage;
    public DamageCollisionProperty(double property) {
	super(property);
	myDamage = property;
    }
    
    public DamageCollisionProperty(Property p) {
	super(p.getProperty());
	myDamage = p.getProperty();
    }

    @Override
    public void collidesWith(Enemy target) {
	target.loseHealth(myDamage);
    }
    

}
