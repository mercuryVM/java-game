package entities;

import game.GameManager;
import libraries.GameLib;
import math.Vector2;

import java.awt.*;

public class Laser extends Entity {
    private float angle;
    private final float rotationSpeed;
    private final float length;
    public final Entity sender;

    public Laser(GameManager manager, Vector2 position, float rotationSpeed, float length, Entity sender) {
        super(manager);
        this.position = position;
        this.angle = 0f;
        this.rotationSpeed = rotationSpeed;
        this.length = length;
        this.sender = sender;
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        angle += rotationSpeed * deltaTime;
        angle %= (2f * (float) Math.PI);
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        GameLib.setColor(Color.red);

        float endX = position.x + length * (float) Math.cos(angle);
        float endY = position.y + length * (float) Math.sin(angle);

        GameLib.drawLine(position.x, position.y, endX, endY);

        return false;
    }

    public float getAngle() {
        return angle;
    }

    public float getLength(){
        return length;
    }
}
