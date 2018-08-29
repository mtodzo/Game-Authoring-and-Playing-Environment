package engine;


import java.io.IOException;

import com.sun.javafx.tools.packager.Log;

import java.io.FileNotFoundException;

import com.sun.javafx.tools.packager.Log;

import authoring.frontend.exceptions.MissingPropertiesException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import xml.BadGameDataException;
import xml.PlaySaverWriter;
import xml.XMLFactory;

/**
 * This class will handle all of the gameLoop interactions, and will also hold the 
 * current instance of the PlayState
 * @author ryanpond
 * 
 */
public class GameEngine {

	private final int FRAMES_PER_SECOND = 60;
	private final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	private final Integer DEFAULT_RELATIVE_SPEED = 5;
	private final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private PlayState myPlayState;
	private Timeline ANIMATION;
	private double timeFactor;

	public GameEngine() {
		myPlayState = null;
		timeFactor = 1;

		setSpeed(DEFAULT_RELATIVE_SPEED);
		// attach "game loop" to time line to play it
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
				e -> {
				    try {
					loop(SECOND_DELAY);
				    } catch (MissingPropertiesException i) {
					Log.debug(i);
				    } catch (FileNotFoundException i) {
					Log.debug(i);
				    }
				});
		ANIMATION = new Timeline();
		ANIMATION.setCycleCount(Animation.INDEFINITE);
		ANIMATION.getKeyFrames().add(frame);

	}

	/**
	 * Sets new playState when a new play is initiated
	 * @param p
	 */
	public void setPlayState(PlayState p) {
		myPlayState = p;
		
	}

	/**
	 * @return current PlayState being used to play game
	 */
	public PlayState getPlayState() {
		return myPlayState;
	}


	/**
	 * Calls the update function every loop
	 * @param elapsedTime
	 * @throws MissingPropertiesException 
	 * @throws FileNotFoundException 
	 */
	public void loop(double elapsedTime) throws MissingPropertiesException, FileNotFoundException {
	    myPlayState.update(elapsedTime*timeFactor);
	}


	/**
	 * Pauses Game Loop animation so Game State stays constant
	 * COMMENTED OUT BECAUSE THIS WOULD PAUSE THE ENTIRE GAMEENGINE NOT THE PLAYSTATE
	 */
	 public void pause() {
	     ANIMATION.pause();
	
	 }

	/**
	 * Starts Game Loop animation, so Game State continuously loops
	 */
	public void start() {
		ANIMATION.play();
	}

	/**
	 * Sets Game Loop speed, to determine how fast level steps through.
	 * 
	 * @param speed: speed at which animation should iterate
	 * (relative speed of program, 1 being slowest, 5 being normal, 10 being fastest)
	 */
	public void setSpeed(Integer relativeSpeed) {
		timeFactor = relativeSpeed;
	}

	/**
	 * Saves current Game State to File
	 * @throws IOException 
	 * @throws BadGameDataException 
	 */
	public void savePlay(String filename) throws BadGameDataException, IOException {
		PlaySaverWriter p = (PlaySaverWriter) XMLFactory.generateWriter("PlaySaverWriter");
		p.write(myPlayState, filename);
	}

	/**
	 * Updates Game State to new Level as specified in XML File
	 * 
	 * @param l: integer denoting level to jump to
	 */
	public void jumpLevel(int l) {
		//TODO: 
	}

	/**
	 * Jumps to a certain level played in the Play
	 * 
	 * @param newLevel : The PlayState object with information about that level
	 */
	public void setLevel(PlayState newLevel) {

	}
	

	/**
	 * Stops game loop for when user wants to return to editing/authoring the game
	 * @author susiechoi
	 */
	public void endLoop() {
		if (ANIMATION != null) {
			ANIMATION.stop();
		}
	}



}
