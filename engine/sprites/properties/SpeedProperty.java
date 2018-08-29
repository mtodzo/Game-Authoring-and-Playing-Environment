package engine.sprites.properties;

/**
 * Speed property implemented by launcher. Determines how fast projectiles move after
 * being launched by Launcher object.
 * 
 * @author Katherine Van Dyk
 * @date 4/7/18
 *
 */
public class SpeedProperty extends UpgradeProperty {
    
    /**
     * Constructor that takes in cost of range property, upgrade value, and 
     * initial range value.
     * 
     * @param cost
     * @param value
     * @param speed
     */
    public SpeedProperty(double cost, double value, double speed) {
	super(cost, value, speed);
    }
    
    public SpeedProperty(UpgradeProperty p) {
	super(p);
    }
    
    public void setProperty(double newSpeed) {
	this.setProperty(newSpeed);
    }

}
