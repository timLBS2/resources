package edu.uob.extendedCommands;

import edu.uob.GameAction;
import edu.uob.GameEntity;
import edu.uob.entities.Artefact;
import edu.uob.entities.Location;
import edu.uob.entities.Player;

import java.util.HashSet;

/**
 * @className: Subject
 * @description check if subject is available.Being "available" requires the entity to either be in the inventory of the player invoking the action or for that entity to be in the room/location where the action is being performed. This feature is intended to be a shortcut so that a player can use an entity in their location without having to explicitly pick it up first.
 * @Package edu.uob.extendedCommands
 * @author: Li
 * @date: 03/05/2023
 **/
public class AvailableSubjects {
    /**
     * @param player
     * @param gameAction
     * @return boolean
     * @description check if all subjects in command are available: in inventory or in location
     */
    public static boolean AreAllSubjectsAvailable(Player player, GameAction gameAction) {
        HashSet<Artefact> inventory = player.getInventory();
        HashSet<String> subjectNames = gameAction.getSubjects();
        HashSet<String> entityNames = new HashSet<>();
        Location location = player.getPlayerLocation();
        for (Artefact item : inventory) {
            entityNames.add(item.getName());
        }
        HashSet<GameEntity> localEntities = location.getEntities();
        for (GameEntity entity : localEntities) {
            entityNames.add(entity.getName());
        }
        return entityNames.containsAll(subjectNames);
    }
}
