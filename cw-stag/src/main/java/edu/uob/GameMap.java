package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import edu.uob.entities.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: GameMap
 * @description parse the entity DOT files, extract a set of GraphViz Objects and then store them in a suitable data structure$
 * @author: Li
 * @date: 28/04/2023
 **/
public class GameMap {
    private static Map<String, Location> gameMap = new HashMap<>(); // store all the locations including paths
    private static Location startingPoint;  // the starting point of the game
    private static Map<String,Player>players = new HashMap<>();    // store all the players in the game
    /**
     * @methodName: buildGameMapFromDot
     * @description: To build the game map from the DOT file
     * @param: [entitiesFile]
     * @return: java.util.Map<java.lang.String,edu.uob.entities.Location>
     * @throws:
     **/
    public static void getGameMapFromDot(File entitiesFile) {
        Parser parser = new Parser();
        try {
            parser.parse(new FileReader(entitiesFile));
        } catch (ParseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Graph wholeDocument = parser.getGraphs().get(0);
        ArrayList<Graph> sections = wholeDocument.getSubgraphs();   // sections = [locations, paths]
        loadLocations(sections);
        loadPaths(sections);

    }
    /**
     * @methodName: loadLocations
     * @description: To load the locations in the game map
     * @param: [sections]
     * @return: void
     * @throws:
     **/
    public static void loadLocations(ArrayList<Graph> sections) {
        // The locations will always be in the first subgraph
        ArrayList<Graph>locations = sections.get(0).getSubgraphs();
        boolean isFirstLocation = true;     //To set the first location as the starting point
        for (Graph locationGraph : locations) {
            Node locationDetails = locationGraph.getNodes(false).get(0);
            String locationName = locationDetails.getId().getId();
            String locationDescription = locationDetails.getAttribute("description");
            Location location = new Location(locationName, locationDescription);
            if (isFirstLocation) {
                startingPoint = location;
                isFirstLocation = false;
            }
            loadLocalEntities(locationGraph,location);
            gameMap.put(locationName, location);
        }
    }
    /**
     * @methodName: loadLocalEntities
     * @description: To load the entities in the location
     * @param: [locationGraph, location]
     * @return: void
     * @throws:
     **/
    public static void loadLocalEntities(Graph locationGraph,Location location){
        ArrayList<Graph> localEntities = locationGraph.getSubgraphs();
        for(Graph localEntity:localEntities){   //localEntity = subgraph entity...
            ArrayList<Node> entityNodes = localEntity.getNodes(false);  // entityNodes = [Node entity1, Node entity2, ...]
            for (Node entityNode : entityNodes){    // entityNode = Node [shape = "diamond"];
                String entityName = entityNode.getId().getId(); // entityName = "potion"
                String entityDescription = entityNode.getAttribute("description");
                //To decide which type of entity it is
                GameEntity entity = switch (localEntity.getId().getId()) {
                    case "artefacts" -> new Artefact(entityName, entityDescription);
                    case "characters" -> new GameCharacter(entityName, entityDescription);
                    case "furniture" -> new Furniture(entityName, entityDescription);
                    default -> throw new IllegalStateException("Unexpected entity: " + localEntity.getId().getId());
                };
                location.addEntity(entity);
            }
        }
    }
    /**
     * @methodName: loadPaths
     * @description: To load the paths in the game map
     * @param: [sections]
     * @return: void
     * @throws:
     **/
    public static void loadPaths(ArrayList<Graph> sections) {
        // The paths will always be in the second subgraph
        ArrayList<Edge> paths = sections.get(1).getEdges();
        for (Edge path : paths) {
            Node fromLocation = path.getSource().getNode(); // Node cabin
            String fromName = fromLocation.getId().getId(); // String cabin
            Node toLocation = path.getTarget().getNode();   // Node forest
            String toName = toLocation.getId().getId();     // String forest
            Location from = gameMap.get(fromName);
            Location to = gameMap.get(toName);
            Path p = new Path(to);
            from.addPath(p);    // Add the path to the fromLocation
        }
    }

    /**
     * @methodName: getStartingPoint
     * @description: To get the getStarting Point
     * @param:
     * @return: java.util.Map<java.lang.String,edu.uob.entities.Location>
     **/
    public static Location getStartingPoint() {
        return startingPoint;
    }

    /**
     * @methodName: getPlayerLocation
     * @description: To get the location of the player by the player's name
     * @param: [playerName]
     * @return: edu.uob.entities.Location
     * @throws:
     **/
    public static Location getLocationByPlayerName(String playerName) {
        for (Location location : gameMap.values()) {
            for (GameEntity entity : location.getEntities()) {
                if (entity instanceof Player player) {
                    if (player.getName().equals(playerName)) {
                        return location;
                    }
                }
            }
        }
        return null;
    }
    public static Map<String,Player> loadPlayers() {
        for (Location location : gameMap.values()) {
            for (GameEntity entity : location.getEntities()) {
                if (entity instanceof Player player) {
                    players.put(player.getName(), player);
                }
            }
        }
        return players;
    }
    public static void addPlayer(Player player) {
        players.put(player.getName(), player);
    }
    /**
     * @methodName: getPlayerByName
     * @description: To get the player by the player's name
     * @param: [PlayerName]
     * @return: edu.uob.entities.Player
     * @throws:
     **/
    public static Player getPlayerByName(String PlayerName) {
        return players.get(PlayerName);
    }
    /**
     * @methodName: getLocationByName
     * @description: To get the location by the location's name
     * @param: [locationName]
     * @return: edu.uob.entities.Location
     * @throws:
     **/
    public static Location getLocationByName(String locationName) {
        return gameMap.get(locationName);
    }
    /**
     * @methodName: getGameEntityByName
     * @description: To get the game entity except location by the entity's name
     * @param: [entityName]
     * @return: edu.uob.entities.GameEntity
     **/
    public static GameEntity getNonLocationEntityByName(String entityName) {
        for (Location location : gameMap.values()) {
            for (GameEntity entity : location.getEntities()) {
                if (entity.getName().equalsIgnoreCase(entityName)) {
                    return entity;
                }
            }
        }
        return null;
    }
    /**
     * @methodName: getGameEntityByName
     * @description: To get the location by the game entity
     * @param: [entityName]
     * @return: edu.uob.entities.GameEntity
     **/
    public static Location getLocationByGameEntity(GameEntity gameEntity) {
        if (gameEntity != null) {
            for (Location location : gameMap.values()) {
                for (GameEntity entityInLocation : location.getEntities()) {
                    if (gameEntity.equals(entityInLocation)) {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    public static Map<String, Location> getGameMap() {
        return gameMap;
    }

    public static void resetGameMap() {
        gameMap = new HashMap<>();
        startingPoint = null;
        players = new HashMap<>();
    }
}


