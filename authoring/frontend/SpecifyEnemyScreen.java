/**
 * @author susiechoi
 * Provides necessary information (object description) to populate screen that allows user to choose whether to
 * create a new enemy or edit an existing one
 */

package authoring.frontend;


class SpecifyEnemyScreen extends SpecifyObjectScreen {
	
	public static final String DEFAULT_DESCRIPTION = "Enemy";

	protected SpecifyEnemyScreen(AuthoringView view) {
		super(view, DEFAULT_DESCRIPTION);
	}
	
}