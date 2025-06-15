package scene.config;

import math.Vector2;

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
}
