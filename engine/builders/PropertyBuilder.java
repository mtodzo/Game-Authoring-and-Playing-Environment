package engine.builders;

import authoring.frontend.exceptions.MissingPropertiesException;
import engine.sprites.properties.Property;
import engine.sprites.properties.UpgradeProperty;
import frontend.PropertiesReader;
import voogasalad.util.reflection.Reflection;


public class PropertyBuilder {

    private String PACKAGE = "engine.sprites.properties.";
    public static final String DEFAULT_PROPERTIES_FILES_PATH = "default_objects/Properties/properties.properties";
    private PropertiesReader myReader;

    public PropertyBuilder() {
	myReader = new PropertiesReader();
    }
    
    public Property getProperty(Property p) throws MissingPropertiesException {
	Property ret;
	String propertyName = p.getName();
	String className = PACKAGE + propertyName;
	String type = null;

	for(String key : myReader.allKeys(DEFAULT_PROPERTIES_FILES_PATH)) {
	    if(propertyName.equals(key)) {
		type = myReader.findVal(DEFAULT_PROPERTIES_FILES_PATH, key);
	    }
	}

	if(type == null) {
	    return null;
	}
	else if(type.equals("UpgradeProperty")) {
	    ret = createUpgradeProperty(className, type, p);
	}
	else if(type.equals("Property")) {
	    ret = createProperty(className, type, p);
	}
	else {
	    return null;
	}
	return ret;
    }

    private Property createUpgradeProperty(String className, String type, Property p) {
	return (UpgradeProperty) Reflection.createInstance(className, p );
    }

    private Property createProperty(String className, String type, Property p) {
	return (Property) Reflection.createInstance(className, p);
    }
}


