package engine.sprites.towers.launcher;

import java.util.ArrayList;
import java.util.List;

import authoring.frontend.exceptions.MissingPropertiesException;
import engine.builders.PropertyBuilder;
import engine.managers.Manager;
import engine.sprites.ShootingSprites;
import engine.sprites.properties.FireRateProperty;
import engine.sprites.properties.Property;
import engine.sprites.properties.UpgradeProperty;
import engine.sprites.towers.projectiles.Projectile;

/**
 * Class to manage different projectiles. Takes a defined range and trigger to determine when to
 * fire projectiles
 * 
 * @author Ben Hodgson 
 * @author Ryan Pond
 * @author Katherine Van Dyk
 * @author Miles Todzo 4/9
 * @date 3/28/18
 */
public class Launcher extends Manager<Projectile>{

    private Projectile myProjectile;
    private double timeSinceLastShot;
    private List<Property> launcherProperties;
    private PropertyBuilder myPropertyFactory;
    private boolean fakeBool;

    public Launcher(Projectile projectile, List<Property> properties) {
	super();
	myProjectile = projectile;
	launcherProperties = properties;
	timeSinceLastShot = 0;
	fakeBool=false;
    }

    public Launcher(Launcher launcher) throws MissingPropertiesException {
	super();
	myPropertyFactory = new PropertyBuilder();
	launcherProperties = new ArrayList<Property>();
	for(Property p : launcher.getProperties()) {
	    launcherProperties.add(myPropertyFactory.getProperty(p));
	}
	myProjectile = launcher.getProjectile();
	timeSinceLastShot = 0;
	fakeBool = false;
    }

    /**
     * Updates the tower's projectile fire rate
     * 
     * @param balance : balance of the user
     * @return : returns the new user balance
     */
    public double upgradeProperty(String name, double balance) {
	for(Property property : launcherProperties) {
	    if(property.getName().equals(name)) {
		balance -= ((UpgradeProperty) property).upgrade(balance);
	    }
	}
	return balance;
    }

    /**
     * Launch method will make sure that enough time has passed since last shot and then fire a new projectile
     * @throws MissingPropertiesException 
     * 
     */
    public Projectile launch(ShootingSprites target, double shooterX, double shooterY) throws MissingPropertiesException {
	timeSinceLastShot = 0;
	//System.out.println(target);
	Projectile launchedProjectile = new Projectile(myProjectile, target, shooterX, shooterY);
	this.addToActiveList(launchedProjectile);
	return launchedProjectile;
    }

    public double getPropertyValue(String name) {
	for(Property property : launcherProperties) {
	    if(property.getName().equals(name)) {
		return property.getProperty();
	    }
	}
	return -1;
    }

    public double upgradeDamage(double balance) {
	return myProjectile.upgradeProperty("DamageProperty", balance);
    } 

    public void addProjectileProperty(Property property) {
	myProjectile.addProperty(property);
    }

    public List<Property> getProperties(){
	return launcherProperties;
    }

    public void setProjectileImage(String image){
	myProjectile.setImage(image);
    }
    
    public void setProjectileSound(String sound) {
	myProjectile.setShootingSound(sound);
    }

    public void addProperty(Property property) {
	Property toRemove = null;
	for(Property p : launcherProperties) {
	    if(property.getName().equals(p.getName())) {
		toRemove = p;
	    }
	}
	launcherProperties.remove(toRemove);
	launcherProperties.add(property);
    }

    /**
     * Returns the projectile object associated with the Launcher
     * @return
     */
    public Projectile getProjectile() {
	return myProjectile;
    }

    /**
     * Sets the current projectile type managed by the ProjectileManager
     */
    public void setProjectile(Projectile projectile) {
	myProjectile = projectile;
    }

    /**
     * Checks to see if the rate of fire is less than the time elapsed since the last shot
     * @return 
     */
    public boolean hasReloaded(double elapsedTime) {
	FireRateProperty fireRate = (FireRateProperty) this.getProperty("FireRateProperty");
	//System.out.println("fire rate is " + fireRate.getProperty());
	boolean hasReloaded = fireRate.hasReloaded(timeSinceLastShot);
	if(!fakeBool) {
	    fakeBool = true;
	    return true;
	}
	timeSinceLastShot+=elapsedTime;
	return hasReloaded;
    }

    public Property getProperty(String propertyName) {
	for (Property p: this.launcherProperties) {
	    if (p.getName().equals(propertyName)) {
		return p;
	    }
	}
	return null;
    }

    public double upgradeProperty(double balance, String propertyName) {
	UpgradeProperty propToUpgrade = (UpgradeProperty)this.getProperty(propertyName);
	return propToUpgrade.upgrade(balance);
    }

    public String getProjectileImage() {
	return myProjectile.getImageString(); 
    }

    public double getProjectileDamage() {
	return myProjectile.getDamage(); 
    }
    
    public double getDamage() {
	return myProjectile.getDamage();
    }

    public Projectile getNewProjectile(double towerX, double towerY, double targetX, double targetY) throws MissingPropertiesException{
	Projectile newProjectile = new Projectile(myProjectile, towerX, towerY, targetX, targetY);
	this.addToActiveList(newProjectile);
	for (Projectile o: this.getListOfActive()) {
	//    System.out.println("printing out active projectiles *******************************"+ o);
	}
	return newProjectile;
    }

}

