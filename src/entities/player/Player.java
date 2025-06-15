package entities.player;

import entities.ShooterEntity;
import entities.player.modifiers.PlayerModifier;
import entities.player.modifiers.ModifierList;
import entities.player.modifiers.entry.PlayerModifierEntry;
import entities.projectile.PlayerProjectile;
import entities.projectile.Projectile;
import game.GameManager;
import libraries.GameLib;
import math.Vector2;
import time.Time;

import java.awt.*;
import java.util.Collections;

public class Player extends ShooterEntity {
    private float defaultHealth = 0.0f;
    private final ModifierList<PlayerModifierEntry> currentModifiers = new ModifierList<PlayerModifierEntry>();

    public Player(GameManager manager, float defaultHealth ) {
        super(manager);
        this.radius = 12;
        this.defaultHealth = defaultHealth;
    }

    @Override
    public void Respawn() {
        this.SetSpawn();
        this.setActive();
    }

    public void SetSpawn() {
        this.currentHealth = defaultHealth;
        position = new Vector2((float) GameLib.WIDTH / 2, (float) GameLib.HEIGHT * 0.90f);
    }

    public void GiveHealth(float health) {
        health = Math.abs(health);
        this.currentHealth += health;
    }

    public void AddModifier(PlayerModifier modifier) {
        var entry = new PlayerModifierEntry(
                this,
                modifier,
                Time.time
        );

        currentModifiers.add(entry);
    }

    public java.util.List<PlayerModifierEntry> getModifiers() {
        return (java.util.List<PlayerModifierEntry>) Collections.unmodifiableList(
                currentModifiers.getEntities()
        );
    }

    private float invincibilityUntil = 0f;

    public void MakeInvincible(float invincibilityUntil) {
        if(state == State.ACTIVE)
        {
            state = State.INVINCIBLE;
            this.invincibilityUntil = invincibilityUntil;
        }
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        if(super.Render(deltaTime, currentTime)) return true;

        if(state == State.INVINCIBLE) {
            GameLib.setColor(
                    new Color(
                            1,
                            1,
                            0,
                            (float)Math.abs(Math.sin((Time.getTimeFromStart() / 1000f) * 2f)) * 0.75f + 0.25f
                    )
            );
        }else {
            GameLib.setColor(Color.BLUE);
        }

        GameLib.drawPlayer(position.x, position.y, radius);
        return false;
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        if(state == State.INVINCIBLE && currentTime >= invincibilityUntil) {
            state = State.ACTIVE;
        }

        if(state == State.EXPLODING && isNotDead()) {
            gameManager.HandlePlayerDeath();
        }

        currentModifiers.update(deltaTime, currentTime);
    }

    public void setInactive() {
        state = State.INACTIVE;
    }

    @Override
    public Class<? extends Projectile> ProjectileClass() {
        return PlayerProjectile.class;
    }
}
