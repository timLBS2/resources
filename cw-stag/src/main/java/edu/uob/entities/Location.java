package edu.uob.entities;

import edu.uob.GameEntity;
import java.util.*;

/**
 * @className: Location
 * @description: The class of location, which extends GameEntity, and has a list of paths, characters, artefacts and furniture
 * @author: Li
 * @date: 27/04/2023
 **/
public class Location extends GameEntity {
    private HashSet<GameEntity> entities = new HashSet<>();
    private HashSet<Path> paths = new HashSet<>();

    public Location(String name, String description) {
        super(name, description);
    }
    public Location(){}

    public void addEntity(GameEntity entity) {
        entities.add(entity);
    }
    public GameEntity getEntityByName(String name){
        for (GameEntity entity : entities) {
            if(entity.getName().equals(name)){
                return entity;
            }
        }
        return null;
    }
    public void removeEntity(GameEntity entity) {
        entities.remove(entity);
    }
    public HashSet<GameEntity> getEntities() {
        return entities;
    }
    public void addPath(Path path) {
        paths.add(path);
    }
    public void removePath(Path path) {
        paths.remove(path);
    }

    public HashSet<Path> getPaths() {
        return paths;
    }

    @Override
    public String toString() {
        return "Location" +" "+"name:" +'\'' +super.getName() + '\'' ;
    }
}
