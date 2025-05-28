package entities.enemies;

import entities.ShooterEntity;
import entities.projectile.EnemyProjectile;
import entities.projectile.Projectile;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;

public class Enemy extends ShooterEntity {
    public Enemy(GameManager manager) {
        super(manager);
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        super.Update(deltaTime, currentTime);
    }

    public boolean OutOfBounds() {
        return position.y > GameLib.HEIGHT + 30 || position.y <= -30
                || position.x > GameLib.WIDTH + 30 || position.x <= -30
                ;
    }

    @Override
    protected void AddProjectile() {
        gameManager.AddProjectile(
                this.position.copy(),
                new Vector2(0.0f, -0.45f),
                2.0f,
                ProjectileClass(),
                this
        );
    }

    @Override
    protected long getShootInterval() {
        return (long)(200 + Math.random() * 500);
    }

    @Override
    public Class<? extends Projectile> ProjectileClass() {
        return EnemyProjectile.class;
    }
}
