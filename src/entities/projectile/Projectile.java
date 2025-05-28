package entities.projectile;

import collision.Collider;
import entities.Entity;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;

import java.awt.*;

public class Projectile extends Entity {
    private final Collider collider = new Collider(this);
    public final Entity sender;

    public Projectile(GameManager manager, Vector2 position, Vector2 velocity, float radius, Entity sender) {
        super(manager);
        this.position = position;
        this.radius = radius;
        this.velocity = velocity;
        this.sender = sender;
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        angle += (float) (rotationVelocity * Math.cos(angle) * deltaTime);
        position.x += (float) (velocity.x * deltaTime);
        position.y -= (float) (velocity.y * deltaTime);
    }
}
