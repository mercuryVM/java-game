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
        super.Update(deltaTime, currentTime);
        //Verificar se pode atirar
        if(currentTime >= nextShoot) {
            Shoot(currentTime);
        }
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        if(super.Render(deltaTime, currentTime)) return true;
        GameLib.setColor(Color.CYAN);
        GameLib.drawCircle(position.x, position.y, radius);

        return false;
    }
}
