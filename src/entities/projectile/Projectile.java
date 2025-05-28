package entities.projectile;

import collision.Collider;
import entities.Entity;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;

import java.awt.*;

public class Projectile extends Entity {
    private final Collider collider = new Collider(this);

    public Projectile(GameManager manager, Vector2 position, Vector2 velocity, float radius) {
        super(manager);
        this.position = position;
        this.radius = radius;
        this.velocity = velocity;
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        super.Update(deltaTime, currentTime);
    }

    @Override
    public void Render(float deltaTime, long currentTime) {
        GameLib.setColor(Color.GREEN);
        GameLib.drawLine(position.x, position.y - 5, position.x, position.y + 5);
        GameLib.drawLine(position.x - 1, position.y - 3, position.x - 1, position.y + 3);
        GameLib.drawLine(position.x + 1, position.y - 3, position.x + 1, position.y + 3);
    }
}
