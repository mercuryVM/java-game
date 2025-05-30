package entities.enemies.boss;

import entities.enemies.Enemy;
import game.GameManager;
import libraries.GameLib;
import time.Time;

import java.awt.*;

public class Boss extends Enemy {
    public Boss(GameManager manager) {
        super(manager);
        this.currentHealth = 1000;
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        if(super.Render(deltaTime, currentTime)) return true;
        GameLib.setColor(Color.CYAN);
        float time = Time.getTimeFromStart() / 1000f;
        double radius = Math.abs(Math.cos(time) * 20) + 30;
        GameLib.drawCircle(position.x, position.y, radius);
        GameLib.setColor(Color.gray);
        GameLib.drawLine(position.x, position.y, position.x * Math.cos(time), position.y * Math.sin(time));
        return false;
    }
}
