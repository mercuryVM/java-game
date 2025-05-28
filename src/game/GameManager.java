package game;

import background.Background;
import entities.Entity;
import entities.enemies.Enemy;
import entities.enemies.EnemyA;
import entities.enemies.EnemyB;
import entities.player.Player;
import entities.projectile.EnemyProjectile;
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
    public List<Enemy> enemies = new ArrayList<Enemy>();
    public List<Projectile> projectiles = new ArrayList<>();
    public List<Background> backgrounds = new ArrayList<>();
    private final Input input;
    private final LinkedList<Entity> removalList = new LinkedList<Entity>();
    private final SpawnManager spawnManager = new SpawnManager(this);
    private int currentScene = 0;
    private final GameConfig gameConfig;

    public Scene getCurrentScene() {
        return gameConfig.sceneList.get(currentScene);
    }

    public GameManager(GameConfig gameConfig) {
        this.gameConfig = gameConfig;

        backgrounds.add(
                new Background(20, 0.070f, Color.DARK_GRAY, 2)
        );

        backgrounds.add(
                new Background(50, 0.045f, Color.GRAY, 3)
        );

        player = new Player(this);

        input = new Input(player);

        player.setActive();

        player.velocity = new Vector2(
                0.25f,
                0.25f
        );

        player.position = new Vector2(
                (float)GameLib.WIDTH / 2,
                (float)GameLib.HEIGHT * 0.90f
        );
    }

    private void UpdateEntities(List<?> entities, float deltaTime, long currentTime) {
        for(var e : entities) {
            var entity = (Entity)e;
            entity.Update(deltaTime, currentTime);

            if(entity.OutOfBounds()) {
                removalList.add(entity);
            }
        }
    }

    public void OnEnemy2Dispsoed() {
        spawnManager.OnEnemy2Disposed();
    }

    public void Update(float deltaTime, long currentTime) {
        Time.time = currentTime;
        Time.deltaTime = deltaTime;

        input.Process(deltaTime, currentTime);

        player.Update(deltaTime, currentTime);

        this.UpdateEntities(projectiles, deltaTime, currentTime);

        for(var removal : removalList) {
            projectiles.remove(removal);
            removal.Dispose();
        }
        removalList.clear();

        this.UpdateEntities(enemies, deltaTime, currentTime);

        for(var removal : removalList) {
            enemies.remove(removal);
            removal.Dispose();
        }
        removalList.clear();

        spawnManager.Update(deltaTime, currentTime);

        CollisionUpdate(deltaTime, currentTime);
    }

    private void CollisionUpdate(float deltaTime, long currentTime) {
            for(var e1 : enemies) {
                if(player.isActive() && player.collider.TestCollision(e1)) {
                    player.setDead();
                    break;
                }
            }

            for(var p1 : projectiles) {
                if(player.isActive() && player.collider.TestCollision(p1) && p1.sender != player) {
                    player.setDead();
                    break;
                }
            }
    }

    public void Render(float deltaTime, long currentTime) {
        for(var background : backgrounds) {
            background.Render(deltaTime, currentTime);
        }

        player.Render(deltaTime, currentTime);

        for(var projectile : projectiles) {
            projectile.Render(deltaTime, currentTime);
        }

        for(var enemy : enemies) {
            enemy.Render(deltaTime, currentTime);
        }

        GameLib.display();
    }

    public void SpawnEnemyA() {
        var enemy = new EnemyA(this);
        enemy.position = new Vector2(
                (float)(Math.random() * (GameLib.WIDTH - 20.0) + 10.0),
                -10.0f
        );
        enemy.velocity = new Vector2(
                0.20f + (float)Math.random() * 0.15f,
                0.20f + (float)Math.random() * 0.15f
        );
        enemy.angle = (3f * (float)Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;

        enemies.add(enemy);
    }

    public void SpawnEnemyB() {
        var enemy = new EnemyB(this);
        enemy.position = new Vector2(
                (float)(GameLib.WIDTH * 0.20),
                -10.0f
        );
        enemy.velocity = new Vector2(
                0.42f,
                0.42f
        );
        enemy.angle = (3f * (float)Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;

        enemies.add(enemy);
    }

    public void AddProjectile(Vector2 position, Vector2 velocity, float radius, Class<? extends Projectile> projectileClass, Entity sender) {
        try {
            Constructor<? extends Projectile> constructor = projectileClass.getConstructor(
                    GameManager.class, Vector2.class, Vector2.class, float.class, Entity.class
            );

            Projectile projectile = constructor.newInstance(
                    this, position, velocity, radius, sender
            );

           // projectile.angle = -(3f * (float)Math.PI) / 2f;

            projectiles.add(projectile);
        } catch (Exception e) {
            e.printStackTrace(); // Você pode lidar com erro de outra forma
        }
    }
}
