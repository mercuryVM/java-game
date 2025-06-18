package game;

import background.Background;
import entities.Entity;
import entities.enemies.Enemy;
import entities.enemies.EnemyA;
import entities.enemies.EnemyB;
import entities.enemies.boss.Boss;
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
import time.Time;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class GameManager {
    public final Player player;
    public final EntityList<Enemy> enemies = new EntityList<>();
    public final EntityList<Projectile> projectiles = new EntityList<>();
    public final EntityList<Powerup> powerups = new EntityList<>();
    public final List<Background> backgrounds = new ArrayList<>();
    public int currentGameMode;     // 0 = fases; 1 = infinito
    private final Input input;
    private final SpawnManager spawnManager;
    private final GameConfig gameConfig;
    private Scene currentScene = null;
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
                String newScenePath = gameConfig.sceneConfigs.get(newSceneIndex);
                Scene scene = new Scene(newScenePath, currentTime, newSceneIndex);
                this.currentScene = scene;
                RenderBackground();
            }
            if(newSceneIndex == -1){        // com cena padrão (modo de jogo infinito)
                Scene scene = new Scene();
                this.currentScene = scene;
                RenderBackground();
            }
        }
        catch(IOException | SAXException | ParserConfigurationException e){
            System.out.println(e.getMessage());
            return;
        }
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
                spawnManager.prepareSpawns();
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
            spawnManager.prepareSpawns();
            System.out.println("começando proxima fase");
        }
        if(currentScene.SceneIsDone() && currentScene.getIndex()+1 >= gameConfig.numberOfScenes && currentGameMode == 0){       // se o boss morreu e acabaram as cenas, muda pro modo infinito
            LoadScene(-1, Time.time);
            this.currentGameMode = 1;
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
        powerups.update(deltaTime, currentTime);

        spawnManager.Update(deltaTime, currentTime);
        CollisionUpdate(deltaTime, currentTime);
    }

    private void CollisionUpdate(float deltaTime, long currentTime) {
        for (var enemy : enemies.getEntities()) {
            if (player.isActive() && player.collider.TestCollision(enemy)) {
                if (player.ApplyDamage(
                        (float) Math.random() * 50.0f)) {
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
                if (player.ApplyDamage(
                        (float) Math.random() * 50.0f)) {
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

    public void SpawnPowerup() {
        var powerup = new HealthPowerup(this);
        powerup.setActive();
        powerup.position = new Vector2((float) (Math.random() * (GameLib.WIDTH - 20.0) + 10.0), -10.0f);
        powerup.velocity = new Vector2(0.05f, 0.05f);
        powerup.angle = (3f * (float) Math.PI) / 2f;
        powerup.rotationVelocity = 0.0f;

        powerups.add(powerup);
    }

    public void SpawnPowerup(float posX, float posY) {      // overload pra modo de fase com powerups predefinidos 
        var powerup = new HealthPowerup(this);
        powerup.setActive();
        powerup.position = new Vector2(posX, posY);
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

    public void SpawnBoss() {
        var enemy = new Boss(this);
        enemy.position = new Vector2(GameLib.WIDTH / 2f, GameLib.HEIGHT / 2f);
        // enemy.velocity = new Vector2(0.42f, 0.42f);
        enemy.angle = (3f * (float) Math.PI) / 2f;
        enemy.rotationVelocity = 0.0f;
        enemy.setActive();

        enemies.add(enemy);
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

    public void RemoveEnemy(Enemy enemy) {
        enemies.scheduleRemoval(enemy);
    }

    public void RemoveProjectile(Projectile projectile) {
        projectiles.scheduleRemoval(projectile);
    }
}
