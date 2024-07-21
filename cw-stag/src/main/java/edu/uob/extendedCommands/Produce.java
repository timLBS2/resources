package edu.uob.extendedCommands;

import edu.uob.GameAction;
import edu.uob.GameEntity;
import edu.uob.GameMap;
import edu.uob.entities.Location;
import edu.uob.entities.Path;
import edu.uob.entities.Player;

import java.util.HashSet;

/**
 * @className: executeProduce
 * @description When an entity is produced, it should be moved from its current location in the game (which might be
 * in the storeroom) to the location in which the action was trigged. The entity should NOT
 * automatically appear in a players inventory - it might be furniture (which the player can't carry) or it
 * might be an artefact they don't actually want to pick up !
 * @Package edu.uob.extendedCommands
 * @author: Li
 * @date: 01/05/2023
 **/
public class Produce {
    public void executeProduce(Player player,GameAction gameAction) {
        Location currentLocation = player.getPlayerLocation();
        HashSet<String> producedAllEntityNames = new HashSet<>(gameAction.getProduced());
        if (producedAllEntityNames.size()!=0) {
            HashSet<String> AllEntityNamesNoHealth = produceHealth(player,producedAllEntityNames);
            HashSet<Location>producedLocations = findLocationInCommand(AllEntityNamesNoHealth);
            HashSet<GameEntity>nonLocationEntities = findNonLocationEntities(AllEntityNamesNoHealth);
            if (producedLocations.size()!=0) {
                executeProducedLocations(producedLocations,currentLocation);
            }
            if (nonLocationEntities.size()!=0) {
                executeProducedNonLocations(nonLocationEntities,currentLocation);
            }
        }
    }
    public HashSet<String> produceHealth(Player player,HashSet<String> producedAllEntityNames){
        if (producedAllEntityNames.contains("health")){
            player.increaseHealth();
            producedAllEntityNames.remove("health");
        }
        return producedAllEntityNames;
    }
    public HashSet<Location> findLocationInCommand(HashSet<String> AllEntityNamesNoHealth) {
        HashSet<Location>producedLocations = new HashSet<>();
        for(String producedEntityName:AllEntityNamesNoHealth){
            if (GameMap.getLocationByName(producedEntityName)!=null) {
                producedLocations.add(GameMap.getLocationByName(producedEntityName));
            }
        }
        return producedLocations;
    }
    public HashSet<GameEntity> findNonLocationEntities(HashSet<String> AllEntityNamesNoHealth){
        HashSet<GameEntity> nonLocationEntities = new HashSet<>();
        for(String producedEntityName:AllEntityNamesNoHealth){
            if (GameMap.getNonLocationEntityByName(producedEntityName)!=null) {
                nonLocationEntities.add(GameMap.getNonLocationEntityByName(producedEntityName));
            }
        }
        return nonLocationEntities;
    }
    public void executeProducedLocations(HashSet<Location>producedLocations,Location currentLocation){
        for(Location producedLocation:producedLocations){
            Path newPath = new Path(producedLocation);
            currentLocation.addPath(newPath);
        }
    }
    public String executeProducedNonLocations(HashSet<GameEntity> nonLocationEntities,Location currentLocation){
        for(GameEntity nonLocationEntity:nonLocationEntities ){
            Location originalLocation = GameMap.getLocationByGameEntity(nonLocationEntity);
            if(originalLocation!=null) {
                originalLocation.removeEntity(nonLocationEntity);
                currentLocation.addEntity(nonLocationEntity);
            }else{
                return("The entity is not in any location.Maybe it is in someone's inventory.");
            }
        }
        return "   ";
    }
}
