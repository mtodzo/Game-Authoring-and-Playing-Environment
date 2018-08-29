package engine.builders;

import engine.Settings;

/**
 * 
 * @author Ben Hodgson 4/10/18
 * @author benauriemma
 *
 * Builder class that constructs a new Settings object
 */
public class SettingsBuilder {
    
    public Settings construct(String gameName, double startingHealth, double startingMoney, String css, String theme, String instructions, String backgroundMusic, String levelWinSound, String levelLossSound) {
	return new Settings(gameName, startingHealth, startingMoney, css, theme, instructions, backgroundMusic, levelWinSound, levelLossSound);
    }

}
