package entities.player.modifiers;

import entities.player.Player;

import java.awt.*;

public class HealthAddModifier extends PlayerModifier{
    private final float healthToAdd;

    public HealthAddModifier(float healthToAdd) {
        this.healthToAdd = healthToAdd;
    }

    public float getHealthToAdd() {
        return healthToAdd;
    }

    @Override
    public void apply(Player player, float deltaTime, long currentTime) {
        player.GiveHealth(healthToAdd);
    }

    @Override
    public String getName() {
        return "Health";
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }
}
