package scene.config;

import java.util.ArrayList;

import entities.player.modifiers.PlayerModifier;

public class PowerupConfig {
    public ArrayList<PlayerModifier> modifiers = new ArrayList<>();
    private long powerupSpawnInterval;
    private float positionX;
    private float positionY;

    public PowerupConfig(long interval, float posX, float posY){
        this.powerupSpawnInterval = interval;
        this.positionX = posX;
        this.positionY = posY;
    }

    public long getSpawnInterval(){
        return powerupSpawnInterval;
    } 
    public float getPositionX(){
        return positionX;
    }
    public float getPositionY(){
        return positionY;
    }
}
