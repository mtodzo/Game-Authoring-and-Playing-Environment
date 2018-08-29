package controller;

import authoring.AuthoringController;
import frontend.MainScreen;
import frontend.StageManager;
import frontend.View;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Ben Hodgson 3/27/18
 * @author Katherine Van Dyk 4/6/18
 *
 * Class that handles mediating program functionality between other, more specified controller
 * objects. 
 */
public class ChiefController {
    public static final String DEFAULT_LANGUAGE = "English";
    private final StageManager STAGE_MANAGER;
 
    /**
     * Creates Stage Manager which handles passing stage to different
     * aspects of the program
     * 
     * @param stage: Application stage passed by Driver
     */
    public ChiefController(Stage stage) {
	STAGE_MANAGER = new StageManager(stage);
    }
    
    /**
     * Starts the application. Launches Main Screen and prompts for 
     * user input about the size of the screen.
     */
    public void start() {
	MainScreen mainScreen  = new MainScreen(STAGE_MANAGER, new View(STAGE_MANAGER, DEFAULT_LANGUAGE, new AuthoringController(STAGE_MANAGER, DEFAULT_LANGUAGE)));
	Scene scene = new Scene(mainScreen.getScreen());
	STAGE_MANAGER.switchScene(scene);
    }


}
