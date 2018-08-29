package engine.sprites.properties;

/**
 * Speed property implemented by launcher. Determines how fast projectiles move after
 * being launched by Launcher object.
 * 
 * @author Katherine Van Dyk
 * @date 4/7/18
 *
 */
public class ConstantSpeedProperty extends Property {
    
    /**
     * Constructor that takes in cost of range property, upgrade value, and 
     * initial range value.
     * 
     * @param cost
     * @param value
     * @param speed
     */
    public ConstantSpeedProperty(double speed) {
	super(speed);
    }
    
    public ConstantSpeedProperty(Property p) {
	super(p);
    }
    
    public void setProperty(double newSpeed) {
	this.setProperty(newSpeed);
    }


}
