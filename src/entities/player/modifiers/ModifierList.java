package entities.player.modifiers;

import entities.Entity;
import entities.player.modifiers.entry.PlayerModifierEntry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ModifierList<T extends PlayerModifierEntry> {
    private final List<T> entities = new ArrayList<>();
    private final LinkedList<T> toRemove = new LinkedList<>();

    public void add(T entity) {
        entities.add(entity);
    }

    public void scheduleRemoval(T entity) {
        toRemove.add(entity);
    }

    public void update(float deltaTime, long currentTime) {
        for (var entity : entities) {
            if(entity.getStartEntry() + entity.getModifier().getTimeToExpire() < currentTime && entity.wasExecutedOnce()) {
                scheduleRemoval(entity);
                entity.Dispose();
            }else {
                entity.Update(deltaTime, currentTime);
            }
        }

        for (var entity : toRemove) {
            entities.remove(entity);
        }
        toRemove.clear();
    }

    public List<T> getEntities() {
        return entities;
    }

    public void clear() {
        entities.clear();
        toRemove.clear();
    }
}
