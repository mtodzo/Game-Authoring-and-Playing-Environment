package engine.sprites.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * A property describes any attribute held by an object.
 * 
 * @author Katherine Van Dyk
 *
 */
public abstract class Property {

    private double myProperty;
    private String mySimpleName;
    
    /**
     * Constructor for property object
     * 
     * @param property: initial value of property
     */
    public Property(double property) {
	myProperty = property;
	mySimpleName = this.getClass().getSimpleName();
    }
    
    public Property(Property property) {
	myProperty = property.getProperty();
	mySimpleName = this.getClass().getSimpleName();
    }
    
//    /**
//     * 
//     * @param args
//     * @return the object corresponding to the specific property class
//     */
//    public abstract T execute(Object...args); 
//    
    /**
     * @return current value of property
     */
    public double getProperty() {
	return myProperty;
    }
    
    /**
     * @return current value of property
     */
    public void setProperty(double newValue) {
	myProperty = newValue;
    }
    
    /**
     * @return String name of property
     */
    public String getName() {
    	return mySimpleName;
    }
    
    public List<Object> getAttributes(){
	List<Object> ret = new ArrayList<Object>();
	ret.add(myProperty);
	return ret;
    }
    
}
