package engine;

import authoring.frontend.exceptions.MissingPropertiesException;
import frontend.PropertiesReader;

/**
 * @author ???
 * @author benauriemma
 *
 */
public class Settings {

	public static final String DEFAULT_CSS_THEME_PROPS = "src/styling/CurrentCSS.properties";
	public static final String DEFAULT_GAMEPLAYER_PROPS = "engine/gameplayer/gameplayer.properties";
	
	private String myGameName; 
	private double myStartingHealth;
	private double myStartingMoney; 
	private String myCSSTheme; // USE src/styling/CurrentCSS.properties TO FIND CSS FILEPATH CORRESPONDING WITH CSS THEME 
	private String myGameTheme;
	private String myInstructions;

	private String myBackgroundMusic;
	private String myLevelWinSound;
	private String myLevelLossSound;
	
	public Settings(String gameName, double startingHealth, double starting$, String cssTheme, String gameTheme, String instructions, String backgroundMusic, String levelWinSound, String levelLossSound) {
		myGameName = gameName; 
		myStartingHealth = startingHealth;
		myStartingMoney = starting$; 
		myCSSTheme = cssTheme; 
		myGameTheme = gameTheme;
		myInstructions = instructions;
		myBackgroundMusic = backgroundMusic;
		myLevelWinSound = levelWinSound;
		myLevelLossSound = levelLossSound;
	}
	
	public void setGameName(String gameName) {
		myGameName = gameName;
	}

	public String getGameName() {
	    return myGameName;
	}
	
	public double startingMoney() {
	    return myStartingMoney;
	}
	
	public double startingHealth() {
	    return myStartingHealth;
	}
	

	public String getInstructions() {
	    return myInstructions;
	}
	public String getBackgroundMusic() {
	    return myBackgroundMusic;
	}

	public void setBackgroundMusic(String backgroundMusic) {
	    this.myBackgroundMusic = backgroundMusic;
	}
	
	public String getLevelWinSound() {
	    return myLevelWinSound;
	}

	public void setLevelWinSound(String levelWinSound) {
	    this.myLevelWinSound = levelWinSound;
	}

	public String getLevelLossSound() {
	    return myLevelLossSound;
	}

	public void setLevelLossSound(String levelLossSound) {
	    this.myLevelLossSound = levelLossSound;
	}
	
	public String getCSSTheme() throws MissingPropertiesException {
		PropertiesReader pr = new PropertiesReader();
		return pr.findVal(DEFAULT_CSS_THEME_PROPS, myCSSTheme);
	}
	
}
