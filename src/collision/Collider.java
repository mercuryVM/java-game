package collision;

import entities.Entity;
import math.Vector2;

public class Collider {
    protected final Entity entity;

    public Collider(Entity entity) {
        this.entity = entity;
    }

    public boolean ignoreCollisions = false;

    public void setSize(float x, float y) {

    }

    public boolean TestCollision(Entity entity, float extraRadius) {
        if(ignoreCollisions) return false;
        float dist = Vector2.distance(entity.position, this.entity.position);
        return dist < entity.radius + (extraRadius == 0.0f ? this.entity.radius : extraRadius);
    }
}
