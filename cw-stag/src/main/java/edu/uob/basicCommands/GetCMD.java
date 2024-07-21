package edu.uob.basicCommands;

import edu.uob.GameEntity;
import edu.uob.entities.Artefact;
import edu.uob.entities.Location;
import edu.uob.entities.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @className: GetCMD
 * @description picks up a specified artefact from the current location and adds it into player's inventory$
 * @author: Li
 * @date: 27/04/2023
 **/
public class GetCMD extends BasicCMD{
    public String execute(Player player, String[] tokens) {
        Location location = player.getPlayerLocation();
        if (isSingleEntityCommand(tokens,location)) {
            Artefact artefact = (Artefact) getSingleEntity(tokens,location);
            if (artefact != null && logicOrder(tokens, artefact.getName())) {
                player.addArtefact(artefact);
                location.removeEntity(artefact);
            } else {
                return "You cannot pick up this artefact.";
            }
            return "You picked up a " + artefact.getName() + ".";
        } else {
            return "You cannot pick up more than one artefact at a time.Maybe it is not artefact.";
        }
    }
    public boolean isSingleEntityCommand(String[] tokens,Location location) {
        int count = 0;
        for (GameEntity entity : location.getEntities()) {
            if (Arrays.asList(tokens).contains(entity.getName()) && (entity instanceof Artefact)) {
                count++;
            }
        }
        return count == 1;
    }
    public GameEntity getSingleEntity(String[] tokens,Location location) {
        for (GameEntity entity : location.getEntities()) {
            if (Arrays.asList(tokens).contains(entity.getName()) && (entity instanceof Artefact)) {
                return entity;
            }
        }
        return null;
    }
    private boolean logicOrder(String[] tokens, String artefactName){
        List<String> tokenlist = Arrays.asList(tokens);
        int index1 = tokenlist.indexOf("get");
        int index2 = tokenlist.indexOf(artefactName);
        return index1 < index2;
    }
}
