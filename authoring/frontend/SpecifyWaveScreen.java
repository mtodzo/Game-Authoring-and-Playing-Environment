package authoring.frontend;

/**
 * Class to specify which wave/a new wave a designer would like to specify.
 * @author Sarahbland
 *
 */
public class SpecifyWaveScreen extends SpecifyObjectScreen{
	public static final String DEFAULT_DESCRIPTION = "Wave";

	protected SpecifyWaveScreen(AuthoringView view) {
		super(view, DEFAULT_DESCRIPTION);
	}
}
