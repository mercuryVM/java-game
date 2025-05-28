package entities.player;

import entities.ShooterEntity;
import game.GameManager;
import libraries.GameLib;

import java.awt.*;

public class Player extends ShooterEntity {
    public Player(GameManager manager) {
        super(manager);
    }

    @Override
    public void Render(float deltaTime, long currentTime) {
        GameLib.setColor(Color.BLUE);
        GameLib.drawPlayer(position.x, position.y, radius);
    }
}
