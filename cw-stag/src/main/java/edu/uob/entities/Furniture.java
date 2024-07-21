package edu.uob.entities;

import edu.uob.GameEntity;

/**
 * @className: Furniture
 * @author: Li
 * @date: 27/04/2023
 **/
public class Furniture extends GameEntity {
    public Furniture(){}
    public Furniture(String entityName, String entityDescription) {
        super(entityName, entityDescription);
    }
    /**
     * @methodName: toString
     * @description: Override toString method to return the name of the furniture
     * @param: []
     * @return: java.lang.String
     * @throws:
     **/
    @Override
    public String toString() {
        return "Furniture" +" "+"name:" +'\'' +super.getName() + '\'' ;
    }
}
