package edu.uob.basicCommands;

import edu.uob.entities.Artefact;
import edu.uob.entities.Player;

/**
 * @className: InventoryCMD
 * @description lists all the artefacts currently being carried by the player
 * @author: Li
 * @date: 25/04/2023
 **/
public class InventoryCMD extends BasicCMD{
    public String execute(Player player, String[] tokens){
        StringBuilder result = new StringBuilder();
        result.append("You are carrying: \n");
        if(player.getInventory().size()==0){
            result.append("Nothing");
        }else {
            for (Artefact a : player.getInventory()) {
                result.append(a.getName()).append("\n");
            }
        }
        return result.toString();
    }
}
