import controller.ChiefController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/** 
 * Use the driver JavaFX library to start the application.
 *
 * @author Ben Hodgson 3/27/18
 * @author Katherine Van Dyk 4/10/18
 * 
 */
public class Driver extends Application {  
	
	public static final String DEFAULT_WINDOW_TITLE = "IfTrueReturnTrue VOOGASALAD";
	public static final String DEFAULT_WINDOW_IMG = "file:images/sarahbland.png"; 

    /**
     * Initialize the program and begin the animation loop 
     * 
     * 
     * @param stage: Primary stage to attach all scenes
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
	primaryStage.setTitle(DEFAULT_WINDOW_TITLE);
	primaryStage.getIcons().add(new Image(DEFAULT_WINDOW_IMG));
	ChiefController controller = new ChiefController(primaryStage);
	controller.start();
    }

    /**
     * Start the program
     * 
     */
    public static void main (String[] args) {
	launch(args);
    }
}
