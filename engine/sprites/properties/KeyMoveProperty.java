package engine.sprites.properties;

import engine.sprites.towers.Tower;
import javafx.scene.input.KeyCode;
/**
 * Source on making object move continuously:https://stackoverflow.com/questions/40920531/how-do-i-keep-an-object-moving-using-arrow-keys
 * 
 * @author Katherine Van Dyk
 *
 */
public class KeyMoveProperty extends Property {

    private double xVel;
    private double yVel;
    private final double elapsedTime = .25;

    public KeyMoveProperty(double speed) {
	super(speed);
    }

    public KeyMoveProperty(Property p) {
	super(p.getProperty());
 
    }
	
    public void move(Tower tower, KeyCode code) {
	getSpeed(code);
	double xMove = tower.getX() + xVel * elapsedTime; 
	double yMove = tower.getY() + yVel * elapsedTime;
	tower.getImageView().setX(xMove);
	tower.getImageView().setY(yMove);
    }
	
    private void getSpeed(KeyCode code) {
	if(code == KeyCode.W) {
	    xVel = 0;
	    yVel = -getProperty();
	}
	else if(code == KeyCode.S) {
	    xVel = 0;
	    yVel = getProperty();
	}
	else if(code == KeyCode.A) {
	    xVel = -getProperty();
	    yVel = 0;
	}
	else if(code == KeyCode.D) {
	    xVel = getProperty();
	    yVel = 0;
	}
	else {
	    xVel = 0;
	    yVel = 0;
	}
    }
}


