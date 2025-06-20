package math;

public class Vector2 {
    public float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public static Vector2 diff(Vector2 a, Vector2 b) {
        return new Vector2(b.x - a.x, b.y - a.y);
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    public static float distance(Vector2 a, Vector2 b) {
        Vector2 dif = diff(a, b);
        dif.x *= dif.x;
        dif.y *= dif.y;
        return (float)Math.sqrt(dif.x + dif.y);
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    public float dot(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

}
