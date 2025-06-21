package scene.config;

public class EntityConfig {
    private int entityType;
    private long entitySpawnInterval;
    private float positionX;
    private float positionY;
    private int amount;
    private int health;
    private boolean isBoss = false;

    public EntityConfig(int t, long i, float x, float y, int a){
        this.entityType = t;
        this.entitySpawnInterval = i;
        this.positionX = x;
        this.positionY = y;
        this.amount = a;
    }

    public EntityConfig(int t, int h, long i, float x, float y){        // construtor com healthpoints, tb define que é boss
        this.entityType = t;
        this.health = h;
        this.entitySpawnInterval = i;
        this.positionX = x;
        this.positionY = y;
        this.isBoss = true;
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
    public int getTotalHealth(){
        return health;
    }
    public boolean isBoss(){
        return isBoss;
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
