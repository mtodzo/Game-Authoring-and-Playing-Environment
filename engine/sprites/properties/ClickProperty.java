package engine.sprites.properties;

import engine.sprites.Sprite;

/**
 * This property is given to sprites that have some action that executes on a click.
 * @author milestodzo
 *
 */
public abstract class ClickProperty extends Property {
    
    public ClickProperty(double speed) {
	super(speed);
    }
    
    public abstract void handleClick(Sprite spriteToMove, double destX, double destY);

}
