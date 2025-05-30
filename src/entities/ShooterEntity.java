package entities;

import entities.projectile.Projectile;
import game.GameManager;
import math.Vector2;

public abstract class ShooterEntity extends Entity {
    protected long nextShoot = 0;
    private int bulletsPerShoot = 1;

    public void setBulletsPerShoot(int bulletsPerShoot) {
        this.bulletsPerShoot = bulletsPerShoot;
    }

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
                new Vector2((float)Math.random() * 0.02f, 1.0f),
                2.0f,
                ProjectileClass(),
                this
        );
    }

    protected long getShootInterval() {
        return 100 / bulletsPerShoot;
    }
}
