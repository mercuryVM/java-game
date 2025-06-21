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

    public boolean TestLaserCollision(entities.Laser laser) {
        if (ignoreCollisions) return false;

        Vector2 start = laser.position;
        Vector2 end = new Vector2(
                start.x + laser.getLength() * (float) Math.cos(laser.getAngle()),
                start.y + laser.getLength() * (float) Math.sin(laser.getAngle())
        );

        Vector2 circleCenter = entity.position;
        float radius = entity.radius;

        Vector2 d = end.subtract(start);
        Vector2 f = start.subtract(circleCenter);

        float a = d.dot(d);
        float b = 2 * f.dot(d);
        float c = f.dot(f) - radius * radius;

        float discriminant = b*b - 4*a*c;

        if (discriminant < 0) {
            return false;
        } else {
            discriminant = (float) Math.sqrt(discriminant);

            float t1 = (-b - discriminant) / (2*a);
            float t2 = (-b + discriminant) / (2*a);

            return (t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1);
        }
    }

}
