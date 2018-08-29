package controller;

import authoring.frontend.exceptions.MissingPropertiesException;
import frontend.StageManager;

/**
 * Interface that both controllers in the Authoring/GamePlay MVC setups must
 * implement. Used to maximized shared responsibility of View class.
 * @author Sarahbland
 *
 */
public interface MVController {
    
    /**
     * Method that creates a new playController and demos a game 
     * based on that playController
     * @param manager is StageManager holding current stage
     * @param instructions is String of instructions to be given at the beginning
     * of the game
     * @throws MissingPropertiesException
     */
    public void playControllerDemo(StageManager manager, String instructions) throws MissingPropertiesException;
}
