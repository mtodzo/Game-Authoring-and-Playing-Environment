package engine.sprites.properties;

import java.awt.Point;

import engine.sprites.Sprite;
import engine.sprites.towers.projectiles.Projectile;

public class ClickToShootProperty extends ClickProperty {

    public ClickToShootProperty(double speed) {
	super(speed);
    }
    
    public ClickToShootProperty(Property p) {
  	super(p.getProperty());
      }

    @Override
    public void handleClick(Sprite spriteToMove, double destX, double destY) {
	spriteToMove.addProperty(new HeatSeekingProperty(this.getProperty()));
	Projectile myProjectile = (Projectile) spriteToMove;
	Point newPoint = new Point();
	newPoint.setLocation(destX,destY);
	myProjectile.setProjectileTarget(newPoint);
    }

}
