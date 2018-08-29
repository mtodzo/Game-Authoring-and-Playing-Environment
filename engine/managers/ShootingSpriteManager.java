package engine.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import authoring.frontend.exceptions.MissingPropertiesException;
import engine.sprites.ShootingSprites;
import engine.sprites.Sprite;
import engine.sprites.towers.projectiles.Projectile;

/**
 * 
 * @author Miles Todzo
 * @author Ryan Pond
 *
 */

public class ShootingSpriteManager extends Manager<ShootingSprites>{

    /**
     * Checks for collisions between between the list of active actors held by the Manager the method
     * was called on and the list of active actors passed as a parameter
     * @param passedSprites
     */
    public List<Sprite> checkForCollisions(List<ShootingSprites> passedSprites) {
	List<Sprite> spritesToBeRemoved = new ArrayList<>();
	for (ShootingSprites activeSprite: this.getListOfActive()) {
	    for (ShootingSprites passedActor: passedSprites) {
		List<Sprite> deadSprites = activeSprite.checkForCollision(passedActor);
		spritesToBeRemoved.addAll(deadSprites);
	    }
	}
	return spritesToBeRemoved;
    }


    /**
     * The Manager shoots from it's Launcher at the passedSprites
     * @param passedSprites : target being shot at
     * @return Projectiles to add to the front end view
     * @throws MissingPropertiesException 
     */
    public List<Projectile> shoot(List<ShootingSprites> passedSprites, double elapsedTime) throws MissingPropertiesException {
	List<Projectile> newProjectiles = new ArrayList<>();
	for (ShootingSprites shootingSprite: this.getListOfActive()) { //all the towers
	    if(shootingSprite.hasReloaded(elapsedTime)) {
		for (ShootingSprites passedSprite: passedSprites) {	//all the enemies
		    if (shootingSprite.hasReloaded(elapsedTime) && 
			    shootingSprite.hasInRange(passedSprite)&& 
			    passedSprite!=null) {
			Projectile newProjectile = shootingSprite.launch(passedSprite, shootingSprite.getX(), shootingSprite.getY());
			//System.out.println("launched : " + newProjectile);
			if (newProjectile != null) {
			    newProjectiles.add(newProjectile);
			}
		    }
		}
	    }
	}
	return newProjectiles;
    }

    /**
     * Moves the projectiles. Goes through the Manager and gets the list of projectiles, and moves them
     */
    public List<Sprite> moveProjectiles(double elapsedTime) {
	List<Sprite> removeAllProjectiles = new ArrayList<Sprite>();
	for (ShootingSprites shootingSprite: this.getListOfActive()) {
	    List<Projectile> removeSpritesProjectiles = new ArrayList<Projectile>();
	    for (Projectile projectile: shootingSprite.getProjectiles()) {
		if (projectile.move(elapsedTime)) {
		    removeSpritesProjectiles.add(projectile);
		}
	    }
	    for (Projectile spriteProjectile : removeSpritesProjectiles) {
		shootingSprite.getLauncher().removeFromActiveList(spriteProjectile);
		removeAllProjectiles.add(spriteProjectile);
	    }
	}
	return removeAllProjectiles;
    }
    
    /**
     * Removes all of the projectiles from the tower manager
     * @return
     */
    public Collection<Projectile> removeAllProjectiles() {
	List<Projectile> toBeRemoved = new ArrayList<>();
	for(ShootingSprites tower : this.getListOfActive()) {
	    toBeRemoved.addAll(tower.getLauncher().getListOfActive());
	    tower.getLauncher().getListOfActive().clear();
	}
	return toBeRemoved;
    }
}
