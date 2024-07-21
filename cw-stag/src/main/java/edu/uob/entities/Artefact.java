package edu.uob.entities;

import edu.uob.GameEntity;

/**
 * @className: Artefact
 * @description: Artefact class extends GameEntity class
 * @author: Li
 * @date: 27/04/2023
 **/
public class Artefact extends GameEntity {
    public Artefact(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return "Artefact[" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' + ']';
    }
}

