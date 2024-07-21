package edu.uob.entities;

import edu.uob.GameEntity;

/**
 * @className: Path
 * @description As one field of Location, only contains the location that the path leads to, and whether the path is one way
 * @author: Li
 * @date: 30/04/2023
 **/
public class Path extends GameEntity {
    private Location toLocation;    // the location that the path leads to

    public Path(String name, String description, Location toLocation) {
        super(name, description);
        this.toLocation = toLocation;
    }
    public Path(Location toLocation) {
        this.toLocation = toLocation;
    }

    public Path() {}

    public Location getToLocation() {
        return toLocation;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
    }

}
