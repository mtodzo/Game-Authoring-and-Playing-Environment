package engine.sprites.properties;

public class ValueProperty extends Property {

    /**
     * Returns a value of the sprite (how much it sells for)
     * 
     * @param cost
     * @param upgradeValue
     * @param propertyValue
     */
    public ValueProperty(double propertyValue) {
	super(propertyValue);
    }
    
    public ValueProperty(Property property) {
	super(property);
    }

    /**
     * Updates an object's value in response to an upgrade 
     * (assumption: value is incremented by property's cost)
     * 
     * @param features
     */
    public void updateValue(UpgradeProperty feature) {
	double newValue = this.getProperty() + feature.getCost();
	this.setProperty(newValue);
    }
}
