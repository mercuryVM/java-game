package entities.enemies.boss;

import entities.Laser;
import game.GameManager;
import libraries.GameLib;

import java.awt.*;

public class BossB extends Boss {
    private final float radius = 40.0f;
    private float angle;
    private final float margin;
    private final float topMargin;
    private Laser laser;
    private final GameManager manager;

    public BossB(GameManager manager, int bossHealth) {
        super(manager, bossHealth);
        this.margin = radius + 10f;
        this.topMargin = margin + 22f;
        this.angle = 0f;
        this.manager = manager;

        position.x = margin;
        position.y = topMargin;

        InitializeLaser();
    }

    private void InitializeLaser() {
        float laserRotationSpeed = 0.005f;
        float laserLength = radius * 10.0f;

        manager.AddLaser(
                position.copy(),
                laserRotationSpeed,
                laserLength,
                Laser.class,
                this
        );
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        super.Update(deltaTime, currentTime);

        if(state == State.ACTIVE) {
            float rotationSpeed = 0.001f;
            angle += rotationSpeed * deltaTime;
            angle %= (2f * (float)Math.PI);

            float effectiveWidth = GameLib.WIDTH - 2f * margin;
            float effectiveHeight = GameLib.HEIGHT - margin - topMargin;

            if (angle < Math.PI / 2f) {
                position.x = margin + (angle / ((float)Math.PI / 2f)) * effectiveWidth;
                position.y = topMargin;
            }
            else if (angle < Math.PI) {
                position.x = GameLib.WIDTH - margin;
                position.y = topMargin + ((angle - (float)Math.PI/2f) / ((float)Math.PI/2f)) * effectiveHeight;
            }
            else if (angle < 3f * (float)Math.PI / 2f) {
                position.x = GameLib.WIDTH - margin - ((angle - (float)Math.PI) / ((float)Math.PI/2f)) * effectiveWidth;
                position.y = GameLib.HEIGHT - margin;
            }
            else {
                position.x = margin;
                position.y = GameLib.HEIGHT - margin - ((angle - 3f * (float)Math.PI/2f) / ((float)Math.PI/2f)) * effectiveHeight;
            }

            if(laser != null) {
                laser.position = position.copy();
            }
        }
        else if(state == State.EXPLODING) {
            if(laser != null) {
                manager.RemoveLaser(laser);
                laser = null;
            }

            if(isNotDead()) {
                Respawn();
                InitializeLaser();
            }
        }
        else if(state == State.INACTIVE) {
            if(laser != null) {
                manager.RemoveLaser(laser);
                laser = null;
            }
        }
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        if(super.Render(deltaTime, currentTime)) return true;
        GameLib.setColor(Color.orange);
        GameLib.drawCircle(position.x, position.y, radius);

        return false;
    }

    @Override
    public void Dispose() {
        if(laser != null) {
            manager.RemoveLaser(laser);
            laser = null;
        }
        super.Dispose();
    }

    public float getRadius() {
        return this.radius;
    }

    public Laser getLaser() {
        return this.laser;
    }

    public void setLaser(Laser laser) {
        this.laser = laser;
    }
}