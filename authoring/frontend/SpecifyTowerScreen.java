/**
 * @author susiechoi
 * Provides necessary information (object description) to populate screen that allows user to choose whether to
 * create a new tower or edit an existing one
 */

package authoring.frontend;

public class SpecifyTowerScreen extends SpecifyObjectScreen {
	
	public static final String DEFAULT_DESCRIPTION = "Tower";

	protected SpecifyTowerScreen(AuthoringView view) {
		super(view, DEFAULT_DESCRIPTION);
	}


}
