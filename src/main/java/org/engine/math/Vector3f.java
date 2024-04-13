package org.engine.math;

public class Vector3f {

    public float x, y, z;

    public Vector3f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f add(Vector3f r) {
        return new Vector3f(x + r.x, y + r.y, z + r.z);
    }


}
