package game;

import background.Background;
import entities.Entity;
import entities.enemies.Enemy;
import entities.enemies.EnemyA;
import entities.enemies.EnemyB;
import entities.enemies.boss.Boss;
import entities.enemies.boss.BossA;
import entities.player.Player;
import entities.powerups.HealthPowerup;
import entities.powerups.Powerup;
import entities.projectile.Projectile;
import input.Input;
import libraries.GameLib;
import math.Vector2;
import scene.Scene;
import scene.config.GameConfig;
import time.Time;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameManager {
    public final Player player;
    public final EntityList<Enemy> enemies = new EntityList<>();
    public final EntityList<Projectile> projectiles = new EntityList<>();
    public final EntityList<Powerup> powerups = new EntityList<>();
    public final List<Background> backgrounds = new ArrayList<>();
    private final Input input;
    private final SpawnManager spawnManager = new SpawnManager(this);
    private int currentScene = 0;
    private final GameConfig gameConfig;
    private int currentScore = 0;

    public int getCurrentScore() {
        return currentScore;
    }

    public Scene getCurrentScene() {
        return gameConfig.sceneList.get(currentScene);
    }

    public float getCurrentHealth() {
        return player.getCurrentHealth();
    }

    public GameManager(GameConfig gameConfig) {
        this.gameConfig = gameConfig;

        backgrounds.add(new Background(20, 0.070f, Color.DARK_GRAY, 2));
        backgrounds.add(new Background(50, 0.045f, Color.GRAY, 3));
        backgrounds.add(new Background(100, 0.025f, new Color(0.1f, 0, 0.3f), 2));

        player = new Player(this, gameConfig.playerHealth);
        input = new Input(player);
        player.setActive();
        player.velocity = new Vector2(0.25f, 0.25f);
        player.SetSpawn();
    }

    public void OnEnemy2Dispsoed() {
        spawnManager.OnEnemy2Disposed();
    }

    public void Update(float deltaTime, long currentTime) {
        Time.time = currentTime;
        Time.deltaTime = deltaTime;

        input.Process(deltaTime, currentTime);
        player.Update(deltaTime, currentTime);

        enemies.update(deltaTime, currentTime);
        projectiles.update(deltaTime, currentTime);
        powerups.update(deltaTime, currentTime);

        spawnManager.Update(deltaTime, currentTime);
        CollisionUpdate(deltaTime, currentTime);
    }

    private void CollisionUpdate(float deltaTime, long currentTime) {
        for (var enemy : enemies.getEntities()) {
            if (player.isActive() && player.collider.TestCollision(enemy)) {
                if(player.ApplyDamage(
                        (float)Math.random() * 50.0f
                )) {
                    ui.ApplyDamage();
                }

                break;
            }
        }

        for (var p : powerups.getEntities()) {
            if (player.isActive() && player.collider.TestCollision(p)) {
                powerups.scheduleRemoval(p);
                p.acquire(player);

                break;
            }
        }

        for (var projectile : projectiles.getEntities()) {
            if (player.isActive() && player.collider.TestCollision(projectile) && projectile.sender != player) {
                if(player.ApplyDamage(
                        (float)Math.random() * 50.0f
                )) {
                    ui.ApplyDamage();
                }
                projectiles.scheduleRemoval(projectile);
            }

            for (var enemy : enemies.getEntities()) {
                if (enemy.isActive() && enemy.collider.TestCollision(projectile) && projectile.sender == player) {
                    enemy.ApplyDamage(50);
                    currentScore++;
                    projectiles.scheduleRemoval(projectile);
                }
            }
        }
    }

    private final UI ui = new UI(this);

    public void Render(float deltaTime, long currentTime) {
        for (var background : backgrounds) {
            background.Render(deltaTime, currentTime);
        }

        player.Render(deltaTime, currentTime);
        projectiles.render(deltaTime, currentTime);
        enemies.render(deltaTime, currentTime);
        powerups.render(deltaTime, currentTime);

        ui.RenderUI(deltaTime, currentTime);

        GameLib.display();
    }

    public void SpawnEnemyA() {
        var enemy = new EnemyA(this);
        enemy.setActive();
        enemy.position = new Vector2((float) (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0f);
        enemy.velocity = new Vector2(0.20f + (float) Math.random() * 0.15f, 0.20f + (float) Math.random() * 0.15f);
        enemy.angle = (3f * (float) Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;

        enemies.add(enemy);
    }

    public void SpawnEnemyB() {
        var enemy = new EnemyB(this);
        enemy.position = new Vector2((float) (GameLib.WIDTH * 0.20), -10.0f);
        enemy.velocity = new Vector2(0.42f, 0.42f);
        enemy.angle = (3f * (float) Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;
        enemy.setActive();

        enemies.add(enemy);
    }

    public void SpawnPowerup() {
        var powerup = new HealthPowerup(this);
        powerup.setActive();
        powerup.position = new Vector2((float) (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0f);
        powerup.velocity = new Vector2(0.05f, 0.05f);
        powerup.angle = (3f * (float) Math.PI) / 2f;
        powerup.rotationVelocity = 0.0f;

        powerups.add(powerup);
    }

    private int playerLives = 3;

    public int getPlayerLives() {
        return playerLives;
    }

    public void HandlePlayerDeath() {
        if(playerLives == 0) {
            player.setInactive();
            ui.GameOver();
            spawnManager.GameOver();
            return;
        }
        playerLives--;
        player.Respawn();
        spawnManager.ResetRound();
    }

    public void SpawnBossA() {
        var boss = new BossA(this, 1000);
        boss.position = new Vector2((float) (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), 50.0f);
        //boss.velocity = new Vector2(0.05f, 0.05f);
        boss.angle = (3f * (float) Math.PI) / 2f;
        boss.rotationVelocity = 0.0f;
        boss.setActive();

        System.out.println("aaa");
        enemies.add(boss);
    }


    public void AddProjectile(Vector2 position, Vector2 velocity, float radius, Class<? extends Projectile> projectileClass, Entity sender) {
        try {
            Constructor<? extends Projectile> constructor = projectileClass.getConstructor(
                    GameManager.class, Vector2.class, Vector2.class, float.class, Entity.class
            );

            Projectile projectile = constructor.newInstance(this, position, velocity, radius, sender);
            projectiles.add(projectile);
        } catch (Exception e) {
            e.printStackTrace(); // Pode melhorar tratamento depois
        }
    }

    public void RemoveEnemy(Enemy enemy) {
        enemies.scheduleRemoval(enemy);
    }

    public void RemoveProjectile(Projectile projectile) {
        projectiles.scheduleRemoval(projectile);
    }
}
