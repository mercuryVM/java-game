package entities;

import game.GameManager;
import libraries.GameLib;
import math.Vector2;

import java.awt.*;

public class Entity {
    protected final GameManager gameManager;

    public Entity(GameManager manager) {
        this.gameManager = manager;
    }

    public enum State {
        INACTIVE,
        ACTIVE,
        EXPLODING
    }

    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    public float angle = 0;
    public double rotationVelocity = 0;
    public float radius = 9.0F;
    private State state = State.INACTIVE;

    public void Update(float deltaTime, long currentTime) {
        angle += (float) (rotationVelocity * deltaTime);
        position.x += (float) (velocity.x * deltaTime);
        position.y -= (float) (velocity.y * deltaTime);
    }

    public boolean OutOfBounds() {
        return position.y > GameLib.HEIGHT || position.y <= 0;
    }

    public void Render(float deltaTime, long currentTime) {

    }

    public void Dispose() {

    }

    public boolean isActive() {
        return state == State.ACTIVE;
    }

    public void setActive() {
        this.state = State.ACTIVE;
    }
}
