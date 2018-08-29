package engine.sprites;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.List;

import authoring.frontend.exceptions.MissingPropertiesException;
import engine.builders.PropertyBuilder;
import engine.sprites.properties.Property;
import engine.sprites.properties.UpgradeProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import xml.serialization.ImageWrapper;

/**
 * Interface for an actor in the current Game. All game objects are sprites and have images
 * and IDs, as well as methods for moving them and checking their intersections with other 
 * objects.
 * 
 * @author Katherine Van Dyk 4/3/18
 * @author Ryan Pond
 * @author Miles Todzo
 * @author Ben Hodgson 4/8/18
 */
public class Sprite implements FrontEndSprite{

    private String myName;
    @XStreamOmitField
    private transient ImageView myImageView;
    private ImageWrapper myWrapper;
    private String myImageString;
    private PropertyBuilder myPropertyBuilder;
    private List<Property> myProperties;
    private final String FILEPATH = "file:";
    private final int SIZE = 50;
    private final String COLLISION = "CollisionProperty";
    private final String DAMAGE = "DamageProperty";

    /**
     * Constructor that takes in a sprite's image
     * Source for the image path fix: https://stackoverflow.com/questions/16099427/cannot-load-image-in-javafx @author susiechoi
     * Source for resizing image: https://stackoverflow.com/questions/27894945/how-do-i-resize-an-imageview-image-in-javafx
     * 
     * @param image: tower's initial image
     * @param size: size of tower's image
     * @throws MissingPropertiesException 
     */
    public Sprite(String name, String image, List<Property> properties) throws MissingPropertiesException {
	myName = name;
	myImageString = image;
	myImageView = new ImageView(new Image(FILEPATH+image, SIZE, SIZE, true, true));
	myImageView.setPreserveRatio(true);
	myWrapper = new ImageWrapper(image);
	myProperties = new ArrayList<>();
	myPropertyBuilder = new PropertyBuilder();
	for(Property p : properties) {
	    myProperties.add(this.makeProperty(p));
	}
    }

    /**
     * Return the String name of the sprite.
     * 
     * @return String: the name of the Sprite
     */
    public String getName() {
	return myName;
    }

    /**
     * Returns each Sprite's image
     * 
     * @return ImageView representing game object's image
     */
    public ImageView getImageView() { 
	return myImageView;
    }

    public void place(double newX, double newY) {
	myImageView.setX(newX);
	myImageView.setY(newY);
    }

    /**
     * @return Angle of the sprite
     */
    public double getRotate() {
	return this.myImageView.getRotate();
    }

    /**
     * @return X-coordinate of the sprite
     */
    public double getX() {
	return this.myImageView.getX();
    }

    /**
     * @return Y-coordinate of the sprite
     */ 
    public double getY() {
	return this.myImageView.getY();
    }

    /**
     * Sets the shooting sprite's angle to @param rotateVal
     */
    public void setRotate(double rotateVal) {
	this.myImageView.setRotate(rotateVal);
    }
    /**
     * Returns the damage that this sprite inflicts on something (Can be enemy's damage, projectiles damage, etc)
     * @return
     */
    public double getDamage() {
	return getValue("DamageCollisionProperty");
    }


    public String getImageString() {
	return myImageString;
    }

    /**
     * @return : true if the sprite is alive, false if it is dead
     * Can be overridden in subclasses if a collision affects them
     */
    public boolean handleCollision(Sprite collider) {
	return false;
    }

    /**
     * Returns how many points the user gets for killing this Sprite. Default
     * set to 0
     * @return
     */
    public int getPointValue() {
	return 0;
    }

    /**
     * Updates the Imagepath and the ImageView given an input string
     * @param imagePath	String containing path to image
     */
    public void updateImage(String imagePath) {
	myImageString = imagePath; 
	myWrapper.updateImageString(imagePath);
	myImageView = myWrapper.toImageView();
	myImageView.setPreserveRatio(true);
    }

    /**
     * Sets ImageView upon loading a Sprite from XML
     */
    public void loadImage() {
	myImageView = myWrapper.toImageView();
    }

    protected Property makeProperty(Property p) throws MissingPropertiesException {
	return myPropertyBuilder.getProperty(p);
    }

    public Property getProperty(String ID) {
	for(Property property : myProperties) {
	    if(property != null && property.getName().equals(ID)) {
		return property;
	    }
	}
	return null;
    }

    /**
     * Handles upgrading the health of a tower
     */
    public double upgradeProperty(String name, double balance) {
	for(Property property : myProperties) {
	    if(property.getName() == name) {
		return ((UpgradeProperty) property).upgrade(balance);
	    }
	}
	return balance;
    }

    public List<Property> getProperties(){
	return myProperties;
    }

    public void addProperty(Property property) {
	//System.out.println(property.getName());
	String type = property.getClass().getSuperclass().getSimpleName();
	Property toRemove = null; 
	for(Property p : myProperties) {
	    if(property.getName().equals(p.getName())) {
		toRemove = p;
	    }
	    else if(type.equals(p.getClass().getSuperclass().getSimpleName()) && (type.equals(COLLISION) || type.equals(DAMAGE))) {
		toRemove = p;
	    }
	}
	if(toRemove != null) myProperties.remove(toRemove);
	myProperties.add(property);
    }

    public double getValue(String ID) {
	for(Property property : myProperties) {
	    if(property.getName().equals(ID) || property.getClass().getSuperclass().getSimpleName().equals(ID)) {
	//	System.out.println("This is prop val "+ property.getProperty());
		return property.getProperty();
	    }
	}
	return 0;
    }

    /**
     * Returns the superclass of name 'type' (i.e MovingProperty, CollisionProperty, etc)
     * @param type
     * @return
     */
    public Property getPropertySuperclassType(String type) {
	for(Property p : this.getProperties()) {
	    if(p.getClass().getSuperclass().getSimpleName().equals(type)) {
		return p;
	    }
	}
	return null;
    }


}
