package game;

import java.util.Random;

import scene.Scene;
import scene.config.EntityConfig;
import scene.config.PowerupConfig;
import time.Time;

public class SpawnManager {
    private final GameManager gameManager;

    public boolean spawnNewEnemies = true;
    private EntityConfig nextEnemy1 = null, nextEnemy2 = null, Boss = null;
    private long nextEnemy1Interval = 0, nextEnemy2Interval = 0, nextBossInterval = 0;
    private PowerupConfig nextPowerup = null;
    private long nextPowerupInterval = 0;
    private long nextRoundTime = -1;
    public int enemy2Count = 0;
    private Scene currentScene;

    public SpawnManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void OnEnemy2Disposed() {
        enemy2Count--;
    }

    public void ResetRound() {
        nextRoundTime = Time.time + 5 * 1000;
    }

    public void prepareSpawns(){
        if(this.gameManager.currentGameMode == 0){
            this.currentScene = gameManager.getCurrentScene();
            nextEnemy1 = currentScene.getNextEnemyInterval(1);
            nextEnemy2 = currentScene.getNextEnemyInterval(2);
            Boss = currentScene.getBossInterval();
            nextPowerup = currentScene.getNextPowerupInterval();
        }
        else{
            nextEnemy1Interval = Time.time + 1000;
            nextEnemy2Interval = Time.time + 5000;
            nextBossInterval = Time.time + 50000;
        }
    }

    // problema: o intervalo sempre vai ser pequeno, pois no config ele é tanto a partir do iniício, mas ao criar os intervalos não temos o início então são valores mto pequenos
    public void Update(float deltaTime, long currentTime){
        if(!spawnNewEnemies) return;

        if(nextRoundTime == -1) {
            nextRoundTime = currentTime + 5 * 1000;
        }

        if(currentTime < nextRoundTime) return;

        if(this.gameManager.currentGameMode == 0){      // se o gameMode for fase usa os spawns de inimigos de fase
            if(nextEnemy1 != null){
                if(currentTime > nextEnemy1.getInterval()) {
                    gameManager.SpawnEnemyA(nextEnemy1.getPositionX(), nextEnemy1.getPositionY());
                    this.currentScene.removeRecentlySpawnedEnemy(nextEnemy1);        // tira o inimigo tipo 1 que spawnou da lista
                    nextEnemy1 = this.currentScene.getNextEnemyInterval(1);    // pega o prox inimigo tipo 1
                }
            }
    
            if(nextEnemy2 != null){
                if(currentTime > nextEnemy2.getInterval()) {
                    // o inimigo tipo 2 por padrão spawna 10, entao em SceneConfig.xml um inimigo tipo 2 indica 10 spawns desse inimigo

                    if(nextEnemy2.getNumberOfEnemiesSpawned() < nextEnemy2.getAmountOfEnemies()) {
                        gameManager.SpawnEnemyB(nextEnemy2.getPositionX(), nextEnemy2.getPositionY());
                        nextEnemy2.updateEnemiesSpawned();
                        nextEnemy2.updateSpawnInterval(currentTime + 120);      // se nao deu 10 inimigos ainda, continua spawnando
                    } else {
                        this.currentScene.removeRecentlySpawnedEnemy(nextEnemy2);        // tira o inimigo tipo 2 que spawnou da lista
                        nextEnemy2 = this.currentScene.getNextEnemyInterval(2);     // se deu 10, pega o proximo tipo 2 do config
                    }
                }
            }

            if(Boss != null){
                if(currentTime > Boss.getInterval()){      
                    if(Boss.getType() == 1){
                        gameManager.SpawnBossA(Boss.getTotalHealth(), Boss.getPositionX(), Boss.getPositionY());
                    }
                    if(Boss.getType() == 2){
                        gameManager.SpawnBossB(Boss.getTotalHealth(), Boss.getPositionX(), Boss.getPositionY());
                    }

                    this.currentScene.removeRecentlySpawnedEnemy(Boss);
                    Boss = this.currentScene.getBossInterval();
                }
            }

            if(nextPowerup != null){
                if(currentTime > nextPowerup.getSpawnInterval()) {
                    gameManager.SpawnPowerup(nextPowerup);
                    this.currentScene.removeRecentlySpawnedPowerup(nextPowerup);
                    nextPowerup = this.currentScene.getNextPowerupInterval();
                }
            }
        }

        if(this.gameManager.currentGameMode == 1){      // se gameMode for infinito usa spawns aleatórios infinitos
            if(currentTime > nextEnemy1Interval) {
                gameManager.SpawnEnemyA();
                nextEnemy1Interval = currentTime + 300 + (long)(Math.random() * 200);
            }
    
            if(currentTime > nextEnemy2Interval) {
                if(enemy2Count < 0)
                    enemy2Count = 0;

                if(enemy2Count >= 0 && enemy2Count < 10) {
                    nextEnemy2Interval = currentTime + 120;
                }else {
                    nextEnemy2Interval = currentTime + 10000;
                }
    
                gameManager.SpawnEnemyB();
                enemy2Count++;
            }

            if(currentTime > nextBossInterval){
                Random rand = new Random();
                int type = rand.nextInt(2) + 1;
                if(type == 1){
                    gameManager.SpawnBossA();
                }
                if(type == 2){
                    gameManager.SpawnBossB();
                }
                spawnNewEnemies = false;
                nextBossInterval = currentTime + 50000;
            }

            if(currentTime > nextPowerupInterval) {
                gameManager.SpawnPowerup();
                nextPowerupInterval = currentTime + 30 * 1000;
            }
        }
    }

    public void GameOver() {
        spawnNewEnemies = false;
    }
}
