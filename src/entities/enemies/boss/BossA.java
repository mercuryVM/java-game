package entities.enemies.boss;


import game.GameManager;
import libraries.GameLib;
import math.Vector2;
import time.Time;

import java.awt.*;

public class BossA extends Boss{
    protected Vector2 baseVelocity = new Vector2();
    private double InnerRadius;

    public BossA(GameManager manager, int bossHealth){
        super(manager, bossHealth);
        this.baseVelocity.x = 0.05f;
        this.baseVelocity.y = 0.05f;

        this.velocity = this.baseVelocity.copy();
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        double radius = getRadius();

        if(state == State.ACTIVE) {

            if(position.y <= 0 + radius){
                velocity.y = baseVelocity.y * 1;
            }
            if(position.y >= GameLib.HEIGHT - radius){
                velocity.y = baseVelocity.y * -1;
            }
            if(position.x <= 0 + radius){
                velocity.x = baseVelocity.x * 1;
            }

            if(position.x >= GameLib.WIDTH - radius){
                velocity.x = baseVelocity.x * -1;
            }

            angle += (float) (rotationVelocity * deltaTime);
            position.x += (float) (velocity.x * deltaTime);
            position.y += (float) (velocity.y * deltaTime);
        }

        if(state == State.EXPLODING && isNotDead()) {
            Respawn();
        }
    }

    private float getTime(){
        return Time.getTimeFromStart() / 1000f;
    }

    private double getRadius(){
        return Math.abs(Math.cos(getTime()) * 20) + 30;
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        if(super.Render(deltaTime, currentTime)) return true;
        GameLib.setColor(Color.YELLOW);

        double radius = getRadius();

        GameLib.drawCircle(position.x, position.y, radius);
        GameLib.setColor(Color.gray);
        GameLib.drawLine(position.x, position.y, position.x * Math.cos(getTime()), position.y * Math.sin(getTime()));
        return false;
    }
}
