package entities.player.modifiers;

import entities.player.Player;

import java.awt.*;

public class PlayerModifier {
    public void apply(Player player, float deltaTime, long currentTime) {

    }

    public void dispose(Player player) {

    }

    public int getTimeToExpire() {
        return 0;
    }

    public String getName() {
        return "Modifier";
    }

    public Color getColor() {
        return Color.white;
    }
}
