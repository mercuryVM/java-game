package entities.player.modifiers;

import entities.player.Player;

import java.awt.*;

public class InvincibleModifier extends PlayerModifier{
    @Override
    public void apply(Player player, float deltaTime, long currentTime) {
        player.MakeInvincible(currentTime + getTimeToExpire());
    }

    @Override
    public int getTimeToExpire() {
        return 5000;
    }

    @Override
    public String getName() {
        return "Invincibility";
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }
}
