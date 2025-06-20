package game;

import time.Time;

public class SpawnManager {
    private final GameManager gameManager;

    public SpawnManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private boolean spawnNewEnemies = true;
    private long nextEnemy1 = 0, nextEnemy2 = 0, nextPowerup = 0;
    private long nextRoundTime = -1;
    private int enemy2Count = 0;
    private int bossCount = 0;

    public void OnEnemy2Disposed() {
        enemy2Count--;
    }

    public void ResetRound() {
        nextRoundTime = Time.time + 5 * 1000;
    }

    public void Update(float deltaTime, long currentTime) {
        if(!spawnNewEnemies) return;

        if(nextRoundTime == -1) {
            nextRoundTime = currentTime + 5 * 1000;
        }

        if(currentTime < nextRoundTime) return;

        //utilizar contagem de inimigos mortos dps
        if(bossCount == 0){
            gameManager.SpawnBossA();
            bossCount++;
            spawnNewEnemies = false;
        }



        if(currentTime > nextEnemy1) {
            gameManager.SpawnEnemyA();
            nextEnemy1 = currentTime + 300 + (long)(Math.random() * 200);
        }

        if(currentTime > nextEnemy2) {
            if(enemy2Count < 10) {
                nextEnemy2 = currentTime + 120;
            }else {
                nextEnemy2 = currentTime + 10000;
            }

            gameManager.SpawnEnemyB();
            enemy2Count++;
        }

        if(currentTime > nextPowerup) {
            gameManager.SpawnPowerup();

            nextPowerup = currentTime + 30 * 1000;
        }
    }

    public void GameOver() {
        spawnNewEnemies = false;
    }
}
