package game;

public class SpawnManager {
    private final GameManager gameManager;

    public SpawnManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private long nextEnemy1 = 0, nextEnemy2 = 0;
    private int enemy2Count = 0;

    public void OnEnemy2Disposed() {
        enemy2Count--;
    }

    public void Update(float deltaTime, long currentTime) {
        if(currentTime > nextEnemy1) {
            gameManager.SpawnEnemyA();
            nextEnemy1 = currentTime + 500;
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
    }
}
