package edu.uob.entities;

import edu.uob.GameEntity;

/**
 * @className: Health
 * @description health level of the player
 * @Package edu.uob.entities
 * @author: Li
 * @date: 03/05/2023
 **/
public class Health extends GameEntity {
    private int currentHealth;
    private final int maxHealth = 3;
    public Health(){
        this.currentHealth = maxHealth;
    }
    public int getCurrentHealth() {
        return currentHealth;
    }

    public void consumeHealth() {
        if (currentHealth < 0) {
            this.currentHealth = 0;
        } else currentHealth--;
    }
    public void produceHealth() {
        if (currentHealth >3) {
            this.currentHealth = 3;
        } else currentHealth++;
    }
    public void reset() {
        currentHealth = maxHealth;
    }
}
