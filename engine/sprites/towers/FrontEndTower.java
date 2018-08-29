package engine.sprites.towers;

import java.util.Map;

import engine.sprites.FrontEndSprite;

/**
 * Interface Gameplayer will use to populate tower-specific panels, and to trigger upgrades of tower
 * @author andrewarnold
 *
 */
public interface FrontEndTower extends FrontEndSprite{


    /**
     * Method to get information about the tower, was thinking the key would be the 
     * type of statistic (health, damage, rate-of-fire, etc) and then the map value would be
     * the value of the statistic. NOTE: should include sell price of tower
     * @return
     */
    public Map<String,Integer> getTowerStats();

    /**
     * 
     * @return The name (type) of the tower
     */
    @Override
    public String getName();

    /**
     * Sell tower
     * @return updated resources value
     */
    public int sell();

    /**
     * Tells engine a tower was attempted to be placed/purchased
     * @param myResources	the current currency value
     * @return Updated resources value
     * @throws CannotAffordException 
     */
    public int purchase(int myResources) throws CannotAffordException;
    
    /**
     * Getter for tower cost
     * @return The price of this tower
     */
    public int getTowerCost();

    public double getTowerRange();


    /**
     * Method to get list of available upgrades, map key would be upgrade type so we can assign
     * a particular image to the icon (health, damage, rate-of-fire, etc) [would be nice to have these
     * standardized between this and getTowerStats but not required] and the map value would be the cost
     * @return Map of the available upgrades
     */
    //    public Map<String,Double> getUpgrades();
    //    
    //    /**
    //     * Method to request information/statistics on a specific upgrade for population of UpgradePanel
    //     *  $$Return doesn't need to be a string, I'm happy to parse out whatever you give me if the info is there$$
    //     * @param upgradeName the type of upgrade, will be the value given as a key from getUpgrades()
    //     * @return upgrade specifics
    //     */
    //    public String getSpecificUpgradeInfo(String upgradeName);
    //    
    //    /**
    //     * Triggers the actual update
    //     * @param upgradeName	which upgrade to trigger, will be the value given as a key 
    //     * 				from getUpgrades()
    //     * @return if upgrade was successful (did user have enough money)
    //     */
    //    public boolean upgrade(String upgradeName);


}
