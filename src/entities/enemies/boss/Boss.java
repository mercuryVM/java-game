package entities.enemies.boss;

import entities.enemies.Enemy;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;
import time.Time;

import java.awt.*;

public class Boss extends Enemy {

    public Boss(GameManager manager, int health) {
        super(manager);
        this.currentHealth = health;
    }

    @Override
    public boolean OutOfBounds() {
        return false;
    }

}
