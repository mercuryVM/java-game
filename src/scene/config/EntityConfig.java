package scene.config;

public class EntityConfig {
    private int entityType;
    private int entitySpawnInterval;
    private float positionX;
    private float positionY;

    public EntityConfig(int t, int i, float x, float y){
        this.entityType = t;
        this.entitySpawnInterval = i;
        this.positionX = x;
        this.positionY = y;
    }

    public int getType(){
        return entityType;
    }
    public int getInterval(){
        return entitySpawnInterval;
    }
    public float getPositionX(){
        return positionX;
    }
    public float getPositionY(){
        return positionY;
    }
}
