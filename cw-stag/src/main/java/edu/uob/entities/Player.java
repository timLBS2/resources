package edu.uob.entities;
import edu.uob.GameEntity;
import edu.uob.GameMap;
import java.util.HashSet;

/**
 * @className: Player
 * @description:  store the health value of the player and the location of the player
 * @author: Li
 * @date: 25/04/2023
 **/
public class Player extends GameEntity {
    private Health health = new Health(); // Health value of the player ∈ [0,3]
    private HashSet<Artefact> inventory = new HashSet<>();
    private Location playerLocation = GameMap.getStartingPoint();    //Set the starting point of the player
    public Player(String name, String description) {
        super(name.toLowerCase(), description);
    }
    public Player(){}

    public String getPlayerLocationName() {
        return playerLocation.getName();
    }
    public Location getPlayerLocation() {
        return playerLocation;
    }

    public void setPlayerLocation(Location playerLocation) {
        this.playerLocation = playerLocation;
    }

    public void addArtefact(Artefact artefact) {
        inventory.add(artefact);
    }
    public void removeArtefact(Artefact artefact) {
        inventory.remove(artefact);
    }
    public Artefact removeFromInventoryByName(String artefactName) {
        for (Artefact artefact : inventory) {
            if (artefact.getName().equalsIgnoreCase(artefactName)) {
                inventory.remove(artefact);
                return artefact;
            }
        }
        return null;
    }
    public HashSet<Artefact> getInventory() {
        return inventory;
    }
    public void increaseHealth() {
        health.produceHealth();
    }
    public void decreaseHealth() {
        health.consumeHealth();
        if (health.getCurrentHealth() == 0) {
            afterLife();
        }
    }
    public int getHealth() {
        return health.getCurrentHealth();
    }
    public void clearInventory() {
        inventory.clear();
    }
    public void dropAllArtefact() {
        Location location = getPlayerLocation();
        Artefact[] artefacts = inventory.toArray(new Artefact[inventory.size()]);
        for (Artefact artefact : artefacts) {
            if (artefact != null) {
                location.addEntity(artefact);
                inventory.remove(artefact);
            }
        }
    }
    public void afterLife() {
        dropAllArtefact(); // Drop all the artefacts in the inventory and put them in the current location
        getPlayerLocation().removeEntity(this); // Remove the player from the current location
        GameMap.getStartingPoint().addEntity(this); // Add the player to the starting point
        health.reset();
        setPlayerLocation(GameMap.getStartingPoint()); // Set the location of the player to the starting point
    }
    @Override
    public String toString() {
        return "Player"+" "+
                "name='" + getName() + '\'';
    }
    public Artefact getArtefactByName(String artefactName) {
        for (Artefact artefact : inventory) {
            if (artefact.getName().equalsIgnoreCase(artefactName)) {
                return artefact;
            }
        }
        return null;
    }
}
