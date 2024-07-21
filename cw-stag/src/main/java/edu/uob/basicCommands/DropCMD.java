package edu.uob.basicCommands;

import edu.uob.GameEntity;
import edu.uob.entities.Artefact;
import edu.uob.entities.Location;
import edu.uob.entities.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @className: DropCMD
 * @description puts down an artefact from player's inventory and places it into the current location$
 * @author: Li
 * @date: 27/04/2023
 **/
public class DropCMD extends BasicCMD{
    public String execute(Player player, String[] tokens) { //tokens from the BasicCommandParser.executeBasicCommand
        Location location = player.getPlayerLocation();
        Artefact artefact = getSingleEntity(player, tokens);
        if (isSingleEntityCommand(player, tokens)) {
            if (artefact != null && logicOrder(tokens, artefact.getName())) {
                location.addEntity(artefact);
                player.removeArtefact(artefact);
                return "You drop " + artefact.getName() + ".";
            } else {
                return "You do not carry this artefact,so cannot drop.";
            }
        } else {
            return "You cannot drop more than one artefact at a time, invalid command.";
        }
    }
    /**
     * @methodName: isSingleEntityCommand
     * @description: check if the command contains a single entity
     * @param: [player, tokens]
     * @return: boolean
     * @throws:
     **/
    public boolean isSingleEntityCommand(Player player, String[] tokens) {
        int count = 0;
        for (Artefact item : player.getInventory()) {
            if (Arrays.asList(tokens).contains(item.getName())) {
                count++;
            }
        }
        return count == 1;
    }
    /**
     * @methodName: getSingleEntity
     * @description: get the single only entity from the command
     * @param: [player, tokens]
     * @return: edu.uob.entities.Artefact
     * @throws:
     **/
    public Artefact getSingleEntity(Player player, String[] tokens) {
        for (Artefact item : player.getInventory()) {
            if (Arrays.asList(tokens).contains(item.getName())) {
                return item;
            }
        }
        return null;
    }
    /**
     * @methodName: logicOrder
     * @description: make sure verb is before noun
     * @param: [tokens, artefactName]
     * @return: boolean
     * @throws:
     **/
    public boolean logicOrder(String[] tokens,String artefactName){
        List<String> tokenlist = Arrays.asList(tokens);
        int index1 = tokenlist.indexOf("drop");
        int index2 = tokenlist.indexOf(artefactName);
        return index1 < index2;
    }
}
