package engine.managers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.path.Path;
import engine.sprites.Sprite;
import engine.sprites.enemies.Enemy;

/**
 * Enemy manager uses composite design pattern to handle updating all 
 * active Enemy objects in the game loop.
 * 
 * @author Miles Todzo
 * @author Ryan POnd
 */
public class EnemyManager extends ShootingSpriteManager {

    int count = 0;

    private Map<Path, List<Enemy>> myEnemies;


    /**
     * Constructor for Enemy manager
     */
    public EnemyManager() {
	super();
	myEnemies = new HashMap<>();
    }

    /**
     * Moves any projectiles that the enemies have shot on every step of the GameLoop
     */
    public void moveProjectiles() {
	// TODO Auto-generated method stub

    }


    /**
     * Moves all the enemies along the path on every step.
     * If the enemy isn't alive, remove it from the enemy manager. If the enemy reaches the end 
     * of the path, add it to the returned list so the PlayState knows to deduct the player's 
     * health appropriately. 
     * @return List<Sprite>: A list of enemy objects that have reached the end of the path
     */
    public List<Sprite> moveEnemies(double elapsedTime) {
	//System.out.println("Enemy manager move");
	List<Sprite> deadEnemies = new ArrayList<>();
	for (Path path : myEnemies.keySet()) {
	    for (Enemy enemy : myEnemies.get(path)) {
		//System.out.println("Moving individual enemies");
		moveEnemy(enemy, deadEnemies, path, elapsedTime);
	    }
	    myEnemies.get(path).removeAll(deadEnemies);
	}
	return deadEnemies;
    }
    
    private void moveEnemy(Enemy enemy, List<Sprite> deadEnemies, Path path, double elapsedTime) {
	//System.out.println(enemy.currentPosition().getX() + " " + enemy.currentPosition().getY());
	if (!enemy.isAlive()) {
	    return;
	}
	else if(path.checkKill(enemy.currentPosition()) && enemy.isAlive()) {
	 //   System.out.println("");
	    deadEnemies.add(enemy);
	}
	else if(!isInRange(enemy.currentPosition(),enemy.targetPosition())) {
	    enemy.move(elapsedTime);
	}
	else {
	    Point newPosition = path.nextPosition(enemy.getIndex());
	    int pathIndex = path.getIndex(enemy.currentPosition(), enemy.getIndex());
	    enemy.setNewPosition(newPosition);
	    enemy.move(elapsedTime);
	    enemy.setIndex(pathIndex);
	}
	//System.out.println(enemy.currentPosition().getX() + " " + enemy.currentPosition().getY());
    }

    private boolean isInRange(Point curr, Point target) {
	return curr.distance(target)<10;
    }

    /**
     * Adds an enemy to the manager that's mapped to a specific path
     * 
     * @param path: the path that the enemy follows
     * @param enemy: the enemy object to be added to the manager
     */
    public void addEnemy(Path path, Enemy enemy) {
	if (myEnemies.containsKey(path)) {
	    List<Enemy> pathEnemies = myEnemies.get(path);
	    pathEnemies.add(enemy);
	    myEnemies.put(path, pathEnemies);
	}
	else {
	    List<Enemy> pathEnemies = new ArrayList<>();
	    pathEnemies.add(enemy);
	    myEnemies.put(path, pathEnemies);
	}
    }

    /**
     * Clears all the current enemies in the map
     */
    public void clearEnemiesMap() {
	myEnemies.clear();
    }

    /**
     * Method to remove enemies from the current enemies map
     * @param toBeRemoved
     */
    @SuppressWarnings("unlikely-arg-type")
    public void removeFromMap(List<Sprite> toBeRemoved) {
	for(Sprite s : toBeRemoved) {
	    myEnemies.remove(s);
	}

    }

}
