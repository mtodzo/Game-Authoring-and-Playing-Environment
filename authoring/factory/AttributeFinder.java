/**
 * @author susiechoi
 * Object that returns the value of a field with a specified name from an object
 * Useful in populating sliders, dropdowns, and textboxes with user input that is currently held in objects
 */

package authoring.factory;

import java.lang.reflect.Field;

import authoring.frontend.exceptions.ObjectNotFoundException;

public class AttributeFinder {

    /**
     * Retrieves value of requested field from specified object
     * @param fieldName - name of the field whose info is desired
     * @param objectWithFields - object of interest
     * @return value held within the field
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public Object retrieveFieldValue(String fieldName, Object objectWithFields) throws IllegalArgumentException, IllegalAccessException, ObjectNotFoundException{
	Object fieldValue = null; 
	for (Field aField : objectWithFields.getClass().getDeclaredFields()) {
	    String fieldSimpleString = aField.toString().substring(aField.toString().lastIndexOf(".")+1); 
	    if (fieldSimpleString.equals(fieldName)) {
		aField.setAccessible(true);
		fieldValue = aField.get(objectWithFields);
		return fieldValue; 
	    }
	}
	throw new ObjectNotFoundException(fieldName);
    }

    /**
     * Sets a field value for a specified object
     * @param fieldName is field needing to be set
     * @param objectWithFields is object with that field
     * @param fieldValue - value desired for that field
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ObjectNotFoundException
     */
    public void setFieldValue(String fieldName, Object objectWithFields, Object fieldValue) throws IllegalArgumentException, IllegalAccessException, ObjectNotFoundException {
	for (Field aField : objectWithFields.getClass().getDeclaredFields()) {
	    String fieldSimpleString = aField.toString().substring(aField.toString().lastIndexOf(".")+1); 
	    if (fieldSimpleString.equals(fieldName)) {
		aField.setAccessible(true);
		aField.set(objectWithFields, fieldValue);
		return; 
	    }
	}
	throw new ObjectNotFoundException(fieldName);
    }

    /**
     * Returns object field value when field value is found in object's superclass
     * @param fieldName is field needing to be set
     * @param objectWithFields is object with that field
     * @param fieldValue - value desired for that field
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ObjectNotFoundException
     */
    public Object retrieveFieldValueSuper(String fieldName, Object objectWithFields) throws IllegalArgumentException, IllegalAccessException, ObjectNotFoundException {
	Object fieldValue = null; 
	while (objectWithFields.getClass().getSuperclass() != null) {
	    for (Field aField : objectWithFields.getClass().getSuperclass().getDeclaredFields()) {
		String fieldSimpleString = aField.toString().substring(aField.toString().lastIndexOf(".")+1); 
		if (fieldSimpleString.equals(fieldName)) {
		    aField.setAccessible(true);
		    fieldValue = aField.get(objectWithFields);
		    return fieldValue; 
		}
	    }
	}
	throw new ObjectNotFoundException(fieldName);
    }
    /**
     * Sets a field value for a specified object when the field is found in the object's superclass
     * @param fieldName is field needing to be set
     * @param objectWithFields is object with that field
     * @param fieldValue - value desired for that field
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ObjectNotFoundException
     */
    public void setFieldValueSuper(String fieldName, Object objectWithFields, Object fieldValue) throws IllegalArgumentException, IllegalAccessException, ObjectNotFoundException {	
	while (objectWithFields.getClass().getSuperclass() != null) {
	    for (Field aField : objectWithFields.getClass().getSuperclass().getDeclaredFields()) {
		String fieldSimpleString = aField.toString().substring(aField.toString().lastIndexOf(".")+1); 
		if (fieldSimpleString.equals(fieldName)) {
		    aField.setAccessible(true);
		    aField.set(objectWithFields, fieldValue);
		}
	    }
	}
	throw new ObjectNotFoundException(fieldName);
    }

}