package authoring.frontend;

/**
 * Class to create screen allowing users to specify name of a newly created
 * Enemy.
 * @author SusieChoi
 *
 */
public class SpecifyEnemyNameScreen extends SpecifyNameScreen {

	public static final String DEFAULT_DESCRIPTION = "Enemy";
	
	protected SpecifyEnemyNameScreen(AuthoringView view) {
		super(view, DEFAULT_DESCRIPTION);
 	}

}

