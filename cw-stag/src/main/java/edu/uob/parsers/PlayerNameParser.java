package edu.uob.parsers;

import edu.uob.GameMap;
import edu.uob.entities.Player;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className: PlayerNameParser
 * @description handle player name and get action command
 * @Package edu.uob
 * @author: Li
 * @date: 01/05/2023
 **/
public class PlayerNameParser {

    public static Player handlePlayerName(String command) {
        if (!isValidPlayerName(command)) {
            return null;
        }
        String playerName = getPlayerName(command);
        if (containsReservedWords(playerName)) {
            return null;
        }
        if(GameMap.getPlayerByName(playerName)==null){
            return createPlayer(playerName);
        }else{
            return GameMap.getPlayerByName(playerName);
        }
    }
    public static boolean isValidPlayerName(String command) {
        Matcher matcher = Pattern.compile("^[A-Za-z\\s'-]+:\\s*.+$").matcher(command);
        return matcher.matches();
    }

    public static String getPlayerName(String command) {
        return command.split(":")[0].trim().toLowerCase();
    }

    /**
     * @description: check if the player name contains reserved words
     * @param: [playerName]
     * @return: boolean
     * @auther: Li
     * @date: 01/05/2023
     */
    public static boolean containsReservedWords(String playerName){
        String[] reservedWords = {"look", "goto", "drop", "inventory", "inv", "get"};
        for (String word : reservedWords) {
            if (word.equals(playerName)) {
                return true;
            }
        }
        return false;
    }
    /**
     * @description: create a new player and add it to the game map
     * @param: [playerName]
     * @return: edu.uob.entities.Player
     * @auther: Li
     * @date: 01/05/2023
     */
    public static Player createPlayer(String playerName){
        Player newPlayer = new Player(playerName, playerName);
        GameMap.getStartingPoint().addEntity(newPlayer);
        GameMap.addPlayer(newPlayer);
        return newPlayer;
    }

    public static String getActionCommand(String command) {
        return command.split(":")[1].trim();
    }
}

