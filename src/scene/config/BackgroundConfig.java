package scene.config;

import java.awt.*;

public class BackgroundConfig {
    private int numberOfStars;
    private float speed;
    private Color color;
    private int size;

    public BackgroundConfig(int num, float speed, Color color, int size){
        this.numberOfStars = num;
        this.speed = speed;
        this.color = color;
        this.size = size;
    }

    public int getNumberOfStars(){
        return numberOfStars;
    }
    public float getStarsSpeed(){
        return speed;
    }
    public Color getStarsColor(){
        return color;
    }
    public int getStarsSize(){
        return size;
    }
}
