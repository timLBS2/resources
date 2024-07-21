package edu.uob;

import edu.uob.entities.Player;
import edu.uob.parsers.BasicCommandParser;
import edu.uob.parsers.ExtendedCommandParser;
import edu.uob.parsers.PlayerNameParser;

/**
 * @className: CommandHandler
 * @description check if it is valid basic command or extended command and execute it
 * @author: Li
 * @date: 01/05/2023
 **/
public  class CommandHandler {
    public static String handleCommand(String command) {
        Player player = PlayerNameParser.handlePlayerName(command);
        if (player != null) {
            String actionCommand = PlayerNameParser.getActionCommand(command).trim().toLowerCase();
            String[] tokens = actionCommand.split(" ");
            //extendedAction=null: not extended command; extendedAction!=null: extended command
            GameAction extendedAction = ExtendedCommandParser.getGameActionInCommand(actionCommand);
            boolean isSingleBasicAction = BasicCommandParser.singleBasicAction(tokens);
            if((!isSingleBasicAction) && (extendedAction == null)){   //neither basic nor extended
                return("Invalid Command") ;
            } else if (isSingleBasicAction && (extendedAction !=null)) { //both basic and extended
                return("Composite Command");
            } else if (isSingleBasicAction) { //only basic
                return BasicCommandParser.executeBasicCommand(player,tokens);
            } else { //only extended
                return ExtendedCommandParser.executeExtendedCommand(player,extendedAction);
            }
        } else {
            return "Invalid Player Name";
        }
    }
}
