package game;

import background.Background;
import entities.Entity;
import entities.Laser;
import entities.enemies.Enemy;
import entities.enemies.EnemyA;
import entities.enemies.EnemyB;
import entities.enemies.boss.Boss;
import entities.enemies.boss.BossA;
import entities.enemies.boss.BossB;
import entities.player.Player;
import entities.powerups.HealthPowerup;
import entities.powerups.Powerup;
import entities.projectile.Projectile;
import input.Input;
import libraries.GameLib;
import math.Vector2;
import scene.Scene;
import scene.config.BackgroundConfig;
import scene.config.GameConfig;
import scene.config.PowerupConfig;
import time.Time;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class GameManager {
    public final Player player;
    private final UI ui = new UI(this);
    public final EntityList<Enemy> enemies = new EntityList<>();
    public final EntityList<Projectile> projectiles = new EntityList<>();
    public final EntityList<Laser> lasers = new EntityList<>();
    public final EntityList<Powerup> powerups = new EntityList<>();
    public final List<Background> backgrounds = new ArrayList<>();
    public int currentGameMode;     // 0 = fases; 1 = infinito
    private final Input input;
    private final SpawnManager spawnManager;
    private final GameConfig gameConfig;
    public Scene currentScene = null;
    private int currentScore = 0;

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
    }

    public void RenderBackground(){
        this.backgrounds.clear();
        for(int i = 0; i < this.currentScene.backgroundsConfig.size(); i++){
            BackgroundConfig bgc = this.currentScene.backgroundsConfig.get(i);
            this.backgrounds.add(new Background(bgc.getNumberOfStars(), bgc.getStarsSpeed(), bgc.getStarsColor(), bgc.getStarsSize()));
        }
    }

    public void LoadScene(int newSceneIndex, long currentTime){
        try{
            if(newSceneIndex >= 0){         // com cena dos configs (modo de jogo levels)
                String newSceneFile = gameConfig.sceneConfigs.get(newSceneIndex);
                Scene scene = new Scene(newSceneFile, currentTime, newSceneIndex);
                this.currentScene = scene;
                RenderBackground();
                spawnManager.prepareSpawns();
            }
            if(newSceneIndex == -1){        // com cena padrão (modo de jogo infinito)
                Scene scene = new Scene();
                this.currentScene = scene;
                RenderBackground();
                spawnManager.prepareSpawns();
            }
        }
        catch(IOException | SAXException | ParserConfigurationException e){
            System.out.println(e.getMessage());
            return;
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

    private void UpdateSceneAndGameMode(){
        if(this.currentScene == null){      // se a fase ainda nao foi carregada
            if(this.currentGameMode == 0){          // e o modo é de fases
                LoadScene(0, Time.time);        // carrega a primeira fase    
                System.out.println("começando primeira fase");
            }   
            if(this.currentGameMode == 1){          // se o modo é infiníto
                LoadScene(-1, Time.time);               // começa direto no infinito
                System.out.println("iniciando modo de jogo infinito");
            }
            return;
        }
        if(currentScene.SceneIsDone() && currentScene.getIndex()+1 < gameConfig.numberOfScenes && currentScene.getIndex() != -1){    // se o boss morreu e ainda nao acabaram as cenas, carrega a proxima
            LoadScene(currentScene.getIndex()+1, Time.time);
            System.out.println("começando proxima fase");
        }
        if(currentScene.SceneIsDone() && currentScene.getIndex()+1 >= gameConfig.numberOfScenes && currentGameMode == 0){       // se o boss morreu e acabaram as cenas, muda pro modo infinito
            this.currentGameMode = 1;
            this.currentScene = null;
            LoadScene(-1, Time.time);
            System.out.println("modo de jogo alterado para infinito");
        }
    }

    public void Update(float deltaTime, long currentTime) {
        Time.time = currentTime;
        Time.deltaTime = deltaTime;

        UpdateSceneAndGameMode();

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

    public void SpawnEnemyA() {
        var enemy = new EnemyA(this);
        enemy.setActive();
        enemy.position = new Vector2((float) (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0f);
        enemy.velocity = new Vector2(0.20f + (float) Math.random() * 0.15f, 0.20f + (float) Math.random() * 0.15f);
        enemy.angle = (3f * (float) Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;

        enemies.add(enemy);
    }

    public void SpawnEnemyA(float posX, float posY) {       // overload pra modo fase com posição inicial pré-demilimitada
        var enemy = new EnemyA(this);
        enemy.setActive();
        enemy.position = new Vector2(posX, posY);
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

    public void SpawnEnemyB(float posX, float posY) {       // overload pra modo fase com posição inicial pré-demilimitada
        var enemy = new EnemyB(this);
        enemy.position = new Vector2(posX, posY);
        enemy.velocity = new Vector2(0.42f, 0.42f);
        enemy.angle = (3f * (float) Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;
        enemy.setActive();

        enemies.add(enemy);
    }

    public void SpawnBossA() {
        var boss = new BossA(this, 1000);
        boss.position = new Vector2((float) (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), 50.0f);
        boss.velocity = new Vector2(0.05f, 0.05f);
        boss.angle = (3f * (float) Math.PI) / 2f;
        boss.rotationVelocity = 0.0f;
        boss.setActive();
        enemies.add(boss);

        ui.setIsBoss(true);
    }

    public void SpawnBossA(int health, float posX, float posY) {    // overload pra modo fase com posição inicial e hp predefinidos
        var boss = new BossA(this, health);
        boss.position = new Vector2(posX, posY);
        boss.velocity = new Vector2(0.05f, 0.05f);
        boss.angle = (3f * (float) Math.PI) / 2f;
        boss.rotationVelocity = 0.0f;
        boss.setActive();
        enemies.add(boss);

        ui.setIsBoss(true);
    }

    public void SpawnBossB() {
        var boss = new BossB(this, 5000);
        boss.position = new Vector2( 60.0f, 60.0f);
        boss.velocity = new Vector2(0.05f, 0.05f);
        boss.angle = (3f * (float) Math.PI) / 2f;
        boss.rotationVelocity = 0.0f;
        boss.setActive();
        enemies.add(boss);

        ui.setIsBoss(true);
    }

    public void SpawnBossB(int health, float posX, float posY) {
        var boss = new BossB(this, health);
        boss.position = new Vector2(posX, posY);
        boss.velocity = new Vector2(0.05f, 0.05f);
        boss.angle = (3f * (float) Math.PI) / 2f;
        boss.rotationVelocity = 0.0f;
        boss.setActive();
        enemies.add(boss);

        ui.setIsBoss(true);
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

    public void SpawnPowerup(PowerupConfig pwupConfig) {      // overload pra modo de fase com powerups predefinidos 
        var powerup = new Powerup(this);
        powerup.modifierList = pwupConfig.modifiers;
        powerup.setActive();
        powerup.position = new Vector2(pwupConfig.getPositionX(), pwupConfig.getPositionY());
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
