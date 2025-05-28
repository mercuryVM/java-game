package entities.enemies;

import game.GameManager;
import libraries.GameLib;
import math.Vector2;

import java.awt.*;

public class EnemyB extends Enemy {
    public EnemyB(GameManager manager) {
        super(manager);
    }

    @Override
    public void Dispose() {
        gameManager.OnEnemy2Dispsoed();
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        boolean shootNow = false;

        float previousY = position.y;

        super.Update(deltaTime, currentTime);

        double threshold = GameLib.HEIGHT * 0.30f;

        if(previousY < threshold && position.y >= threshold) {
            if(position.x < GameLib.WIDTH / 2f) rotationVelocity = 0.003f;
            else rotationVelocity = -0.003f;
        }
        if(rotationVelocity > 0f && Math.abs(angle - 3f * Math.PI) < 0.05){

            rotationVelocity = 0.0f;
            angle = 3f * (float)Math.PI;
            shootNow = true;
        }

        if(rotationVelocity < 0f && Math.abs(angle) < 0.05f){

            rotationVelocity = 0.0f;
            angle = 0.0f;
            shootNow = true;
        }

        if (shootNow) {
            float[] angles = {
                    (float)Math.PI / 2f + (float)Math.PI / 8f,
                    (float)Math.PI / 2f,
                    (float)Math.PI / 2f - (float)Math.PI / 8f
            };

            for (float v : angles) {
                double a = v + Math.random() * Math.PI/6 - Math.PI/12;
                double vx = Math.cos(a) * 0.3;
                double vy = Math.sin(a) * 0.3;

                Shoot(currentTime, new Vector2((float)vx, (float)-vy));
            }
        }
    }

    protected void Shoot(long currentTime, Vector2 direction) {
        AddProjectile(direction);

        nextShoot = currentTime + getShootInterval();
    }

    protected void AddProjectile(Vector2 shootDirection) {
        gameManager.AddProjectile(
                this.position.copy(),
                new Vector2(shootDirection.x, shootDirection.y),
                2.0f,
                ProjectileClass(),
                this
        );
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        if(super.Render(deltaTime, currentTime)) return true;
        GameLib.setColor(Color.RED);
        GameLib.drawCircle(position.x, position.y, radius);
        return false;
    }
}
