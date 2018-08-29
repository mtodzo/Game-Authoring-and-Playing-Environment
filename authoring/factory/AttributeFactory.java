package authoring.factory;

import authoring.AuthoredGame;
import authoring.frontend.exceptions.ObjectNotFoundException;
import engine.level.Level;
import engine.path.Path;
import engine.sprites.ShootingSprites;
import engine.sprites.Sprite;
import engine.sprites.enemies.Enemy;
import engine.sprites.enemies.wave.Wave;
import engine.sprites.towers.Tower;

/**
 * Factory class that handles setting/getting object attributes from Sprites
 * @author Katie Van Dyk
 * @author Sarahbland
 *
 */
public class AttributeFactory {
    public static final String DEFAULT_IMAGE_IDENTIFIER = "myImage";
    public static final String DEFAULT_PROJECTILE_IMAGE_IDENTIFIER = "myProjectileImage";
    public static final String DEFAULT_SOUND_IDENTIFIER = "mySound";
    private AttributeFinder attributeFinder; 

    public AttributeFactory() {
	attributeFinder = new AttributeFinder();
    }

    /**
     * Sets object attributes not associated with a specific Property (i.e.
     * images or sounds) or attributes for objects without Properties (like Settings)
     * @param level is level containing object
     * @param objectType is type of object (i.e. Tower)
     * @param name is user-given name of object
     * @param attribute is attribute needed to be changed
     * @param attributeValue is value attribute will be given
     * @param game is AuthoredGame containing object
     * @throws ObjectNotFoundException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void setObjectAttribute(int level, String objectType, String name, String attribute, Object attributeValue, AuthoredGame game) throws ObjectNotFoundException, IllegalArgumentException, IllegalAccessException {
	if (objectType.equals("Enemy")) {
	    Level currentLevel = game.levelCheck(level);
	    if (currentLevel.containsEnemy(name)) {
		setObjectStringField(currentLevel.getEnemy(name), attribute, attributeValue);

	    }
	}
	else if (objectType.equals("Tower")) {
	    Level currentLevel = game.levelCheck(level);
	    if (currentLevel.containsTower(name)) {
		setObjectStringField(currentLevel.getTower(name), attribute, attributeValue);
	    }
	}
	else if (objectType.equals("Projectile")) {
	    Level currentLevel = game.levelCheck(level);
	    if (currentLevel.containsTower(name)) {
		setObjectStringField(currentLevel.getTower(name), attribute, attributeValue);
	    }
	    else if (currentLevel.containsEnemy(name)) {
		setObjectStringField(currentLevel.getEnemy(name), attribute, attributeValue);
	    }
	}
	else if (objectType.equals("Settings")) {
	    attributeFinder.setFieldValue(attribute, game.getSettings(), attributeValue);
	}
    }

    /**
     * Gets object attributes not associated with a specific Property (i.e.
     * images or sounds) or attributes for objects without Properties (like Settings)
     * @param level is level containing object
     * @param objectType is type of object (i.e. Tower)
     * @param name is user-given name of object
     * @param attribute is attribute needed to be found
     * @param game is AuthoredGame containing object
     * @return value of desired attribute
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ObjectNotFoundException
     */
    public Object getObjectAttribute(int level, String objectType, String name, String attribute, AuthoredGame game) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ObjectNotFoundException {
	Object attributeValue = null;
	AttributeFinder attributeFinder = new AttributeFinder(); 
	if (objectType.equals("Enemy")) {
	    Level currentLevel = game.levelCheck(level);
	    if (currentLevel.containsEnemy(name)) {
		Enemy enemy = currentLevel.getEnemy(name);
		attributeValue = attributeFinder.retrieveFieldValue(attribute, enemy);
	    }
	}
	else if (objectType.equals("Tower")) {
	    Level currentLevel = game.levelCheck(level);
	    if (currentLevel.containsTower(name)) {
		Tower tower = currentLevel.getTower(name);
		attributeValue = attributeFinder.retrieveFieldValue(attribute, tower);
	    }
	}
	else if (objectType.equals("Settings")) {
	    attributeValue = attributeFinder.retrieveFieldValue(attribute, game.getSettings());
	}
	else if (objectType.equals("Path")) {
	    Level currentLevel = game.levelCheck(level);
	    Path path = currentLevel.getPath();
//	    System.out.println("PATH LEVEL LOADING FROM: " + level);
	    attributeValue = attributeFinder.retrieveFieldValue(attribute, path);
	}
	else if(objectType.equals("Wave")) {
	    Level currentLevel = game.levelCheck(level);
	    if (currentLevel.containsWaveNumber(Integer.parseInt(name))) {
		Wave wave = currentLevel.getWaves().get(Integer.parseInt(name));
		attributeValue = attributeFinder.retrieveFieldValue(attribute, wave);
	    }
	}
	if (attributeValue == null) {
	    throw new ObjectNotFoundException(name);
	}
	if (attributeValue.getClass() == Double.class) {
	    return Double.toString((double) attributeValue); 
	} 
	else return attributeValue; 
    }
    private void setObjectStringField(Sprite spriteToSet, String attribute, Object attributeValue) {
	    if(attribute.equals(DEFAULT_IMAGE_IDENTIFIER)) {
		//System.out.println("setting a tower/enemy image");
		spriteToSet.updateImage((String) attributeValue);
	    }
	    else if(attribute.equals(DEFAULT_PROJECTILE_IMAGE_IDENTIFIER) && spriteToSet instanceof ShootingSprites) {
		//System.out.println("setting a projectile image");
		((ShootingSprites) spriteToSet).setProjectileImage((String) attributeValue);
	    }
	    else if(attribute.equals(DEFAULT_SOUND_IDENTIFIER)) {
		//System.out.println("setting a sound");
		((ShootingSprites) spriteToSet).setSoundString(((String) attributeValue));
	    }
	}

}
