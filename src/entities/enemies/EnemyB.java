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

        position.x += (float) (velocity.x * Math.cos(this.angle) * deltaTime);
        position.y += (float) (velocity.y * Math.sin(this.angle) * deltaTime * (-1.0f));
        this.angle += (float) (this.rotationVelocity * deltaTime);

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
                float randomOffset = ((float)Math.random() - 0.5f) * ((float)Math.PI / 6f); // -π/12 a π/12
                float a = v + randomOffset;

                float vx = (float)Math.cos(a);
                float vy = (float)Math.sin(a);

                Vector2 direction = new Vector2(-vx * 0.3f, -vy * 0.3f);

                Shoot(currentTime, direction);
            }
        }

    }

    protected void Shoot(long currentTime, Vector2 direction) {
        if(currentTime < nextShoot) return;

        AddProjectile(direction);

        nextShoot = currentTime + getShootInterval();
    }

    protected void AddProjectile(Vector2 shootDirection) {
        gameManager.AddProjectile(
                this.position.copy(),
                new Vector2(shootDirection.x, shootDirection.y),
                2.0f,
                ProjectileClass()
        );
    }

    @Override
    public void Render(float deltaTime, long currentTime) {
        GameLib.setColor(Color.RED);
        GameLib.drawCircle(position.x, position.y, radius);
    }
}
