package engine.sprites.properties;

import java.awt.Point;
import java.util.List;

import engine.sprites.towers.projectiles.Projectile;

/**
 * Abstract superclass whose subclasses will specify the move methods for different patterns of projectile movement.
 * @author milestodzo
 *
 */

public abstract class MovingProperty extends Property{
    
    private Point projectileOrigin;
    
    public MovingProperty(double range) {
	super(range);
	projectileOrigin = new Point();
      }
    
    public abstract boolean move(Projectile projectile, double elapsedTime);
    
    public boolean checkIfProjectileIsOutOfRange(Projectile projectile) {
	return false;
	//double distanceBetween = Math.sqrt(Math.pow(projectileOrigin.getX()-projectile.getX(),2)+Math.pow(projectileOrigin.getY()-projectile.getY(), 2));
	//return (distanceBetween >= this.getProperty());
    }
    
    public void setProjectileOrigin(double x, double y) {
	projectileOrigin.setLocation(x, y);
    }
}
