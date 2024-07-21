package edu.uob;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @className: LoadedActions
 * @description Load actions from the XML file,  store the actions in a suitable data structure
 * @author: Li
 * @date: 28/04/2023
 **/
public class LoadedActions {
       private static HashMap<String, HashSet<GameAction>> actions = new HashMap<>();

    /**
     * @methodName: loadActionsFromXML
     * @description: Load actions from the XML file,  store the actions in a suitable data structure
     * @param: [actionsFile]
     * @return: void
     **/
    public static void loadActionsFromXML(File actionsFile) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(actionsFile);
            Element root = document.getDocumentElement();
            NodeList actionList = root.getChildNodes();    // Get the list of actions
            for (int i = 1; i < actionList.getLength(); i+=2){ // Only the odd items are actually actions - 1, 3, 5 etc.
                Element action = (Element)actionList.item(i);
                addGameAction(getGameAction(action));
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @methodName: getGameAction
     * @description: Get the GameAction from the XML file
     * @param: [action]
     * @return: edu.uob.GameAction
     **/
    public static GameAction getGameAction(Element action){
        HashSet<String> triggers = new HashSet<>();
        HashSet<String> subjects = new HashSet<>();
        HashSet<String> consumed = new HashSet<>();
        HashSet<String> produced = new HashSet<>();
        addElementsToGameAction(action,"triggers","keyphrase",triggers);
        addElementsToGameAction(action,"subjects","entity",subjects);
        addElementsToGameAction(action,"consumed","entity",consumed);
        addElementsToGameAction(action,"produced","entity",produced);
        return new GameAction(triggers,subjects,consumed,produced,addNarrationToAction(action));
    }
    /**
     * @methodName: addElementsToGameAction
     * @description: Add the elements to the GameAction
     * @param: [action, firstTagName, secondTagName, actionFieldSet]
     * @return: void
     **/
    public static void addElementsToGameAction(Element action,String firstTagName,String secondTagName,HashSet<String> actionFieldSet){
        Element firstTagNameElement = (Element) action.getElementsByTagName(firstTagName).item(0); // for example,get the Element triggers
        NodeList secondTagNameList = firstTagNameElement.getElementsByTagName(secondTagName);
        for (int j = 0; j < secondTagNameList.getLength(); j++) {
            String textContent = secondTagNameList.item(j).getTextContent();// for example,get the keyphrase string
            actionFieldSet.add(textContent);
        }
    }
    public static String addNarrationToAction(Element action){
        Element narrationElement = (Element) action.getElementsByTagName("narration").item(0); // Get the triggers element
        return narrationElement.getTextContent();
    }

    /**
     * @methodName: addGameAction
     * @description: Add the GameAction to the actions set
     * @param: [gameAction]
     * @return: void
     **/
    private static void addGameAction(GameAction gameAction) {
        for (String trigger:gameAction.getTriggers()) {
            if (actions.containsKey(trigger)){
                actions.get(trigger).add(gameAction);
            } else {
                HashSet<GameAction> newGameActionSet = new HashSet<>();
                newGameActionSet.add(gameAction);
                actions.put(trigger, newGameActionSet);
            }
        }
    }

    public static HashMap<String, HashSet<GameAction>> getActions() {
        return actions;
    }

    /**
     * @methodName: getAllSubjects
     * @description: Get all the subjects from the actions
     * @param: []
     * @return: java.util.HashSet<java.lang.String>
     **/
    public static HashSet<String> getAllSubjects(){
        HashSet<String> allSubjects = new HashSet<>();
        for (HashSet<GameAction> gameActionSet:actions.values()) {
            for (GameAction gameAction:gameActionSet) {
                allSubjects.addAll(gameAction.getSubjects());
            }
        }
        return allSubjects;
    }
}

