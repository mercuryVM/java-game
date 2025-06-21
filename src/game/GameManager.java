package game;

import background.Background;
import entities.Entity;
import entities.Laser;
import entities.enemies.Enemy;
import entities.enemies.boss.Boss;
import entities.enemies.boss.BossB;
import entities.player.Player;
import entities.powerups.Powerup;
import entities.projectile.Projectile;
import input.Input;
import libraries.GameLib;
import math.Vector2;
import scene.Scene;
import scene.config.BackgroundConfig;
import scene.config.GameConfig;
import time.Time;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    public final Player player;
    public final UI ui = new UI(this);
    public final EntityList<Enemy> enemies = new EntityList<>();
    public final EntityList<Projectile> projectiles = new EntityList<>();
    public final EntityList<Laser> lasers = new EntityList<>();
    public final EntityList<Powerup> powerups = new EntityList<>();
    public final List<Background> backgrounds = new ArrayList<>();
    public final SpawnManager spawnManager;
    public final SceneManager sceneManager;
    public Scene currentScene = null;
    public int currentGameMode;         // 0 = fases; 1 = infinito
    public final GameConfig gameConfig;
    private int currentScore = 0;
    private final Input input;

    public GameManager(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        
        player = new Player(this, gameConfig.playerHealth);
        player.setActive();
        player.velocity = new Vector2(0.25f, 0.25f);
        player.SetSpawn();

        if(gameConfig.numberOfScenes > 0)   // se na config do jogo tem fases, inicia o jogo em fases
            this.currentGameMode = 0;
        else
            this.currentGameMode = 1;       // se nao tem, inicia infinito
        
        input = new Input(player);

        spawnManager = new SpawnManager(this);
        sceneManager = new SceneManager(this);
    }

    public void RenderBackground(){
        this.backgrounds.clear();
        for(int i = 0; i < this.currentScene.backgroundsConfig.size(); i++){
            BackgroundConfig bgc = this.currentScene.backgroundsConfig.get(i);
            this.backgrounds.add(new Background(bgc.getNumberOfStars(), bgc.getStarsSpeed(), bgc.getStarsColor(), bgc.getStarsSize()));
        }
    }

    public float getCurrentBossHealth(){
        for (var e : enemies.getEntities()) {
            if(e instanceof Boss) {
                return e.getCurrentHealth();
            }
        }
        return 0f;
    }

    public float getBossHealth(){
        for (var e : enemies.getEntities()) {
            if(e instanceof Boss) {
                return ((Boss) e).getInitialHealth();
            }
        }
        return 0f;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }

    public Scene getCurrentScene() {
        return this.currentScene;
    }

    public float getCurrentHealth() {
        return player.getCurrentHealth();
    }

    public void OnEnemy2Dispsoed() {
        spawnManager.OnEnemy2Disposed();
    }


    public void Update(float deltaTime, long currentTime) {
        Time.time = currentTime;
        Time.deltaTime = deltaTime;

        sceneManager.UpdateSceneAndGameMode();

        input.Process(deltaTime, currentTime);
        player.Update(deltaTime, currentTime);

        enemies.update(deltaTime, currentTime);
        projectiles.update(deltaTime, currentTime);
        lasers.update(deltaTime, currentTime);
        powerups.update(deltaTime, currentTime);

        spawnManager.Update(deltaTime, currentTime);
        CollisionUpdate(deltaTime, currentTime);
    }

    public void Render(float deltaTime, long currentTime) {
        for (var background : backgrounds) {
            background.Render(deltaTime, currentTime);
        }

        player.Render(deltaTime, currentTime);
        projectiles.render(deltaTime, currentTime);
        lasers.render(deltaTime, currentTime);
        enemies.render(deltaTime, currentTime);
        powerups.render(deltaTime, currentTime);

        ui.RenderUI(deltaTime, currentTime);

        GameLib.display();
    }

    private void CollisionUpdate(float deltaTime, long currentTime) {
        //colisão física
        for (var enemy : enemies.getEntities()) {
            float extraRadius = 0.0f;
            if(enemy instanceof Boss) {
                extraRadius = ((Boss) enemy).getRadius();
            }
            if (player.isActive() && player.collider.TestCollision(enemy, extraRadius)) {
                if(player.ApplyDamage(
                        (float)Math.random() * 50.0f
                )) {
                    ui.ApplyDamage();
                }

                break;
            }
        }

        for (var p : powerups.getEntities()) {
            if (player.isActive() && player.collider.TestCollision(p, 0.0f)) {
                powerups.scheduleRemoval(p);
                p.acquire(player);

                break;
            }
        }

        for (var laser : lasers.getEntities()) {
            if (player.isActive() && player.collider.TestLaserCollision(laser)) {
                if(player.ApplyDamage(
                        (float)Math.random() * 50.0f
                )) {
                    ui.ApplyDamage();
                }
            }
        }

        for (var projectile : projectiles.getEntities()) {
            if (player.isActive() && player.collider.TestCollision(projectile, 0.0f) && projectile.sender != player) {
                if(player.ApplyDamage(
                        (float)Math.random() * 50.0f
                )) {
                    ui.ApplyDamage();
                }
                projectiles.scheduleRemoval(projectile);
            }

            for (var enemy : enemies.getEntities()) {
                float extraRadius = 0.0f;
                if(enemy instanceof Boss) {
                    extraRadius = ((Boss) enemy).getRadius();
                }
                if (enemy.isActive() && enemy.collider.TestCollision(projectile, extraRadius) && projectile.sender == player) {
                    enemy.ApplyDamage(50);
                    currentScore++;
                    projectiles.scheduleRemoval(projectile);
                }
            }
        }
    }

    private int playerLives = 3;

    public int getPlayerLives() {
        return playerLives;
    }

    public void HandlePlayerDeath() {
        if (playerLives == 0) {
            player.setInactive();
            ui.GameOver();
            spawnManager.GameOver();
            return;
        }
        playerLives--;
        player.Respawn();
        spawnManager.ResetRound();
    }

    public void AddProjectile(Vector2 position, Vector2 velocity, float radius,
            Class<? extends Projectile> projectileClass, Entity sender) {
        try {
            Constructor<? extends Projectile> constructor = projectileClass.getConstructor(
                    GameManager.class, Vector2.class, Vector2.class, float.class, Entity.class);

            Projectile projectile = constructor.newInstance(this, position, velocity, radius, sender);
            projectiles.add(projectile);
        } catch (Exception e) {
            e.printStackTrace(); // Pode melhorar tratamento depois
        }
    }

    public void AddLaser(Vector2 position, float rotationSpeed, float length,
                         Class<? extends Laser> laserClass, Entity sender) {
        try {
            Constructor<? extends Laser> constructor = laserClass.getConstructor(
                    GameManager.class, Vector2.class, float.class, float.class, Entity.class
            );

            Laser laser = constructor.newInstance(this, position, rotationSpeed, length, sender);
            lasers.add(laser);

            // Se for um laser de boss, conecta ao boss
            if(sender instanceof BossB) {
                ((BossB)sender).setLaser(laser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RemoveEnemy(Enemy enemy) {
        enemies.scheduleRemoval(enemy);
        if (enemy instanceof Boss) {
            ui.setIsBoss(false);
            spawnManager.spawnNewEnemies = true;

            if(this.currentScene != null)
                this.currentScene.bossDied();

            if (enemy instanceof BossB) {
                Laser bossLaser = ((BossB) enemy).getLaser();
                if (bossLaser != null) {
                    lasers.scheduleRemoval(bossLaser);
                }
            }
        }
    }

    public void RemoveLaser(Laser laser) {
        lasers.scheduleRemoval(laser);
    }

    public void RemoveProjectile(Projectile projectile) {
        projectiles.scheduleRemoval(projectile);
    }
}
