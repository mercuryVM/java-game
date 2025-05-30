package entities.player.modifiers;

import entities.player.Player;

import java.awt.*;

public class DoubleTapModifier extends PlayerModifier{
    @Override
    public void apply(Player player, float deltaTime, long currentTime) {
        player.setBulletsPerShoot(2);
    }

    @Override
    public void dispose(Player player) {
        player.setBulletsPerShoot(1);
    }

    @Override
    public int getTimeToExpire() {
        return 10 * 1000;
    }

    @Override
    public String getName() {
        return "Double-tap";
    }

    @Override
    public Color getColor() {
        return Color.YELLOW;
    }
}