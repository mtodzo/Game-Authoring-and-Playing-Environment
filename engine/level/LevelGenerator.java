package engine.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Ben Hodgson 4/4/2018
 * 
 * A class that generates a set number of levels with automated increasing difficulty. 
 * The class takes a base level and automatically creates successive levels with different
 * wave composition. The waves become increasingly difficult by increasing the 
 * number of enemies.
 *
 */
public class LevelGenerator {
    
    private final List<Level> LEVELS = new ArrayList<Level>();
    
    public LevelGenerator(int number, Level baseLevel) {
	LEVELS.add(baseLevel);
    }
    
    /**
     * Returns an unmodifiable list of all of the levels generated for the game. 
     * 
     * @return List<Level>: an unmodifiable list of levels
     */
    public List<Level> getUnmodifiableLevels() {
	return Collections.unmodifiableList(LEVELS);
    }
    
    // TODO: implement this later
    private void createLevels(int number) {
	
    }

}
