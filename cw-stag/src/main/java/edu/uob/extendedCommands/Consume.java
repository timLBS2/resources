package edu.uob.extendedCommands;

import edu.uob.GameAction;
import edu.uob.GameEntity;
import edu.uob.GameMap;
import edu.uob.entities.Artefact;
import edu.uob.entities.Location;
import edu.uob.entities.Path;
import edu.uob.entities.Player;
import java.util.HashSet;

/**
 * @className: executeConsume
 * @description remove entities.When an entity is consumed it should be removed from its current location
 * moved into the storeroom location.
 * @Package edu.uob.extendedCommands
 * @author: Li
 * @date: 01/05/2023
 **/
public class Consume {
    /**
     * @param player
     * @param gameAction
     * @return String
     * @description execute consume command.List all entities that are consumed:Health,Location,Artifect in inventory,Entity in location
     * remove entities from Set after consume entities,if there are still entities in Set,they must be not available right now.
     */
    public String executeConsume(Player player,GameAction gameAction) {
       Location currentLocation = player.getPlayerLocation();
        HashSet<String> AllEntityNamesNoHealth;
        HashSet<String> consumedAllEntityNames = new HashSet<>(gameAction.getConsumed());
        if (consumedAllEntityNames.size()!=0) {
            AllEntityNamesNoHealth = consumeHealth(player, consumedAllEntityNames);
            HashSet<Location> consumedLocations = findLocation(AllEntityNamesNoHealth);
            if(consumedLocations.size()!=0){
               AllEntityNamesNoHealth = removeLocationNames(AllEntityNamesNoHealth, consumedLocations);
               executeConsumedLocations(consumedLocations, currentLocation);
            }
            HashSet<GameEntity> inventoryEntities = findEntityFromInv(player, AllEntityNamesNoHealth);
            if (inventoryEntities.size()!=0) {
                AllEntityNamesNoHealth = removeInvEntitiesNames(inventoryEntities, AllEntityNamesNoHealth);
                executeInventoryEntities(player, inventoryEntities);
            }
            HashSet<GameEntity> entitiesFromLocation = findEntityFromLocation(player, AllEntityNamesNoHealth);
            if (entitiesFromLocation.size()!=0) {
                AllEntityNamesNoHealth = removeLocalEntitiesNames(entitiesFromLocation, AllEntityNamesNoHealth);
                executeEntitiesFromLocation(entitiesFromLocation, currentLocation);
            }
            if(AllEntityNamesNoHealth.size()!=0){
                return ("Something is not here right now.");
            }
        }
        return "";
    }

    public HashSet<String> consumeHealth(Player player,HashSet<String> consumedAllEntityNames){
        if(consumedAllEntityNames.contains("health")){
            player.decreaseHealth();
            consumedAllEntityNames.remove("health");
        }
        return consumedAllEntityNames;
    }
    public HashSet<Location> findLocation(HashSet<String> consumedAllEntityNames) {
        HashSet<Location>consumedLocations = new HashSet<>();
        for(String consumedEntityName:consumedAllEntityNames){
            consumedLocations.add(GameMap.getLocationByName(consumedEntityName));
        }
        return consumedLocations;
    }

    public  HashSet<String> removeLocationNames(HashSet<String> consumedAllEntityNames,HashSet<Location> consumedLocations) {
        HashSet<String> nonLocationEntitiesNames = new HashSet<>(consumedAllEntityNames);
        HashSet<String> consumedLocationNames = new HashSet<>();
        for(Location consumedLocation:consumedLocations){
            if (consumedLocation != null) {
                consumedLocationNames.add(consumedLocation.getName());
            }
        }
        nonLocationEntitiesNames.removeAll(consumedLocationNames);
        return nonLocationEntitiesNames;
    }
    public HashSet<GameEntity> findEntityFromInv(Player player, HashSet<String>nonLocationEntitiesNames) {
        HashSet<GameEntity> inventoryEntities = new HashSet<>();
        HashSet<String> playerInventoryEntitiesNames = new HashSet<>();
        for (Artefact artefact : player.getInventory()) {
            if (artefact != null) {
                playerInventoryEntitiesNames.add(artefact.getName());
            }
        }
        HashSet<String> intersection = new HashSet<>(playerInventoryEntitiesNames);
        intersection.retainAll(nonLocationEntitiesNames);
        for (String entityName : intersection) {
            GameEntity inventoryEntity = player.getArtefactByName(entityName);
            if (inventoryEntity != null) {
                inventoryEntities.add(inventoryEntity);
            }
        }
        return inventoryEntities;
    }
    public HashSet<String> removeInvEntitiesNames(HashSet<GameEntity>inventoryEntities,HashSet<String>nonLocationEntitiesNames){
        HashSet<String> nonLocationNonArtEntitiesNames = new HashSet<>(nonLocationEntitiesNames);
        HashSet<String> inventoryEntitiesNames = new HashSet<>();
        for (GameEntity artefact : inventoryEntities) {
            if (artefact != null){
                inventoryEntitiesNames.add(artefact.getName());
            }
        }
        nonLocationNonArtEntitiesNames.removeAll(inventoryEntitiesNames);
        return nonLocationNonArtEntitiesNames;
    }
    public HashSet<GameEntity> findEntityFromLocation(Player player, HashSet<String>nonLocationNonArtEntitiesNames) {
        HashSet<GameEntity> entitiesFromLocation = new HashSet<>();
        HashSet<String> playerLocationEntitiesNames = new HashSet<>();
        for (GameEntity entity : player.getPlayerLocation().getEntities()) {
            if (entity != null) {
                playerLocationEntitiesNames.add(entity.getName());
            }
        }
        HashSet<String> intersection = new HashSet<>(playerLocationEntitiesNames);
        intersection.retainAll(nonLocationNonArtEntitiesNames);
        for (String entityName : intersection) {
            GameEntity entityFromLocation = player.getPlayerLocation().getEntityByName(entityName);
            if (entityFromLocation != null) {
                entitiesFromLocation.add(entityFromLocation);
            }
        }
        return entitiesFromLocation;
    }
    public HashSet<String>removeLocalEntitiesNames(HashSet<GameEntity> entitiesFromLocation,HashSet<String> nonLocationNonArtEntitiesNames) {
        HashSet<String> NonLocationEntitiesNames = new HashSet<>(nonLocationNonArtEntitiesNames);
        HashSet<String> entitiesFromLocationNames = new HashSet<>();
        for (GameEntity entity : entitiesFromLocation) {
            if (entity != null) {
                entitiesFromLocationNames.add(entity.getName());
            }
        }
        NonLocationEntitiesNames.removeAll(entitiesFromLocationNames);
        return NonLocationEntitiesNames;
    }
    public void executeConsumedLocations(HashSet<Location> consumedLocations,Location currentLocation) {
        for(Location consumedlocation:consumedLocations) {
            if (consumedlocation != null) {
                for(Path pToHere:consumedlocation.getPaths()){
                    if(pToHere.getToLocation().equals(currentLocation)){
                        consumedlocation.removePath(pToHere);
                    }
                }
                for(Path pFromHere:currentLocation.getPaths()){
                    if(pFromHere.getToLocation().equals(consumedlocation)){
                        currentLocation.removePath(pFromHere);
                    }
                }
            }
        }
    }
    public void executeInventoryEntities(Player player,HashSet<GameEntity>inventoryEntities){
        for(GameEntity inventoryEntity:inventoryEntities) {
            if (inventoryEntity != null) {
                player.getInventory().remove((Artefact) inventoryEntity);
                GameMap.getLocationByName("storeroom").addEntity(inventoryEntity);
            }
        }
    }
    public void executeEntitiesFromLocation(HashSet<GameEntity>entitiesFromLocation,Location currentLocation){
        for(GameEntity entityFromLocation:entitiesFromLocation) {
            if (entityFromLocation != null) {
                currentLocation.removeEntity(entityFromLocation);
                GameMap.getLocationByName("storeroom").addEntity(entityFromLocation);
            }
        }
    }
}

