package authoring.frontend;

import javafx.scene.Parent;

/**
 * Class responsible for specifying toolbar at the bottom of WaveScreen.
 * @author Sarahbland
 *
 */
public class WaveToolBar extends PathToolBar {
    public WaveToolBar(AuthoringView view) {
 	super(view);
     }
    @Override
    protected void makePanel() {
	//Panel does not include anything for WaveToolBar,
	//but if additional elements (like buttons) were desired, they would be created here
    }
    
    /**
     * Since the toolbar currently is just a placeholder, it does not need styling,
     * so returning null will not cause an issue. If additional buttons were desired,
     * they would be added to a root returned here.
     * @see frontend.Screen#makeScreenWithoutStyling()
     */
    @Override
    
    public Parent makeScreenWithoutStyling() {
	return null;
    }

}
