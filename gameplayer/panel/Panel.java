package gameplayer.panel;

import javafx.scene.Parent;

/**
 * 
 * @author Ben Hodgson 3/27/18
 *
 *
 * Interface for generating Panel objects, displays that make up only a part of the Screen
 * object displayed to the user. For example, the menu pop up window would be considered a 
 * Panel.
 */

public abstract class Panel{

    protected Parent PANEL;
    
    /**
     * Returns the panel object to be loaded on the screen
     */
    public Parent getPanel() {
	if(PANEL == null) {
	    makePanel();
	}
	return PANEL;
    }
    
    /**
     * Creates the panel
     */
    public abstract void makePanel();
}
