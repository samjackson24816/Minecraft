package org.engine.math;

public class Vector2f {

    public float x, y;

    public Vector2f() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f add(Vector2f r) {
        return new Vector2f(x + r.x, y + r.y);
    }


}
