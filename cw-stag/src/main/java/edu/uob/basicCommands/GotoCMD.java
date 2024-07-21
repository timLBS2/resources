package edu.uob.basicCommands;

import edu.uob.GameEntity;
import edu.uob.GameMap;
import edu.uob.entities.Artefact;
import edu.uob.entities.Location;
import edu.uob.entities.Path;
import edu.uob.entities.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @className: GotoCMD
 * @description moves the player to the specified location (if there is a path to that location)
 * @author: Li
 * @date: 27/04/2023
 **/
public class GotoCMD extends BasicCMD {
    public String execute(Player player, String[] tokens) {
        Location fromLocation = player.getPlayerLocation();
        if (isSingleLocationCommand(tokens)) {
            Location tolocation = GameMap.getLocationByName(getSingleToLocation(tokens));
            if(tolocation == null){
                return("There is no such a location.");
            }else if(fromLocation.getPaths().size() == 0){
                return("There is no path to that location");
            } else if (!PathFromTo(fromLocation, tolocation)){
                return("There is no path to that location");
            } else if(!logicOrder(tokens ,getSingleToLocation(tokens))){
                return("invalid command");
            }else{
                player.setPlayerLocation(tolocation);
                tolocation.addEntity(player);
                fromLocation.removeEntity(player);
                return new LookCMD().execute(player,tokens);
            }
        } else {
            return "You cannot go to more than one location at a time.";
        }
    }
    public boolean PathFromTo(Location fromLocation, Location toLocation) {
        for (Path path : fromLocation.getPaths()) {
            if (path.getToLocation().equals(toLocation)) {
                return true;
            }
        }
        return false;
    }
    public boolean isSingleLocationCommand(String[] tokens) {
        int count = 0;
        for (String locationName : GameMap.getGameMap().keySet()) {
            if (Arrays.asList(tokens).contains(locationName)){
                count++;
            }
        }
        return count == 1;
    }
    public String getSingleToLocation(String[] tokens) {
        for (String locationName : GameMap.getGameMap().keySet()) {
            if (Arrays.asList(tokens).contains(locationName)){
                return locationName;
            }
        }
        return null;
    }
    private boolean logicOrder(String[] tokens, String toLocationName){
        List<String> tokenlist = Arrays.asList(tokens);
        int index1 = tokenlist.indexOf("goto");
        int index2 = tokenlist.indexOf(toLocationName);
        return index1 < index2;
    }
}