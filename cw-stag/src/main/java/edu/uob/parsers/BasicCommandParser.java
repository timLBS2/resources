package edu.uob.parsers;

import edu.uob.basicCommands.*;
import edu.uob.entities.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @className: BasicCommandParser
 * @description Parse and execute basic commands
 * @Package edu.uob
 * @author: Li
 * @date: 30/04/2023
 **/
public class BasicCommandParser{
    private static Map<String, BasicCMD> commandMap;

    static {
        commandMap = new HashMap<>();
        commandMap.put("look", new LookCMD());
        commandMap.put("goto", new GotoCMD());
        commandMap.put("drop", new DropCMD());
        commandMap.put("inventory", new InventoryCMD());
        commandMap.put("inv", new InventoryCMD());
        commandMap.put("get", new GetCMD());
    }


    /**
     * @methodName singleBasicAction
     * @description: check if the command matches only one basic command action.
     * True if only one basic command action.
     * False if more than or not one basic command action
     * @param: [tokens]
     * @return: boolean
     * @auther: Li
     * @date: 30/04/2023
     */
    public static boolean singleBasicAction(String[] tokens) {
        int count = 0;
        Set<String> basicKeys = commandMap.keySet();
        for (String token :tokens) {
            if (basicKeys.contains(token)) {
                count++;
            }
        }
        return count == 1;
    }
    /**
     * @description: If the tokens contain a basic command, return the basic command.
     *               If not, return null.
     * @param: [tokens]
     * @return: boolean
     * @auther: Li
     * @date: 30/04/2023
     */
    public static BasicCMD getSingleActionBasicCommand(String[] tokens) {
        for (String token :tokens) {
            if ((commandMap.containsKey(token))){
                return commandMap.get(token);
            }
        }
        return null;
    }
    /**
     * @description: execute the basic command
     * @param: [player, tokens]
     * @return: java.lang.String
     * @auther: Li
     * @date: 30/04/2023
     */
    public static String executeBasicCommand(Player player, String[] tokens){
        BasicCMD command = getSingleActionBasicCommand(tokens);
        if(command == null){
            return("Contains no basic command");
        }else {
            return command.execute(player, tokens);
        }
    }
}
