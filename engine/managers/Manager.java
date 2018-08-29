package engine.managers;

import java.util.ArrayList;
import java.util.List;


/**
 * Uses a composite design pattern to manage all active objects. Used by towers,
 * enemies and projectiles
 * 
 * @author Katherine Van Dyk
 * @author Ryan Pond
 * @author Miles Todzo
 */
public class Manager<E> {

    private List<E> active;
    private List<E> available;

    public Manager() {
	active = new ArrayList<>();
	available = new ArrayList<>();
    }

    /**
     * Returns observable list of all active objects
     * 
     * @return
     */
    public List<E> getListOfAvailable(){
	return  available;
    }
    /**
     * Returns observable list of all active objects
     * 
     * @return
     */
    public List<E> getListOfActive(){
	return active;
    }

    public void addToActiveList(E sprite) {
	active.add(sprite);
    }

    public void addToAvailableList(E sprite) {
	available.add(sprite);
    }
    
    public void removeFromActiveList(E sprite) {
    	active.remove(sprite);
    }
    
    public void setActiveList(List<E> activeSprites) {
    	active = activeSprites;
    }

}