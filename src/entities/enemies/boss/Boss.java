package entities.enemies.boss;

import entities.enemies.Enemy;
import game.GameManager;

public class Boss extends Enemy {

    private final int initialHealth;

    public Boss(GameManager manager, int health) {
        super(manager);
        this.currentHealth = health;
        this.initialHealth = health;
    }

    public float getRadius(){
        return 0.0f;
    }

    public int getInitialHealth() {
        return initialHealth;
    }

    @Override
    public boolean OutOfBounds() {
        return false;
    }

}
