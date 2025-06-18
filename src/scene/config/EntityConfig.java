package scene.config;

public class EntityConfig {
    private int entityType;
    private long entitySpawnInterval;
    private float positionX;
    private float positionY;
    private int amount;

    public EntityConfig(int t, long i, float x, float y, int amount){
        this.entityType = t;
        this.entitySpawnInterval = i;
        this.positionX = x;
        this.positionY = y;
        this.amount = amount;
    }

    public int getType(){
        return entityType;
    }
    public long getInterval(){
        return entitySpawnInterval;
    }
    public float getPositionX(){
        return positionX;
    }
    public float getPositionY(){
        return positionY;
    }
    public int getAmountOfEnemies(){
        return amount;
    }

    public void updateSpawnInterval(long increase){
        if(increase >= 0){
            entitySpawnInterval = increase;
        }
    }

    private int enemiesSpawned = 0;
    public int getNumberOfEnemiesSpawned(){
        return enemiesSpawned;
    }
    public void updateEnemiesSpawned(){
        enemiesSpawned++;
    }
}
