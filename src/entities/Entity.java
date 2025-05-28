package entities;

import collision.Collider;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;
import time.Time;

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
    protected State state = State.INACTIVE;
    public Collider collider = new Collider(this);

    private long deadStart = 0, deadEnd = 0;

    public void Update(float deltaTime, long currentTime) {
        angle += (float) (rotationVelocity * deltaTime);
        position.x += (float) (velocity.x * Math.cos(angle) * deltaTime);
        position.y -= (float) (velocity.y * Math.sin(angle) * deltaTime);

        if(state == State.EXPLODING && isNotDead()) {
            setActive();
        }
    }

    public boolean OutOfBounds() {
        return position.y > GameLib.HEIGHT || position.y <= 0;
    }

    public boolean Render(float deltaTime, long currentTime) {
        if(state == State.EXPLODING && currentTime < deadEnd) {
            double alpha = (double) (currentTime - deadStart) / (deadEnd - deadStart);
            GameLib.drawExplosion(position.x, position.y, alpha);
            return true;
        }
        return false;
    }

    public void Dispose() {

    }

    public boolean isActive() {
        return state == State.ACTIVE;
    }

    public void setActive() {
        this.state = State.ACTIVE;
        deadEnd = 0;
        deadStart = 0;
    }

    public boolean isNotDead() {
        return Time.time > deadEnd;
    }

    public void setDead() {
        this.state = State.EXPLODING;
        deadEnd = Time.time + 2000;
        deadStart = Time.time;
    }
}
