package scene.config;

public class EntityConfig {
    private int entityType;
    private long entitySpawnInterval;
    private float positionX;
    private float positionY;

    public EntityConfig(int t, long i, float x, float y){
        this.entityType = t;
        this.entitySpawnInterval = i;
        this.positionX = x;
        this.positionY = y;
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

    public void updateSpawnInterval(long increase){
        if(increase >= 0){
            entitySpawnInterval = increase;
        }
    }
}
