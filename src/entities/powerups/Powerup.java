package entities.powerups;

import entities.Entity;
import entities.player.Player;
import entities.player.modifiers.PlayerModifier;
import game.GameManager;
import libraries.GameLib;
import time.Time;
import utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public class Powerup extends Entity {
    public List<PlayerModifier> modifierList = new ArrayList<>();

    public Powerup(GameManager manager) {
        super(manager);
        this.radius = 20f;
    }

    private float size = 0.0f;

    public void acquire(Player player) {
        for(var modifier : modifierList) {
            player.AddModifier(modifier);
        }
    }

    public String getName() {
        return "Powerup";
    }

    @Override
    public boolean Render(float deltaTime, long currentTime) {
        GameLib.setColor(
                ColorUtils.getRainbowColor(currentTime)
        );

        GameLib.fillRect(position.x - size, position.y - size, size, size);
        return false;
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        super.Update(deltaTime, currentTime);
        size = Math.abs((float)Math.sin(Time.getTimeFromStart() / 1000f) * 10f) + 10f;
    }

    @Override
    public boolean OutOfBounds() {
        return false;
    }
}
