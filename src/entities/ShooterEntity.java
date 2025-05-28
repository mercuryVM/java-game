package entities;

import entities.projectile.Projectile;
import game.GameManager;
import math.Vector2;

public abstract class ShooterEntity extends Entity {
    protected long nextShoot = 0;

    public ShooterEntity(GameManager manager) {
        super(manager);
    }

    public Class<? extends Projectile> ProjectileClass() {
        return Projectile.class;
    }

    public void Shoot(long currentTime) {
        if(currentTime < nextShoot) return;

        AddProjectile();

        nextShoot = currentTime + getShootInterval();
    }

    protected void AddProjectile() {
        gameManager.AddProjectile(
                this.position.copy(),
                new Vector2(0.0f, 1.0f),
                2.0f,
                ProjectileClass()
        );
    }

    protected long getShootInterval() {
        return 100;
    }
}
