package game;

import entities.Entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EntityList<T extends Entity> {
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
            entity.Update(deltaTime, currentTime);
            if (entity.OutOfBounds()) {
                scheduleRemoval(entity);
            }
        }

        for (var entity : toRemove) {
            entities.remove(entity);
            entity.Dispose();
        }
        toRemove.clear();
    }

    public void render(float deltaTime, long currentTime) {
        for (var entity : entities) {
            entity.Render(deltaTime, currentTime);
        }
    }

    public List<T> getEntities() {
        return entities;
    }

    public void clear() {
        entities.clear();
        toRemove.clear();
    }
}
