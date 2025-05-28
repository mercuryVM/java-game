package entities.projectile;

import entities.Entity;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;

import java.awt.*;

public class PlayerProjectile extends Projectile {
    public PlayerProjectile(GameManager manager, Vector2 position, Vector2 velocity, float radius, Entity sender) {
        super(manager, position, velocity, radius, sender);
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        GameLib.setColor(Color.GREEN);
        GameLib.drawLine(position.x, position.y - 5, position.x, position.y + 5);
        GameLib.drawLine(position.x - 1, position.y - 3, position.x - 1, position.y + 3);
        GameLib.drawLine(position.x + 1, position.y - 3, position.x + 1, position.y + 3);
        return false;
    }
}
