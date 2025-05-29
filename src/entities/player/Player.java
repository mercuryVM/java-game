package entities.player;

import entities.ShooterEntity;
import entities.projectile.PlayerProjectile;
import entities.projectile.Projectile;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;

import java.awt.*;

public class Player extends ShooterEntity {
    public Player(GameManager manager) {
        super(manager);
        this.radius = 12;
    }

    @Override
    protected void Respawn() {
        this.SetSpawn();
    }

    public void SetSpawn() {
        position = new Vector2((float) GameLib.WIDTH / 2, (float) GameLib.HEIGHT * 0.90f);
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        if(super.Render(deltaTime, currentTime)) return true;
        GameLib.setColor(Color.BLUE);
        GameLib.drawPlayer(position.x, position.y, radius);
        return false;
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        if(state == State.EXPLODING && isNotDead()) {
            setActive();
        }
    }

    @Override
    public Class<? extends Projectile> ProjectileClass() {
        return PlayerProjectile.class;
    }
}
