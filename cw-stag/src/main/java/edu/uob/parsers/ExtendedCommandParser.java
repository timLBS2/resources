package edu.uob.parsers;

import edu.uob.GameAction;
import edu.uob.LoadedActions;
import edu.uob.entities.Player;
import edu.uob.extendedCommands.AvailableSubjects;
import edu.uob.extendedCommands.Consume;
import edu.uob.extendedCommands.Produce;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className: ExtendedCommandParser
 * @description check if it is valid extended command and execute it
 * @Package edu.uob
 * @author: Li
 * @date: 01/05/2023
 **/
public class ExtendedCommandParser {
    /**
     * @param player
     * @param gameAction
     * @return String
     * @description execute extended command
     */
    public static String executeExtendedCommand(Player player, GameAction gameAction){
        if (gameAction == null){
            return ("The command cannot be composite extended command");
        }else if(!AvailableSubjects.AreAllSubjectsAvailable(player,gameAction)){
            return ("The subjects in command are not all available.");
        }else{
            new Consume().executeConsume(player,gameAction);
            new Produce().executeProduce(player,gameAction);
            return gameAction.getNarration();
        }
    }
    /**
     * @param actionCommand
     * @return gameAction
     * @description check if triggers & subjects in command  match only one action in the actions file,if yes then return GameAction that matches,
     *              if no, return null
     */
    public static GameAction getGameActionInCommand(String actionCommand){
        for (HashSet<GameAction> gameActionSet : LoadedActions.getActions().values()) {
            for (GameAction gameAction:gameActionSet) {
                HashSet<String> actionTriggers = gameAction.getTriggers();
                HashSet<String> actionSubjects = gameAction.getSubjects();
                HashSet<String> gotTriggers = getAllExactTriggersInCommand(actionCommand);
                HashSet<String> gotSubjects = getAllSubjectsInCommand(actionCommand);
                if((gotTriggers.size()!=0) && (gotSubjects.size()!=0)){
                    boolean containsAllTriggers = actionTriggers.containsAll(gotTriggers);
                    boolean containsAllSubjects = actionSubjects.containsAll(gotSubjects);
                    if (containsAllTriggers && containsAllSubjects) {
                        return gameAction;
                    }
                }
            }
        }
        return null;
    }
    /**
     * @param actionCommand
     * @return HashSet<String>
     * @description Get all exact triggers in command
     */
    public static HashSet<String> getAllExactTriggersInCommand(String actionCommand) {
        HashSet<String> gotTriggers = new HashSet<>();
        HashSet<String> triggers = new HashSet<>(LoadedActions.getActions().keySet());
        for (String trigger : triggers) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(trigger) + "\\b");
            Matcher matcher = pattern.matcher(actionCommand);
            if (matcher.find()) {
                gotTriggers.add(trigger);
            }
        }
        return gotTriggers;
    }

    /**
     * @param actionCommand
     * @return HashSet<String>
     * @description Get all subjects in command
     */
    public static HashSet<String> getAllSubjectsInCommand(String actionCommand) {
        String[] tokens = actionCommand.split(" ");
        HashSet<String> gotSubjects = new HashSet<>();
        HashSet<String> subjects = LoadedActions.getAllSubjects();
        for (String subject : subjects){
            if (Arrays.asList(tokens).contains(subject)){
                gotSubjects.add(subject);
            }
        }
        return gotSubjects;
    }
}
