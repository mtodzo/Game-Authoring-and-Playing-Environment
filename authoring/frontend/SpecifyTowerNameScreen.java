package authoring.frontend;

/**
 * Class to create screen allowing users to specify name of a newly created
 * Tower.
 * @author SusieChoi
 *
 */
public class SpecifyTowerNameScreen extends SpecifyNameScreen {
	
	public static final String DEFAULT_DESCRIPTION = "Tower";
	
	protected SpecifyTowerNameScreen(AuthoringView view) {
		super(view, DEFAULT_DESCRIPTION);
	}

}
