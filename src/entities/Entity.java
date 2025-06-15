package entities;

import collision.Collider;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;
import time.Time;

public class Entity {
    protected final GameManager gameManager;
    protected float currentHealth = 1.0f;

    public Entity(GameManager manager) {
        this.gameManager = manager;
    }

    public enum State {
        INACTIVE,
        ACTIVE,
        EXPLODING,
        INVINCIBLE
    }

    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    public float angle = 0;
    public double rotationVelocity = 0;
    public float radius = 9.0F;
    protected State state = State.INACTIVE;
    public Collider collider = new Collider(this);

    private long deadStart = 0, deadEnd = 0;

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void Update(float deltaTime, long currentTime) {
        if(state == State.ACTIVE) {
            angle += (float) (rotationVelocity * deltaTime);
            position.x += (float) (velocity.x * Math.cos(angle) * deltaTime);
            position.y -= (float) (velocity.y * Math.sin(angle) * deltaTime);
        }

        if(state == State.EXPLODING && isNotDead()) {
            Respawn();
        }
    }

    protected void Respawn() {
        setActive();
    }

    public boolean ApplyDamage(float damage) {
        if(state == State.INVINCIBLE) return false;

        damage = Math.abs(damage);
        if(this.currentHealth - damage <= 0) {
            this.currentHealth = 0f;
            this.setDead();
        }else {
            this.currentHealth -= damage;
        }
        return true;
    }

    public boolean OutOfBounds() {
        return position.y > GameLib.HEIGHT || position.y <= 0;
    }

    public boolean Render(float deltaTime, long currentTime) {
        if(state == State.EXPLODING && currentTime < deadEnd) {
            double alpha = (double) (currentTime - deadStart) / (deadEnd - deadStart);
            if(alpha >= 0 && alpha <= 1)
                GameLib.drawExplosion(position.x, position.y, alpha);
            return true;
        }

        return state == State.INACTIVE;
    }

    public void Dispose() {

    }

    public boolean isActive() {
        return state == State.ACTIVE || state == State.INVINCIBLE;
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
