package collision;

import entities.Entity;
import math.Vector2;

public class Collider {
    private final Entity entity;

    public Collider(Entity entity) {
        this.entity = entity;
    }

    public boolean TestCollision(Entity entity) {
        float dist = Vector2.distance(entity.position, this.entity.position);
        return dist < entity.radius;
    }
}
