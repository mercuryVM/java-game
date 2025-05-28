package background;

import math.Vector2;
import libraries.GameLib;
import java.awt.*;

public class Background {
    private int numberOfStars = 0;
    private float speed = 0f;
    private final Color color;
    private final Vector2[] stars;
    private float localTime = 0f;
    private int size = 2;

    public Background(int numberOfStars, float speed, Color color, int width) {
        this.numberOfStars = numberOfStars;
        this.speed = speed;
        this.color = color;
        this.size = width;

        stars = new Vector2[this.numberOfStars];

        for(int i = 0; i < this.numberOfStars; i++) {
            stars[i] = new Vector2((float)Math.random() * GameLib.WIDTH, (float)Math.random() * GameLib.HEIGHT);
        }
    }

    public void Render(float deltaTime, float currentTime) {
        GameLib.setColor(this.color);
        localTime += speed * deltaTime;
        for(int i = 0; i < numberOfStars; i++) {
            var star = stars[i];
            GameLib.fillRect(star.x, (star.y + localTime) % GameLib.HEIGHT, size, size);
        }
    }
}
