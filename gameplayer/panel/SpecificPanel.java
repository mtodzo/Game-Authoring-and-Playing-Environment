package gameplayer.panel;

import engine.sprites.towers.FrontEndTower;

/**
 * class for panels which display/use information from a specific placed tower
 * @author andrewarnold
 *
 */
public abstract class SpecificPanel extends Panel {

    protected final FrontEndTower TOWER;
    
    public SpecificPanel(FrontEndTower tower) {
	TOWER = tower;
    }
    
}
