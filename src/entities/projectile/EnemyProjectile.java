package entities.projectile;

import collision.Collider;
import entities.Entity;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;

import java.awt.*;

public class EnemyProjectile extends Projectile {
    public EnemyProjectile(GameManager manager, Vector2 position, Vector2 velocity, float radius, Entity sender) {
        super(manager, position, velocity, radius, sender);
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        GameLib.setColor(Color.RED);
        GameLib.drawCircle(this.position.x, this.position.y, this.radius);
        return false;
    }
}