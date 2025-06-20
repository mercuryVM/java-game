package entities.enemies.boss;

import entities.enemies.Enemy;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;
import time.Time;

import java.awt.*;

public class Boss extends Enemy {

    private int initialHealth;

    public Boss(GameManager manager, int health) {
        super(manager);
        this.currentHealth = health;
        this.initialHealth = health;
    }

    public float getRadius(){
        return 0.0f;
    };

    public int getInitialHealth() {
        return initialHealth;
    }

    @Override
    public boolean OutOfBounds() {
        return false;
    }

}
