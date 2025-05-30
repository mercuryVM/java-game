package entities.player.modifiers.entry;

import entities.player.Player;
import entities.player.modifiers.PlayerModifier;
import time.Time;
import utils.IUpdateable;

public class PlayerModifierEntry implements IUpdateable {
    private PlayerModifier modifier;
    private Player player;
    private long startEntry = 0;
    private boolean executedOnce = false;

    public PlayerModifier getModifier() {
        return modifier;
    }

    public boolean wasExecutedOnce() {
        return executedOnce;
    }

    public long getStartEntry() {
        return startEntry;
    }

    public int getTimeLeft() {
        return (int)Math.ceil((double) (getStartEntry() + modifier.getTimeToExpire() - Time.time) / 1000);
    }

    public PlayerModifierEntry(Player player, PlayerModifier modifier, long startEntry) {
        this.player = player;
        this.modifier = modifier;
        this.startEntry = startEntry;
    }

    @Override
    public void Update(float deltaTime, long currentTime) {
        executedOnce = true;
        modifier.apply(player, deltaTime, currentTime);
    }

    public void Dispose() {
        modifier.dispose(player);
    }
}
