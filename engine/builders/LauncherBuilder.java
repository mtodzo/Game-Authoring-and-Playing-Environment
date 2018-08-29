package engine.builders;

import java.util.ArrayList;
import java.util.List;

import engine.sprites.properties.FireRateProperty;
import engine.sprites.properties.Property;
import engine.sprites.properties.RangeProperty;
import engine.sprites.towers.launcher.Launcher;
import engine.sprites.towers.projectiles.Projectile;

/**
 * Class that uses builder design pattern to construct a launcher
 * 
 * @author Katherine Van Dyk
 * @date 4/7/18
 *
 */
public class LauncherBuilder {
    
    public Launcher construct(double fireRate, double fireRange, Projectile projectile) {
	FireRateProperty rate = new FireRateProperty(0, 0, fireRate);
	RangeProperty range = new RangeProperty(fireRange);
	List<Property> launcherProps = new ArrayList<>();
	launcherProps.add(rate);
	launcherProps.add(range);
	return new Launcher(projectile, launcherProps);
    }

}
