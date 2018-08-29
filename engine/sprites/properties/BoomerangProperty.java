package engine.sprites.properties;

import engine.sprites.towers.projectiles.Projectile;

/**
 * This class handles projectile movement for boomerang type projectiles.
 * @author milestodzo
 *
 * @param <Projectile>
 */

public class BoomerangProperty extends MovingProperty{
    private double tCircle = 0.0;
    private double range = 50;

    public BoomerangProperty(double range) {
	super(range);
    }

    @Override
    public boolean move(Projectile projectile, double elapsedTime) {
	tCircle += 1;
	double x = projectile.getX();
	double y = projectile.getY();
	projectile.getImageView().setX(range* Math.cos(tCircle) + x);
	projectile.getImageView().setY(range* Math.sin(tCircle) + y);
	return (tCircle >= 360);
    }

    public BoomerangProperty(Property p) {
	super(p.getProperty());
    }

}
