package entities.powerups;

import entities.player.modifiers.DoubleTapModifier;
import entities.player.modifiers.HealthAddModifier;
import entities.player.modifiers.InvincibleModifier;
import game.GameManager;

public class HealthPowerup extends Powerup{
    public HealthPowerup(GameManager manager) {
        super(manager);
        this.modifierList.add(
                new HealthAddModifier(100)
        );
        this.modifierList.add(
                new InvincibleModifier()
        );
        this.modifierList.add(
                new DoubleTapModifier()
        );
    }
}
