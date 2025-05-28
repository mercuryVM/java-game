package entities.enemies;

import game.GameManager;
import libraries.GameLib;

import java.awt.*;

public class EnemyA extends Enemy {
    public EnemyA(GameManager manager) {
        super(manager);
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        position.x += (float) (velocity.x * Math.cos(this.angle) * deltaTime);
        position.y += (float) (velocity.y * Math.sin(this.angle) * deltaTime * (-1.0));
        this.angle += (float) (this.rotationVelocity * deltaTime);
        Shoot(currentTime);
    }

    @Override
    public void Render(float deltaTime, long currentTime) {
        GameLib.setColor(Color.CYAN);
        GameLib.drawCircle(position.x, position.y, radius);
    }
}
