package edu.uob.entities;

import edu.uob.GameEntity;

/**
 * @className: Character
 * @author: Li
 * @date: 27/04/2023
 **/
public class GameCharacter extends GameEntity {
    public GameCharacter(String name, String description) {
        super(name, description);
    }
    public GameCharacter(){}

    /**
        * @methodName: toString
        * @description: Override toString method to return the name of the character
        * @param: []
        * @return: java.lang.String
        * @throws:
        **/
        @Override
        public String toString() {
            return "Character" +" "+"name:" +'\'' +super.getName() + '\'';
        }
}
