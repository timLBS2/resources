package edu.uob.basicCommands;

import edu.uob.GameEntity;
import edu.uob.entities.Path;
import edu.uob.entities.Player;


/**
 * @className: lookCMD
 * @description prints names and descriptions of entities in the current location and lists paths to other locations$
 * @author: Li
 * @date: 27/04/2023
 **/
public class LookCMD extends BasicCMD{
    public String execute(Player player, String[] tokens) {
        StringBuilder result = new StringBuilder();
        result.append("You are in ").append(player.getPlayerLocation().getDescription()).append(".");
        result.append("You can see: \n");
        for (GameEntity entity : player.getPlayerLocation().getEntities()) {
            if(!entity.getName().equalsIgnoreCase(player.getName())) {
                result.append(entity.getDescription()).append("\n");
            }
        }
        result.append("You can access from here: \n");
        if(player.getPlayerLocation().getEntities().size()==0){
            result.append("No path to other locations");
        }else {
            for (Path p : player.getPlayerLocation().getPaths()) {
                result.append(p.getToLocation().getName()).append("\n");
            }
        }
        return result.toString();
    }
}
