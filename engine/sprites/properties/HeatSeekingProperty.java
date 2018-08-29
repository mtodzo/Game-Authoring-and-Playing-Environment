package engine.sprites.properties;

import java.awt.Point;

import engine.sprites.ShootingSprites;
import engine.sprites.towers.projectiles.Projectile;

/**
 * This class will handle the movement of a projectile that is heat seeking 
 * (Follows the enemy it is shot at)
 * @author ryanpond
 *
 */
public class HeatSeekingProperty extends MovingProperty{

    public HeatSeekingProperty(double range) {
	super(range);
    }
    
    public HeatSeekingProperty(Property p) {
	super(p.getProperty());
    }

    @Override
    public boolean move(Projectile projectile, double elapsedTime) {
	if (projectile.isTargetAlive()) {
	    rotateImage(projectile, projectile.getTargetDestination());
	}
	double totalDistanceToMove = projectile.getSpeed()*elapsedTime;
	double xMove = Math.sin(Math.toRadians(projectile.getRotate()))*totalDistanceToMove;
	double yMove = Math.cos(Math.toRadians(projectile.getRotate()))*totalDistanceToMove;
	projectile.getImageView().setX(projectile.getX()+xMove);
	projectile.getImageView().setY(projectile.getY()+yMove);
	
	return this.checkIfProjectileIsOutOfRange(projectile);
    }

    /**
     * Rotates the image to face the target
     */
    private void rotateImage(Projectile projectile, Point myTarget) {
	double xDifference = myTarget.getX() - projectile.getX();
	double yDifference = myTarget.getY() - projectile.getY();
	double angleToRotateRads = Math.atan2(xDifference,yDifference);
	projectile.setRotate(Math.toDegrees(angleToRotateRads));
    }

    
    
    
    

}
